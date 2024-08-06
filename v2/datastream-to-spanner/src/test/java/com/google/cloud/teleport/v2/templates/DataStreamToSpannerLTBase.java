/*
 * Copyright (C) 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.teleport.v2.templates;

import static java.util.Arrays.stream;
import static org.apache.beam.it.common.logging.LogStrings.formatForLogging;
import static org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Preconditions.checkArgument;

import com.google.api.services.bigquery.model.TableRow;
import com.google.cloud.bigquery.InsertAllRequest.RowToInsert;
import com.google.cloud.datastream.v1.DestinationConfig;
import com.google.cloud.datastream.v1.SourceConfig;
import com.google.cloud.datastream.v1.Stream;
import com.google.common.io.Resources;
import com.google.pubsub.v1.SubscriptionName;
import com.google.pubsub.v1.TopicName;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.beam.it.common.PipelineLauncher.LaunchInfo;
import org.apache.beam.it.common.TestProperties;
import org.apache.beam.it.gcp.TemplateLoadTestBase;
import org.apache.beam.it.gcp.bigquery.BigQueryResourceManager;
import org.apache.beam.it.gcp.datastream.DatastreamResourceManager;
import org.apache.beam.it.gcp.datastream.JDBCSource;
import org.apache.beam.it.gcp.pubsub.PubsubResourceManager;
import org.apache.beam.it.gcp.spanner.SpannerResourceManager;
import org.apache.beam.it.gcp.storage.GcsResourceManager;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.MoreObjects;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Strings;

/**
 * Base class for DataStreamToSpanner Load tests. It provides helper functions related to
 * environment setup and assertConditions.
 */
public class DataStreamToSpannerLTBase extends TemplateLoadTestBase {

  /**
   * Helper function for creating all datastream resources required by DataStreamToSpanner template.
   * Source connection profile, Destination connection profile, Stream. And then Starts the stream.
   *
   * @param artifactBucketName
   * @param gcsPrefix
   * @param jdbcSource
   * @param datastreamResourceManager
   * @return created stream
   */
  public Stream createDatastreamResources(
      String artifactBucketName,
      String gcsPrefix,
      JDBCSource jdbcSource,
      DatastreamResourceManager datastreamResourceManager) {
    SourceConfig sourceConfig =
        datastreamResourceManager.buildJDBCSourceConfig("mysql", jdbcSource);

    // Create Datastream GCS Destination Connection profile and config
    DestinationConfig destinationConfig =
        datastreamResourceManager.buildGCSDestinationConfig(
            "gcs",
            artifactBucketName,
            gcsPrefix,
            DatastreamResourceManager.DestinationOutputFormat.AVRO_FILE_FORMAT);

    // Create and start Datastream stream
    Stream stream =
        datastreamResourceManager.createStream("ds-spanner", sourceConfig, destinationConfig);
    datastreamResourceManager.startStream(stream);
    return stream;
  }

  /**
   * Helper function for creating Spanner DDL. Reads the sql file from resources directory and
   * applies the DDL to Spanner instance.
   *
   * @param spannerResourceManager Initialized SpannerResourceManager instance
   * @param resourceName SQL file name with path relative to resources directory
   */
  public void createSpannerDDL(SpannerResourceManager spannerResourceManager, String resourceName)
      throws IOException {
    String ddl =
        String.join(
            " ", Resources.readLines(Resources.getResource(resourceName), StandardCharsets.UTF_8));
    ddl = ddl.trim();
    String[] ddls = ddl.split(";");
    for (String d : ddls) {
      if (!d.isBlank()) {
        spannerResourceManager.executeDdlStatement(d);
      }
    }
  }

  /**
   * Helper function for creating all pubsub resources required by DataStreamToSpanner template.
   * PubSub topic, Subscription and notification setup on a GCS bucket with gcsPrefix filter.
   *
   * @param pubsubResourceManager Initialized PubSubResourceManager instance
   * @param gcsPrefix Prefix of Avro file names in GCS relative to bucket name
   * @return SubscriptionName object of the created PubSub subscription.
   */
  public SubscriptionName createPubsubResources(
      String identifierSuffix,
      PubsubResourceManager pubsubResourceManager,
      String gcsPrefix,
      GcsResourceManager gcsResourceManager) {
    String topicNameSuffix = "it" + identifierSuffix;
    String subscriptionNameSuffix = "it-sub" + identifierSuffix;
    TopicName topic = pubsubResourceManager.createTopic(topicNameSuffix);
    SubscriptionName subscription =
        pubsubResourceManager.createSubscription(topic, subscriptionNameSuffix);
    String prefix = gcsPrefix;
    if (prefix.startsWith("/")) {
      prefix = prefix.substring(1);
    }
    gcsResourceManager.createNotification(topic.toString(), prefix);
    return subscription;
  }

  /**
   * Returns the full GCS path given a list of path parts.
   *
   * <p>"path parts" refers to the bucket, directories, and file. Only the bucket is mandatory and
   * must be the first value provided.
   *
   * @param pathParts everything that makes up the path, minus the separators. There must be at
   *     least one value, and none of them can be empty
   * @return the full path, such as 'gs://bucket/dir1/dir2/file'
   */
  public String getGcsPath(String... pathParts) {
    checkArgument(pathParts.length != 0, "Must provide at least one path part");
    checkArgument(
        stream(pathParts).noneMatch(Strings::isNullOrEmpty), "No path part can be null or empty");

    return String.format("gs://%s", String.join("/", pathParts));
  }

  /**
   * Exports the metrics of given dataflow job to BigQuery.
   *
   * @param launchInfo Job info of the job
   * @param metrics metrics to export
   */
  protected void exportMetricsToBigQuery(
      String spannerTestName, LaunchInfo launchInfo, Map<String, Double> metrics) {
    LOG.info("Exporting metrics:\n{}", formatForLogging(metrics));
    try {
      // either use the user specified project for exporting, or the same project
      String exportProject = MoreObjects.firstNonNull(TestProperties.exportProject(), project);
      BigQueryResourceManager bigQueryResourceManager =
          BigQueryResourceManager.builder(testName, exportProject, CREDENTIALS)
              .setDatasetId(TestProperties.exportDataset())
              .build();
      // exporting metrics to bigQuery table
      Map<String, Object> rowContent = new HashMap<>();
      rowContent.put("timestamp", launchInfo.createTime());
      rowContent.put("sdk", launchInfo.sdk());
      rowContent.put("version", launchInfo.version());
      rowContent.put("job_type", launchInfo.jobType());
      putOptional(rowContent, "template_name", launchInfo.templateName());
      putOptional(rowContent, "template_version", launchInfo.templateVersion());
      putOptional(rowContent, "template_type", launchInfo.templateType());
      putOptional(rowContent, "pipeline_name", launchInfo.pipelineName());
      rowContent.put("test_name", spannerTestName);
      // Convert parameters map to list of table row since it's a repeated record
      List<TableRow> parameterRows = new ArrayList<>();
      for (Entry<String, String> entry : launchInfo.parameters().entrySet()) {
        TableRow row = new TableRow().set("name", entry.getKey()).set("value", entry.getValue());
        parameterRows.add(row);
      }
      rowContent.put("parameters", parameterRows);
      // Convert metrics map to list of table row since it's a repeated record
      List<TableRow> metricRows = new ArrayList<>();
      for (Entry<String, Double> entry : metrics.entrySet()) {
        TableRow row = new TableRow().set("name", entry.getKey()).set("value", entry.getValue());
        metricRows.add(row);
      }
      rowContent.put("metrics", metricRows);
      bigQueryResourceManager.write(
          TestProperties.exportTable(), RowToInsert.of("rowId", rowContent));
    } catch (IllegalStateException e) {
      LOG.error("Unable to export results to datastore. ", e);
    }
  }
}

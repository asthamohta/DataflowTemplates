DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id INT64 NOT NULL,
    full_name STRING(25),
    `from` STRING(25)
) PRIMARY KEY(id);

DROP TABLE IF EXISTS users2;

CREATE TABLE IF NOT EXISTS users2 (
    id INT64 NOT NULL,
    full_name STRING(25),
    ) PRIMARY KEY(id);

DROP TABLE IF EXISTS alldatatypetransformation;

CREATE TABLE IF NOT EXISTS alldatatypetransformation (
    varchar_column STRING(20) NOT NULL,
    tinyint_column STRING(MAX),
    text_column STRING(MAX),
    date_column STRING(MAX),
    smallint_column STRING(MAX),
    mediumint_column STRING(MAX),
    int_column STRING(MAX),
    bigint_column STRING(MAX),
    float_column STRING(MAX),
    double_column STRING(MAX),
    decimal_column STRING(MAX),
    datetime_column STRING(MAX),
    timestamp_column STRING(MAX),
    time_column STRING(MAX),
    year_column STRING(MAX),
    char_column STRING(10),
    tinytext_column STRING(MAX),
    mediumtext_column STRING(MAX),
    longtext_column STRING(MAX),
    enum_column STRING(MAX),
    bool_column STRING(MAX),
    other_bool_column STRING(MAX),
    list_text_column JSON,
    list_int_column JSON,
    frozen_list_bigint_column JSON,
    set_text_column JSON,
    set_date_column JSON,
    frozen_set_bool_column JSON,
    map_text_to_int_column JSON,
    map_date_to_text_column JSON,
    frozen_map_int_to_bool_column JSON,
    map_text_to_list_column JSON,
    map_text_to_set_column JSON,
    set_of_maps_column JSON,
    list_of_sets_column JSON,
    frozen_map_text_to_list_column JSON,
    frozen_map_text_to_set_column JSON,
    frozen_set_of_maps_column JSON,
    frozen_list_of_sets_column JSON,
    varint_column STRING(MAX),
    inet_column STRING(MAX),
    timeuuid_column STRING(MAX),
    duration_column STRING(MAX),
    uuid_column STRING(MAX),
    ascii_column STRING(MAX),
    bytes_column STRING(MAX)
) PRIMARY KEY(varchar_column);

DROP TABLE IF EXISTS alldatatypecolumns;

CREATE TABLE IF NOT EXISTS alldatatypecolumns (
    varchar_column STRING(20) NOT NULL,
    tinyint_column INT64,
    text_column STRING(MAX),
    date_column DATE,
    smallint_column INT64,
    mediumint_column INT64,
    int_column INT64,
    bigint_column INT64,
    float_column FLOAT64,
    double_column FLOAT64,
    decimal_column NUMERIC,
    datetime_column TIMESTAMP,
    timestamp_column TIMESTAMP,
    time_column STRING(MAX),
    year_column STRING(MAX),
    char_column STRING(10),
    tinytext_column STRING(MAX),
    mediumtext_column STRING(MAX),
    longtext_column STRING(MAX),
    enum_column STRING(MAX),
    bool_column BOOL,
    other_bool_column BOOL,
    bytes_column BYTES(MAX),
    list_text_column JSON,
    list_int_column JSON,
    frozen_list_bigint_column JSON,
    set_text_column JSON,
    set_date_column JSON,
    frozen_set_bool_column JSON,
    map_text_to_int_column JSON,
    map_date_to_text_column JSON,
    frozen_map_int_to_bool_column JSON,
    map_text_to_list_column JSON,
    map_text_to_set_column JSON,
    set_of_maps_column JSON,
    list_of_sets_column JSON,
    frozen_map_text_to_list_column JSON,
    frozen_map_text_to_set_column JSON,
    frozen_set_of_maps_column JSON,
    frozen_list_of_sets_column JSON,
    varint_column STRING(MAX),
    inet_column STRING(MAX),
    timeuuid_column STRING(MAX),
    duration_column STRING(MAX),
    uuid_column STRING(MAX),
    ascii_column STRING(MAX),
    list_text_column_from_array ARRAY<STRING(MAX)>,
    set_text_column_from_array ARRAY<STRING(MAX)>
) PRIMARY KEY(varchar_column);

DROP TABLE IF EXISTS boundaryconversiontesttable;

CREATE TABLE IF NOT EXISTS boundaryconversiontesttable (
    varchar_column STRING(20) NOT NULL,
    tinyint_column INT64,
    smallint_column INT64,
    int_column INT64,
    bigint_column INT64,
    float_column FLOAT64,
    double_column FLOAT64,
    decimal_column NUMERIC,
    bool_column BOOL,
    ascii_column STRING(MAX),
    text_column STRING(MAX),
    bytes_column BYTES(MAX),
    date_column STRING(MAX),
    time_column STRING(MAX),
    timestamp_column STRING(MAX),
    duration_column STRING(MAX),
    uuid_column STRING(MAX),
    timeuuid_column STRING(MAX),
    inet_column STRING(MAX),
    map_bool_column JSON,
    map_float_column JSON,
    map_double_column JSON,
    map_tinyint_column JSON,
    map_smallint_column JSON,
    map_int_column JSON,
    map_bigint_column JSON,
    map_varint_column JSON,
    map_decimal_column JSON,
    map_ascii_column JSON,
    map_varchar_column JSON,
    map_blob_column JSON,
    map_date_column JSON,
    map_time_column JSON,
    map_timestamp_column JSON,
    map_duration_column JSON,
    map_uuid_column JSON,
    map_timeuuid_column JSON,
    map_inet_column JSON
) PRIMARY KEY(varchar_column);

DROP TABLE IF EXISTS emptystringjsontable;

CREATE TABLE IF NOT EXISTS emptystringjsontable (
    varchar_column STRING(20) NOT NULL,
    empty_column STRING(20),
    double_float_map_col JSON,
    decimal_set_col JSON,
    date_double_map_col JSON,
    uuid_ascii_map_col JSON,
    ascii_text_map_col JSON,
    timestamp_list_col JSON,
    int_set_col JSON,
    smallint_set_col JSON,
    varchar_list_col JSON,
    inet_list_col JSON,
    bigint_list_col JSON,
    tinyint_varint_map_col JSON,
    text_set_col JSON,
    double_set_col JSON,
    time_list_col JSON,
    frozen_ascii_list_col JSON,
    int_list_col JSON,
    ascii_list_col JSON,
    date_set_col JSON,
    double_inet_map_col JSON,
    timestamp_set_col JSON,
    time_tinyint_map_col JSON,
    bigint_set_col JSON,
    varchar_set_col JSON,
    tinyint_set_col JSON,
    bigint_boolean_map_col JSON,
    text_list_col JSON,
    boolean_list_col JSON,
    blob_list_col JSON,
    timeuuid_set_col JSON,
    int_time_map_col JSON,
    time_set_col JSON,
    boolean_set_col JSON,
    float_set_col JSON,
    ascii_set_col JSON,
    uuid_list_col JSON,
    varchar_bigint_map_col JSON,
    blob_int_map_col JSON,
    varint_blob_map_col JSON,
    double_list_col JSON,
    float_list_col JSON,
    smallint_list_col JSON,
    varint_list_col JSON,
    float_smallint_map_col JSON,
    smallint_timestamp_map_col JSON,
    text_timeuuid_map_col JSON,
    timeuuid_list_col JSON,
    date_list_col JSON,
    uuid_set_col JSON,
    boolean_decimal_map_col JSON,
    blob_set_col JSON,
    inet_text_map_col JSON,
    varint_set_col JSON,
    tinyint_list_col JSON,
    timestamp_uuid_map_col JSON,
    decimal_duration_map_col JSON,
    decimal_list_col JSON,
    inet_set_col JSON,
    timeuuid_varchar_map_col JSON,
    duration_list_col JSON,
    frozen_ascii_set_col JSON
) PRIMARY KEY(varchar_column);

CREATE TABLE testtable_03tpcovf16ed0klxm3v808ch3btgq0uk (
    id STRING(100) NOT NULL,
    col_qcbf69rmxtre3b_03tpcovf16ed STRING(100)
) PRIMARY KEY (id);


CREATE CHANGE STREAM allstream
  FOR ALL OPTIONS (
  value_capture_type = 'NEW_ROW',
  retention_period = '7d'
);
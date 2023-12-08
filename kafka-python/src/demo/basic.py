from pyflink.common import Configuration
from pyflink.datastream import StreamExecutionEnvironment
from pyflink.table import StreamTableEnvironment

TABLE_SOURCE_SQL = """\
CREATE TABLE sample_source (text STRING)
WITH (
    'connector' = 'kafka',
    'topic' = 'sample-source',
    'properties.bootstrap.servers' = 'kafka:9092',
    'properties.group.id' = 'flink',
    'properties.auto.offset.reset' = 'latest',
    'format' = 'json'
)
"""

TABLE_SINK_SQL = """\
CREATE TABLE sample_sink (text STRING)
WITH (
    'connector' = 'kafka',
    'topic' = 'sample-sink',
    'properties.bootstrap.servers' = 'kafka:9092',
    'format' = 'json'
)
"""

INSERT_SQL = """\
INSERT INTO sample_sink
SELECT UPPER(text) FROM sample_source
"""


def main():
    conf = Configuration()
    env = StreamExecutionEnvironment.get_execution_environment(conf)
    table_env = StreamTableEnvironment.create(env)
    table_env.get_config().set("pipeline.name", "kafka-python-basic")
    table_env.get_config().set_local_timezone("UTC")
    table_env.execute_sql(TABLE_SOURCE_SQL)
    table_env.execute_sql(TABLE_SINK_SQL)
    table_env.execute_sql(INSERT_SQL)


if __name__ == "__main__":
    main()

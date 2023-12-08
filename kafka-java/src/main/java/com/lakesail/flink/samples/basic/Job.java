package com.lakesail.flink.samples.basic;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Job {
    public static String KAFKA_BOOTSTRAP_SERVERS = "kafka:9092";
    public static String SOURCE_TOPIC = "sample-source";
    public static String SINK_TOPIC = "sample-sink";
    public static String GROUP_ID = "flink";
    public static String JOB_NAME = "kafka-java-basic";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<Event> kafkaSource = KafkaSource.<Event>builder()
            .setBootstrapServers(KAFKA_BOOTSTRAP_SERVERS)
            .setGroupId(GROUP_ID)
            .setTopics(SOURCE_TOPIC)
            .setValueOnlyDeserializer(new JsonSchema<>(Event.class))
            .setStartingOffsets(OffsetsInitializer.latest())
            .build();

        DataStream<Event> stream = env.fromSource(kafkaSource,
            WatermarkStrategy.noWatermarks(), "Kafka Source");

        KafkaSink<Event> kafkaSink = KafkaSink.<Event>builder()
            .setBootstrapServers(KAFKA_BOOTSTRAP_SERVERS)
            .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                .setTopic(SINK_TOPIC)
                .setValueSerializationSchema(new JsonSchema<>(Event.class))
                .build())
            .build();

        stream.map(Event::toUpperCase).sinkTo(kafkaSink);

        env.execute(JOB_NAME);
    }
}

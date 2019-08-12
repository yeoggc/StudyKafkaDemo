package producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 同步实现的非常精妙，重用了异步处理逻辑，只是利用KafkaProducer#send方法返回的future对象的特性
 *
 */
public class SyncCustomProducer {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ggc:9092");//kafka集群，broker-list
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 1);//重试次数
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);//批次大小
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1000);//等待时间
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);//RecordAccumulator缓冲区大小
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);

        ProducerRecord<String, String> producerRecord;
        for (int i = 0; i < 10; i++) {
            producerRecord = new ProducerRecord<>("first", "message" + i);
            Future<RecordMetadata> future = kafkaProducer.send(producerRecord);
            RecordMetadata metadata = future.get();
            if (metadata != null) {
                System.out.println("success:" + metadata.topic() + "-" + metadata.partition() + "-" + metadata.offset());
            }
        }

        kafkaProducer.close();
    }
}

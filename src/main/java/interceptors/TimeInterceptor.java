package interceptors;


import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class TimeInterceptor implements ProducerInterceptor<String, String> {


    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {

        String newValue = System.currentTimeMillis() + record.value();

        return new ProducerRecord<>
                (record.topic(), record.partition(), record.timestamp(),
                        record.key(), newValue);
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}

package br.com.sample.consumer;

import br.com.sample.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService<T> {

    void parse(ConsumerRecord<String, Message<T>> record);
    String getTopic();
    String getConsumerGroup();

}

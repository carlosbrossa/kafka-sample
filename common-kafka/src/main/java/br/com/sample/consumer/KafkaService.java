package br.com.sample.consumer;

import br.com.sample.Message;
import br.com.sample.dipatcher.GsonSerializer;
import br.com.sample.dipatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
    private final KafkaConsumer<String, Message<T>> consumer;
    private final ConsumerFunction parse;

    public KafkaService(String groupId, String topic, ConsumerFunction<T> parse, Map<String,String> extraProperties ){
        this(parse, groupId, extraProperties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaService(String groupId, Pattern topic, ConsumerFunction<T> parse, Map<String,String> extraProperties) {
    this(parse, groupId, extraProperties);
        consumer.subscribe(topic);
    }

    public KafkaService(ConsumerFunction<T> parse, String groupId, Map<String,String> extraProperties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer(properties(groupId, extraProperties));
    }


    private Properties properties(String groupId, Map<String, String> extraProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1"); //maximo de poll para comitar de um em um e evitar erros de balanceamento
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.putAll(extraProperties);
        return properties;
    }

    public void run() throws InterruptedException, ExecutionException {

        try(var deadLetter = new KafkaDispatcher<>()){
            while (true) {
                var records = consumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    System.out.println("encontrei registros " + records.count());

                    for (var record : records) {
                        try {
                            parse.consume(record);
                        } catch (Exception e) {
                            e.printStackTrace();
                            deadLetter.send("SCHEDULE_DEADLETTER", record.value().getId().toString(),
                                    record.value().getId().continueWith("DeadLetter"),
                                    new GsonSerializer().serialize("", record.value()));
                        }
                    }
                }
            }

        }

    }

    @Override
    public void close() {
        this.consumer.close();
    }
}

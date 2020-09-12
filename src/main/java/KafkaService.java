import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction parse;

    public KafkaService(String groupId, String topic, ConsumerFunction parse, Class<T> type, Map<String,String> extraProperties ){
        this(parse, groupId, type, extraProperties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaService(String groupId, Pattern topic, ConsumerFunction parse, Class<T> type, Map<String,String> extraProperties) {
    this(parse, groupId, type, extraProperties);
        consumer.subscribe(topic);
    }

    public KafkaService(ConsumerFunction parse, String groupId, Class<T> type, Map<String,String> extraProperties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer(properties(type, groupId, extraProperties));
    }


    private Properties properties(Class<T> type, String groupId, Map<String, String> extraProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1"); //maximo de poll para comitar de um em um e evitar erros de balanceamento
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        properties.putAll(extraProperties);
        return properties;
    }

    public void run() throws InterruptedException {

        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("encontrei registros " + records.count());

                for (var record : records) {
                    parse.consume(record);
                }
            }
        }
    }

    @Override
    public void close() {
        this.consumer.close();
    }
}

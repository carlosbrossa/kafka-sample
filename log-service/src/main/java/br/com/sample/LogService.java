package br.com.sample;

import br.com.sample.consumer.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        var logService = new LogService();
        try(var kafkaService = new KafkaService(LogService.class.getSimpleName(),
                Pattern.compile("SCHEDULE.*"),
                logService::parse,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))){
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Message<String>> record) {
        System.out.println("----------------------");
        System.out.println("logging: " + record.topic());
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
    }


}

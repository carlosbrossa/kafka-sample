package br.com.sample;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class RobotService {

    public static void main(String[] args) throws InterruptedException {

        var robotService = new RobotService();
        try(var kafkaService = new KafkaService(RobotService.class.getSimpleName(),
                "SCHEDULE",
                robotService::parse,
                Schedule.class,
                Map.of())) {
            kafkaService.run();
        }

    }

    private void parse(ConsumerRecord<String, Schedule> record) throws InterruptedException {
        System.out.println("----------------------");
        System.out.println("processing new schedule");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        Thread.sleep(3000);
    }
}

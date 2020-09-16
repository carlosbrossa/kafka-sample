package br.com.sample;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RobotService {

    private final KafkaDispatcher<Schedule> scheduleDispatcher = new KafkaDispatcher<>();


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

    private void parse(ConsumerRecord<String, Schedule> record) throws InterruptedException, ExecutionException {
        System.out.println("----------------------");
        System.out.println("processing new schedule");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        Thread.sleep(3000);

        var email = record.value().getEmail();

        var number = (Math.random() * (1000 - 1)) + 1;

        if(number > 500){
            System.out.println("scheduled");
            scheduleDispatcher.send("SCHEDULE_CONFIRMED", email, record.value());
        }else{
            System.out.println("conflict schedule");
            scheduleDispatcher.send("SCHEDULE_CONFLICT", email, record.value());
        }
    }

}

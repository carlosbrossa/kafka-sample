package br.com.sample;

import br.com.sample.consumer.KafkaService;
import br.com.sample.dipatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RobotService {

    private final KafkaDispatcher<Schedule> scheduleDispatcher = new KafkaDispatcher<>();


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        var robotService = new RobotService();
        try(var kafkaService = new KafkaService(RobotService.class.getSimpleName(),
                "SCHEDULE",
                robotService::parse,
                Map.of())) {
            kafkaService.run();
        }

    }

    private void parse(ConsumerRecord<String, Message<Schedule>> record) throws InterruptedException, ExecutionException {
        System.out.println("----------------------");
        System.out.println("processing new schedule");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        Thread.sleep(3000);

        var schedule = record.value().getPayload();

        var email = schedule.getEmail();

        var number = (Math.random() * (1000 - 1)) + 1;

        if(number > 500){
            System.out.println("scheduled");
            scheduleDispatcher.send("SCHEDULE_CONFIRMED",
                    email,
                    record.value().getId().continueWith(this.getClass().getSimpleName()),
                    schedule);
        }else{
            System.out.println("conflict schedule");
            scheduleDispatcher.send(
                    "SCHEDULE_CONFLICT",
                    email,
                    record.value().getId().continueWith(this.getClass().getSimpleName()),
                    schedule);
        }
    }

}

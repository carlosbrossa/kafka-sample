package br.com.sample;

import br.com.sample.consumer.KafkaService;
import br.com.sample.dipatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ScheduleEmailService {

    private final KafkaDispatcher<Email> scheduleDispatcher = new KafkaDispatcher<>();


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        var emailService = new ScheduleEmailService();
        try(var kafkaService = new KafkaService(ScheduleEmailService.class.getSimpleName(),
                "SCHEDULE",
                emailService::parse,
                Map.of())) {
            kafkaService.run();
        }

    }

    private void parse(ConsumerRecord<String, Message<Schedule>> record) throws InterruptedException, ExecutionException {
        System.out.println("----------------------");
        System.out.println("preparing email schedule");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        Thread.sleep(3000);

        var schedule = record.value().getPayload();

        var email = new Email(schedule.getEmail(),"Your exam is scheduled");
        scheduleDispatcher.send(
                "SCHEDULE_SEND_EMAIL",
                email.getEmail(),
                record.value().getId().continueWith(ScheduleEmailService.class.getSimpleName()),
                email);
    }

}

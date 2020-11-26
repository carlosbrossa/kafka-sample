package br.com.sample;

import br.com.sample.consumer.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EmailService {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        var emailService = new EmailService();
        try(var service = new KafkaService(EmailService.class.getSimpleName(),
                "SCHEDULE_SEND_EMAIL",
                emailService::parse,
                Map.of())){
            service.run();
        }

    }

    private void parse(ConsumerRecord<String, Message<Email>> record) throws InterruptedException {
            System.out.println("----------------------");
            System.out.println("email send");
            System.out.println(record.key());
            System.out.println(record.value());
            System.out.println(record.partition());
            System.out.println(record.offset());
            Thread.sleep(1000);
    }
}

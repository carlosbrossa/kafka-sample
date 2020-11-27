package br.com.sample;

import br.com.sample.consumer.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EmailService implements ConsumerService<Email> {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new ServiceProvider().run(EmailService::new);
    }

    public String getConsumerGroup(){
        return EmailService.class.getSimpleName();
    }

    public String getTopic(){
        return  "SCHEDULE_SEND_EMAIL";
    }

    public void parse(ConsumerRecord<String, Message<Email>> record) {
            System.out.println("----------------------");
            System.out.println("email send");
            System.out.println(record.key());
            System.out.println(record.value());
            System.out.println(record.partition());
            System.out.println(record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

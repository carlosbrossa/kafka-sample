import org.apache.kafka.clients.consumer.ConsumerRecord;

public class RobotService {

    public static void main(String[] args) throws InterruptedException {

        var robotService = new RobotService();
        try(var kafkaService = new KafkaService(RobotService.class.getSimpleName(), "SCHEDULE",
                robotService::parse)) {
            kafkaService.run();
        }

    }

    private void parse(ConsumerRecord<String, String> record) throws InterruptedException {
        System.out.println("----------------------");
        System.out.println("processing new order");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        Thread.sleep(3000);
    }
}

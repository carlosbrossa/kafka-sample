import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewScheduleMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try(var kafkaDispatcher = new KafkaDispatcher()) {

            for (var i = 0; i < 10; i++) {

                var value = "545445,32442,777777";
                var key = value + UUID.randomUUID().toString();
                kafkaDispatcher.send("SCHEDULE", key, value);

                var email = "thank you for your order";
                kafkaDispatcher.send("SCHEDULE_SEND_EMAIL", key, email);

            }
        }

    }

}

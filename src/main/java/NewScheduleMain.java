import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewScheduleMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try(var scheduleKafkaDispatcher = new KafkaDispatcher<Schedule>()){
            try(var emailKafkaDispatcher = new KafkaDispatcher<Email>()) {

                for (var i = 0; i < 10; i++) {

                    String userId = UUID.randomUUID().toString();
                    String scheduleId = UUID.randomUUID().toString();
                    String examId = "HEMO";

                    var schedule = new Schedule(userId, scheduleId, examId);

                    scheduleKafkaDispatcher.send("SCHEDULE", userId, schedule);

                    var email = new Email(userId + "@teste.com","Your exam is scheduled");
                    emailKafkaDispatcher.send("SCHEDULE_SEND_EMAIL", userId, email);

                }
            }
        }

    }

}

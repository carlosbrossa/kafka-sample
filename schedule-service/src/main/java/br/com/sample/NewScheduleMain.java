package br.com.sample;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewScheduleMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try(var scheduleKafkaDispatcher = new KafkaDispatcher<Schedule>()){
            try(var emailKafkaDispatcher = new KafkaDispatcher<Email>()) {

                for (var i = 0; i < 10; i++) {

                    String userId = UUID.randomUUID().toString();
                    String scheduleId = UUID.randomUUID().toString();
                    String examId = Math.random() + "HEMO";

                    var email = Math.random() + "@email";
                    var schedule = new Schedule(userId, scheduleId, examId, email);

                    scheduleKafkaDispatcher.send(
                            "SCHEDULE",
                            examId,
                            new CorrelationId(NewScheduleMain.class.getSimpleName()),
                            schedule);

                    var emailCode = new Email(userId + "@teste.com","Your exam is scheduled");
                    emailKafkaDispatcher.send(
                            "SCHEDULE_SEND_EMAIL",
                            examId,
                            new CorrelationId(NewScheduleMain.class.getSimpleName()),
                            emailCode);

                }
            }
        }

    }

}

package br.com.sample;

public class Schedule {

    private final String userId, scheduleId, examCode, email;


    public Schedule(String userId, String scheduleId, String examCode, String email) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.examCode = examCode;
        this.email = email;
    }
}

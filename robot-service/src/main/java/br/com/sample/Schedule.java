package br.com.sample;

public class Schedule {

    private final String userId, scheduleId;
    private final String examCode;


    public Schedule(String userId, String scheduleId, String examCode) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.examCode = examCode;
    }
}

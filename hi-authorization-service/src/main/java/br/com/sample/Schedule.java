package br.com.sample;

public class Schedule {

    private final String userId, scheduleId, email;
    private final String examCode;


    public Schedule(String userId, String scheduleId, String examCode, String email) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.examCode = examCode;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getExamCode() {
        return examCode;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "userId='" + userId + '\'' +
                ", scheduleId='" + scheduleId + '\'' +
                ", email='" + email + '\'' +
                ", examCode='" + examCode + '\'' +
                '}';
    }
}

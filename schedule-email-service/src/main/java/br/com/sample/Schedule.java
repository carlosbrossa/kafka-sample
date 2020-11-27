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

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "userId='" + userId + '\'' +
                ", scheduleId='" + scheduleId + '\'' +
                ", examCode='" + examCode + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

package br.com.sample;

public class Pacient {

    private final String uuid;

    public Pacient(String uuid){
        this.uuid = uuid;
    }

    public String getReportPath() {
        return "target/" + uuid + "-report.txt";
    }

    public String getUuid() {
        return uuid;
    }
}

package br.com.sample;

public class Email {

    private final String email, subject;

    public Email(String email, String subject) {
        this.email = email;
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }
}

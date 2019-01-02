package xyz.tincat.backuper.email;


import java.util.Properties;

public class Email {
    private String host;
    private String username;
    private String password;
    private String receiver;
    private String subject;
    private String text;
    private String filename;

    public Email(Properties props) {
        this.host = props.getProperty("host");
        this.username = props.getProperty("username");
        this.password = props.getProperty("password");
        this.receiver = props.getProperty("receiver");
        this.subject = props.getProperty("subject");
        this.text = props.getProperty("text");
        this.filename = props.getProperty("attachFilePath");
    }

    @Override
    public String toString() {
        return "Email{" +
                "host='" + host + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", receiver='" + receiver + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
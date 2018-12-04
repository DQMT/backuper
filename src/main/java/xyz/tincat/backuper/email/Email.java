package xyz.tincat.backuper.email;

import lombok.Data;
import lombok.ToString;

import java.util.Properties;

@Data
@ToString
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
        this.filename = props.getProperty("filename");
    }

}
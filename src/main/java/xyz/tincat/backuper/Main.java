package xyz.tincat.backuper;

import xyz.tincat.backuper.email.Email;
import xyz.tincat.backuper.email.EmailUtil;
import xyz.tincat.backuper.option.Options;

import javax.mail.MessagingException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Options options = Options.getInstance("email.properties");
        Email email = new Email(options.getPropMap());
        try {
            EmailUtil.sendEmail(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
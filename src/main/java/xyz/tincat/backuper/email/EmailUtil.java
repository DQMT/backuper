package xyz.tincat.backuper.email;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * @ Date       ：Created in 14:15 2018/12/4
 * @ Modified By：
 * @ Version:     0.1
 */
@Slf4j
public class EmailUtil {
    public static void sendEmail(final Email email) throws MessagingException, IOException {
        log.info("send email " + email);
        // 配置发送邮件的环境属性
        Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
        // 表示SMTP发送邮件，需要进行身份验证
        props.setProperty("mail.host", email.getHost());
        props.setProperty("mail.smtp.auth", "true");


        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                return new PasswordAuthentication(email.getUsername(), email.getPassword());
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        String fromwho=email.getUsername()+email.getHost().replace("smtp.", "@").replace("SMTP.", "@");
        InternetAddress form = new InternetAddress(fromwho);
        message.setFrom(form);

        // 设置收件人
        InternetAddress to = new InternetAddress(email.getReceiver());
        message.setRecipient(Message.RecipientType.TO, to);


        // 设置邮件标题
        message.setSubject(email.getSubject());
        MimeBodyPart mbp1 = new MimeBodyPart();
        MimeBodyPart mbp2 = new MimeBodyPart();
        Multipart mp = new MimeMultipart();
        mbp1.setText(email.getText());
        if (email.getFilename() != null) {
            mbp2.attachFile(email.getFilename());
        }
        mp.addBodyPart(mbp1);
        mp.addBodyPart(mbp2);

        // 设置邮件的内容体
        message.setContent(mp);
        message.setSentDate(new Date());
        // 发送邮件
        Transport.send(message);
        log.info("send email success");
    }

}
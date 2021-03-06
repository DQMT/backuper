package xyz.tincat.backuper.task;

import lombok.extern.slf4j.Slf4j;
import xyz.tincat.backuper.email.Email;
import xyz.tincat.backuper.email.EmailUtil;
import xyz.tincat.backuper.option.Options;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @ Date       ：Created in 11:57 2018/12/6
 * @ Modified By：
 * @ Version:     0.1
 */
@Slf4j
public class BackupTask extends TimerTask {
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    private Timer timer;
    private Date nextDate;
    private MessageDigest md;
    private String md5;


    public BackupTask(Timer timer, Calendar calendar) {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.timer = timer;
        this.nextDate = calendar.getTime();
        if (this.nextDate.before(new Date())) {//如果不加一天，任务会立即执行
            Calendar startDT = Calendar.getInstance();
            startDT.setTime(this.nextDate);
            startDT.add(Calendar.DAY_OF_MONTH, 1);
            this.nextDate = startDT.getTime();
        }
    }


    public void start() {
        timer.schedule(this, this.nextDate, PERIOD_DAY);
    }

    @Override
    public void run() {
        log.info("backuper task start");
        Options options = Options.getInstance("email.properties");
        String filesAndFolders = options.get("filesAndFolders");
        FilePacker filePacker = new FilePacker(filesAndFolders);
        filePacker.pack(options.get("attachFilePath"));
        Email email = new Email(options.getProperties());
        tryEmailBackup(email);
        log.info("backuper task finish");
    }

    private void tryEmailBackup(Email email) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(email.getFilename());
            byte[] buffer = new byte[8192];
            int length = -1;
            md.reset();
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            String digest = Arrays.toString(md.digest());
            if (!digest.equals(md5)) {
                String text = email.getText();
                text = text + "\n\n" + new Date() + " 备份文件已更新！";
                email.setText(text);
                EmailUtil.sendEmail(email);
                md5 = digest;
            }else{
                String text = email.getText();
                text = text + "\n\n" + new Date() + " 备份文件没有更新！";
                email.setText(text);
                email.setFilename(null);
                EmailUtil.sendEmail(email);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
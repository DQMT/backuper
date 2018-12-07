package xyz.tincat.backuper.task;

import xyz.tincat.backuper.email.Email;
import xyz.tincat.backuper.email.EmailUtil;
import xyz.tincat.backuper.option.Options;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ Date       ：Created in 11:57 2018/12/6
 * @ Modified By：
 * @ Version:     0.1
 */
public class BackupTask extends TimerTask {
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    private Timer timer;
    private String filename;
    private Date nextDate;

    public BackupTask(Timer timer, Calendar calendar) {
        this(timer, null, calendar);
    }

    public BackupTask(Timer timer, String filename, Calendar calendar) {
        this.timer = timer;
        this.filename = filename;
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
        Options options = Options.getInstance("email.properties");
        Email email = new Email(options.getPropMap());
        if (filename != null) {
            email.setFilename(filename);
        }
        try {
            EmailUtil.sendEmail(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
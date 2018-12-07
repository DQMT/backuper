package xyz.tincat.backuper;

import xyz.tincat.backuper.task.BackupTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class Main {

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Timer timer = new Timer();
        BackupTask backupTask = new BackupTask(timer, calendar);
        backupTask.start();
        System.out.println(new Date());
    }
}
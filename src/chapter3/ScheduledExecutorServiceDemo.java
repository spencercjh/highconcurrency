package chapter3;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author spencercjh
 * Page100~102 newScheduledThreadPool计划任务线程池
 * scheduleAtFixedRate 周期是一次任务的时间加上间隔时间，如果这个周期比任务时间短，周期就会变成任务时间
 * scheduleWithFixedDelay 周期是两次任务的间隔时间
 */
public class ScheduledExecutorServiceDemo {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(2000);
                System.out.println(new Date(System.currentTimeMillis()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

}

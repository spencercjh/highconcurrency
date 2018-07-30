package chapter4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author spencercjh
 * Page 151-154 ThreadLocal的资源回收
 */
public class ThreadLocalDemo_Gc {
    static volatile ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println(this.toString() + " is gc");
        }
    };
    static volatile CountDownLatch countDownLatch = new CountDownLatch(10000);

    public static class ParseDate implements Runnable {
        int i = 0;

        public ParseDate(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
                if (threadLocal.get() == null) {
                    threadLocal.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") {
                        @Override
                        protected void finalize() throws Throwable {
                            System.out.println(this.toString() + " is gc");
                        }
                    });
                    System.out.println(Thread.currentThread().getId() + " :create SimpleDateFormat");
                }
                Date date = threadLocal.get().parse("2015-03-29 19:29:" + i % 60);
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executorService.execute(new ParseDate(i));
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("mission complete");
        threadLocal = null;
        System.gc();
        System.out.println("first gc complete");
        threadLocal = new ThreadLocal<SimpleDateFormat>();
        countDownLatch = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            executorService.execute(new ParseDate(i));
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.gc();
        System.out.println("second gc complete");
    }

}
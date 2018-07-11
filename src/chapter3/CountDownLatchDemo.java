package chapter3;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author spencercjh
 * Page87~89 倒计时器CountDownLatch
 */
public class CountDownLatchDemo implements Runnable {
    private static final CountDownLatch end = new CountDownLatch(10);
    private static final CountDownLatchDemo demo = new CountDownLatchDemo();


    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(10) * 1000);
            System.out.println("check complete!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            end.countDown();
        }
    }

    @SuppressWarnings("AlibabaThreadPoolCreation")
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(demo);
        }
        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Go");
        executorService.shutdown();

    }
}

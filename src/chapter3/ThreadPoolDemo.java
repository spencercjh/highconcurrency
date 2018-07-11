package chapter3;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author spencercjh
 * Page98~100 newFixedThreadPool(int)是固定大小的线程池
 * newCachedTheadPool()不固定
 */
public class ThreadPoolDemo {
    private static class MyTask extends Thread {
        @Override
        public void run() {
            System.out.println(new Date(System.currentTimeMillis()) + " " + Thread.currentThread().getId());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyTask myTask = new MyTask();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            executorService.submit(myTask);
        }
    }
}

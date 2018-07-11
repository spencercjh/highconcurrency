package chapter3;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author spencercjh
 * Page106~108 自定义线程池，线程工厂和拒绝策略
 */
public class RejectThreadPoolDemo {
    private static class MyTask extends Thread {
        @Override
        public void run() {
            System.out.println(new Date(System.currentTimeMillis()) + " " + Thread.currentThread().getId());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("AlibabaThreadShouldSetName")
    public static void main(String[] args) {
        MyTask myTask = new MyTask();
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                System.out.println("create！" + thread);
                return thread;
            }
        };
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10), threadFactory,
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println(r.toString() + " is discard");
                    }
                });
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            executorService.submit(myTask);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

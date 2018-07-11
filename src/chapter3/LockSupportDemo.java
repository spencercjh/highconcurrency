package chapter3;

import java.util.concurrent.locks.LockSupport;

/**
 * @author spencercjh
 * Page92~95 LockSupport线程阻塞工具类
 */
public class LockSupportDemo {
    private static final Object u = new Object();
    private static ChangeObjectThread thread1 = new ChangeObjectThread("t1");
    private static ChangeObjectThread thread2 = new ChangeObjectThread("t2");


    public static class ChangeObjectThread extends Thread {
        ChangeObjectThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (u) {
                System.out.println("in " + getName());
                LockSupport.park();
            }
        }
    }

    public static void main(String[] args) {
        thread1.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread1.getState());
        thread2.start();
        LockSupport.unpark(thread1);
        LockSupport.unpark(thread2);
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

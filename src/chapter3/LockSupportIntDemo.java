package chapter3;

import java.util.concurrent.locks.LockSupport;

/**
 * @author spencercjh
 * Page92~95 LockSupport线程阻塞工具类
 */
public class LockSupportIntDemo {
    private static final Object u = new Object();
    private static ChangeObjectThread thread1 = new ChangeObjectThread("thread1");
    private static ChangeObjectThread thread2 = new ChangeObjectThread("thread2");

    private static class ChangeObjectThread extends Thread {
        ChangeObjectThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (u) {
                System.out.println("in " + getName());
                LockSupport.park();
                if (Thread.interrupted()) {
                    System.out.println(getName() + "被中断了");
                }
            }
            System.out.println(getName() + "执行结束");
        }
    }

    public static void main(String[] args) {
        thread1.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
        thread1.interrupt();
        LockSupport.unpark(thread2);
    }
}

package chapter4;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author spencercjh
 * Page168-169 AtomicIntegerArray
 */
public class AtomicIntegerArrayDemo {
    private static AtomicIntegerArray array = new AtomicIntegerArray(10);
    private static int[] A = new int[10];

    public static class AddThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                array.getAndIncrement(i % array.length());
            }
        }
    }

    public static class testThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                A[i % A.length]++;
            }
        }
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new AddThread();
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 10; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(array);
        Thread[] threads2 = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads2[i] = new testThread();
        }
        for (int i = 0; i < 10; i++) {
            threads2[i].start();
        }
        for (int i = 0; i < 10; i++) {
            try {
                threads2[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Arrays.toString(A));
    }
}

package chapter2;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author spencercjh
 * Page62~63
 * 抛出了ArrayIndexOutOfBoundsException异常且size不正确
 * ArrayList在扩容过程中，内部一致性遭到破坏，但由于没有锁的保护，另外一个线程访问到了不一致的内存状态，导致出现了越界问题
 * 使用线程安全的Vector替代ArrayList
 * 或者对线程内操作加锁
 * 但由于线程和锁争抢的随机性，在我的代码中无法准确得出到底哪种模式最快
 */
public class ArrayListMutiThread {
    private static ArrayList<Integer> arrayList = new ArrayList<>(10);
    private static Vector<Integer> vector = new Vector<>(10);

    public static class ArrayListAddThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                arrayList.add(i);
            }
        }
    }

    public static class VectorAddThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                vector.add(i);
            }
        }
    }

    public static class SynArrayListAddThreadA extends Thread {
        @Override
        public void run() {
            synchronized (arrayList) {
                for (int i = 0; i < 1000000; i++) {
                    arrayList.add(i);
                }
            }
        }
    }

    public static class SynArrayListAddThreadB extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                synchronized (arrayList) {
                    arrayList.add(i);
                }
            }
        }
    }

    public static void main(String[] args) {
        int threadNumber = 10;
        System.out.println("多线程数：" + threadNumber);
        System.out.println("不正确的多线程ArrayList");
        long beginTime = System.currentTimeMillis();
        ArrayListAddThread[] arrayListAddThreads = new ArrayListAddThread[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            arrayListAddThreads[i] = new ArrayListAddThread();
            arrayListAddThreads[i].start();
        }
        try {
            for (int i = 0; i < threadNumber; i++) {
                arrayListAddThreads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(arrayList.size());
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + String.valueOf(endTime - beginTime));
        try {
            Thread.sleep(3000);
            System.out.println("正确的多线程Vector");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        beginTime = System.currentTimeMillis();
        VectorAddThread[] vectorAddThreads = new VectorAddThread[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            vectorAddThreads[i] = new VectorAddThread();
            vectorAddThreads[i].start();
        }
        try {
            for (int i = 0; i < threadNumber; i++) {
                vectorAddThreads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(vector.size());
        endTime = System.currentTimeMillis();
        System.out.println("耗时：" + String.valueOf(endTime - beginTime));
        try {
            Thread.sleep(3000);
            System.out.println("正确的多线程中大循环加锁ArrayList");
            arrayList.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        beginTime = System.currentTimeMillis();
        SynArrayListAddThreadA[] synArrayListAddThreadAs = new SynArrayListAddThreadA[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            synArrayListAddThreadAs[i] = new SynArrayListAddThreadA();
            synArrayListAddThreadAs[i].start();
        }
        try {
            for (int i = 0; i < threadNumber; i++) {
                synArrayListAddThreadAs[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(arrayList.size());
        endTime = System.currentTimeMillis();
        System.out.println("耗时：" + String.valueOf(endTime - beginTime));
        try {
            Thread.sleep(3000);
            System.out.println("正确的多线程中循环内部加锁ArrayList");
            arrayList.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        beginTime = System.currentTimeMillis();
        SynArrayListAddThreadB[] synArrayListAddThreadBs = new SynArrayListAddThreadB[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            synArrayListAddThreadBs[i] = new SynArrayListAddThreadB();
            synArrayListAddThreadBs[i].start();
        }
        try {
            for (int i = 0; i < threadNumber; i++) {
                synArrayListAddThreadBs[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(arrayList.size());
        endTime = System.currentTimeMillis();
        System.out.println("耗时：" + String.valueOf(endTime - beginTime));
    }
}

package chapter2;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author spencercjh
 * Page63~66 在多线程环境下hashMap的size不能正确的计算出来，内部的链式结构已经被多线程破坏了
 * 使用ConcurrentHashMap可以解决
 * 或者对线程内操作加锁
 * 但由于线程和锁争抢的随机性，在我的代码中无法准确得出到底哪种模式最快
 */
public class HashMapMutiThread {
    private static HashMap<String, String> hashMap = new HashMap<>();
    private static ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();

    public static class WrongAddThread extends Thread {
        private int start;

        WrongAddThread(int start) {
            this.start = start;
        }

        @Override
        public void run() {
            for (int i = start; i < 100000; i++) {
                hashMap.put(Integer.toString(i), Integer.toBinaryString(i));
            }
        }
    }

    public static class SynAddThreadA extends Thread {
        private int start;

        SynAddThreadA(int start) {
            this.start = start;
        }

        @Override
        public void run() {
            for (int i = start; i < 100000; i++) {
                concurrentHashMap.put(Integer.toString(i), Integer.toBinaryString(i));
            }
        }
    }

    public static class SynAddThreadB extends Thread {
        private int start;

        SynAddThreadB(int start) {
            this.start = start;
        }

        @Override
        public void run() {
            synchronized (hashMap) {
                for (int i = start; i < 100000; i++) {
                    hashMap.put(Integer.toString(i), Integer.toBinaryString(i));
                }
            }

        }
    }

    public static class SynAddThreadC extends Thread {
        private int start;

        public SynAddThreadC(int start) {
            this.start = start;
        }

        @Override
        public void run() {
            for (int i = start; i < 100000; i++) {
                synchronized (hashMap) {
                    hashMap.put(Integer.toString(i), Integer.toBinaryString(i));
                }
            }
        }
    }

    public static void main(String[] args) {
        int threadNumber = 10;
        System.out.println("多线程数：" + threadNumber);
        System.out.println("不正确的多线程HashMap");
        long beginTime = System.currentTimeMillis();
        WrongAddThread[] wrongAddThreads = new WrongAddThread[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            wrongAddThreads[i] = new WrongAddThread(i);
            wrongAddThreads[i].start();
        }
        try {
            for (int i = 0; i < threadNumber; i++) {
                wrongAddThreads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(hashMap.size());
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + Long.toString(endTime - beginTime));
        try {
            Thread.sleep(3000);
            System.out.println("正确的多线程ConcurrentHashMap");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        beginTime = System.currentTimeMillis();
        SynAddThreadA[] synAddThreadAs = new SynAddThreadA[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            synAddThreadAs[i] = new SynAddThreadA(i);
            synAddThreadAs[i].start();
        }
        try {
            for (int i = 0; i < threadNumber; i++) {
                synAddThreadAs[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(concurrentHashMap.size());
        endTime = System.currentTimeMillis();
        System.out.println("耗时：" + Long.toString(endTime - beginTime));
        try {
            Thread.sleep(3000);
            hashMap.clear();
            System.out.println("正确的大循环外加锁HashMap");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        beginTime = System.currentTimeMillis();
        SynAddThreadB[] synAddThreadBs = new SynAddThreadB[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            synAddThreadBs[i] = new SynAddThreadB(i);
            synAddThreadBs[i].start();
        }
        try {
            for (int i = 0; i < threadNumber; i++) {
                synAddThreadBs[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(hashMap.size());
        endTime = System.currentTimeMillis();
        System.out.println("耗时：" + Long.toString(endTime - beginTime));
        try {
            Thread.sleep(3000);
            hashMap.clear();
            System.out.println("正确的大循环内加锁HashMap");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        beginTime = System.currentTimeMillis();
        SynAddThreadC[] synAddThreadCs = new SynAddThreadC[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            synAddThreadCs[i] = new SynAddThreadC(i);
            synAddThreadCs[i].start();
        }
        try {
            for (int i = 0; i < threadNumber; i++) {
                synAddThreadCs[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(hashMap.size());
        endTime = System.currentTimeMillis();
        System.out.println("耗时：" + Long.toString(endTime - beginTime));
    }
}
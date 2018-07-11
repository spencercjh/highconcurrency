package chapter3;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author spencercjh
 * Page89~92 循环栅栏CyclicBarrier
 */
public class CyclicBarrierDemo {
    public static class Soldier extends Thread {
        private String soldier;
        private final CyclicBarrier cyclicBarrier;

        Soldier(CyclicBarrier cyclicBarrier, String soldier) {
            this.soldier = soldier;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                cyclicBarrier.await();
                doWork();
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private void doWork() {
            try {
                Thread.sleep(Math.abs(new Random().nextInt() % 10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(soldier + "任务完成");
        }
    }

    public static class BarrierRun implements Runnable {
        private boolean flag;
        private int N;

        BarrierRun(boolean flag, int n) {
            this.flag = flag;
            N = n;
        }

        @Override
        public void run() {
            if (flag) {
                System.out.println("司令：【士兵" + N + "个，任务完成！】");
            } else {
                System.out.println("司令：【士兵" + N + "个，集合完毕！】");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final int N = 10;
        Thread[] allSoldiers = new Thread[N];
        boolean flag = false;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(N, new BarrierRun(flag, N));
        System.out.println("集合队伍！");
        for (int i = 0; i < N; i++) {
            System.out.println("士兵" + (i + 1) + "报道");
            allSoldiers[i] = new Soldier(cyclicBarrier, "士兵" + (i + 1));
            allSoldiers[i].start();
        }
    }
}

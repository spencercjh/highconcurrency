package chapter4;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author spencercjh
 * Page 169-170
 */
public class AtomicIntegerFiledUpdaterDemo {
    public static class Candidate {
        int id;
        volatile int score;
    }

    public final static AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater.
            newUpdater(Candidate.class, "score");
    public static AtomicInteger allScore = new AtomicInteger(0);

    public static void main(String[] args) {
        final Candidate candidate = new Candidate();
        Thread[] threads = new Thread[10000];
        for (int i = 0; i < 10000; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    if (Math.random() > 0.5) {
                        scoreUpdater.incrementAndGet(candidate);
                        allScore.incrementAndGet();
                    }
                }
            };
            threads[i].start();
        }
        for (int i = 0; i < 10000; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(candidate.score);
        System.out.println(allScore);
    }
}

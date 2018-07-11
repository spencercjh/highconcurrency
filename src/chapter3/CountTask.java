package chapter3;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author spencercjh
 * Page117~121 ForkJoin线程池执行复杂任务
 */
public class CountTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000;
    private long start;
    private long end;
    private static int eachStep = 10000000;

    public CountTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Long compute() {
        long sum = 0;
        boolean canCompute = (end - start) < THRESHOLD;
        if (canCompute) {
            for (long i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            long step = (start + end) / eachStep;
            ArrayList<CountTask> subTasks = new ArrayList<>();
            long pos = start;
            for (int i = 0; i < eachStep; i++) {
                long lastOne = pos + step;
                if (lastOne > end) {
                    lastOne = end;
                }
                CountTask subTask = new CountTask(pos, lastOne);
                pos += step + 1;
                subTasks.add(subTask);
                subTask.fork();
            }
            for (CountTask countTask : subTasks) {
                sum += countTask.join();
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask countTask = new CountTask(0, Integer.MAX_VALUE);
        ForkJoinTask<Long> result = forkJoinPool.submit(countTask);
        try {
            long res = result.get();
            long endTime = System.currentTimeMillis();
            System.out.println("sum=" + res);
            System.out.println("耗时：" + Long.toString(endTime - beginTime));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}

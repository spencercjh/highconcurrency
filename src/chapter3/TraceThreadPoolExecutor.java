package chapter3;

import java.util.concurrent.*;

/**
 * @author spencercjh
 * Page113~117 重写execute和submit方法使得线程内部的异常能够抛出来
 */
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {
    private static class DivTask extends Thread {
        private int a, b;

        DivTask(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            double re = a / b;
            System.out.println(re);
        }
    }

    private TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit timeUnit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, workQueue);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, clientTrace(), Thread.currentThread().getName()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrap(task, clientTrace(), Thread.currentThread().getName()));
    }

    private Exception clientTrace() {
        return new Exception("Client Stack Trace");
    }

    private Runnable wrap(final Runnable task, final Exception clientStack, String clientTreadName) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    clientStack.printStackTrace();
                    throw e;
                }
            }
        };
    }

    public static void main(String[] args) {
        ThreadPoolExecutor poolExecutor = new TraceThreadPoolExecutor(0, Integer.MAX_VALUE,
                0L, TimeUnit.SECONDS, new SynchronousQueue<>());
        for (int i = 0; i < 5; i++) {
            poolExecutor.execute(new DivTask(100, i));
        }
    }
}

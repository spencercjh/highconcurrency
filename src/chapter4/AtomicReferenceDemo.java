package chapter4;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author spencercjh
 * Page 163-165 AtomicReference无锁原子对象引用
 */
public class AtomicReferenceDemo {
    static AtomicReference<Integer> money = new AtomicReference<>();

    public static void main(String[] args) {
        money.set(19);
        for (int i = 0; i < 3; i++) {
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        while (true) {
                            Integer m = money.get();
                            if (m < 20) {
                                if (money.compareAndSet(m, m + 20)) {
                                    System.out.println("余额小于20元，充值成功，余额：" + money.get() + " 元");
                                    break;
                                }
                            } else {
                                System.out.println("余额大于20元，无需充值");
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }

                    }
                }
            }.start();
        }
        new Thread() {

            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    while (true) {
                        Integer m = money.get();
                        if (m > 10) {
                            System.out.println("大于10元");
                            if (money.compareAndSet(m, m - 10)) {
                                System.out.println("成功消费10元，余额：" + money.get() + " 元");
                                break;
                            }
                        } else {
                            System.out.println("没有足够的余额");
                            break;
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }
}

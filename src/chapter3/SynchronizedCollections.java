package chapter3;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author spencercjh
 * Page121-137 JDK并发容器
 */
public class SynchronizedCollections {
    public static void main(String[] args) {
        List<String> synchronizedList = Collections.synchronizedList(new LinkedList<>());
        Map<Integer, Integer> hashMap = new HashMap<>(30);
        Map<Integer, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<>(30));
        ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        BlockingQueue blockingQueue = new ArrayBlockingQueue(100);
        BlockingQueue blockingQueue1 = new LinkedBlockingQueue();
        Map<Integer, Integer> concurrentSkipListMap = new ConcurrentSkipListMap<>();
        for (int i = 0; i < 30; i++) {
            concurrentSkipListMap.put(i, i);
            synchronizedMap.put(i, i);
            hashMap.put(i, i);
        }
        for (Map.Entry<Integer, Integer> entry : concurrentSkipListMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println();
        for (Map.Entry<Integer, Integer> entry : synchronizedMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println();
        for (Map.Entry<Integer, Integer> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}

package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * newCachedThreadPool 可以动态创建线程
 * 但没有设定核心线程，且最大线程数为 Integer.MAX_VALUE 相当于可以无限创建线程 任务过多时 也会导致占用内存过多
 */
public class CachedThreadPool {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Task());
        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        }
    }

}

package threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 关闭线程池 demo
 */
public class ShutDownThreadPool {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Task());
        }
        Thread.sleep(1000);
        System.out.println(executorService.isShutdown());
        executorService.shutdown();
        System.out.println(executorService.isShutdown()); // 判断是否开始停止线程
        System.out.println(executorService.isTerminated()); // 判断是否所有线程全部停止
        System.out.println(executorService.awaitTermination(10L, TimeUnit.SECONDS)); // 判断在10秒种内是否全部执行完毕
        System.out.println(executorService.shutdownNow()); // 强制退出，不推荐直接强制关闭（需要设计一下优雅关闭线程）
        List<Runnable> runnables = executorService.shutdownNow(); // 强行停止时在队列中还未被执行的任务，可以根据需求自行处理
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

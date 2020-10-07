package threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * newScheduledThreadPool 可按照时间策略创建线程池
 * 类似于定时任务（延时、时间间隔等）
 */
public class ScheduledThreadPool {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

        scheduledExecutorService.schedule(new Task(),5, TimeUnit.SECONDS); //延时5秒触发一次

        scheduledExecutorService.scheduleAtFixedRate(new Task(),1,3, TimeUnit.SECONDS); //初始1秒后执行，之后每隔3秒执行
    }

    static class Task implements Runnable{
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

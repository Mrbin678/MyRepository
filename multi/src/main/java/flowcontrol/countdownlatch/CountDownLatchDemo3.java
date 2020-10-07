package flowcontrol.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1等N N等1结合
 */
public class CountDownLatchDemo3 {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(5);
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("No." + no + "准备开始，等待开始信号");
                    try {
                        begin.await();
                        System.out.println("No." + no + "开始任务");
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("No." + no + "已完成");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        end.countDown();
                    }
                }
            };
            service.submit(runnable);
        }
        Thread.sleep(5000);
        System.out.println("开始信号触发");
        begin.countDown();

        end.await();
        System.out.println("所有任务完成");
    }

}

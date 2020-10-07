package cache;

import cache.computable.ExpensiveFunction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用CountDownLatch实现压测
 */
public class TestCache {

    static Cache5<String, Integer> cache5 = new Cache5<>(new ExpensiveFunction());

    public static CountDownLatch count = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(100);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            service.submit(() -> {
                Integer result = null;
                try {
                    System.out.println(Thread.currentThread().getName() + "开始等待");
                    count.await();
                    SimpleDateFormat format = ThreadSafeFormatter.dateFormatThreadLocal.get();
                    String time = format.format(new Date());
                    System.out.println(Thread.currentThread().getName() + " : " + time + " 开始执行");
                    result = cache5.compute("666");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(result);
            });
        }
        Thread.sleep(5000);
        count.countDown();
        service.shutdown();
    }

}

class ThreadSafeFormatter {

    public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("mm:ss");
        }

        @Override
        public SimpleDateFormat get() {
            return super.get();
        }
    };
}
package future;

import java.util.concurrent.*;

/**
 * get()超时演示，超时后调用cancel()方法处理
 * cancel()传入 true、false的区别
 */
public class Timeout {

    public static void main(String[] args) {
        Timeout timeout = new Timeout();
        timeout.printAd();
    }

    private static final Ad DEFAULT_AD = new Ad("默认显示");
    ExecutorService service = Executors.newFixedThreadPool(10);

    static class Ad {
        String name;

        public Ad(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Ad{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    static class FetchAdTask implements Callable<Ad> {
        @Override
        public Ad call() throws Exception {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println("sleep期间被中断了");
                return new Ad("被中断时显示");
            }
            return new Ad("正常显示");
        }
    }

    public void printAd() {
        Future<Ad> future = service.submit(new FetchAdTask());
        Ad ad = null;
        try {
            ad = future.get(4000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("超时");
            ad = DEFAULT_AD;
            boolean cancel = future.cancel(false);
            System.out.println("cancel结果：" + cancel);
        }
        service.shutdown();
        System.out.println(ad);
    }

}

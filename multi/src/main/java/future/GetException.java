package future;

import java.util.concurrent.*;

/**
 * get方法中抛出异常，for循环演示抛出异常时机。并不是说一产生异常就抛出，而是直到我们get执行时，才会抛出
 * <p>
 * isDone()方法演示
 */
public class GetException {

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Integer> future = service.submit(new CallableTask());
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println(i);
                Thread.sleep(500);
            }
            System.out.println(future.isDone()); // 只是返回是否完成，有没有错误不关心
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }

    static class CallableTask implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            throw new Exception("Callable抛出异常");
        }
    }

}

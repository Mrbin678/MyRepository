package cache;

import cache.computable.Computable;
import cache.computable.MayFail;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 处于安全考虑，缓存需要设置有效期
 * demo7 增加缓存定时清除功能
 */
public class Cache7<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private final Computable<A, V> c;

    public Cache7(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> callable = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<>(callable);
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    System.out.println("从FutureTask调用了计算函数");
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg);
                throw e;
            } catch (InterruptedException e) {
                cache.remove(arg);
                throw e;
            } catch (ExecutionException e) {
                cache.remove(arg);
                System.out.println("计算错误，需要重试");
            }
        }
    }

    public final static ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

    public V computeRandomExpire(A arg) throws InterruptedException {
        long time = (long) (Math.random() * 10000);
        System.out.println("time: " + time);
        return compute(arg, time);
    }

    public V compute(A arg, long expire) throws InterruptedException {
        if (expire > 0) {
            service.schedule(new Runnable() {
                @Override
                public void run() {
                    expire(arg);
                }
            }, expire, TimeUnit.MILLISECONDS);
        }
        return compute(arg);
    }

    public synchronized void expire(A key) {
        Future<V> vFuture = cache.get(key);
        if (vFuture != null) {
            if (vFuture.isDone()) {
                System.out.println("Future任务被取消");
                vFuture.cancel(true);
            }
            System.out.println("过期时间到，清除缓存");
            cache.remove(key);
        }
    }

    public static void main(String[] args) throws Exception {
        Cache7<String, Integer> cache2 = new Cache7<>(new MayFail());
        System.out.println("开始计算");
        Object result = cache2.computeRandomExpire("13");
        System.out.println("第一次计算结果：" + result);
        result = cache2.computeRandomExpire("13");
        System.out.println("第二次计算结果：" + result);
    }

}
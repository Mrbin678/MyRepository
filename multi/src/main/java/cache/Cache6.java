package cache;

import cache.computable.Computable;
import cache.computable.ExpensiveFunction;
import cache.computable.MayFail;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 使用putIfAbsent
 * demo6 彻底解决重复计算问题，但是考虑计算失败如何处理？
 * 使用while捕获计算异常并重试，注意缓存污染问题，需要remove掉被污染的缓存数据
 */
public class Cache6<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private final Computable<A, V> c;

    public Cache6(Computable<A, V> c) {
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

    public static void main(String[] args) throws Exception {
        Cache6<String, Integer> cache2 = new Cache6<>(new MayFail());
        System.out.println("开始计算");
        Object result = cache2.compute("13");
        System.out.println("第一次计算结果：" + result);
        result = cache2.compute("13");
        System.out.println("第二次计算结果：" + result);
    }

}
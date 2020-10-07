package cache;

import cache.computable.Computable;
import cache.computable.ExpensiveFunction;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 使用putIfAbsent
 * demo5 彻底解决重复计算问题，但是考虑计算失败如何处理？
 */
public class Cache5<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private final Computable<A, V> c;

    public Cache5(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws Exception {
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
        return f.get();
    }

    public static void main(String[] args) throws Exception {
        Cache5<String, Integer> cache2 = new Cache5<>(new ExpensiveFunction());
        System.out.println("开始计算");
        Object result = cache2.compute("13");
        System.out.println("第一次计算结果：" + result);
        result = cache2.compute("13");
        System.out.println("第二次计算结果：" + result);
    }

}
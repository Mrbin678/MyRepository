package cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存实现
 * demo1 最初级版
 */
public class Cache {

    private final Map<Object, Object> cache = new HashMap();

    public Object computer(Object obj) throws InterruptedException {
        Object result = cache.get(obj);
        //先检查之前有没有保存过结果
        if (result == null) {
            //如果缓存中找不到，那么现在进行处理，并保存
            result = doComputer(obj);
            cache.put(obj, result);
        }
        return result;
    }

    private Object doComputer(Object obj) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        return obj;
    }

    public static void main(String[] args) throws InterruptedException {
        Cache cache = new Cache();
        System.out.println("开始计算");
        Object result = cache.computer("13");
        System.out.println("第一次计算结果：" + result);
        result = cache.computer("13");
        System.out.println("第二次计算结果：" + result);
    }
}

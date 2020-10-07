package cache.computable;

import java.io.IOException;

/**
 * 耗时计算实现类，模拟有概率计算失败
 */
public class MayFail implements Computable<String, Integer> {
    @Override
    public Integer compute(String arg) throws Exception {
        double r = Math.random();
        if (r > 0.5) {
            throw new IOException("模拟读取文件出错");
        }
        Thread.sleep(3000);
        return Integer.valueOf(arg);
    }
}

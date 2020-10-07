package cache.computable;

/**
 * 耗时计算实现类，实现Computable接口，本身不需具备缓存能力，也不该考虑缓存
 */
public class ExpensiveFunction implements Computable<String, Integer> {

    @Override
    public Integer compute(String arg) throws Exception {
        Thread.sleep(5000);
        return Integer.valueOf(arg);
    }

}

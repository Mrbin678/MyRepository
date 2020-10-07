package cache.computable;

/**
 * 计算函数，用来代表耗时计算，每个计算器都要实现这个接口，这样可以无侵入实现缓存功能，实现代码解藕
 */
public interface Computable<A, V> {

    V compute(A arg) throws Exception;

}

package cas;

/**
 * 模拟CAS操作，等价代码
 */
public class SimulatedCAS {

    private volatile int value;

    /**
     * 等价于CAS操作
     */
    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;
        if (oldValue == expectedValue) {
            value = newValue;
        }
        return oldValue;
    }

}

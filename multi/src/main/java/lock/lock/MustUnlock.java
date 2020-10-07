package lock.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock 不会像synchronized一样，异常时自动释放锁。
 * 所以要在finally中释放锁，以便保证异常时锁一定被释放
 */
public class MustUnlock {

    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        lock.lock();
        try{
            System.out.println(Thread.currentThread().getName()+" 开始");
        }finally {
            lock.unlock();
        }
    }

}

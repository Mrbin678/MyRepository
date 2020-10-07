package lock.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lockInterruptibly避免死锁 demo
 */
public class LockInterruptibly implements Runnable {

    public static void main(String[] args) {
        LockInterruptibly lockInterruptibly = new LockInterruptibly();
        Thread thread1 = new Thread(lockInterruptibly);
        Thread thread2 = new Thread(lockInterruptibly);
        thread1.start();
        thread2.start();
        try{
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
        thread2.interrupt(); // 中断其中一个
    }

    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "尝试获取锁");
        try {
            lock.lockInterruptibly();
            try {
                System.out.println(Thread.currentThread().getName() + "获取到锁");
                Thread.sleep(5000);
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + "睡眠期间被中断");
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + "释放了锁");
            }
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + "获取锁期间被中断");
        }
    }
}

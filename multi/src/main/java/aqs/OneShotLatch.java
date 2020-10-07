package aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 利用AQS自定义一个线程协作器
 */
public class OneShotLatch {

    private final Sync sync = new Sync();

    public void signal() {
        sync.releaseShared(0); // 下面方法，具体实现
    }

    public void await() {
        sync.acquireShared(0); // 下面方法，具体实现
    }

    private class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected int tryAcquireShared(int arg) {
            return (getState() == 1) ? 1 : -1; // 返回1表示没有放入阻塞队列，说明获取锁成功
//            return super.tryAcquireShared(arg);
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1);
            return true;  // 设置1 返回true 表示可以唤醒之前的线程
//            return super.tryReleaseShared(arg);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        OneShotLatch oneShotLatch = new OneShotLatch();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 尝试获取Latch，获取失败则进行等待");
                    oneShotLatch.await();
                    System.out.println(Thread.currentThread().getName() + " 放行，继续执行");
                }
            }).start();
        }
        Thread.sleep(3000);
        oneShotLatch.signal(); // 唤醒线程
    }

}
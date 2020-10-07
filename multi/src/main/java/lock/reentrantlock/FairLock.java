package lock.reentrantlock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁、不公平锁 demo
 */
public class FairLock {

    public static void main(String[] args) {
        PrintQueue printQueue = new PrintQueue();
        Thread thread[] = new Thread[10];
        for (int i = 0; i < 10; i++) {
            thread[i] = new Thread(new Job(printQueue));
        }
        for (int i = 0; i < 10; i++) {
            thread[i].start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Job implements Runnable {

        PrintQueue printQueue;

        public Job(PrintQueue printQueue) {
            this.printQueue = printQueue;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "开始打印");
            printQueue.printJob(new Object());
            System.out.println(Thread.currentThread().getName() + "结束打印");
        }
    }

    static class PrintQueue {
        private Lock queueLock = new ReentrantLock(false);// true/false 设置公平非公平

        public void printJob(Object doc) {
            queueLock.lock();
            try {
                int duration = new Random().nextInt(10) + 1;
                System.out.println(Thread.currentThread().getName() + " 正在打印，需要" + duration);
                Thread.sleep(duration*1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                queueLock.unlock();
            }

            queueLock.lock();
            try {
                int duration = new Random().nextInt(10) + 1;
                System.out.println(Thread.currentThread().getName() + " 正在打印，需要" + duration);
                Thread.sleep(duration*1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                queueLock.unlock();
            }

        }
    }

}

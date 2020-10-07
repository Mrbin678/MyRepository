package lock.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * reentrantlock 案例  电影院预定座位 demo
 */
public class CinemaBookSeat {


    public static void main(String[] args) {
        new Thread(()->bookSeat()).start();
        new Thread(()->bookSeat()).start();
        new Thread(()->bookSeat()).start();
        new Thread(()->bookSeat()).start();
    }

    private static ReentrantLock lock = new ReentrantLock();

    private static void bookSeat() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "开始预定座位");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + "完成预定座位");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}

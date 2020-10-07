package atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AtomicIntegerFiledUpdater 将普通类升级为具有原子功能
 *
 * 升级注意点：必须是可见性的（public），不被static修饰
 */
public class AtomicIntegerFileUpdateDemo implements Runnable {


    public static void main(String[] args) throws InterruptedException {
        tom = new Candidate();
        peter = new Candidate();

        AtomicIntegerFileUpdateDemo r = new AtomicIntegerFileUpdateDemo();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("普通变量：" + peter.score);
        System.out.println("升级变量：" + tom.score);
    }

    static Candidate tom;
    static Candidate peter;

    public static AtomicIntegerFieldUpdater<Candidate> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            peter.score++;
            atomicIntegerFieldUpdater.getAndIncrement(tom);
        }
    }

    public static class Candidate {
        volatile int score;
    }

}

package threadpool;

/**
 * 不用线程池，每个任务都新开一个线程处理，开销太大，超过上限容易内存溢出
 * 为什么使用线程池？反复创建线程开销大，过多线程占用太多内存
 * 解决思路：使用少量线程，避免内存占用过多。让这些线程都保持工作，可以反复执行任务，避免生命周期的损耗
 * 线程池好处：加快响应速度，合理利用CUP和内存，统一管理线程
 */
public class EveryTaskOneThread {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Task());
            thread.start();
        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            System.out.println("测试运行");
        }
    }

}

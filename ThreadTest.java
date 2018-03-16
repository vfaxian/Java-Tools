package liyf;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {

    public static void main(String[] args) throws InterruptedException {
        /*Runnable runnable = new Runnable() {
            private int ticket = 5;

            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    synchronized (this) {
                        if (ticket > 0) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("ticket = " + ticket--);
                        }
                    }
                }
            }
        };
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();*/

        ExecutorService pool = Executors.newFixedThreadPool(2);

        CountDownLatch doneSignal = new CountDownLatch(2);
        VolatileFeatureTest vft = new VolatileFeatureTest(5);
        pool.execute(new Worker(doneSignal, "worker1", vft));
        pool.execute(new Worker(doneSignal, "worker2", vft));
        doneSignal.await();
        System.out.println("all of the threads are finished");
        pool.shutdown();
    }

//    @Test
    public void printThreadInfo() {
        // get the manager(MXBean) of Java.
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // don't need get the information of the monitor and synchronizer
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }
    }

    public static final class Worker implements Runnable {

        private final CountDownLatch doneSignal;
        private VolatileFeatureTest vft;
        private String workerName;

        Worker(CountDownLatch doneSignal, String workerName, VolatileFeatureTest vft) {
            this.doneSignal = doneSignal;
            this.workerName = workerName;
            this.vft = vft;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (vft.get() > 0) {
                        System.out
                                .println("Worker: " + workerName + " was running. " + "ticket = " + vft.getAndReduce());
                    } else {
                        break;
                    }
                }
            }
            doneSignal.countDown();
        }
    }
/**
 * 
 * Used volatile to ensure tickets synchronization in multi-thread
 * @author vfaxian
 *
 */
    public static class VolatileFeatureTest {
        private volatile int ticket;

        public VolatileFeatureTest(int ticket) {
            set(ticket);
        }

        public int get() {
            return ticket;
        }

        public void set(int ticket) {
            this.ticket = ticket;
        }

        public int getAndReduce() {
            if (ticket < 1) {
                return 0;
            }
            return ticket--;
        }
    }

}

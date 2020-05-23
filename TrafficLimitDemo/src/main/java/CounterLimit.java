import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器法，计算一段时间内请求次数
 */
public class CounterLimit {
    Integer count = 10;

    AtomicInteger atomicInteger = new AtomicInteger(count);

    private void startLimit(){
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    atomicInteger.set(count);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public boolean get(){
        return atomicInteger.decrementAndGet() >= 0;
    }

    public static void main(String[] args) {
        final CounterLimit counterLimit = new CounterLimit();
        counterLimit.startLimit();
        int threadCount = 11;
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount);
        Thread[] threads = new Thread[threadCount];
        for(int i=0;i<threadCount;i++){
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    try {
                        cyclicBarrier.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(counterLimit.get()){
                        System.out.println("允许执行");
                    }else {
                        System.out.println("禁止执行");
                    }
                }
            });
            threads[i].start();
        }


    }

}

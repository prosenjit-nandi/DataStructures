package thread;

public class ThreadSample {
    int count = 0;
    int[] buffer = new int[1000];
    Producer producer = new Producer();
    Consumer consumer = new Consumer();
    private static final Object lock = new Object();

    public void runThread(){
        Runnable produceTask = () -> {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    for (int i = 0; i < 100; i++) {
                        producer.produce();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consumeTask = () -> {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    for (int i = 0; i < 100; i++) {
                        consumer.consume();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread pThread = new Thread(produceTask);
        pThread.start();

        Thread cThread = new Thread(consumeTask);
        cThread.start();

        try {
            pThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            cThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class Producer{
        public void produce() throws InterruptedException {
            synchronized (lock) {
                if (count == buffer.length) {
                    lock.wait();
                }
                buffer[count++] = 1;
                lock.notify();
            }
        }
    }

    private class Consumer{
        public void consume() throws InterruptedException {
            synchronized (lock) {
                if (count == 0) {
                    lock.wait();
                }
                buffer[--count] = 0;
                lock.notify();
            }
        }
    }
}

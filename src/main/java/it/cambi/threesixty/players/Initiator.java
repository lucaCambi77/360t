/**
 * 
 */
package it.cambi.threesixty.players;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author luca
 *
 */
public class Initiator extends Thread
{

    private AtomicInteger countDown;

    private BlockingQueue<String> queue;
    private BlockingQueue<String> playerXQueue;
    private CountDownLatch latch;

    private Object lock = new Object();

    /**
     * 
     */
    public Initiator(BlockingQueue<String> queue, AtomicInteger countDown, BlockingQueue<String> playerXQueue, CountDownLatch latch)
    {
        this.queue = queue;
        this.countDown = countDown;
        this.playerXQueue = playerXQueue;
        this.latch = latch;
    }

    @Override
    public void run()
    {
        Thread.currentThread().setName("Initiator");

        System.out.println("Avvio l'initiator");
        while (countDown.get() >= 0)
        {
            try
            {
                synchronized (lock)
                {

                    Thread.sleep(1000);
                    System.out.println("Mando un messaggio all'altro player");

                    playerXQueue.add("Mando un messaggio all'altro player " + (countDown.get() + 1));
                    String message = queue.take();

                    System.out.println("Ho trovo un messaggio dell'altro player:" + message);
                    countDown.decrementAndGet();
                    latch.countDown();
                }
            }
            catch (InterruptedException e)
            {
            }
        }

        System.out.println("Initiator ha terminato il gioco...");
    }

}

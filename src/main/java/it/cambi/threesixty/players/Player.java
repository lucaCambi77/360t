/**
 * 
 */
package it.cambi.threesixty.players;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author luca
 *
 */
public class Player extends Thread
{

    private BlockingQueue<String> queue;
    private BlockingQueue<String> othersQueue;
    private AtomicInteger countDown;

    private Object lock = new Object();

    /**
     * 
     */
    public Player(BlockingQueue<String> queue, AtomicInteger countDown, BlockingQueue<String> othersQueue)
    {
        this.queue = queue;
        this.countDown = countDown;
        this.othersQueue = othersQueue;
    }

    @Override
    public void run()
    {
        System.out.println("Avvio la rumba");

        while (countDown.get() >= 0)
        {

            try
            {
                synchronized (lock)
                {
                    System.out.println("Cerco messaggi dell'initiator");
                    String message = queue.take();
                    System.out.println("Ho trovo un messaggio :" + message);

                    othersQueue.add("Messaggio per l'initiator");
                    System.out.println("Mando un messaggio all'initiator");

                    Thread.sleep(1000);

                }
            }
            catch (InterruptedException e)
            {
            }

        }

    }

}

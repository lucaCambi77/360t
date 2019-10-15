/**
 * 
 */
package it.cambi.threesixty.players;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import it.cambi.threesixty.message.Dispatcher;
import it.cambi.threesixty.players.enums.PlayersEnum;

/**
 * @author luca
 *
 */
public class Initiator extends Thread
{

    private AtomicInteger countDown;

    private BlockingQueue<Dispatcher> queue;
    private BlockingQueue<Dispatcher> playerXQueue;
    private CountDownLatch latch;

    private Object lock = new Object();

    /**
     * 
     */
    public Initiator(BlockingQueue<Dispatcher> queue, AtomicInteger countDown, BlockingQueue<Dispatcher> playerXQueue, CountDownLatch latch)
    {
        this.queue = queue;
        this.countDown = countDown;
        this.playerXQueue = playerXQueue;
        this.latch = latch;
    }

    @Override
    public void run()
    {
        Thread.currentThread().setName(PlayersEnum.INITIATOR.getDescription());

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setPlayerType(PlayersEnum.INITIATOR);

        System.out.println(PlayersEnum.INITIATOR.getDescription() + " thread running");

        while (countDown.get() > 0)
        {
            try
            {
                synchronized (lock)
                {

                    takeMessage();

                    countDown.decrementAndGet();
                    latch.countDown();
                    
                    dispatcher.setMessage(Thread.currentThread().getName() + " is sending message number " + countDown.get());

                    putMessage(dispatcher);


                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException e)
            {
            }
        }

    }

    /**
     * @throws InterruptedException
     */
    private void takeMessage() throws InterruptedException
    {
        Dispatcher dispatcher = queue.take();

        System.out.println(Thread.currentThread().getName() + " ha trovo un messaggio in coda : " + dispatcher.getMessage());
    }

    /**
     * @throws InterruptedException
     */
    public void putMessage(Dispatcher dispatcher) throws InterruptedException
    {
        System.out.println(Thread.currentThread().getName() + " is sending a message");

        playerXQueue.add(dispatcher);
    }

}

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
    private int sentMessages = 1;

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

                    Thread.sleep(1000);

                    dispatcher.setMessage(Thread.currentThread().getName() + " in sending message n." + ++sentMessages);

                    putMessage(dispatcher);

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
        System.out.println(Thread.currentThread().getName() + " is waiting for messages");

        Dispatcher dispatcher = queue.take();

        System.out.println(Thread.currentThread().getName() + " has found a new message : " + dispatcher.getMessage());
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

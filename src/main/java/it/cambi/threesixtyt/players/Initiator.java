/**
 * 
 */
package it.cambi.threesixtyt.players;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import it.cambi.threesixtyt.message.Dispatcher;
import it.cambi.threesixtyt.players.enums.PlayersEnum;

/**
 * @author luca
 *
 *         Initiator player of the game communicating with {@link PlayerX} It loops until count down gets to zero and also keeps alive the main thread
 *         with a {@link CountDownLatch} until required condition is satisfied
 */
public class Initiator extends Thread implements Player
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

                    dispatcher.setMessage(Thread.currentThread().getName() + " is sending message n." + ++sentMessages);

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
    @Override
    public void takeMessage() throws InterruptedException
    {
        System.out.println(Thread.currentThread().getName() + " is waiting for messages");

        Dispatcher dispatcher = queue.take();

        System.out.println(Thread.currentThread().getName() + " has found a new message : " + dispatcher.getMessage());
    }

    /**
     * @throws InterruptedException
     */
    @Override
    public void putMessage(Dispatcher dispatcher) throws InterruptedException
    {
        System.out.println(Thread.currentThread().getName() + " is sending a message");

        playerXQueue.add(dispatcher);
    }

}

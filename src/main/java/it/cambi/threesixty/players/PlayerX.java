/**
 * 
 */
package it.cambi.threesixty.players;

import java.util.concurrent.BlockingQueue;

import it.cambi.threesixty.message.Dispatcher;
import it.cambi.threesixty.players.enums.PlayersEnum;

/**
 * @author luca
 *
 *         Generic player of the game communicating with {@link Initiator} through thread safe {@link BlockingQueue} that waits until a queue has
 *         messages to process
 */
public class PlayerX extends Thread implements Player
{

    private BlockingQueue<Dispatcher> queue;
    private BlockingQueue<Dispatcher> othersQueue;
    private int sentMessages = 0;

    private Object lock = new Object();

    /**
     * 
     */
    public PlayerX(BlockingQueue<Dispatcher> queue, BlockingQueue<Dispatcher> othersQueue)
    {
        this.queue = queue;
        this.othersQueue = othersQueue;
    }

    @Override
    public void run()
    {
        Thread.currentThread().setName(PlayersEnum.PLAYERX.getDescription());

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setPlayerType(PlayersEnum.PLAYERX);

        System.out.println(PlayersEnum.PLAYERX.getDescription() + " thread running");

        while (true)
        {

            try
            {
                synchronized (lock)
                {
                    takeMessage();

                    dispatcher.setMessage(Thread.currentThread().getName() + " is sending message n. " + ++sentMessages);

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
     * 
     */
    @Override
    public void putMessage(Dispatcher dispatcher) throws InterruptedException
    {
        System.out.println(Thread.currentThread().getName() + " is sending a message");

        othersQueue.put(dispatcher);
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

}

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
 */
public class Player extends Thread
{

    private BlockingQueue<Dispatcher> queue;
    private BlockingQueue<Dispatcher> othersQueue;
    private volatile boolean isGame = true;
    private int sentMessages = 0;

    private Object lock = new Object();

    /**
     * 
     */
    public Player(BlockingQueue<Dispatcher> queue, BlockingQueue<Dispatcher> othersQueue)
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

        while (isGame)
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
    private void putMessage(Dispatcher dispatcher) throws InterruptedException
    {
        System.out.println(Thread.currentThread().getName() + " is sending a message");

        othersQueue.put(dispatcher);
    }

    /**
     * @throws InterruptedException
     */
    private void takeMessage() throws InterruptedException
    {
        System.out.println(Thread.currentThread().getName() + " is waiting for messages");
        Dispatcher dispatcher = queue.take();
        if (!dispatcher.isGame())
            stopGame();

        System.out.println(Thread.currentThread().getName() + " has found a new message : " + dispatcher.getMessage());
    }

    private void stopGame()
    {
        isGame = false;
    }

}

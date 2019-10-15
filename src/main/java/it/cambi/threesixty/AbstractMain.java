/**
 * 
 */
package it.cambi.threesixty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import it.cambi.threesixty.constant.Constant;
import it.cambi.threesixty.message.Dispatcher;

/**
 * @author luca
 *
 *         Abstract main class used to define common objects for both games and also useful for tests
 */
public class AbstractMain
{

    private AtomicInteger countDown = new AtomicInteger(Constant.numberOfMessages);

    private CountDownLatch latch = new CountDownLatch(Constant.numberOfMessages);

    private BlockingQueue<Dispatcher> initiatorQueue = new LinkedBlockingQueue<>(Constant.numberOfMessages);

    private BlockingQueue<Dispatcher> playerXQueue = new LinkedBlockingQueue<>(Constant.numberOfMessages);

    public AtomicInteger getCountDown()
    {
        return countDown;
    }

    public CountDownLatch getLatch()
    {
        return latch;
    }

    public BlockingQueue<Dispatcher> getInitiatorQueue()
    {
        return initiatorQueue;
    }

    public BlockingQueue<Dispatcher> getPlayerXQueue()
    {
        return playerXQueue;
    }

}

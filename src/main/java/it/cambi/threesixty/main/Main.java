/**
 * 
 */
package it.cambi.threesixty.main;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import it.cambi.threesixty.constant.Constant;
import it.cambi.threesixty.message.Dispatcher;
import it.cambi.threesixty.players.Initiator;
import it.cambi.threesixty.players.Player;
import it.cambi.threesixty.players.enums.PlayersEnum;

/**
 * @author luca
 *
 */
public class Main
{
    private BlockingQueue<Dispatcher> queue = new ArrayBlockingQueue<>(Constant.numberOfMessages);
    private BlockingQueue<Dispatcher> playerXQueue = new ArrayBlockingQueue<>(Constant.numberOfMessages);
    private AtomicInteger countDown = new AtomicInteger(Constant.numberOfMessages);

    private CountDownLatch latch = new CountDownLatch(Constant.numberOfMessages);
    private static Main instance = new Main();

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException
    {

        instance.play();

    }

    /**
     * @param initiator
     * @param otherPlayer
     */
    private void play() throws InterruptedException
    {
        play(new Initiator(queue, countDown, playerXQueue, latch), new Player(playerXQueue, queue), latch);
    }

    public void play(Initiator initiator, Player playerX, CountDownLatch latch) throws InterruptedException
    {
        /**
         * Initiator send first message
         */
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setPlayerType(PlayersEnum.INITIATOR);
        dispatcher.setMessage("Hello from Initiator");
        initiator.putMessage(dispatcher);

        /**
         * Start threads and wait until count down latch goes to zero
         */
        initiator.start();
        playerX.start();

        latch.await();

        System.out.println(PlayersEnum.INITIATOR.getDescription() + " ha terminato il gioco...");

    }
}

/**
 * 
 */
package it.cambi.threesixty.main;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import it.cambi.threesixty.players.Initiator;
import it.cambi.threesixty.players.Player;

/**
 * @author luca
 *
 */
public class Main
{
    private BlockingQueue<String> initiatorQueue = new ArrayBlockingQueue<>(9);
    private BlockingQueue<String> otherPlayerQueue = new ArrayBlockingQueue<>(9);
    private AtomicInteger countDown = new AtomicInteger(9);

    private static Main instance = new Main();

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        instance.play();

    }

    private void play()
    {
        Initiator initiator = new Initiator(initiatorQueue, countDown, otherPlayerQueue, new CountDownLatch(10));
        Player otherPlayer = new Player(otherPlayerQueue, countDown, initiatorQueue);

        initiator.start();
        otherPlayer.start();
    }
}

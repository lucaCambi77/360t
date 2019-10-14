/**
 * 
 */
package it.cambi.threesixty.thread.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import it.cambi.threesixty.players.Initiator;
import it.cambi.threesixty.players.Player;

/**
 * @author luca
 *
 */
@ExtendWith(MockitoExtension.class)
public class GameThreadTest
{
    private BlockingQueue<String> queue = new ArrayBlockingQueue<>(9);
    private BlockingQueue<String> playerXQueue = new ArrayBlockingQueue<>(9);
    private AtomicInteger countDown = new AtomicInteger(9);

    @Test
    public void testInput() throws InterruptedException
    {
        CountDownLatch latch = new CountDownLatch(10);

        Initiator initiator = Mockito.spy(new Initiator(queue, countDown, playerXQueue, latch));
        Player otherPlayer = Mockito.spy(new Player(playerXQueue, countDown, queue));

        initiator.start();
        otherPlayer.start();

        latch.await();

        assertEquals(0, countDown.get());
    }
}

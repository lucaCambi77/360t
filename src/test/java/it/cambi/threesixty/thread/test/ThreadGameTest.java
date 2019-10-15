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

import it.cambi.threesixty.constant.Constant;
import it.cambi.threesixty.main.Main;
import it.cambi.threesixty.message.Dispatcher;
import it.cambi.threesixty.players.Initiator;
import it.cambi.threesixty.players.Player;
import it.cambi.threesixty.players.enums.PlayersEnum;

/**
 * @author luca
 *
 */
@ExtendWith(MockitoExtension.class)
public class ThreadGameTest
{
    private BlockingQueue<Dispatcher> queue = new ArrayBlockingQueue<>(Constant.numberOfMessages);
    private BlockingQueue<Dispatcher> playerXQueue = new ArrayBlockingQueue<>(Constant.numberOfMessages);
    private AtomicInteger countDown = new AtomicInteger(Constant.numberOfMessages);

    private Main main = new Main();

    @Test
    public void testInput() throws InterruptedException
    {
        CountDownLatch latch = new CountDownLatch(Constant.countDownlLatch);

        Initiator initiator = Mockito.spy(new Initiator(queue, countDown, playerXQueue, latch));
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setPlayerType(PlayersEnum.INITIATOR);
        dispatcher.setMessage("Hello from Initiator");
        initiator.putMessage(dispatcher);

        Player otherPlayer = Mockito.spy(new Player(playerXQueue, queue));

        main.play(initiator, otherPlayer);

        latch.await();

        assertEquals(0, countDown.get());
    }
}

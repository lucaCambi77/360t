/**
 * 
 */
package it.cambi.threesixty.thread.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import it.cambi.threesixty.AbstractMain;
import it.cambi.threesixty.Main;
import it.cambi.threesixty.players.Initiator;
import it.cambi.threesixty.players.Player;

/**
 * @author luca
 *
 */
@ExtendWith(MockitoExtension.class)
public class ThreadGameTest extends AbstractMain
{

    private Main main = new Main();

    @Test
    public void testThreadGame() throws InterruptedException
    {

        Initiator initiator = Mockito.spy(new Initiator(getInitiatorQueue(), getCountDown(), getPlayerXQueue(), getLatch()));

        Player playerX = Mockito.spy(new Player(getPlayerXQueue(), getInitiatorQueue()));

        main.play(initiator, playerX, getLatch());

        assertEquals(0, getCountDown().get());

    }
}

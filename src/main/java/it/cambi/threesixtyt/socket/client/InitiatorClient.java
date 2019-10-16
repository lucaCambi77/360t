/**
 * 
 */
package it.cambi.threesixtyt.socket.client;

import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.cambi.threesixtyt.message.Dispatcher;
import it.cambi.threesixtyt.message.SocketDispatcher;
import it.cambi.threesixtyt.players.Player;
import it.cambi.threesixtyt.players.enums.PlayersEnum;

/**
 * 
 * @author luca
 *
 */
public class InitiatorClient extends AbstractClient implements Player
{
    private AtomicInteger countDown;
    private CountDownLatch latch;
    private int sentMessages = 1;

    public InitiatorClient(String IPAddress, int port, CountDownLatch latchIn, AtomicInteger countDownIn) throws Exception
    {
        setSocket(new Socket(IPAddress, port));
        setMessages(new LinkedBlockingQueue<SocketDispatcher>());
        setServer(new ConnectionToServer(getSocket()));

        this.latch = latchIn;
        this.countDown = countDownIn;

        SocketDispatcher dispatcherInitiator = new SocketDispatcher();
        dispatcherInitiator.setPlayerType(PlayersEnum.INITIATOR);
        dispatcherInitiator.setSocket(getSocketString());

        Thread messageHandling = new Thread()
        {
            public void run()
            {
                while (countDown.get() > 0)
                {
                    try
                    {
                        takeMessage();

                        putMessage(dispatcherInitiator);
                    }
                    catch (InterruptedException | JsonProcessingException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }

        };
        messageHandling.setDaemon(true);
        messageHandling.start();
    }

    @Override
    public <T extends Dispatcher> void putMessage(T dispatcher) throws InterruptedException, JsonProcessingException
    {
        dispatcher.setMessage("Initiator is sending message number " + ++sentMessages);

        send((SocketDispatcher) dispatcher);

    }

    @Override
    public void takeMessage() throws InterruptedException
    {
        SocketDispatcher message = getMessages().take();

        System.out.println("Initiator says ... Message Received: " + message.getMessage());

        countDown.decrementAndGet();
        latch.countDown();

        Thread.sleep(1000);
    }

}

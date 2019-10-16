/**
 * 
 */
package it.cambi.threesixtyt;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import it.cambi.threesixtyt.constant.Constant;
import it.cambi.threesixtyt.message.Dispatcher;
import it.cambi.threesixtyt.message.SocketDispatcher;
import it.cambi.threesixtyt.players.Initiator;
import it.cambi.threesixtyt.players.PlayerX;
import it.cambi.threesixtyt.players.enums.PlayersEnum;
import it.cambi.threesixtyt.socket.client.InitiatorClient;
import it.cambi.threesixtyt.socket.client.PlayerXClient;
import it.cambi.threesixtyt.socket.server.SocketServer;

/**
 * @author luca
 * 
 *         Main entry point of the application with 2 different games depending on the args input.
 * 
 *         {@link #playSocket} will start a game simulating two client communicating over a network with sockets
 * 
 *         {@link #playThread} will start a game of two simple Thread communicating with queues
 * 
 *         Both games anyway are based on a count down that will stop after required condition is satisfied for the initiator.
 * 
 *         {@link CountDownLatch} will help to wait for the condition to be completed
 */
public class Main extends AbstractMain
{
    private static Main instance = new Main();

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        if ("THREAD".equals(args[0]))
        {
            instance.playThread();
        }

        if ("SOCKET".equals(args[0]))
        {

            instance.startServer();
            instance.playSocket();
        }

    }

    /**
     * @throws IOException
     * 
     */
    public void startServer() throws IOException
    {

        new SocketServer(Constant.socketPort);

    }

    private void playSocket() throws Exception
    {

        InitiatorClient initiatorClient = new InitiatorClient(Constant.localhost, Constant.socketPort, getLatch(), getCountDown());

        play(initiatorClient, new PlayerXClient(Constant.localhost, Constant.socketPort), getLatch());
    }

    public void play(InitiatorClient initiator, PlayerXClient playerX, CountDownLatch latch) throws InterruptedException, IOException
    {
        System.out.println("Game started ...");

        SocketDispatcher initiatorDispatcher = new SocketDispatcher();
        initiatorDispatcher.setPlayerType(PlayersEnum.INITIATOR);
        initiatorDispatcher.setSocket(initiator.getSocketString());

        initiatorDispatcher.setMessage("Message number 1 form InitiatorClient : Hello world!");

        initiator.send(initiatorDispatcher);

        latch.await();

        System.out.println("Game over");

    }

    private void playThread() throws InterruptedException
    {
        Initiator inititator = new Initiator(getInitiatorQueue(), getCountDown(), getPlayerXQueue(), getLatch());
        PlayerX playerX = new PlayerX(getPlayerXQueue(), getInitiatorQueue());

        play(inititator, playerX, getLatch());
    }

    public void play(Initiator initiator, PlayerX playerX, CountDownLatch latch) throws InterruptedException
    {
        System.out.println("Game started ...");

        /**
         * Initiator send first message
         */
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setPlayerType(PlayersEnum.INITIATOR);
        dispatcher.setMessage("Message number 1 from Initiator : Hello world!");
        initiator.putMessage(dispatcher);

        /**
         * Start threads and wait until count down latch goes to zero
         */
        initiator.setDaemon(true);
        initiator.start();

        playerX.setDaemon(true);
        playerX.start();

        latch.await();

        System.out.println(PlayersEnum.INITIATOR.getDescription() + " terminated the game ...");

        System.out.println("... Game over!");

    }
}

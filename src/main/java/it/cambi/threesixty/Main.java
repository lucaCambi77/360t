/**
 * 
 */
package it.cambi.threesixty;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import it.cambi.threesixty.constant.Constant;
import it.cambi.threesixty.message.Dispatcher;
import it.cambi.threesixty.message.SocketDispatcher;
import it.cambi.threesixty.players.Initiator;
import it.cambi.threesixty.players.Player;
import it.cambi.threesixty.players.enums.PlayersEnum;
import it.cambi.threesixty.socket.client.InitiatorClient;
import it.cambi.threesixty.socket.client.PlayerXClient;
import it.cambi.threesixty.socket.server.SocketServer;

/**
 * @author luca
 *
 */
public class Main extends AbstractMain
{
    private static Main instance = new Main();

    /**
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException
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

    public void playSocket() throws InterruptedException, IOException
    {

        InitiatorClient initiatorClient = new InitiatorClient("127.0.0.1", Constant.socketPort, getLatch(), getCountDown());

        play(initiatorClient, new PlayerXClient("127.0.0.1", Constant.socketPort), getLatch());
    }

    public void play(InitiatorClient initiator, PlayerXClient playerX, CountDownLatch latch) throws InterruptedException, IOException
    {
        System.out.println("Game started ...");

        SocketDispatcher initiatorDispatcher = new SocketDispatcher();
        initiatorDispatcher.setPlayerType(PlayersEnum.INITIATOR);
        initiatorDispatcher.setSocket(initiator.getSocketString());

        initiatorDispatcher.setMessage("Hello from InitiatorClient");

        initiator.send(initiatorDispatcher);

        latch.await();

        System.out.println("Game over");

    }

    private void playThread() throws InterruptedException
    {
        Initiator inititator = new Initiator(getInitiatorQueue(), getCountDown(), getPlayerXQueue(), getLatch());
        Player playerX = new Player(getPlayerXQueue(), getInitiatorQueue());

        play(inititator, playerX, getLatch());
    }

    public void play(Initiator initiator, Player playerX, CountDownLatch latch) throws InterruptedException
    {
        System.out.println("Game started ...");

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
        initiator.setDaemon(true);
        initiator.start();

        playerX.setDaemon(true);
        playerX.start();

        latch.await();

        System.out.println(PlayersEnum.INITIATOR.getDescription() + " terminated the game ...");

        System.out.println("... Game over!");

    }
}

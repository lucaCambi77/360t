/**
 * 
 */
package it.cambi.threesixty.socket.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cambi.threesixty.message.SocketDispatcher;
import it.cambi.threesixty.players.enums.PlayersEnum;

public class PlayerXClient
{
    private ConnectionToServer server;
    private LinkedBlockingQueue<SocketDispatcher> messages;
    private Socket socket;
    private ObjectMapper objectMapper = new ObjectMapper();

    public PlayerXClient(String IPAddress, int port) throws IOException
    {
        socket = new Socket(IPAddress, port);
        messages = new LinkedBlockingQueue<SocketDispatcher>();
        server = new ConnectionToServer(socket);

        SocketDispatcher dispatcherInitiator = new SocketDispatcher();
        dispatcherInitiator.setPlayerType(PlayersEnum.PLAYERX);
        dispatcherInitiator.setSocket(getSocketString());

        Thread messageHandling = new Thread()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        SocketDispatcher message = messages.take();

                        System.out.println("PlayerX says ... Message Received: " + message.getMessage());

                        dispatcherInitiator.setMessage("PlayerX is sending message number ");

                        send(dispatcherInitiator);
                    }
                    catch (InterruptedException | JsonProcessingException e)
                    {
                    }
                }
            }
        };

        messageHandling.start();
    }

    class ConnectionToServer
    {
        BufferedReader in;
        PrintWriter out;
        Socket socket;

        ConnectionToServer(Socket socket) throws IOException
        {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            Thread read = new Thread()
            {
                public void run()
                {
                    while (true)
                    {
                        try
                        {
                            SocketDispatcher obj = objectMapper.readValue(in.readLine(), SocketDispatcher.class);
                            messages.put(obj);
                        }
                        catch (IOException | InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            };

            read.start();
        }

        private void write(SocketDispatcher obj) throws JsonProcessingException
        {
            out.println(objectMapper.writeValueAsString(obj));
        }

    }

    public void send(SocketDispatcher obj) throws JsonProcessingException
    {
        server.write(obj);
    }

    public String getSocketString()
    {
        return socket.getInetAddress().toString() + socket.getLocalPort() + socket.getPort();
    }
}

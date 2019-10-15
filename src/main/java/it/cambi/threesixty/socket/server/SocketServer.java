/**
 * 
 */
package it.cambi.threesixty.socket.server;

import java.io.BufferedReader;
/**
 * @author luca
 *
 */
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cambi.threesixty.message.SocketDispatcher;

public class SocketServer
{
    private Map<String, ConnectionToClient> clientList;
    private LinkedBlockingQueue<SocketDispatcher> messages;
    private ServerSocket serverSocket;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @throws IOException
     */
    public SocketServer(int port) throws IOException
    {
        clientList = new HashMap<String, ConnectionToClient>();
        messages = new LinkedBlockingQueue<SocketDispatcher>();
        serverSocket = new ServerSocket(port);

        Thread accept = new Thread()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        System.out.println("Server listening on port: " + port);

                        Socket s = serverSocket.accept();
                        clientList.put(s.getInetAddress().toString() + s.getPort() + s.getLocalPort(), new ConnectionToClient(s));

                        System.out.println("Connected clients ... " + clientList.size());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        accept.setDaemon(true);
        accept.start();

        Thread messageHandling = new Thread()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        SocketDispatcher message = messages.take();

                        System.out.println("Server says ... Received from : " + message.getPlayerType().getDescription() + " , message : "
                                + message.getMessage());

                        sendToAll(message);
                    }
                    catch (InterruptedException | JsonProcessingException e)
                    {
                    }
                }
            }
        };

        messageHandling.setDaemon(true);
        messageHandling.start();

    }

    public void stop() throws IOException
    {
        serverSocket.close();
    }

    class ConnectionToClient
    {
        BufferedReader in;
        PrintWriter out;
        Socket socket;

        ConnectionToClient(Socket socket) throws IOException
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
                            String obj = in.readLine();
                            messages.put(objectMapper.readValue(obj, SocketDispatcher.class));
                        }
                        catch (IOException | InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            };
            
            read.setDaemon(true);
            read.start();
        }

        public void write(SocketDispatcher obj) throws JsonProcessingException
        {

            out.println(objectMapper.writeValueAsString(obj));
        }
    }

    public void sendToAll(SocketDispatcher message) throws JsonProcessingException
    {
        for (Entry<String, ConnectionToClient> client : clientList.entrySet())
            if (client.getKey().compareTo(message.getSocket()) != 0)
                client.getValue().write(message);
    }
}
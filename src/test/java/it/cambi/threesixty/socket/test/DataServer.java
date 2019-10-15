/**
 * 
 */
package it.cambi.threesixty.socket.test;

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
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class DataServer
{
    private ArrayList<ConnectionToClient> clientList;
    private LinkedBlockingQueue<Object> messages;
    private ServerSocket serverSocket;

    private AtomicInteger countDown = new AtomicInteger(9);

    /**
     * @throws IOException
     */
    public DataServer(int port) throws IOException
    {
        clientList = new ArrayList<ConnectionToClient>();
        messages = new LinkedBlockingQueue<Object>();
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
                        clientList.add(new ConnectionToClient(s));
                        System.out.println("Connected clients ... " + clientList.size());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        accept.start();

        Thread messageHandling = new Thread()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        Object message = messages.take();
                        // Do some handling here...
                        System.out.println("Server says ... Message Received: " + message);
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        };

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

        public void write(String obj)
        {

            out.println(obj);
        }
    }

    public void sendToOne(int index, String message) throws IndexOutOfBoundsException
    {
        clientList.get(index).write(message);
    }

    public void sendToAll(String message)
    {
        for (ConnectionToClient client : clientList)
            client.write(message);
    }
}
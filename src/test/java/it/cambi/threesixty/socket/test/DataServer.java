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

    public static void main(String[] args) throws IOException
    {
        DataServer server = new DataServer();
        server.start();
    }

    private AtomicInteger countDown = new AtomicInteger(9);

    /**
     * @throws IOException
     */
    private void start() throws IOException
    {
        clientList = new ArrayList<ConnectionToClient>();
        messages = new LinkedBlockingQueue<Object>();
        serverSocket = new ServerSocket(59090);

        Thread accept = new Thread()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        Socket s = serverSocket.accept();
                        clientList.add(new ConnectionToClient(s));
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
                        System.out.println("Message Received: " + message);
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        };

        messageHandling.start();

        while (countDown.get() >= 0)
        {
        }

        stop();
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
            out = new PrintWriter(socket.getOutputStream());

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

        public void write(Object obj)
        {

            out.println(obj);
        }
    }
}
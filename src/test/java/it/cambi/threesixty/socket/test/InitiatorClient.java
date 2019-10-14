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

public class InitiatorClient
{
    private ConnectionToServer server;
    private LinkedBlockingQueue<String> messages;
    private Socket socket;

    public InitiatorClient(String IPAddress, int port) throws IOException
    {
        socket = new Socket(IPAddress, port);
        messages = new LinkedBlockingQueue<String>();
        server = new ConnectionToServer(socket);

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

        private void write(Object obj)
        {
            out.println(obj);
        }

    }

    public void send(Object obj)
    {
        server.write(obj);
    }
}

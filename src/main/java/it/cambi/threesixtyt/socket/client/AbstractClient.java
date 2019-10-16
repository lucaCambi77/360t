/**
 * 
 */
package it.cambi.threesixtyt.socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cambi.threesixtyt.message.SocketDispatcher;

/**
 * @author luca
 * 
 *         Abstract client where an instance of {@link ConnectionServer} can be defined to stream data over the {@link SocketServer}
 *
 */
public abstract class AbstractClient
{
    private ConnectionToServer server;
    private LinkedBlockingQueue<SocketDispatcher> messages;
    private Socket socket;
    private ObjectMapper objectMapper = new ObjectMapper();

    class ConnectionToServer
    {
        BufferedReader in;
        PrintWriter out;
        Socket socket;

        ConnectionToServer(Socket socket) throws IOException
        {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);

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
            read.setDaemon(true);
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

    public ConnectionToServer getServer()
    {
        return server;
    }

    public LinkedBlockingQueue<SocketDispatcher> getMessages()
    {
        return messages;
    }

    public Socket getSocket()
    {
        return socket;
    }

    public void setServer(ConnectionToServer server)
    {
        this.server = server;
    }

    public void setMessages(LinkedBlockingQueue<SocketDispatcher> messages)
    {
        this.messages = messages;
    }

    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }
}

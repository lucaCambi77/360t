/**
 * 
 */
package it.cambi.threesixty.socket.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.cambi.threesixty.players.enums.PlayersEnum;

/**
 * A command line client for the date server. Requires the IP address of the server as the sole argument. Exits after printing the response.
 */
public class PlayerXClient
{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException
    {

        PlayerXClient client = new PlayerXClient();
        client.startConnection("127.0.0.1", 59090);

        Message message = new Message(PlayersEnum.PLAYERX);
        message.setMessage("Topa");

        System.out.println("Server response: " + client.sendMessage(message));
    }

    public void startConnection(String ip, int port) throws UnknownHostException, IOException
    {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public <T extends Message> String sendMessage(T msg) throws IOException
    {
        out.println(mapper.writeValueAsString(msg));
        String resp = in.readLine();
        return resp;
    }
}

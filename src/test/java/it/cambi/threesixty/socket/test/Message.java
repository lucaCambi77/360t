/**
 * 
 */
package it.cambi.threesixty.socket.test;

import it.cambi.threesixty.players.enums.PlayersEnum;

/**
 * @author luca
 *
 */
public class Message
{

    private String message;
    private PlayersEnum playerType;

    public Message()
    {
    }

    public Message(PlayersEnum playerType)
    {
        this.playerType = playerType;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public PlayersEnum getPlayerType()
    {
        return playerType;
    }

    public void setPlayerType(PlayersEnum playerType)
    {
        this.playerType = playerType;
    }
}

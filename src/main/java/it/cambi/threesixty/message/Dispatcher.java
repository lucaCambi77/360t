/**
 * 
 */
package it.cambi.threesixty.message;

import it.cambi.threesixty.players.enums.PlayersEnum;

/**
 * @author luca
 *
 *         Dispatcher class user to share messages with the queues
 * 
 *         {@link SocketDispatcher } inherits from this class and it can also define a socket string id as a unique definition of a Socket in the game
 */
public class Dispatcher
{

    private String message;
    private PlayersEnum playerType;
    private boolean isGame = true;

    public Dispatcher()
    {
    }

    public Dispatcher(PlayersEnum playerType)
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

    public boolean isGame()
    {
        return isGame;
    }

    public void setGame(boolean isGame)
    {
        this.isGame = isGame;
    }
}

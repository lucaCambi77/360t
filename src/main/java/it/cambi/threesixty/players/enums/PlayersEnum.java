/**
 * 
 */
package it.cambi.threesixty.players.enums;

/**
 * @author luca
 *
 */
public enum PlayersEnum
{

    INITIATOR("Intiator"), PLAYERX("PlayerX");

    private String description;

    PlayersEnum(String description)
    {
        this.description = description;

    }

    public String getDescription()
    {
        return description;
    }

}

/**
 * 
 */
package it.cambi.threesixtyt.players;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.cambi.threesixtyt.message.Dispatcher;

/**
 * @author luca
 *
 *         Player interface for thread game that put and take messages from a queue
 */
public interface Player
{

    <T extends Dispatcher> void putMessage(T dispatcher) throws InterruptedException, JsonProcessingException;

    void takeMessage() throws InterruptedException;

}

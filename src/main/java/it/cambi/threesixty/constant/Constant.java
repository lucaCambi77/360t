/**
 * 
 */
package it.cambi.threesixty.constant;

/**
 * @author luca
 *
 */
public class Constant
{

    public final static int numberOfMessages = 11; // 10 + 1 -> 10 messages to be sent and receive plus one to count down to zero
    public final static int countDownlLatch = numberOfMessages + 1; // count down latch terminates at zero, so it must be one unit greater then
                                                                    // number of messages
}

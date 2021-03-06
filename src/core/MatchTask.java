/**
 * 
 */
package core;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class MatchTask {
    public enum Command { SHUTDOWN, MATCHING, BARRIER };
    public Command command;
    public Point matchpoint;
    public BufferedImage scene;
    
    public MatchTask(Command cmd) {
        command = cmd;
    }
}

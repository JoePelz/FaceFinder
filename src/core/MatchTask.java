/**
 * 
 */
package core;

import java.awt.Point;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class MatchTask {
    public enum Command { SHUTDOWN, MATCHING, BARRIER };
    public Command command;
    public Point matchpoint;
}

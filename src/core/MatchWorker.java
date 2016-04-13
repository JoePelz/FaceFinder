/**
 * 
 */
package core;

import java.awt.Point;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class MatchWorker implements Runnable {
    private BlockingQueue<MatchTask> workQueue;
    
    public MatchWorker(BlockingQueue<MatchTask> work) {
        
    }
    
    @Override
    public void run() {
        MatchTask task;
        while (true) {
            try {
                task = workQueue.take();
            } catch (InterruptedException e) {
                return;
            }
            switch(task.command) {
            case BARRIER:
                signalBarrier();
                break;
            case MATCHING:
                checkMatch(task.matchpoint);
                break;
            case SHUTDOWN:
                return;
            default:
                return;
            }
        }
    }
    
    private void signalBarrier() {
        
    }

    public void checkMatch(Point point) {
        
    }
}

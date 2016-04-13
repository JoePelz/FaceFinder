/**
 * 
 */
package core;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import facerecog.FaceImage;
import facerecog.FaceRecognition;
import utilities.Thumbnails;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class MatchWorker implements Runnable {
    private BlockingQueue<MatchTask> workQueue;
    private CyclicBarrier barrier;
    private FaceRecognition recognizer;
    private FaceFinder parent;
    
    public MatchWorker(BlockingQueue<MatchTask> work, CyclicBarrier barrier, FaceRecognition recog, FaceFinder owner) {
        workQueue = work;
        this.barrier = barrier;
        recognizer = recog;
        parent = owner;
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
                checkMatch(task.matchpoint, task.scene);
                break;
            case SHUTDOWN:
                return;
            default:
                return;
            }
        }
    }
    
    private void signalBarrier() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void checkMatch(Point point, BufferedImage scene) {
        BufferedImage tempImg;
        double tempDist;
        //tempImg = Thumbnails.scaleTarget(scene, point.x, point.y, 128);
        //tempImg = Thumbnails.getWider(tempImg);
        tempImg = Thumbnails.scaleTarget(scene, point.x, point.y);
        FaceImage testFace = new FaceImage(tempImg, "testFace");
        tempDist = recognizer.getThetaDistance(testFace);
        parent.gridResult(point, tempDist);
    }
}

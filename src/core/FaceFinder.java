/**
 * 
 */
package core;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

import core.EntryPoint.Mode;
import core.MatchTask.Command;
import facerecog.FaceImage;
import facerecog.FaceRecognition;
import facerecog.FaceSpaceTheta;
import facerecog.ProcessResult;
import ui.ImagePanel;
import utilities.Thumbnails;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class FaceFinder {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final int RES = 256;
    private static final int N_THREADS = Runtime.getRuntime().availableProcessors();
    
    private BlockingQueue<MatchTask> workQueue = new LinkedBlockingQueue<MatchTask>();
    private CyclicBarrier barrier = new CyclicBarrier(N_THREADS + 1);
    private Thread[] matchWorkers; 
    private FaceRecognition recognizer;
    private ImagePanel ref;
    private double bestDist;
    private Point bestPoint = new Point();
    
    
    public FaceFinder(FaceRecognition recog, ImagePanel panel) {
        recognizer = recog;
        ref = panel;
        matchWorkers = new Thread[N_THREADS];
        for (int i = 0; i < N_THREADS; i++) {
            matchWorkers[i] = new Thread(new MatchWorker(workQueue, barrier, recognizer, this));
            matchWorkers[i].start();
        }
    }
    
    public FaceImage findBest(BufferedImage scene) {
        BufferedImage result;
        FaceImage testFace;
        Point best = new Point(320, 240);
        
        if (EntryPoint.mode == Mode.BESTMATCH || EntryPoint.mode == Mode.DEBUG){
            best = gridSearch(scene);
        }
        
        result = Thumbnails.scaleTarget(scene, best.x, best.y);
        testFace = new FaceImage(result, "testFace");
        
        ref.setCenter(best, true, bestDist);
        return testFace;
    }
    
    public Point gridSearch(BufferedImage scene) {
        int xrange = WIDTH - RES;
        int yrange = HEIGHT - RES;
        int steps = 4;
        
        //TODO: optimization available here. Instead of generating an entire image for each face test,
        //  just create an iterator that will access the important pixels?
        
        //test many positions for faces.
        bestDist = Double.MAX_VALUE;
        MatchTask task;
        for (int x = RES/2; x <= WIDTH - RES/2; x += xrange / steps) {
            for (int y = RES / 2; y <= HEIGHT - RES/2; y += yrange / steps) {
                task = new MatchTask(Command.MATCHING);
                task.matchpoint = new Point(x, y);
                task.scene = scene;
                workQueue.add(task);
            }
        }
        
        for (int i = 0; i < N_THREADS; i++) {
            workQueue.add(new MatchTask(Command.BARRIER));
        }
        
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        
        return bestPoint;
    }
    
    public synchronized void gridResult(Point p, double dist) {
        ref.setCenter(p, false, dist);
        if (dist < bestDist) {
            bestDist = dist;
            bestPoint.setLocation(p);
        }
    }
}














/**
 * 
 */
package core;

import java.awt.Point;
import java.awt.image.BufferedImage;

import facerecog.FaceImage;
import facerecog.FaceRecognition;
import facerecog.FaceSpaceTheta;
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
    
    private FaceRecognition recognizer;
    private ImagePanel ref;
    
    
    public FaceFinder(FaceRecognition recog, ImagePanel panel) {
        recognizer = recog;
        ref = panel;
    }
    
    public BufferedImage findBest(BufferedImage scene) {
        BufferedImage result;

        Point best = gridSearch(scene);
        result = Thumbnails.scaleTarget(scene, best.x, best.y, 128);
        result = Thumbnails.getWider(result);
        FaceImage testFace = new FaceImage(result, "testFace");
        
        double theta = recognizer.getThetaDistance(testFace);
        FaceSpaceTheta thetaRange = recognizer.getFaceSpaceTheta();
        if (theta < thetaRange.getMaxTheta()) {
            System.out.println("FACE!  -  Distance theta: " + theta);            
        } else {
            System.out.println("       -  Distance theta: " + theta);
        }
        ref.setCenter(best);
        return result;
    }
    
    public Point gridSearch(BufferedImage scene) {
        int xrange = WIDTH - RES;
        int yrange = HEIGHT - RES;
        int steps = 4;
        
        BufferedImage tempImg;
        Point best = new Point();
        double bestDist = Double.MAX_VALUE;
        double tempDist;
        
        //TODO: optimization available here. Instead of generating an entire image for each face test,
        //  just create an iterator that will access the important pixels.
        
        //test many positions for faces.
        for (int x = RES/2; x <= WIDTH - RES/2; x += xrange / steps) {
            for (int y = RES / 2; y <= HEIGHT - RES/2; y += yrange / steps) {
                tempImg = Thumbnails.scaleTarget(scene, x, y, 128);
                tempImg = Thumbnails.getWider(tempImg);
                FaceImage testFace = new FaceImage(tempImg, "testFace");
                tempDist = recognizer.getThetaDistance(testFace);
                if (tempDist < bestDist) {
                    bestDist = tempDist;
                    best.x = x;
                    best.y = y;
                }
            }
        }
        
        return best;
    }
}














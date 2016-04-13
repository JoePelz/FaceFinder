/**
 * 
 */
package core;

import java.awt.image.BufferedImage;
import java.util.SortedMap;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import facerecog.FaceImage;
import facerecog.FaceRecognition;
import ui.CardPanel;
import ui.ImagePanel;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class CameraWorker implements Runnable {
    private Webcam webcam;
    private ImagePanel ip;
    private CardPanel ipCrop;
    private CardPanel ipResult;
    private volatile boolean active;
    private volatile boolean snapshot;
    private BufferedImage bmp_new;
    private BufferedImage thumbnail;
    private FaceFinder faceFinder;
    private FaceRecognition faceRecognizer;
    
    public CameraWorker(ImagePanel destination, CardPanel crop, CardPanel result) {
        ip = destination;
        ipCrop = crop;
        ipResult = result;
        active = true;
        snapshot = false;
        faceRecognizer = new FaceRecognition();
        faceFinder = new FaceFinder(faceRecognizer, ip);
    }
    
    public synchronized void stop() {
        active = false;
    }
    
    public synchronized void toggleSnapshot() {
        snapshot = !snapshot;
    }

    @Override
    public void run() {
        //open camera
        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();
        while (active) {
            //get image from camera
            getNewImage();
            //save image for UI Thread
            ip.setImage(bmp_new);
            
            /*
            if (snapshot && doMatch) {
                //leave ipCrop alone now.
                checkForMatch();
                doMatch = false;
            } else if (!snapshot){
                thumbnail = Thumbnails.scaleTarget(bmp_new, 320, 240, 128);
                ipCrop.setImage(thumbnail);
            }
            */
            thumbnail = faceFinder.findBest(bmp_new);
            
            ipCrop.setImage(thumbnail, "  Test Face");
            checkForMatch();
        }
        webcam.close();
    }
    
    private void checkForMatch() {
        
        /*
        BufferedImage base = null;
        try {
            base = ImageIO.read(new File("Joe_01.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        FaceImage testFace = new FaceImage(base, "testFace");
        */
        
        
        //do EigenMatching
        FaceImage testFace = new FaceImage(thumbnail, "testFace");
        SortedMap<Double, FaceImage> map = faceRecognizer.compare(testFace);
        
        //display best image in ipResult
        FaceImage result = map.get(map.firstKey());
        ipResult.setImage(result, result.getName().substring(0, result.getName().length() - 7));
        System.out.println("          best pic theta: " + map.firstKey());
    }
    
    private void getNewImage() {
        //bmp_old = bmp_new;
        if (webcam.isOpen()) {
            bmp_new = webcam.getImage();
        }
    }
}

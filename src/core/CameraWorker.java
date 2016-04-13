/**
 * 
 */
package core;

import java.awt.image.BufferedImage;
import java.util.SortedMap;
import java.util.concurrent.Semaphore;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import core.EntryPoint.Mode;
import facerecog.FaceImage;
import facerecog.FaceRecognition;
import facerecog.ProcessResult;
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
    private FaceImage thumbnail;
    private FaceFinder faceFinder;
    private FaceRecognition faceRecognizer;
    private Semaphore sem_wait;
    
    public CameraWorker(ImagePanel destination, CardPanel crop, CardPanel result) {
        ip = destination;
        ipCrop = crop;
        ipResult = result;
        active = true;
        snapshot = false;
        faceRecognizer = new FaceRecognition();
        faceFinder = new FaceFinder(faceRecognizer, ip);
        sem_wait = new Semaphore(0);
    }
    
    public synchronized void stop() {
        active = false;
    }
    
    public synchronized void toggleSnapshot() {
        snapshot = !snapshot;
        sem_wait.release();
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
            
            thumbnail = faceFinder.findBest(bmp_new);
            
            ipCrop.setImage(thumbnail, "  Test Face");
            checkForMatch();
            
            if (EntryPoint.mode == Mode.DEBUG) {
                try {
                    sem_wait.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
        SortedMap<Double, FaceImage> map = faceRecognizer.compare(thumbnail);
        
        //display best image in ipResult
        FaceImage result = map.get(map.firstKey());
        ipResult.setImage(result, result.getName().substring(0, result.getName().length() - 7));
        ProcessResult isFace = new ProcessResult(thumbnail, faceRecognizer, map);

        if (isFace.isFace) {
            System.out.print("FACE |");
        }
        if (isFace.isLikeFace) {
            System.out.print("LIKE FACE | ");
        }
        if (isFace.isNewFace) {
            System.out.print("NEW FACE | ");
        }
        if (isFace.isNotFace) {
            System.out.print("NOT FACE | ");
        }
        System.out.println("theta: " + map.firstKey());
    }
    
    private void getNewImage() {
        //bmp_old = bmp_new;
        if (webcam.isOpen()) {
            bmp_new = webcam.getImage();
        }
    }
}

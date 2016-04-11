/**
 * 
 */
package core;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import ui.ImagePanel;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class Worker implements Runnable {
    private Webcam webcam;
    private ImagePanel ip;
    private volatile boolean active;
    private BufferedImage bmp_old;
    private BufferedImage bmp_new;
    
    public Worker(ImagePanel destination) {
        ip = destination;
        active = true;
    }
    
    public synchronized void stop() {
        active = false;
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
        }
        webcam.close();
    }
    
    private void getNewImage() {
        bmp_old = bmp_new;
        if (webcam.isOpen()) {
            bmp_new = webcam.getImage();
        }
    }
}

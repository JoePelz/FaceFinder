/**
 * 
 */
package core;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import ui.ImagePanel;
import utilities.Thumbnails;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class Worker implements Runnable {
    private Webcam webcam;
    private ImagePanel ip;
    private ImagePanel ipCrop;
    private ImagePanel ipResult;
    private volatile boolean active;
    private BufferedImage bmp_old;
    private BufferedImage bmp_new;
    
    public Worker(ImagePanel destination, ImagePanel crop, ImagePanel result) {
        ip = destination;
        ipCrop = crop;
        ipResult = result;
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
            ipCrop.setImage(Thumbnails.scaleTarget(bmp_new, 320, 240, 128));
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

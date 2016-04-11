/**
 * 
 */
package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class ImagePanel extends JPanel implements Runnable {
    private volatile BufferedImage image;
    
    public ImagePanel() {
        setPreferredSize(new Dimension(640, 480));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
        }
    }

    @Override
    public void run() {
        if (image != null) {
            Graphics g = getGraphics();
            g.drawImage(image, 0, 0, null);
        }
    }
    
    public void setImage(BufferedImage bmp) {
        updateImage(bmp);
        try {
            SwingUtilities.invokeAndWait(this);
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace();
        }
     }

     private synchronized void updateImage(BufferedImage bmp) {
         image = bmp;
     }
}

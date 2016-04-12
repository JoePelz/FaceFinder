/**
 * 
 */
package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    private float scale;

    public ImagePanel() {
        scale = 1.0f;
        setPreferredSize(new Dimension(640, 480));
    }
    public ImagePanel(float scaleFactor) {
        setPreferredSize(new Dimension((int)(256*scaleFactor), (int)(256*scaleFactor)));
        scale = scaleFactor;
    }
    
    @Override
    protected void paintComponent(Graphics g_) {
        Graphics2D g = (Graphics2D) g_;
        super.paintComponent(g);
        if (image != null) {
            g.scale(scale, scale);
            g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
        }
    }

    @Override
    public void run() {
        if (image != null) {
            Graphics2D g = (Graphics2D) getGraphics();
            g.scale(scale, scale);
            g.drawImage(image, 0, 0, null);
            g.dispose();
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
    
    public void setImageQuick(BufferedImage bmp) {
        if (bmp == null) return;
        updateImage(bmp);
        Graphics2D g = (Graphics2D)getGraphics();
        g.scale(scale, scale);
        g.drawImage(bmp, 0, 0, null);
        g.dispose();
     }

     private synchronized void updateImage(BufferedImage bmp) {
         image = bmp;
     }
}

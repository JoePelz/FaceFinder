/**
 * 
 */
package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import core.EntryPoint;
import core.EntryPoint.Mode;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class ImagePanel extends JPanel implements Runnable {
    private volatile BufferedImage image;
    private volatile Point center = new Point();
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
            g.drawImage(image, 0, 0, null);
        }
        if (EntryPoint.mode == Mode.BESTMATCH) {
            if (center.x != 0) {
                g.setColor(Color.YELLOW);
                g.drawRect(center.x - 64, center.y - 128, 128, 256);
            }
        }
    }

    @Override
    public void run() {
        if (image != null) {
            repaint();
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
    
    public void setCenter(final Point c, boolean winner, final double theta) {
        if (winner) {
            center.setLocation(c);
        }
        
        if (EntryPoint.mode == Mode.DEBUG) {
            final Runnable drawBrightBox = new Runnable() {
                public void run() {
                    Graphics2D g = (Graphics2D)getGraphics();
                    g.setColor(Color.YELLOW);
                    g.drawRect(c.x - 64, c.y - 128, 128, 256);
                    g.drawString(String.format("%d", (int)theta), c.x, c.y);
                    g.dispose();
                }
            };
            final Runnable drawDimBox = new Runnable() {
                public void run() {
                    Graphics2D g = (Graphics2D)getGraphics();
                    g.setColor(Color.PINK);
                    g.drawRect(c.x - 64, c.y - 128, 128, 256);
                    //g.drawString(String.format("%.2f", theta), c.x, c.y);
                    g.drawString(String.format("%d", (int)theta), c.x, c.y);
                    g.dispose();
                }
            };
            if (winner) {
                try {
                    SwingUtilities.invokeAndWait(drawBrightBox);
                } catch (InvocationTargetException | InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                try {
                    SwingUtilities.invokeAndWait(drawDimBox);
                } catch (InvocationTargetException | InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
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

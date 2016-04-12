/**
 * 
 */
package ui;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class CardPanel extends JPanel implements Runnable {
    private volatile BufferedImage image;
    private volatile String message;
    private ImagePanel pic;
    private JLabel label;

    public CardPanel(float scaleFactor) {
        pic = new ImagePanel(scaleFactor);
        add(pic);
        label = new JLabel("test\ntest2");
        add(label);
    }

    @Override
    public void run() {
        if (image != null) {
            label.setText(message);
        }
    }
    
    public void setImage(BufferedImage bmp, String message) {
        update(bmp, message);
        pic.setImage(bmp);
        try {
            SwingUtilities.invokeAndWait(this);
            SwingUtilities.invokeAndWait(pic);
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace();
        }
     }
    
    public void setImageQuick(BufferedImage bmp) {
        pic.setImageQuick(bmp);
     }

     private synchronized void update(BufferedImage bmp, String message) {
         image = bmp;
         this.message = message; 
     }
}

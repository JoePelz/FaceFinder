/**
 * 
 */
package utilities;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class Thumbnails {
    public static final int RES = 256;
    
    public static BufferedImage scaleTarget(BufferedImage source, int centerX, int centerY, int radius) {
        BufferedImage bi = getCompatibleImage(RES, RES);
        Graphics2D g2d = bi.createGraphics();
        double scale = (double) RES / radius;
        AffineTransform at = AffineTransform.getTranslateInstance(centerX, centerY);
        at.scale(scale, scale);
        g2d.drawRenderedImage(source, at);
        g2d.dispose();
        return bi;
    }
    
    public static BufferedImage scale(BufferedImage source, double ratio) {
        int w = (int) (source.getWidth() * ratio);
        int h = (int) (source.getHeight() * ratio);
        BufferedImage bi = getCompatibleImage(w, h);
        Graphics2D g2d = bi.createGraphics();
        double xScale = (double) w / source.getWidth();
        double yScale = (double) h / source.getHeight();
        AffineTransform at = AffineTransform.getScaleInstance(xScale,yScale);
        g2d.drawRenderedImage(source, at);
        g2d.dispose();
        return bi;
    }
    
    private static BufferedImage getCompatibleImage(int w, int h) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        return image;
    }
}

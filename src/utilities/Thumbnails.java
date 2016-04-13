/**
 * 
 */
package utilities;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class Thumbnails {
    public static final int RES = 256;

    public static BufferedImage scaleTarget(BufferedImage source, int centerX, int centerY, double radius) {
        //BufferedImage bi = getCompatibleImage(RES, RES);
        BufferedImage bi = new BufferedImage(RES, RES, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        double scale = (double) RES / (radius * 2);
        AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
        at.translate(-centerX + radius, -centerY + radius);
        
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawRenderedImage(source, at);
        g2d.dispose();
        return bi;
    }
    
    public static BufferedImage scaleTarget(BufferedImage source, int centerX, int centerY) {
        //BufferedImage bi = getCompatibleImage(RES, RES);
        BufferedImage bi = new BufferedImage(RES, RES, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(2.0f, 1.0f);
        at.translate(-centerX + 64, -centerY + 128);
        
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
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
    
    public static BufferedImage getGreyscale(BufferedImage source) {
        BufferedImage bi = new BufferedImage(RES, RES, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(source, 0, 0, null);
        g2d.dispose();
        return bi;
    }
    
    public static BufferedImage getWider(BufferedImage source) {
        BufferedImage bi = new BufferedImage(RES, RES, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = bi.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(2.0f,1.0f);
        at.translate(-64, 0);
        g2d.drawRenderedImage(source, at);
        g2d.dispose();
        return bi;
    }
}

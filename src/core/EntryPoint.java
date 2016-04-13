/**
 * 
 */
package core;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ui.BinaryTreePanel;

/**
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class EntryPoint {
    public enum Mode { CONTINUOUS, BESTMATCH, DEBUG }
    public static Mode mode = Mode.CONTINUOUS;
    public static final boolean DEBUG = true;
    /**
     * 
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        final int defaultWidth = 1360;
        final int defaultHeight = 700;
        JFrame frame = new JFrame("Binary Tree Builder, by Joe Pelz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException 
                | InstantiationException
                | IllegalAccessException 
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        BinaryTreePanel etp = new BinaryTreePanel();
        
        
        frame.add(etp);
        frame.pack();
        frame.setSize(defaultWidth, defaultHeight);
        frame.validate();
        frame.setVisible(true);
    }
}

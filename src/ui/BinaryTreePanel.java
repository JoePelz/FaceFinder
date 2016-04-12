package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import core.Worker;
import utilities.Thumbnails;


/**
 * This class is a panel to hold many circuits, 
 * chosen from drop-down list.
 * 
 * @author Joe Pelz
 * @version 1.0
 */
public class BinaryTreePanel extends JPanel {
    /** unique id for serialization. */
    private static final long serialVersionUID = -1712783296868793108L;
    /** the panel that holds the circuits, switched via CardLayout. */
    private JPanel circuits = new JPanel(new GridLayout(1, 2));
    private JPanel inputImagePane;
    private ImagePanel ip;
    private CardPanel ipFT;
    private CardPanel ipFM;
    
    private Worker worker; 
    private Thread thread;
    
    /**
     * Constructor, to build the UI, 
     * including combo box 
     * and circuit panel full of circuits. 
     */
    public BinaryTreePanel() {
        setLayout(new BorderLayout());
        
        TitledBorder myBorder = BorderFactory.createTitledBorder("Who is this?");
        circuits.setBorder(myBorder);
        
        final int numberOfForms = 3;
        JPanel entryPane = new JPanel(new GridLayout(2, numberOfForms));
        JLabel lPreOrder  = new JLabel("  Pre-Order");
        JLabel lInOrder   = new JLabel("  In-Order");
        JLabel lPostOrder = new JLabel("  Post-Order");
        final JTextField tePreOrder  = new JTextField();
        final JTextField teInOrder   = new JTextField();
        final JTextField tePostOrder = new JTextField();
        teInOrder.setEditable(false);
        tePostOrder.setEditable(false);
        entryPane.add(lPreOrder);
        entryPane.add(lInOrder);
        entryPane.add(lPostOrder);
        entryPane.add(tePreOrder);
        entryPane.add(teInOrder);
        entryPane.add(tePostOrder);
        add(entryPane, BorderLayout.PAGE_START);

        inputImagePane = new JPanel();
        myBorder = BorderFactory.createTitledBorder("Image Spot");
        inputImagePane.setBorder(myBorder);
        circuits.add(inputImagePane);

        JPanel tbSplitPane = new JPanel(new GridLayout(2, 1));
        myBorder = BorderFactory.createTitledBorder("TB SPLIT");
        tbSplitPane.setBorder(myBorder);
        circuits.add(tbSplitPane);
        
        JPanel facePane = new JPanel();
        myBorder = BorderFactory.createTitledBorder("Face Test");
        facePane.setBorder(myBorder);
        tbSplitPane.add(facePane);
        
        JPanel matchPane = new JPanel();
        myBorder = BorderFactory.createTitledBorder("Face Match");
        matchPane.setBorder(myBorder);
        tbSplitPane.add(matchPane);
        
        
        add(circuits, BorderLayout.CENTER);
        
        JPanel btnLayout = new JPanel();
        JButton btnBalance = new JButton("Kill Camera");
        btnBalance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                worker.stop();
            }
        });
        JButton btnSnapshot = new JButton("Toggle Snapshot");
        btnSnapshot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                worker.toggleSnapshot();
            }
        });
        add(btnLayout, BorderLayout.PAGE_END);
        btnLayout.add(btnBalance);
        btnLayout.add(btnSnapshot);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                //binaryTree.focusView();
            }
        });

        ip = new ImagePanel();
        inputImagePane.add(ip);
        ipFT = new CardPanel(0.75f);
        facePane.add(ipFT);
        ipFM = new CardPanel(0.75f);
        matchPane.add(ipFM);
        
        worker = new Worker(ip, ipFT, ipFM);
        thread = new Thread(worker);
        thread.start();
    }
    
    private void testCropImage() {
        BufferedImage base = null;
        BufferedImage small = null;
        try {
            base = ImageIO.read(new File("test.bmp"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ipFT.setImageQuick(base);
        //small = Thumbnails.scaleTarget(base, 182, 163, 55);
        small = Thumbnails.scaleTarget(base, 441, 258, 150);
        ipFM.setImageQuick(small);
    }
}

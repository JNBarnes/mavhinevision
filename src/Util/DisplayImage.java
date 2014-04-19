/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 *
 * @author Student
 */
public class DisplayImage {

    public DisplayImage(BufferedImage img) {
        if (img != null) {
        JFrame frame = new JFrame("Display Image");
        frame.setContentPane(new ShowImage(img));
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        }
    }



    public class ShowImage extends JScrollPane {

        BufferedImage img;
        JLabel imgLabel;

        public ShowImage(BufferedImage img) {
            this.imgLabel = new JLabel(new ImageIcon(img));
            this.viewport.add(imgLabel);
            this.viewport.setViewSize(new Dimension(500, 500));
            this.setWheelScrollingEnabled(true);
            this.setAutoscrolls(true);

        }
    }
}

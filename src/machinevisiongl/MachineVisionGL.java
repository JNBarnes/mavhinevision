/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machinevisiongl;

import Exceptions.ConfigOptionMissingException;
import ImageProcessors.*;
import Util.DisplayImage;
import Util.ImageFileFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import javax.imageio.ImageIO;

/**
 *
 * @author Student
 */
public class MachineVisionGL {

    static final ForkJoinPool fjp = new ForkJoinPool();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ConfigOptionMissingException {
        //doAll();
        BufferedImage img = ImageIO.read(new File("C:\\img\\test.jpg"));
        new DisplayImage(fjp.invoke(new ExplorativeEdging(img)));

    }

    public static void doAll() throws IOException, ConfigOptionMissingException {
        BufferedImage img = ImageIO.read(new File("C:\\img\\test.jpg"));
        String[] processorNames = ImageProcessorFactory.getImageProcessorNames();
        for (String processor : processorNames) {
            ImageProcessor proc = ImageProcessorFactory.getInstanceOf(processor, img);
            new DisplayImage(fjp.invoke(proc));
        }
    }

    private static boolean validate(File dir) {
        File[] files;
        if (!dir.exists()) {
            System.out.println("The given directory: " + dir.getAbsolutePath() + "\n does not exist.");
            return false;
        } else {//dir exists
            files = dir.listFiles(new ImageFileFilter());
            if (files.length <= 0) {
                System.out.println("The given directory does not contain any processable files.");
                return false;
            }
        }
        return true;
    }

    private static void saveImage(BufferedImage img, File dir) throws IOException {
        File out = new File(dir.getAbsolutePath() + "\\output\\" + System.currentTimeMillis() + ".png");
        out.mkdirs();
        out.createNewFile();
        ImageIO.write(img, "png", out);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessors;

import Exceptions.ConfigOptionMissingException;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Student
 */
public class Luminance extends ImageProcessor implements ImageProcessorInterface {

    public Luminance() throws ConfigOptionMissingException{
        super(null);
    }

    public Luminance(BufferedImage img) throws ConfigOptionMissingException {
        super(img, ProcessorConfig.getDefaultConfig());
    }

    public Luminance(BufferedImage img, ProcessorConfig config) throws ConfigOptionMissingException {
        super(img, config);
    }

    @Override
    public BufferedImage iterateImage(BufferedImage img) {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int[] rgb = getComponents(img.getRGB(x, y));
                int alpha = 255;
                double red = (rgb[0] * (0.2126));
                double green = (rgb[1] * (0.7152));
                double blue = (rgb[2] * (0.0722));
                int luminance = threshold8Bit((int)(red+green+blue));
                proc.setRGB(x,y,new Color(luminance, luminance, luminance).getRGB());
            }
        }
        return proc;
    }

    @Override
    public void setImage(BufferedImage img) throws ConfigOptionMissingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

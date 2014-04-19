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
public class Thresholding extends ImageProcessor implements ImageProcessorInterface {

    int pixelThreshold = 0;
    
    public Thresholding(BufferedImage img, ProcessorConfig config) throws ConfigOptionMissingException{
        super(img,config);
        this.pixelThreshold = (int) config.get("pixelThreshold");
    }
    @Override
    public BufferedImage iterateImage(BufferedImage img) {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (getComponents(img.getRGB(x, y))[0] <=pixelThreshold) {
                    proc.setRGB(x, y, Color.BLACK.getRGB());
                }else{
                    proc.setRGB(x, y, Color.WHITE.getRGB());
                }
                
            }
        }
        return proc;
    }
    
    public static ProcessorConfig getDefaultConfig(){
        ProcessorConfig config = ProcessorConfig.getDefaultConfig();
        config.put("pixelThreshold", 0);
        return config;
    }
    
    @Override
    public BufferedImage compute(){
        return iterateImage(img);
    }
    
    
}

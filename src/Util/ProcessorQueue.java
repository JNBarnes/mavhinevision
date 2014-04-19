/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import ImageProcessors.ImageProcessor;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author James
 */
public class ProcessorQueue {
    private List<ImageProcessor> queue;
    private BufferedImage inputImage;
    private BufferedImage outputImage;

    public ProcessorQueue(){
        init();
    }

    public ProcessorQueue(BufferedImage inputImage){
        this.inputImage = inputImage;
        init();
    }

    private void init() {
        queue = new ArrayList<>();
    }
    

    public void addProcessor(ImageProcessor ip){
        queue.add(ip);
    }

    public void removeProcessor(ImageProcessor ip){
        queue.remove(ip);
    }
}

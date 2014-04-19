/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessors;

import Exceptions.ConfigOptionMissingException;
import Exceptions.ImageProcessorNotInitialisedException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Student
 */
public abstract class ImageProcessor extends RecursiveTask<BufferedImage> {

    int threshold;
    BufferedImage img;
    BufferedImage proc;
    int boundaryWidth = 0;
    ForkJoinPool fjp;
    ProcessorConfig config;

    public ImageProcessor(BufferedImage img) throws ConfigOptionMissingException {
        init(img, ProcessorConfig.getDefaultConfig());
    }

    public ImageProcessor(BufferedImage img, ProcessorConfig config) throws ConfigOptionMissingException {
        init(img, config);
    }

    private void init(BufferedImage img, ProcessorConfig config) throws ConfigOptionMissingException {
        this.img = img;
        initOutput(img);
        this.config = config;
        loadConfig(config);

    }

    protected boolean initOutput(BufferedImage img) {
        if (img != null) {
            this.proc = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            return true;
        }else{
            return false;
        }
    }

    private void loadConfig(ProcessorConfig config) throws ConfigOptionMissingException {
        this.boundaryWidth = (int) config.get("boundaryWidth");
        this.threshold = (int) config.get("threshold");
    }

    @Override
    protected BufferedImage compute() {
        if (img != null) {
            if (img.getWidth() <= threshold) {
                return iterateImage(img);
            } else {
                ImageProcessor left = null;
                ImageProcessor right = null;
                try {
                    left = this.getClass().cast(getConstructor().newInstance(new Object[]{splitLeft(img), config}));
                    
                    right = this.getClass().cast(getConstructor().newInstance(new Object[]{splitRight(img), config}));
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
                left.fork();
                BufferedImage rightImage = right.compute();
                BufferedImage leftImage = left.join();
                return joinImages(leftImage, rightImage);
            }
        } else {
            return null;
        }
    }

    private Constructor getConstructor() {
        Constructor c = null;
        try {
            c = this.getClass().getConstructor(BufferedImage.class, ProcessorConfig.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    private BufferedImage joinImages(BufferedImage left, BufferedImage right) {
        BufferedImage output = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = output.getGraphics();
        g.drawImage(left, 0, 0, null);
        g.drawImage(right.getSubimage(boundaryWidth, 0, right.getWidth() - boundaryWidth, right.getHeight()), left.getWidth() - boundaryWidth, 0, null);
        g.dispose();
        return output;
    }

    private BufferedImage splitLeft(BufferedImage img) {
        BufferedImage split = img.getSubimage(0, 0, (img.getWidth() / 2) + boundaryWidth, img.getHeight());
        return split;
    }

    private BufferedImage splitRight(BufferedImage img) {
        int width = img.getWidth();
        BufferedImage split = img.getSubimage((width / 2) - boundaryWidth, 0, (width - width / 2) + boundaryWidth, img.getHeight());
        return split;
    }

    public abstract BufferedImage iterateImage(BufferedImage img);

    public void setProcessorConfig(ProcessorConfig config) {
        this.config = config;
    }

    protected int[] getComponents(int rgb) {
        int[] components = new int[3];
        components[0] = (rgb & 0x00ff0000) >> 16;
        components[1] = (rgb & 0x0000ff00) >> 8;
        components[2] = rgb & 0x000000ff;
        return components;
    }

    protected int recombineComponents(int red, int green, int blue) {
        return ((red << 16) | (green << 8) | blue);
    }

    protected int threshold8Bit(int component) {
        if (component < 0) {
            component = 0;
        }
        if (component > 255) {
            component = 255;
        }
        return component;
    }

    protected void setImage(BufferedImage img)throws ConfigOptionMissingException{
        this.img = img;
        initOutput(img);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessors;

import Exceptions.ConfigOptionMissingException;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 *
 * @author Student
 */
public class CannedGaussianBlur extends ImageProcessor implements ImageProcessorInterface {

    int gaussianRadius;

    public CannedGaussianBlur() throws ConfigOptionMissingException{
        super(null);
        this.gaussianRadius = (int)getDefaultConfig().get("gaussianRadius");
    }

    public CannedGaussianBlur(BufferedImage img, ProcessorConfig config) throws ConfigOptionMissingException {
        super(img, config);
        this.gaussianRadius = (int) config.get("gaussianRadius");
    }

    public CannedGaussianBlur(BufferedImage img) throws ConfigOptionMissingException{
        super(img, getDefaultConfig());
        this.gaussianRadius = (int) config.get("gaussianRadius");
    }


    private ConvolveOp getGaussianBlurFilter(int radius,
            boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        int size = radius * 2 + 1;
        float[] data = new float[size];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
    }

    @Override
    public BufferedImage iterateImage(BufferedImage img) {
        this.img = getGaussianBlurFilter(gaussianRadius, true).filter(img, null); // its a 1 dimensional op
        this.img = getGaussianBlurFilter(gaussianRadius, false).filter(this.img, null); // so we apply it twice - |
        return this.img;
    }

    public static ProcessorConfig getDefaultConfig() {
        ProcessorConfig config = CannyEdgeDetection.getDefaultConfig();
        config.put("boundaryWidth", 6);
        return config;
    }

    @Override
    public void setImage(BufferedImage img) throws ConfigOptionMissingException {
        this.img = img;
    }
}

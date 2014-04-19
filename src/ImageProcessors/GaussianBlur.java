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
public class GaussianBlur extends ImageProcessor implements ImageProcessorInterface {

    int[][] gaussian;
    static final int boundaryWidth = 2; //TODO should ideally be properly set by constructor



    public GaussianBlur(BufferedImage img) throws ConfigOptionMissingException{
        super(img, ProcessorConfig.getDefaultConfig());
        initGaussianKernel();
    }

    @Override
    /**
     * Be careful, splitting image into sections < kernel size will cause problems.
     */
    public BufferedImage iterateImage(BufferedImage img) {
        for (int y = 2; y < img.getHeight() - 2; y++) {
            for (int x = 2; x < img.getWidth() - 2; x++) {
                proc.setRGB(x, y, convolveAboutPixel(img.getSubimage(x-2, y-2, 5, 5)));
            }
        }
        return proc;
    }

    private int convolveAboutPixel(BufferedImage subImg) {
        int red = 0,green = 0,blue = 0;
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int[] components = getComponents(subImg.getRGB(x,y));
                red += components[0] * gaussian[x][y];
                green += components[1] * gaussian[x][y];
                blue += components[2] * gaussian[x][y];
            }
        }
        red = threshold8Bit(red/149);
        green = threshold8Bit(green/149);
        blue = threshold8Bit(blue /149);

        return recombineComponents(red,green,blue);

    }

    public static ConvolveOp getGaussianBlurFilter(int radius,
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

        Kernel kernel = null;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }



    /**
     * Builds first derivative gaussian filter
     */
    private void initGaussianKernel() {
        gaussian = new int[5][5];
        gaussian[0] = new int[]{2, 4, 5, 4, 2};
        gaussian[1] = new int[]{4, 9, 12, 9, 4};
        gaussian[2] = new int[]{5, 12, 15, 12, 5};
        gaussian[3] = new int[]{4, 9, 12, 9, 4};
        gaussian[4] = new int[]{2, 4, 5, 4, 2};
    }

    @Override
    public void setImage(BufferedImage img) throws ConfigOptionMissingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}

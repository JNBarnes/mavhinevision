/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessors;

import Exceptions.ConfigOptionMissingException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Student
 */
public class CannyEdgeDetection extends ImageProcessor implements ImageProcessorInterface {

    boolean[][] visited;
    int arcSize;
    int lowThreshold, highThreshold;
    BufferedImage xImg;
    BufferedImage yImg;
    BufferedImage supressed;
    Kernel horizontalKernel;
    Kernel verticalKernel;
    ForkJoinPool subProcs;
    int gaussianRadius = 6;
    int boundaryWidth = 2;

    public CannyEdgeDetection() throws ConfigOptionMissingException{
        super(null);
    }

    public CannyEdgeDetection(BufferedImage img, ProcessorConfig config) throws ConfigOptionMissingException {
        super(img, config);
        init();
    }

    public CannyEdgeDetection(BufferedImage img) throws ConfigOptionMissingException
    {
        super(img,getDefaultConfig());
        init();
    }

    private void init() throws ConfigOptionMissingException {
        subProcs = new ForkJoinPool();
        this.img = subProcs.invoke(new CannedGaussianBlur(img, config));
        this.img = subProcs.invoke(new Luminance(img));
        visited = new boolean[img.getWidth()][img.getHeight()];

        xImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        yImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        supressed = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);


        horizontalKernel = new Kernel(3, 3, new float[]{-1, 0, 1, -2, 0, 2, -1, 0, 1});
        verticalKernel = new Kernel(3, 3, new float[]{-1, -2, -1, 0, 0, 0, 1, 2, 1});

        arcSize = 360 / 8;

        loadConfig(getDefaultConfig());


    }

    private void loadConfig(ProcessorConfig config) throws ConfigOptionMissingException {
        this.highThreshold = (int) config.get("highThreshold");
        this.lowThreshold = (int) config.get("lowThreshold");
        this.boundaryWidth = (int) config.get("boundaryWidth");
        this.gaussianRadius = (int) config.get("gaussianRadius");
    }


    public void setLowerThreshold(int lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    public void setUpperThreshold(int highThreshold) {
        this.highThreshold = highThreshold;
    }

   //TODO perhaps break this out into a proc method for sub class to use also
    @Override
    public BufferedImage iterateImage(BufferedImage img) {
        new ConvolveOp(horizontalKernel).filter(img, xImg);
        new ConvolveOp(verticalKernel).filter(img, yImg);
        for (int y = 1; y < img.getHeight() - 1; y++) {
            for (int x = 1; x < img.getWidth() - 1; x++) {
                followEdge(x,y);
            }
        }
        return proc;
    }



    private int roundAngle(int direction) {
        int base = direction / arcSize;
        int angle = base * arcSize;
        if (angle < 0) {
            return 180 + (180 + angle); //converts range from -180/+180 to 0-360
        } else {
            return angle;
        }
    }


    private void followEdge(int x, int y) {
        int intensity = nonMaximaSupression(x, y);
        if ((intensity < lowThreshold)) {
            visited[x][y] = true;
                proc.setRGB(x, y, Color.black.getRGB());
        } else {
            if (!visited[x][y] && intensity > lowThreshold) {
                visited[x][y] = true;
                intensity = Math.min(intensity, 255);
                //proc.setRGB(x, y, new Color(intensity, intensity, intensity).getRGB());
                proc.setRGB(x, y, Color.WHITE.getRGB());
                followEdge(x, y - 1); //up

                followEdge(x, y + 1);//down

                followEdge(x + 1, y);//right

                followEdge(x - 1, y); //left

                followEdge(x + 1, y - 1); //up right

                followEdge(x + 1, y + 1); //down right

                followEdge(x - 1, y + 1); //down left

                followEdge(x - 1, y - 1); //up left
            }
        }
    }

    /**
     * Non maxima supression reduces edge thickness to 1 pixel in many
     * circumstances
     *
     * @param x
     * @param y
     * @return
     */
    protected int nonMaximaSupression(int x, int y) {
        if (inBounds(x, y)) {
            int i1 = 0, i2 = 0;
            int direction = getGradientDirection(x, y);
            int intensity = getGradientMagnitude(x, y);
            if (direction == 90 || direction == 270) { //horizontal
                //horizontal line check up and down
                i1 = getGradientMagnitude(x, y - 1);
                i2 = getGradientMagnitude(x, y + 1);
            } else if (direction == 0 || direction == 180) { // vertical
                i1 = getGradientMagnitude(x - 1, y);
                i2 = getGradientMagnitude(x + 1, y);
            } else if (direction == 45 || direction == 225) {
                i1 = getGradientMagnitude(x - 1, y - 1);
                i2 = getGradientMagnitude(x + 1, y + 1);
            } else if (direction == 315 || direction == 135) {
                i1 = getGradientMagnitude(x - 1, y + 1);
                i2 = getGradientMagnitude(x + 1, y - 1);
            } else {
                System.out.println("Ooops");
            }

            if (intensity > i1 && intensity > i2) {
                return intensity;
            } else {
                return 0;
            }
        }
        return 0;

    }

    private boolean inBounds(int x, int y) {
        if ((x >= 1 && x <= img.getWidth() - 2) && (y >= 1 && y <= img.getHeight() - 2)) {
            return true;
        }
        return false;
    }

    public int getGradientMagnitude(int x, int y) {
        int xMag = new Color(xImg.getRGB(x, y)).getRed();
        int yMag = new Color(yImg.getRGB(x, y)).getRed();
        int magnitude = (int) Math.abs(Math.sqrt((xMag * xMag) + (yMag * yMag)));
        return magnitude;
    }

    public int getGradientDirection(int x, int y) {
        int xMag = new Color(xImg.getRGB(x, y)).getRed();
        int yMag = new Color(yImg.getRGB(x, y)).getRed();
        return roundAngle((int) Math.toDegrees(Math.atan2(yMag, xMag)));
    }

    public double getGradientDirectionRadians(int x, int y){
        int xMag = new Color(xImg.getRGB(x, y)).getRed();
        int yMag = new Color(yImg.getRGB(x, y)).getRed();
        double rotation = ((180/Math.PI)/2);
        return (Math.atan2(yMag, xMag))+4*rotation;
    }


    public static ProcessorConfig getDefaultConfig() {
        ProcessorConfig config = ProcessorConfig.getDefaultConfig();
        config.put("boundaryWidth", 7);
        config.put("gaussianRadius", 6);
        config.put("highThreshold", 80);
        config.put("lowThreshold", 40);
        config.put("threshold", 50);

        return config;
    }




    @Override
    public void setImage(BufferedImage img) throws ConfigOptionMissingException{
        this.img = img;
        initOutput(img);
        init();

    }
}

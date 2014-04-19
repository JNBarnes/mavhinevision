/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessors;

import Exceptions.ConfigOptionMissingException;
import Util.DisplayImage;
import Util.ImageOps;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.util.concurrent.ForkJoinPool;


public class ExplorativeEdging extends CannyEdgeDetection implements ImageProcessorInterface {

    static final int MAXDEPTH = 4;
    static final int WHITE = Color.white.getRGB();
    static final int BLACK = Color.black.getRGB();
    boolean[][] edgeMap;
    private BufferedImage imgBuffer;

    public ExplorativeEdging() throws ConfigOptionMissingException{
        super(null);
    }

    public ExplorativeEdging(BufferedImage img, ProcessorConfig config) throws ConfigOptionMissingException {
        super(img, config);
        init();
    }

    public ExplorativeEdging(BufferedImage img) throws ConfigOptionMissingException {
        super(img, getDefaultConfig());
        init();
    }

    private void loadConfig(ProcessorConfig config) throws ConfigOptionMissingException {
        this.threshold = (int) config.get("threshold");
    }
    private void init() throws ConfigOptionMissingException {
        this.edgeMap = new boolean[img.getWidth()][img.getHeight()];
        imgBuffer = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        loadConfig(getDefaultConfig());
    }

    @Override
    public BufferedImage iterateImage(BufferedImage img) {

        imgBuffer = ImageOps.deepCopy(img);
        img= super.iterateImage(imgBuffer);
        Graphics2D g = proc.createGraphics();
        g.setColor(Color.red);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (imgBuffer.getRGB(x, y)== Color.WHITE.getRGB()){
                   Point endPoint = getProjectionEndCoordinates(x,y,2);
                   g.drawLine(x, y, endPoint.x, endPoint.y);
                }
            }
        }

        return proc;
    }
    /**
     * Returns a point representing the end of a line that is radius length projected
     * from the point x,y in the direction of the intensity gradient at that point.
     * @param x
     * @param y
     * @param length
     */
    private Point getProjectionEndCoordinates(int x, int y, int radius){
        int a,b = 0;
        double angle = getGradientDirectionRadians(x,y);
        a = (int)(radius * Math.cos(angle) + x);
        b = (int)(radius * Math.sin(angle) + y);
        Point point = new Point(a,b);
        return point;
    }

    private boolean isNextPixelEdge(int x, int y){//pixel to check FROM
        Point p = getPeekCoords(x,y,getGradientDirection(x,y));
        if (validateCoords(p.x,p.y)) {
            return edgeMap[p.x][p.y];
        }else{
            return false;
        }
    }

    private boolean followVector(int x, int y, int direction, int depth) {
        if(depth > 100){
            System.out.println("toolong");
            return false;
        }
        Point p = getPeekCoords(x,y,direction);
        if (validateCoords(p.x,p.y)) {
            if (edgeMap[p.x][p.y]) {//if next pixel is edge
                if (followVector(p.x,p.y,direction,depth++)) {
                    proc.setRGB(x, y, Color.red.getRGB());
                }
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }

    }

    private boolean validateCoords(int x,int y){
         if((x < 0 || x > proc.getWidth() - 1)||(y < 0 || y > proc.getHeight() -1)){
             return false;
         }else{
             return true;
         }
    }

    private boolean exploreEdge(int x, int y) {
        if ((x < 0 || x > proc.getWidth() - 1)
                || (y < 0 || y > proc.getHeight() - 1)) {
            return false;
        } else {
            if (nonMaximaSupression(x, y) > 30) {//upper thresh
                proc.setRGB(x, y, Color.red.getRGB());
            }
            // if peek true mark and peek
            //else return
        }
        return true;
    }

    private Point getPeekCoords(int x, int y, int direction) {
        int peekX = 0, peekY = 0;
        switch (direction) {
            case 0:
                //up
                peekX = x;
                peekY = y - 1;
                break;
            case 180:
                peekX = x;
                peekY = y + 1;
                break;
            case 90:
                //right
                peekX = x + 1;
                peekY = y;
                break;
            case 270:
                //left
                peekX = x - 1;
                peekY = y;
                break;
            case 45:
                //up right
                peekX = x + 1;
                peekY = y - 1;
                break;
            case 135:
                //down right
                peekX = x + 1;
                peekY = y + 1;
                break;
            case 225:
                //down left
                peekX = x - 1;
                peekY = y + 1;
                break;
            case 315:
                //up left
                peekX = x - 1;
                peekY = y - 1;
        }
        return new Point(peekX, peekY);
    }

    private void markEdge(boolean mark, int x, int y) {
        if (mark) {
            proc.setRGB(x, y, BLACK);
        } else {
            proc.setRGB(x, y, BLACK);
        }
    }

    public static ProcessorConfig getDefaultConfig(){
        ProcessorConfig config = CannyEdgeDetection.getDefaultConfig();
        config.put("threshold", 100);
        //config.put("boundaryWidth", 8);
        return config;
    }
}

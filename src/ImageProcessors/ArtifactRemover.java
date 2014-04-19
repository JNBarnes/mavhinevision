/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessors;

import Exceptions.ConfigOptionMissingException;
import Util.Region;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 *
 * @author Student
 */
public class ArtifactRemover extends ImageProcessor implements ImageProcessorInterface {

    boolean[][] checked;
    Region fillRegion;
    Stack<Point> fillStack;

    public ArtifactRemover() throws ConfigOptionMissingException{
        super(null);
    }


    @Override
    public BufferedImage iterateImage(BufferedImage img) {
        checked = new boolean[img.getWidth()][img.getHeight()];
        //buildBooleanArray();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                //Check colour of pixel, if white - flood fill.
                if (!checked[x][y] && new Color(img.getRGB(x, y)).equals(Color.WHITE)) {
                    fillRegion = new Region();
                    fillStack = new Stack<>();
                    validateAndPush(x, y);
                    processStack();
                    if (fillRegion.getTrueArea() < 23) {
                        for (Point p : fillRegion.getPointList()) {
                            proc.setRGB(p.x, p.y, Color.BLACK.getRGB());
                        }
                    }
                }
            }
        }
        return proc;
    }

    private void processStack() {
        while (!fillStack.empty()) {
            Point p = fillStack.pop();
            int x = p.x;
            int y = p.y;
            stackNeighbours(x, y);
            if (fillStack.size() > 10000) {
                return;
            }
        }
    }

    private void stackNeighbours(int x, int y) {
        validateAndPush(x, y - 1); // north
        validateAndPush(x, y + 1); // south
        validateAndPush(x + 1, y); // east
        validateAndPush(x - 1, y); // west
    }

    private void validateAndPush(int x, int y) {
        if (x < 0|| y < 0
                || x >= img.getWidth() || y >= img.getHeight()) {
            return;
        }
        if (!checked[x][y] && (img.getRGB(x,y) != Color.BLACK.getRGB())) {
            fillRegion.addPixel(x, y);
            fillStack.push(new Point(x, y));
        }
        checked[x][y] = true;

    }

    @Override
    public void setImage(BufferedImage img) throws ConfigOptionMissingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

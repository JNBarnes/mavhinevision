/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessors;

import Exceptions.ConfigOptionMissingException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author James
 */
public class MacroAverage extends ImageProcessor implements ImageProcessorInterface {

    private int cellSize;
    private int cellCountWidth;
    private int overflowWidth;
    private int cellCountHeight;
    private int overflowHeight;

    public MacroAverage() throws ConfigOptionMissingException{
        super(null);
    }

    public MacroAverage(BufferedImage img) throws ConfigOptionMissingException {
        super(img,getDefaultConfig());
        init();
    }

    public MacroAverage(BufferedImage img, ProcessorConfig config) throws ConfigOptionMissingException{
        super(img, config);
        init();
    }

    private void init() throws ConfigOptionMissingException {
        this.cellSize = (int) config.get("cellSize");
        this.cellCountWidth = img.getWidth() / cellSize;
        this.overflowWidth = img.getWidth() % cellSize;
        this.cellCountHeight = img.getHeight() / cellSize;
        this.overflowHeight = img.getHeight() % cellSize;
    }


    @Override
    public BufferedImage iterateImage(BufferedImage img) {
        Graphics2D g = proc.createGraphics();
        for (int x = 0; x < cellCountWidth; x++) {
            for (int y = 0; y < cellCountHeight; y++) {
                g.setColor(getAverageColor(getImageTile(x, y)));
                drawAveragedTile(x, y, g);
                if (y == cellCountHeight - 1) {
                    if (overflowHeight > 0) {
                        g.setColor(getAverageColor(getImageTile(x, cellCountHeight,
                                cellSize, overflowHeight)));
                        drawAveragedTile(x, cellCountHeight, g);
                    }
                }
                if (x == cellCountWidth -1) {
                    if (overflowWidth > 0) {
                        g.setColor(getAverageColor(getImageTile(cellCountWidth, y,
                                overflowWidth, cellSize)));
                    }
                }
            }
        }
        return proc;
    }

    private BufferedImage getImageTile(int tileX, int tileY) {
        return img.getSubimage(tileX * cellSize, tileY * cellSize, cellSize, cellSize);
    }

    private void drawAveragedTile(int x, int y, Graphics2D g) {
        g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
    }

    private void drawAveragedTile(int x, int y, int width, int height, Graphics2D g) {
        g.fillRect(x * cellSize, y * cellSize, width, height);
    }

    private BufferedImage getImageTile(int tileX, int tileY, int width, int height) {
        return img.getSubimage(tileX * cellSize, tileY * cellSize, width, height);
    }

    /**
     * Returns average colour of all the pixels in a tile.
     *
     * @param tile
     * @return
     */
    private Color getAverageColor(BufferedImage tile) {
        int width = tile.getWidth(), height = tile.getHeight();
        int[] data = tile.getRGB(0, 0, width, height, null, 0, width);
        int red = 0, green = 0, blue = 0;
        for (int i = 0; i < data.length; i++) {
            int[] components = getComponents(data[i]);
            red += components[0];
            green += components[1];
            blue += components[2];
        }
        //Average
        red /= data.length;
        green /= data.length;
        blue /= data.length;
        return new Color(recombineComponents(red, green, blue));
    }

    public static ProcessorConfig getDefaultConfig(){
        ProcessorConfig config = ProcessorConfig.getDefaultConfig();
        int cellSize = 5;
        config.put("cellSize", cellSize);
        config.put("boundaryWidth", cellSize);
        return config;
    }

    @Override
    public void setImage(BufferedImage img) throws ConfigOptionMissingException{
        this.img = img;
        init();
    }
}

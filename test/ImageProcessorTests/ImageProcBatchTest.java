/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessorTests;

import Exceptions.ConfigOptionMissingException;
import ImageProcessors.ImageProcessor;
import ImageProcessors.ImageProcessorFactory;
import Util.DisplayImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import javax.imageio.ImageIO;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author James
 */
public class ImageProcBatchTest {

    private long time;
    private BufferedImage img;
    private ForkJoinPool fjp;

    public ImageProcBatchTest() {
        fjp = new ForkJoinPool();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        time = 0;
        startTimer();
    }

    @After
    public void tearDown() {
        stopTimer();
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void batchTest_initWithImg() throws ConfigOptionMissingException, IOException{
        BufferedImage img = ImageIO.read(new File("C:\\img\\test.jpg"));
        String[] processorNames = ImageProcessorFactory.getImageProcessorNames();
        for (String processor : processorNames) {
            ImageProcessor proc = ImageProcessorFactory.getInstanceOf(processor, img);
            new DisplayImage(fjp.invoke(proc));
        }
    }

    @Test
    public void batchTest_initWithoutImg() throws IOException, ConfigOptionMissingException{
        BufferedImage img = ImageIO.read(new File("C:\\img\\test.jpg"));
        String[] processorNames = ImageProcessorFactory.getImageProcessorNames();
        for (String processor : processorNames) {
            ImageProcessor proc = ImageProcessorFactory.getInstanceOf(processor);
            new DisplayImage(fjp.invoke(proc));
        }
    }

    private void startTimer() {
        time = System.currentTimeMillis();
    }

    private void stopTimer() {
        System.out.println("Time elapsed (ms): " + (System.currentTimeMillis() - time));


    }
}

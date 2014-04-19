/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessorTests;

import Exceptions.ConfigOptionMissingException;
import ImageProcessors.ImageProcessorFactory;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * This test initialises all available Image Processors with a null BufferedImage then attempts to process.
 * Should pass gracefully. Null Pointer exception = fail.
  * @author James
 */
public class ImageProcNullConstructorTest {

    private ForkJoinPool fjp;

    public ImageProcNullConstructorTest() {
        this.fjp = new ForkJoinPool();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test.

    @Test
    public void testNullImageConstructor(){
        String[] names = ImageProcessorFactory.getImageProcessorNames();

        for (String string : names) {
            try {
                fjp.invoke(ImageProcessorFactory.getInstanceOf(string));
            } catch (    ConfigOptionMissingException | NullPointerException ex) {
                Logger.getLogger(ImageProcNullConstructorTest.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}

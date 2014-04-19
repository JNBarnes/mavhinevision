/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessors;

import Exceptions.ConfigOptionMissingException;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author James
 */
public class ImageProcessorFactory {
    private static final String[] names =
            new String[]{"Explorative Edging", "Canny Edge Detection", "Luminance","Macro Average", "Canned Gaussian Blur"};

    public static String[] getImageProcessorNames(){
        return names;
    }

   public static ImageProcessor getInstanceOf(String processorName) throws ConfigOptionMissingException{
       List<String> nameList =  Arrays.asList(names);
       if (nameList.contains(processorName)) {
           switch (processorName) {
               case "Explorative Edging":
                   return new ExplorativeEdging();
               case "Canny Edge Detection":
                   return new CannyEdgeDetection();
               case "Luminance":
                   return new Luminance();
               case "Macro Average":
                   return new MacroAverage();
               case "Canned Gaussian Blur":
                   return new CannedGaussianBlur();
                              }
       }else{
           throw new AssertionError("Requested processor: "+ processorName + " does not exist.");
       }
       return null;

   }

   public static ImageProcessor getInstanceOf(String processorName, BufferedImage img) throws ConfigOptionMissingException{
       List<String> nameList =  Arrays.asList(names);
       if (nameList.contains(processorName)) {
           switch (processorName) {
               case "Explorative Edging":
                   return new ExplorativeEdging(img);
               case "Canny Edge Detection":
                   return new CannyEdgeDetection(img);
               case "Luminance":
                   return new Luminance(img);
               case "Macro Average":
                   return new MacroAverage(img);
               case "Canned Gaussian Blur":
                   return new CannedGaussianBlur(img);
                              }
       }else{
           throw new AssertionError("Requested processor: "+ processorName + " does not exist.");
       }
       return null;
   }

}

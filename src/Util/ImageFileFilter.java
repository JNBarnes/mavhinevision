/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Student
 */
public class ImageFileFilter implements FileFilter {

    static String[] validExtensions = new String[]{"bmp", "png"};

    @Override
    public boolean accept(File file) {
        for (String extension : validExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}

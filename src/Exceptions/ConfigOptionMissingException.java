/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

import java.util.HashMap;

/**
 *
 * @author James
 */
public class ConfigOptionMissingException extends Exception {
    public ConfigOptionMissingException(String requestedKey, HashMap<String, Object> config){
        System.out.println("Config Option Missing: "+ requestedKey);
        System.out.println("Existing options: "+ config.keySet());
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessors;

import Exceptions.ConfigOptionMissingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class ProcessorConfig{

    private HashMap<String, Object> config;

    public ProcessorConfig(){
        this.config = new HashMap<>();
    }

    public Object get(String key) throws ConfigOptionMissingException{
        if (config.containsKey(key)) {
            return config.get(key);
        }else{
            throw new ConfigOptionMissingException(key, config);
        }
    }

    public boolean containsKey(String key){
        return config.containsKey(key);
    }

    public void put(String key, Object value){
        config.put(key, value);
    }

    public static ProcessorConfig getDefaultConfig(){
        ProcessorConfig config = new ProcessorConfig();
        config.put("boundaryWidth", 1); //overlap between divisions
        config.put("threshold", 200); //size at which task is recursivley split
        return config;
    }
}

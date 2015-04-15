package com.boom.ext.spring.global;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Created by jiangshan on 15/3/31.
 */
public class SystemGlobals {

    private static Properties properties = null;

    static {
        init("config.properties");
    }

    public static boolean init(String fileName){
        properties = new Properties();
        InputStream inputStream = null;
        try {
            URL url = SystemGlobals.class.getClassLoader().getResource(fileName);
            if(url == null){
                System.err.println(fileName + " is not exist.");
                return false;
            }
            inputStream = SystemGlobals.class.getClassLoader().getResource(fileName).openStream();
            properties.load(inputStream);

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    public final static String get(String key){
        return properties.getProperty(key);
    }

    public final static int getInt(String key) {
        String value = get(key);
        try{
            int intValue = Integer.valueOf(value);
            return intValue;
        } catch (Exception ex){
            throw new RuntimeException("get int value from properties. key:" + key + " value:" + value + " " + ex.getMessage(), ex);
        }
    }
}

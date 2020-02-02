package Framework;

import java.io.*;
import java.util.*;

public class Config {

    private static HashMap<String, String> data = new HashMap<String, String>();

    Config()    {
        String sConfigFilePath = "src\\main\\resources\\config.properties";
        try (InputStream input = new FileInputStream(sConfigFilePath)) {
            Properties prop = new Properties();
            prop.load(input);
            Set<Object> keys = prop.keySet();

            for(Object k:keys)  {
                String key=(String) k;
                String value=prop.getProperty(key);
                data.put(key, value);
            }
        }
        catch (Exception e) {
            System.out.println("Error reading config file: " + e);
        }
    }

    public static String getProperty(String key)    {
        return data.getOrDefault(key, "");
    }
}

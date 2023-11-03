package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private static Properties loadProperties(String fileName) throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream(fileName);
        configuration.load(inputStream);
        if (inputStream != null) {
            inputStream.close();
        }
        return configuration;
    }

    public static Properties loadAPIProperties() throws IOException {
        return loadProperties("api.properties");
    }

    public static Properties loadAuthProperties() throws IOException {
        return loadProperties("auth.properties");
    }
}

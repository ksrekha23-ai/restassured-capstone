// ABOUTME: Reads environment-specific property files based on the -Denv system property.
// ABOUTME: Defaults to qa.properties when no environment is specified.

package com.capstone.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    private ConfigReader() {
    }

    public static void loadConfig() {
        properties = new Properties();
        String env = System.getProperty("env", "qa");
        String fileName = env + ".properties";

        try (InputStream stream = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (stream == null) {
                throw new RuntimeException("Property file not found: " + fileName);
            }
            properties.load(stream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config: " + fileName, e);
        }
    }

    public static String getBaseUrl() {
        return getProperty("baseUrl");
    }

    public static String getToken() {
        String tokenOverride = System.getProperty("token");
        if (tokenOverride != null && !tokenOverride.isEmpty() && !tokenOverride.equals("${token}")) {
            return tokenOverride;
        }
        return getProperty("token");
    }

    public static int getTimeout() {
        return Integer.parseInt(getProperty("timeout"));
    }

    public static String getProperty(String key) {
        if (properties == null) {
            loadConfig();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found: " + key);
        }
        return value;
    }
}

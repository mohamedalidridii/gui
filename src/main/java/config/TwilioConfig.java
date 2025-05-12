package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TwilioConfig {
    private static Properties properties;
    
    static {
        try {
            properties = new Properties();
            FileInputStream input = new FileInputStream("src/main/resources/twilio.properties");
            properties.load(input);
            input.close();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des propriétés Twilio: " + e.getMessage());
        }
    }
    
    public static String getAccountSid() {
        return properties.getProperty("twilio.account.sid");
    }
    
    public static String getAuthToken() {
        return properties.getProperty("twilio.auth.token");
    }
    
    public static String getTwilioNumber() {
        return properties.getProperty("twilio.number");
    }
}
package edu.mit.adml.properties;

import edu.mit.adml.DatabaseInitializer;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO use Commons Util or Spring Properties
public class PropertiesConfigurationUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesConfigurationUtil.class);

    /**
     * For getting login details
     */
    private static Configuration login;


    static {
        boolean debug = DatabaseInitializer.isDEBUG();

        try {
            login = new PropertiesConfiguration("connection.properties");
            //TODO merge connection.properties with application-prod.properties
        } catch (Exception e) {
            logger.error("Error setting property file:", e);

            if (!debug) { // if connection.properties is not present
                logger.error("Exiting due to configuration error.");
                System.exit(1);
            }
        }
    }

    public static Credentials getCredentials() {
        final Credentials credentials = new Credentials();

        if (login != null) {
            credentials.setPassword(login.getProperty("login_password").toString());
            credentials.setUrl(login.getProperty("login_url").toString());
            credentials.setUsername_app(login.getProperty("app_username").toString());
            credentials.setPassword_app(login.getProperty("app_password").toString());
        } else {
            logger.info("No information provided for ArchivesSpace. Provide connection.properties.");
        }

        return credentials;
    }
}
package edu.mit.adml;

import edu.mit.adml.service.ItemService;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class DatabaseInitializer {

    private final Logger logger = getLogger(this.getClass());

    private static String[] userList;

    private static String AppIP = ""; //used to obtain token

    {
        try
        {
            final Parameters params = new Parameters();
            FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                            .configure(params.properties()
                                    .setFileName("application.properties"));

            final Configuration config = builder.getConfiguration();

            final String s = config.getString("spring.profiles.active");
            logger.debug("Application Mode:" + s);

            AppIP = config.getString("app.aspace");

            final String[] users = config.getStringArray("app.email");
            logger.debug("Users:" + users);

            userList = users;

        } catch (Exception cex) {
            logger.error("Error reading properties file:" + cex);
        }
    }

    private ItemService itemService;

    @Autowired
    public DatabaseInitializer(ItemService studentService) {
        this.itemService = studentService;
    }

    @PostConstruct
    public void populateDatabase() {
        logger.debug("Application booting.");
    }

    public static String[] getUserList() {
        return userList;
    }

    public static String getAppIP() {
        return AppIP;
    }
}

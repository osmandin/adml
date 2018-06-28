package edu.mit;

import edu.mit.domain.Item;
import edu.mit.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class DatabaseInitializer {

    private final Logger logger = getLogger(this.getClass());

    private ItemService itemService;

    @Autowired
    public DatabaseInitializer(ItemService studentService) {
        this.itemService = studentService;
    }

    /**
     * Populates the database
     */
    @PostConstruct
    public void populateDatabase() {
        //logger.warn("Application booting... OK");
        logger.debug("Initialize database here if you want to . . .");

    }

}

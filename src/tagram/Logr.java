package tagram;

import controller.Controller;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logr {
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

    /**
     * initialize the logger
     */
    public static void initialize() {
        try {
            LOGGER.setUseParentHandlers(false);
            FileHandler fh = new FileHandler("imageLog.txt", true);
            fh.setLevel(Level.ALL);
            SimpleFormatter fm = new SimpleFormatter();
            fh.setFormatter(fm);
            LOGGER.addHandler(fh);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "file handler not working");
        }
    }

    /**
     * record the rename action
     *
     * @param success indicates whether it is a success renaming
     * @param preName the name before renaming
     * @param newName the name after renaming
     */
    static void renameLog(Boolean success, String preName, String newName) {
        if (success) {
            LOGGER.log(Level.INFO, "Change name from   ''" + preName + "''   to   ''" + newName + "''.");
        } else {
            LOGGER.log(Level.INFO, "renaming failed");
        }
    }

    /**
     * record the fact that the program is used by the user in first time.
     */
    static void firstOpenProgramLog() {
        LOGGER.log(Level.INFO, "First using the program, create related files.");
    }

    /**
     * record the fact that the program is used by the user in first time.
     */
    static void saveLog() {
        LOGGER.log(Level.INFO, "documentedImage.txt not found");
    }
}

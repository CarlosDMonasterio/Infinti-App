package org.ih;

import org.apache.commons.lang3.StringUtils;
import org.ih.account.Accounts;
import org.ih.common.logging.Logger;
import org.ih.config.ConfigurationValue;
import org.ih.config.Settings;
import org.ih.dao.hibernate.DbType;
import org.ih.dao.hibernate.HibernateConfiguration;
import org.ih.dto.Setting;
import org.ih.task.TaskRunner;
import org.ih.util.StringUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;

/**
 * Application wide controller for handling tasks that affect the entire application
 *
 * @author Hector Plahar
 */
public class ApplicationInitializer {

    private static final String SERVER_PROPERTY_NAME = "ih-server.properties";
    private static final String DATA_HOME = "IH_DATA_HOME";

    /**
     * Responsible for initializing the system and checking for the existence of needed
     * data (such as settings) and creating as needed
     *
     * @return a valid data directory path or null if none could be detected
     */
    public static Path configure() {
        try {
            // get or initialize data directory
            Path dataDirectory = initializeDataDirectory();
            if (dataDirectory == null || !Files.exists(dataDirectory))
                return null;

            Logger.info("Checking " + Paths.get(dataDirectory.toString(), "config", SERVER_PROPERTY_NAME) + " for db config");

            // check if there is a ih-server.properties in the config directory inside the data directory
            // and use it to connect to the database if so
            if (Files.exists(Paths.get(dataDirectory.toString(), "config", SERVER_PROPERTY_NAME))) {
                loadServerProperties(dataDirectory);
            } else {
                // todo : do not initialize hibernate unless "ih-server.properties is available
                // todo : this will force a redirect on the ui and let user create/select a supported database

                // unless there is already a db directory (check if using for built-in database)
                // since using the built-in doesn't require a config file
                Path dbFolder = Paths.get(dataDirectory.toString(), "db");
                if (Files.exists(dbFolder) && Files.isDirectory(dbFolder)) {
                    // check if there is a database with the name ih-h2db
                    Iterator<Path> files = Files.list(dbFolder).iterator();
                    if (files.hasNext()) {
                        Path file = files.next();
                        String fileName = file.getFileName().toString();
                        if (fileName.endsWith(".db") && fileName.startsWith("ih-h2db")) {
                            HibernateConfiguration.initialize(DbType.H2DB, null, dataDirectory);
                            return dataDirectory;
                        } else {
                            // db folder located but no database
                            return null;
                        }
                    }
                } else if (Files.isWritable(dataDirectory)) {
                    // create the db directory for the file database
                    Files.createDirectory(dbFolder);
                    // todo : create database config file template
                    // todo : create ldap-config.properties.template
                    HibernateConfiguration.initialize(DbType.H2DB, null, dataDirectory); // this creates a directory "/db"
                }
            }

            return dataDirectory;
        } catch (Exception e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public static void loadAuthentication() {

    }

    // this should not create a home directory
    private static Path initializeDataDirectory() {
        // check environ variable
        String propertyHome = System.getenv(DATA_HOME);
        Path ihHome;

        if (StringUtils.isBlank(propertyHome)) {
            // check system property (-D in startup script)
            propertyHome = System.getProperty(DATA_HOME);

            // still nothing, check home directory
            if (StringUtils.isBlank(propertyHome)) {
                String userHome = System.getProperty("user.home");
                ihHome = Paths.get(userHome, ".IHData");
                if (!Files.exists(ihHome)) {
                    // create home directory
                    try {
                        if (Files.isWritable(Paths.get(userHome)))
                            return Files.createDirectory(ihHome);
                        else
                            return null;
                    } catch (IOException e) {
                        Logger.error(e);
                        return null;
                    }
                }
            } else {
                ihHome = Paths.get(propertyHome);
            }
        } else {
            ihHome = Paths.get(propertyHome);
        }

        Logger.info("Using data directory: " + ihHome);
        return ihHome;
    }

    // initialize the database using the database configuration
    private static void loadServerProperties(Path dataDirectory) throws IOException {
        Path serverPropertiesPath = Paths.get(dataDirectory.toString(), "config", SERVER_PROPERTY_NAME);
        Properties properties = new Properties();
        properties.load(new FileInputStream(serverPropertiesPath.toFile()));

        Logger.info("Loading server properties from " + serverPropertiesPath);

        // get type of data base
        String dbTypeString = properties.getProperty("connectionType");
        DbType type;
        if (StringUtils.isBlank(dbTypeString)) {
            Logger.error("Property \"connectionType\" not found. Defaulting to value of " + DbType.POSTGRESQL);
            type = DbType.POSTGRESQL;
        } else {
            type = DbType.valueOf(dbTypeString.toUpperCase());
        }

        // get type of database etc
        HibernateConfiguration.initialize(type, properties, dataDirectory);
    }

    /**
     * initializes application
     */
    public static void start(Path dataDirectory) {

        // start the task runner
        TaskRunner.getInstance();

        // create default admin account (or update password if already created and log to the console)
        new Accounts().createDefaultAdminAccount();

        // initialize settings
        new Settings().initialize();

        // check data directory
        checkDataDirectory(dataDirectory);
    }

    private static void checkDataDirectory(Path dataDirectory) {
        Settings settings = new Settings();
        String value = settings.getValue(ConfigurationValue.DATA_DIR);
        if (!StringUtil.isEmpty(value))
            return;

        Setting setting = new Setting();
        setting.setValue(dataDirectory.toString());
        setting.setKey(ConfigurationValue.DATA_DIR.name());
        settings.update(setting);
    }
}

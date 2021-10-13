package org.ih.config;

import org.apache.commons.lang3.StringUtils;
import org.ih.common.logging.Logger;
import org.ih.dao.DAOFactory;
import org.ih.dao.DataAccessException;
import org.ih.dao.hibernate.ConfigurationDAO;
import org.ih.dao.hibernate.HibernateConfiguration;
import org.ih.dao.model.Configuration;
import org.ih.dto.Setting;
import org.ih.util.StringUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hector Plahar
 */
public class Settings {

    private static final String UI_CONFIG_DIR = "asset";
    private final ConfigurationDAO dao;

    public Settings() {
        this.dao = DAOFactory.getConfigurationDAO();
    }

    public boolean hasDataDirectory() {
        // session valid?
        return HibernateConfiguration.isInitialized();
    }

    /**
     * Checks to ensure that all define {@link ConfigurationValue}s are entered in the settings database
     * Creates those that have not been entered with the default values
     */
    public void initialize() {
        for (ConfigurationValue value : ConfigurationValue.values()) {
            if (dao.get(value.name()) != null)
                continue;

            try {
                Configuration configuration = new Configuration();
                configuration.setKey(value.name());
                configuration.setValue("");
                dao.create(configuration);
            } catch (DataAccessException de) {
                Logger.error(de);
                return;
            }
        }
    }

    /**
     * Creates a new system setting. This is mainly intended to be used to created a fixed
     * set of configuration params on system initialization
     *
     * @param setting wrapper around the key/value pair of the system to create
     * @return created setting or null if setting already exists
     */
    public Setting create(Setting setting) {
        if (setting == null)
            throw new IllegalArgumentException("Cannot create null setting");

        Configuration configuration = dao.get(setting.getKey());
        if (configuration != null && configuration.getValue().equalsIgnoreCase(setting.getValue()))
            return configuration.toDataObject();

        configuration = new Configuration();
        configuration.setKey(setting.getKey());
        configuration.setValue(setting.getValue());
        return dao.create(configuration).toDataObject();
    }

    /**
     * Updates an existing setting in the database. For settings that specify file and directory
     * locations
     *
     * @param setting key/value pair of the setting to update. The key of a setting is unique
     * @return updated setting with the new value (if successful) or null if setting cannot be found
     */
    public Setting update(Setting setting) {
        Configuration configuration = dao.get(setting.getKey());
        if (configuration == null)
            return create(setting);

        configuration.setValue(setting.getValue());
        return dao.update(configuration).toDataObject();
    }

    /**
     * retrieves the value associated with the setting key in the parameter
     *
     * @param key unique setting display identifier which is one of {@link ConfigurationValue}
     * @return value or empty string if one is not found. This method never returns null
     */
    public String getValue(ConfigurationValue key) {
        Configuration configuration = dao.get(key.name());
        if (configuration == null)
            return "";
        return configuration.getValue();
    }

    /**
     * @return all the configuration key/value pairs in the database
     */
    public List<Setting> getAll() {
        List<Configuration> results = dao.getAll();
        if (results == null)
            return null;

        ArrayList<Setting> settings = new ArrayList<>();
        for (Configuration configuration : results) {
            try {
                ConfigurationValue.valueOf(configuration.getKey());
            } catch (IllegalArgumentException e) {
                Logger.info("Skipping configuration key " + configuration.getKey() + " with value "
                        + configuration.getValue());
                continue;
            }
            settings.add(configuration.toDataObject());
        }

        return settings;
    }

    public long getLong(ConfigurationValue key) {
        Configuration configuration = dao.get(key.name());
        if (configuration == null || StringUtil.isEmpty(configuration.getValue()))
            return -1;

        try {
            return Long.decode(configuration.getValue().trim());
        } catch (Exception e) {
            Logger.error("Exception converting config value " + configuration.getValue() + " to a long value");
            return -1;
        }
    }

    public boolean getBoolean(ConfigurationValue key) {
        Configuration configuration = dao.get(key.name());
        if (configuration == null || StringUtil.isEmpty(configuration.getValue()))
            return false;

        try {
            String value = configuration.getValue().trim();
            return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true");
        } catch (Exception e) {
            Logger.error("Exception converting config value '" + configuration.getValue() + "' to a boolean value");
            return false;
        }
    }

    public Setting get(ConfigurationValue key) {
        Configuration configuration = dao.get(key.name());
        if (configuration != null)
            return configuration.toDataObject();
        return new Setting();
    }

    public SiteSettings getSiteSettings() {
        SiteSettings settings = new SiteSettings();
        String dataDirectory = dao.get(ConfigurationValue.DATA_DIR.name()).getValue();
        final String LOGO_NAME = "logo.png";
        final String LOGIN_MESSAGE_FILENAME = "institution.html";
        final String FOOTER_FILENAME = "footer.html";

        settings.setHasLogo(Files.exists(Paths.get(dataDirectory, UI_CONFIG_DIR, LOGO_NAME)));
        settings.setHasLoginMessage(Files.exists(Paths.get(dataDirectory, UI_CONFIG_DIR, LOGIN_MESSAGE_FILENAME)));
        settings.setHasFooter(Files.exists(Paths.get(dataDirectory, UI_CONFIG_DIR, FOOTER_FILENAME)));
        settings.setAssetName(UI_CONFIG_DIR);

        return settings;
    }

    public SiteSettings getInitialValues() {

        SiteSettings siteSettings = new SiteSettings();

        // get the data directory home
        String propertyHome = System.getenv("IH_DATA_HOME");
        Path iceHome;

        if (StringUtils.isBlank(propertyHome)) {
            // check system property (-D in startup script)
            propertyHome = System.getProperty("IH_DATA_HOME");

            // still nothing, check home directory
            if (StringUtils.isBlank(propertyHome)) {
                String userHome = System.getProperty("user.home");
                iceHome = Paths.get(userHome, ".IHData");
            } else {
                iceHome = Paths.get(propertyHome);
            }
        } else {
            iceHome = Paths.get(propertyHome);
        }
        siteSettings.setDataDirectory(iceHome.toString());

        // get the

        return siteSettings;
    }
}



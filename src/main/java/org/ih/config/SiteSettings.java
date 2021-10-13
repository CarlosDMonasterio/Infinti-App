package org.ih.config;

import org.ih.dto.DataObject;

/**
 * Settings for the site
 *
 * @author Hector Plahar
 */
public class SiteSettings implements DataObject {

    private String version = "5.1.16";
    private String assetName;
    private boolean hasLogo;
    private boolean hasLoginMessage;
    private boolean hasFooter;
    private String dataDirectory;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isHasLogo() {
        return hasLogo;
    }

    public void setHasLogo(boolean hasLogo) {
        this.hasLogo = hasLogo;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public boolean isHasLoginMessage() {
        return hasLoginMessage;
    }

    public void setHasLoginMessage(boolean hasLoginMessage) {
        this.hasLoginMessage = hasLoginMessage;
    }

    public boolean isHasFooter() {
        return hasFooter;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }
}

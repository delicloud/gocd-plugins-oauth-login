package com.tw.go.plugin;

import java.io.Serializable;

public class PluginSettings implements Serializable {
    public static final String PLUGIN_SETTINGS_SERVER_BASE_URL = "server_base_url";
    public static final String PLUGIN_SETTINGS_OAUTH_SERVER = "oauth_server";
    public static final String PLUGIN_SETTINGS_ACCESS_TOKEN_URL = "access_token_url";
    public static final String PLUGIN_SETTINGS_PROFILE_URL = "profile_url";
    public static final String PLUGIN_SETTINGS_CONSUMER_KEY = "consumer_key";
    public static final String PLUGIN_SETTINGS_CONSUMER_SECRET = "consumer_secret";

    private String serverBaseURL;
    private String oauthServer;
    private String profileURL;
    private String consumerKey;
    private String consumerSecret;
    private String accessTokenURL;

    public PluginSettings(String serverBaseURL, String oauthServer, String accessTokenURL, String profileURL, String consumerKey, String consumerSecret) {
        this.serverBaseURL = serverBaseURL;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.oauthServer = oauthServer;
        this.accessTokenURL = accessTokenURL;
        this.profileURL = profileURL;
    }


    public String getAccessTokenURL() {
        return accessTokenURL;
    }

    public PluginSettings setAccessTokenURL(String accessTokenURL) {
        this.accessTokenURL = accessTokenURL;
        return this;
    }

    public String getServerBaseURL() {
        return serverBaseURL;
    }

    public void setServerBaseURL(String serverBaseURL) {
        this.serverBaseURL = serverBaseURL;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getOauthServer() {
        return oauthServer.replaceAll("/$", "");
    }

    public void setOauthServer(String oauthServer) {
        this.oauthServer = oauthServer;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public PluginSettings setProfileURL(String profileURL) {
        this.profileURL = profileURL;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginSettings that = (PluginSettings) o;

        if (!serverBaseURL.equals(that.serverBaseURL)) return false;
        if (!oauthServer.equals(that.oauthServer)) return false;
        if (!accessTokenURL.equals(that.accessTokenURL)) return false;
        if (!profileURL.equals(that.profileURL)) return false;
        if (!consumerKey.equals(that.consumerKey)) return false;
        return consumerSecret.equals(that.consumerSecret);

    }

    @Override
    public int hashCode() {
        int result = serverBaseURL.hashCode();
        result = 31 * result + oauthServer.hashCode();
        result = 31 * result + accessTokenURL.hashCode();
        result = 31 * result + profileURL.hashCode();
        result = 31 * result + consumerKey.hashCode();
        result = 31 * result + consumerSecret.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PluginSettings{" +
                "serverBaseURL='" + serverBaseURL + '\'' +
                ", oauthServer='" + oauthServer + '\'' +
                ", accessTokenURL='" + accessTokenURL + '\'' +
                ", profileURL='" + profileURL + '\'' +
                ", consumerKey='" + consumerKey + '\'' +
                ", consumerSecret='" + consumerSecret + '\'' +
                '}';
    }
}

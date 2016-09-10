package com.tw.go.plugin;

import java.io.Serializable;

public class PluginSettings implements Serializable {
    public static final String PLUGIN_SETTINGS_USER_NAME = "user_name";
    public static final String PLUGIN_SETTINGS_USER_PASSWORD = "password";
    public static final String PLUGIN_SETTINGS_SERVER_BASE_URL = "server_base_url";
    public static final String PLUGIN_SETTINGS_OAUTH_SERVER = "oauth_server";
    public static final String PLUGIN_SETTINGS_ACCESS_TOKEN_URL = "access_token_url";
    public static final String PLUGIN_SETTINGS_CONSUMER_KEY = "consumer_key";
    public static final String PLUGIN_SETTINGS_CONSUMER_SECRET = "consumer_secret";

    private String serverBaseURL;
    private String oauthServer;
    private String userName;
    private String password;
    private String consumerKey;
    private String consumerSecret;
    private String accessTokenUrl;

    public PluginSettings(String serverBaseURL, String oauthServer, String accessTokenUrl, String consumerKey, String consumerSecret, String userName, String password) {
        this.serverBaseURL = serverBaseURL;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.oauthServer = oauthServer;
        this.accessTokenUrl = accessTokenUrl;
        this.userName = userName;
        this.password = password;
    }


    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public PluginSettings setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginSettings that = (PluginSettings) o;

        if (!serverBaseURL.equals(that.serverBaseURL)) return false;
        if (!oauthServer.equals(that.oauthServer)) return false;
        if (!accessTokenUrl.equals(that.accessTokenUrl)) return false;
        if (!userName.equals(that.userName)) return false;
        if (!password.equals(that.password)) return false;
        if (!consumerKey.equals(that.consumerKey)) return false;
        return consumerSecret.equals(that.consumerSecret);

    }

    @Override
    public int hashCode() {
        int result = serverBaseURL.hashCode();
        result = 31 * result + oauthServer.hashCode();
        result = 31 * result + accessTokenUrl.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + consumerKey.hashCode();
        result = 31 * result + consumerSecret.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PluginSettings{" +
                "serverBaseURL='" + serverBaseURL + '\'' +
                ", oauthServer='" + oauthServer + '\'' +
                ", accessTokenUrl='" + accessTokenUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", consumerKey='" + consumerKey + '\'' +
                ", consumerSecret='" + consumerSecret + '\'' +
                '}';
    }
}

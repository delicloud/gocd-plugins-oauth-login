package com.tw.go.plugin.provider.deliflow;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.tw.go.plugin.PluginSettings;
import com.tw.go.plugin.User;
import com.tw.go.plugin.provider.PluginProvider;
import com.tw.go.plugin.util.Base64;

import java.io.Serializable;
import java.util.List;

public class DeliFlowPluginProvider implements PluginProvider {

    private static final String IMAGE = Base64.readImageBase64String("DeliFlow-Mark-Light-64px.png");
    private static Logger LOGGER = Logger.getLoggerFor(DeliFlowPluginProvider.class);

    @Override
    public String getPluginId() {
        return "deliflow.oauth.login";
    }



    @Override
    public String getName() {
        return "DeliFlow";
    }
    @Override
    public String getImageURL() {
        return IMAGE;
    }
    @Override
    public String getProviderName() {
        return "deliflow";
    }
    @Override
    public String getConsumerKeyPropertyName() {
        return "api.deliflow.com.consumer_key";
    }
    @Override
    public String getConsumerSecretPropertyName() {
        return "api.deliflow.com.consumer_secret";
    }
    @Override
    public List<User> searchUser(PluginSettings pluginSettings, String searchTerm) {
        return null;
    }
    @Override
    public boolean authorize(PluginSettings pluginSettings, User user) {
        return true;
    }

}

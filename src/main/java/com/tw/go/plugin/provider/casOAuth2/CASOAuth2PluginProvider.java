package com.tw.go.plugin.provider.casOAuth2;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.tw.go.plugin.PluginSettings;
import com.tw.go.plugin.User;
import com.tw.go.plugin.provider.OAuthProvider;
import com.tw.go.plugin.provider.PluginProvider;
import com.tw.go.plugin.util.Base64;

import java.util.List;

public class CASOAuth2PluginProvider implements PluginProvider {

    private static final String IMAGE = Base64.readImageBase64String("cas-logo-64px.png");
    private static Logger LOGGER = Logger.getLoggerFor(CASOAuth2PluginProvider.class);

    @Override
    public String getPluginId() {
        return "deliflow.cas-oauth2.login";
    }

    @Override
    public OAuthProvider getOAuthProvider(PluginSettings settings) throws Exception {
        return new CASOAuth2Provider(settings);
    }

    @Override
    public String getName() {
        return "Deliflow-CAS-OAuth2";
    }

    @Override
    public String getImageURL() {
        return IMAGE;
    }

    @Override
    public String getProviderName() {
        return "deliflow-cas-oauth2";
    }

    @Override
    public String getConsumerKeyPropertyName() {
        return "api.deliflow-cas-oauth2.com.consumer_key";
    }

    @Override
    public String getConsumerSecretPropertyName() {
        return "api.deliflow-cas-oauth2.com.consumer_secret";
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

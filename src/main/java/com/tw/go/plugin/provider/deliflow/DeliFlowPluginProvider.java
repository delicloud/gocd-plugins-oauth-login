package com.tw.go.plugin.provider.deliflow;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.tw.go.plugin.PluginSettings;
import com.tw.go.plugin.User;
import com.tw.go.plugin.provider.PluginProvider;
import com.tw.go.plugin.util.Base64;
import com.tw.go.plugin.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
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

        final String oauthServer = pluginSettings.getOauthServer();
        try {
            final String searchUrl = String.format("%s/users?name=%s", oauthServer, URLEncoder.encode(searchTerm, "UTF-8"));
            final String responseText = HttpUtil.getRequest(searchUrl, pluginSettings.getUserName(), pluginSettings.getPassword());
            LOGGER.debug(String.format("Search user by %s: %s", searchTerm, responseText));
            final JSONArray response = new JSONArray(responseText);
            return FluentIterable.from(response).transform(new Function<Object, User>() {
                @Override
                public User apply(Object obj) {
                    final JSONObject jsonObject = (JSONObject) obj;
                    return new User(jsonObject.optString("name", ""), jsonObject.optString("name", ""), jsonObject.optString("email", ""));
                }
            }).toList();

        } catch (Exception e) {
            LOGGER.error(String.format("Get error when search user by %s", searchTerm), e);
            throw new RuntimeException(String.format("Get error when search user by %s", searchTerm), e);
        }
    }

    @Override
    public boolean authorize(PluginSettings pluginSettings, User user) {
        return true;
    }

}

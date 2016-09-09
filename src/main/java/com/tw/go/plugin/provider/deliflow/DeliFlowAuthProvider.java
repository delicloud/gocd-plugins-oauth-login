package com.tw.go.plugin.provider.deliflow;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.tw.go.plugin.PluginSettings;
import com.tw.go.plugin.User;
import com.tw.go.plugin.provider.OAuthProvider;
import com.tw.go.plugin.util.HttpUtil;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Map;

public class DeliFlowAuthProvider implements OAuthProvider {

    private PluginSettings pluginSettings;
    public static final String ACCESS_TOKEN_PARAMETER_NAME = "access_token";
    private static Logger LOGGER = Logger.getLoggerFor(DeliFlowAuthProvider.class);
    private String successUrl;

    public DeliFlowAuthProvider(PluginSettings pluginSettings) throws Exception {
        this.pluginSettings = pluginSettings;
    }

    @Override
    public User verifyResponse(Map<String, String> requestParams) throws Exception {
        final Map<String, String> data = ImmutableMap.of(
                "code", requestParams.get("code"),
                "grant_type", "authorization_code",
                "redirect_uri", this.successUrl
        );

        final String responseText = HttpUtil.postRequest(getAccessTokenUrl(), data, pluginSettings.getConsumerKey(), pluginSettings.getConsumerSecret());
        final JSONObject response = new JSONObject(responseText);
        final String token = response.optString(ACCESS_TOKEN_PARAMETER_NAME);

        return getUserProfile(token);
    }

    @Override
    public String getLoginRedirectURL(String successUrl) throws Exception {
        this.successUrl = successUrl;
        StringBuffer sb = new StringBuffer();
        final String authenticationUrl = getAuthenticationUrl();
        sb.append(authenticationUrl);
        sb.append(authenticationUrl.indexOf('?') == -1 ? '?' : '&');
        sb.append("client_id=").append(pluginSettings.getConsumerKey());
        sb.append("&response_type=code");
        sb.append("&redirect_uri=").append(URLEncoder.encode(successUrl, "UTF-8"));
        return sb.toString();
    }

    private User getUserProfile(String token) throws Exception {
        final String responseText = HttpUtil.getRequest(String.format("%s/users/me?%s=%s", pluginSettings.getOauthServer(), ACCESS_TOKEN_PARAMETER_NAME, token));
        LOGGER.debug(String.format("User authorization with: %s", responseText));
        final JSONObject response = new JSONObject(responseText);
        return new User(response.optString("name", ""), response.optString("name", ""), response.optString("email", ""));
    }

    private String getAuthenticationUrl() {
        return pluginSettings.getOauthServer() + "/oauth/authorize";
    }

    private String getAccessTokenUrl() {
        return String.format("%s/oauth/token", pluginSettings.getOauthServer());
    }

}

package com.tw.go.plugin.provider.deliflow;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.tw.go.plugin.PluginSettings;
import com.tw.go.plugin.User;
import com.tw.go.plugin.util.HttpUtil;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Map;

public class DeliFlowAuthProvider implements Serializable {
    private PluginSettings pluginSettings;
    public static final String ACCESS_TOKEN_PARAMETER_NAME = "access_token";

    private static Logger LOGGER = Logger.getLoggerFor(DeliFlowAuthProvider.class);
    private String successUrl;

    public DeliFlowAuthProvider(PluginSettings pluginSettings) throws Exception {
        this.pluginSettings = pluginSettings;
    }

    public User verifyResponse(Map<String, String> requestParams) throws Exception {
        final Map<String, String> data = ImmutableMap.of(
                "code", requestParams.get("code"),
                "grant_type", "authorization_code",
                "redirect_uri", this.successUrl
        );

        final String responseText = HttpUtil.getAccessToken(getAccessTokenUrl(), requestParams.get("code"), this.successUrl, pluginSettings.getConsumerKey(), pluginSettings.getConsumerSecret());
        String[] params = responseText.split("&");
        String token = "";
        for (String param : params) {
            if (param.startsWith(ACCESS_TOKEN_PARAMETER_NAME)) {
                token = param.substring(ACCESS_TOKEN_PARAMETER_NAME.length() + 1, param.length());
            }
        }
        //final JSONObject response = new JSONObject(responseText);
        //final String token = response.optString(ACCESS_TOKEN_PARAMETER_NAME);

        return getUserProfile(token);
    }

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
        final String responseText = HttpUtil.getRequest(
                String.format("%s/cas/oauth2.0/profile?%s=%s",
                        pluginSettings.getOauthServer(),
                        ACCESS_TOKEN_PARAMETER_NAME, token));
        LOGGER.debug(String.format("User authorization with: %s", responseText));
        final JSONObject response = new JSONObject(responseText);
        return new User(response.optString("id", ""), response.optString("id", ""), response.optString("id", ""));
    }

    private String getAuthenticationUrl() {
        return String.format("%s/cas/oauth2.0/authorize", pluginSettings.getOauthServer());
    }

    private String getAccessTokenUrl() {
        return String.format("%s/cas/oauth2.0/accessToken", pluginSettings.getOauthServer().replaceAll("https://", "http://"));
    }
}

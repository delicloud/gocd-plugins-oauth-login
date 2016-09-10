package com.tw.go.plugin.handle;


import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;
import com.thoughtworks.go.plugin.api.response.GoApiResponse;
import com.tw.go.plugin.PluginSettings;
import com.tw.go.plugin.User;
import com.tw.go.plugin.provider.deliflow.DeliFlowAuthProvider;
import com.tw.go.plugin.util.Base64;
import com.tw.go.plugin.util.JSONUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

import static com.thoughtworks.go.plugin.api.logging.Logger.getLoggerFor;
import static com.tw.go.plugin.PluginSettings.*;
import static com.tw.go.plugin.util.RegexUtils.isBlank;
import static java.util.Arrays.asList;

public class GoApplicationAccessorWarp extends GoApplicationAccessor {
    public static final String GET_PLUGIN_SETTINGS = "go.processor.plugin-settings.get";
    public static final String GO_REQUEST_SESSION_PUT = "go.processor.session.put";
    public static final String GO_REQUEST_SESSION_GET = "go.processor.session.get";
    public static final String GO_REQUEST_SESSION_REMOVE = "go.processor.session.remove";
    public static final String GO_REQUEST_AUTHENTICATE_USER = "go.processor.authentication.authenticate-user";
    public static final String EXTENSION_NAME = "authentication";
    public static final List<String> goSupportedVersions = asList("1.0");
    private final GoApplicationAccessor goApplicationAccessor;
    private static com.thoughtworks.go.plugin.api.logging.Logger LOGGER = getLoggerFor(WebRequestAuthenticateRequestHandler.class);

    public GoApplicationAccessorWarp(GoApplicationAccessor goApplicationAccessor) {
        this.goApplicationAccessor = goApplicationAccessor;
    }

    @Override
    public GoApiResponse submit(GoApiRequest goApiRequest) {
        return goApplicationAccessor.submit(goApiRequest);
    }

    public PluginSettings getPluginSettings(String pluginId) {
        final String body = JSONUtils.toJSON(ImmutableMap.of("plugin-id", pluginId));
        GoApiResponse response = this.goApplicationAccessor.submit(createGoApiRequest(GET_PLUGIN_SETTINGS, body));

        if (isBlank(response.responseBody())) {
            throw new RuntimeException("plugin is not configured. please provide plugin settings.");
        }

        Map<String, String> responseBodyMap = JSONUtils.fromJSON(response.responseBody());
        return new PluginSettings(
                responseBodyMap.get(PLUGIN_SETTINGS_SERVER_BASE_URL),
                responseBodyMap.get(PLUGIN_SETTINGS_OAUTH_SERVER),
                responseBodyMap.get(PLUGIN_SETTINGS_ACCESS_TOKEN_URL),
                responseBodyMap.get(PLUGIN_SETTINGS_PROFILE_URL),
                responseBodyMap.get(PLUGIN_SETTINGS_CONSUMER_KEY),
                responseBodyMap.get(PLUGIN_SETTINGS_CONSUMER_SECRET)
        );
    }

    public static GoPluginIdentifier getGoPluginIdentifier() {
        return new GoPluginIdentifier(EXTENSION_NAME, goSupportedVersions);
    }

    public DeliFlowAuthProvider getStoredAuthProvider(String pluginId) {
        Map<String, String> requestMap = ImmutableMap.of("plugin-id", pluginId);

        GoApiRequest goApiRequest = createGoApiRequest(GO_REQUEST_SESSION_GET, JSONUtils.toJSON(requestMap));
        GoApiResponse response = submit(goApiRequest);
        // handle error
        String responseBody = response.responseBody();
        LOGGER.info(responseBody);
        Map<String, String> sessionData = JSONUtils.fromJSON(responseBody);
        return deserializeObject(sessionData.get("social-auth-manager"));
    }


    public void storeAuthProvider(String pluginId, DeliFlowAuthProvider authProvider) {
        Map<String, Serializable> requestMap = ImmutableMap.of(
                "plugin-id", pluginId,
                "session-data", ImmutableMap.of("social-auth-manager", serializeObject(authProvider))
        );

        GoApiRequest goApiRequest = createGoApiRequest(GO_REQUEST_SESSION_PUT, JSONUtils.toJSON(requestMap));
        GoApiResponse response = submit(goApiRequest);
        // handle error
    }

    public void deleteStoredAuthProvider(String pluginId) {
        Map<String, String> requestMap = ImmutableMap.of("plugin-id", pluginId);
        GoApiRequest goApiRequest = createGoApiRequest(GO_REQUEST_SESSION_REMOVE, JSONUtils.toJSON(requestMap));
        GoApiResponse response = submit(goApiRequest);
        // handle error
    }

    public void authenticateUser(User user) {
        final Map<String, Map<String, String>> userMap = ImmutableMap.of("user", user.getUserMap());
        GoApiRequest authenticateUserRequest = createGoApiRequest(GO_REQUEST_AUTHENTICATE_USER, JSONUtils.toJSON(userMap));
        GoApiResponse authenticateUserResponse = submit(authenticateUserRequest);
        // handle error
    }

    private String serializeObject(DeliFlowAuthProvider serializable) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return Base64.encodToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T deserializeObject(String deserializeText) {
        try {
            byte bytes[] = Base64.decodeBase64(deserializeText);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GoApiRequest createGoApiRequest(final String api, final String responseBody) {
        return new GoApiRequest() {
            @Override
            public String api() {
                return api;
            }

            @Override
            public String apiVersion() {
                return "1.0";
            }

            @Override
            public GoPluginIdentifier pluginIdentifier() {
                return getGoPluginIdentifier();
            }

            @Override
            public Map<String, String> requestParameters() {
                return null;
            }

            @Override
            public Map<String, String> requestHeaders() {
                return null;
            }

            @Override
            public String requestBody() {
                return responseBody;
            }
        };
    }
}
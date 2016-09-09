package com.tw.go.plugin.handle;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.tw.go.plugin.PluginSettings;
import com.tw.go.plugin.User;
import com.tw.go.plugin.provider.OAuthProvider;
import com.tw.go.plugin.provider.PluginProvider;
import com.tw.go.plugin.util.JSONUtils;

import static com.thoughtworks.go.plugin.api.logging.Logger.getLoggerFor;
import static org.apache.commons.httpclient.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.commons.httpclient.HttpStatus.SC_MOVED_TEMPORARILY;

public class WebRequestAuthenticateRequestHandler implements RequestHandler {
    public static final String WEB_REQUEST_AUTHENTICATE = "authenticate";
    private PluginProvider pluginProvider;
    private static com.thoughtworks.go.plugin.api.logging.Logger LOGGER = getLoggerFor(WebRequestAuthenticateRequestHandler.class);

    public WebRequestAuthenticateRequestHandler(PluginProvider pluginProvider) {

        this.pluginProvider = pluginProvider;
    }

    @Override
    public boolean canHandle(GoPluginApiRequest goPluginApiRequest) {
        return WEB_REQUEST_AUTHENTICATE.equals(goPluginApiRequest.requestName());
    }

    @Override
    public GoPluginApiResponse handle(GoApplicationAccessorWarp goApplicationAccessor, GoPluginApiRequest goPluginApiRequest) {
        return handleAuthenticateWebRequest(goApplicationAccessor, goPluginApiRequest);
    }

    private GoPluginApiResponse handleAuthenticateWebRequest(GoApplicationAccessorWarp goApplicationAccessor, final GoPluginApiRequest goPluginApiRequest) {
        try {
            PluginSettings pluginSettings = goApplicationAccessor.getPluginSettings(pluginProvider.getPluginId());
            OAuthProvider authProvider = goApplicationAccessor.getStoredAuthProvider(this.pluginProvider.getPluginId());
            if (authProvider == null) {
                throw new RuntimeException("DeliFlow auth provider not set");
            }

            final User user = authProvider.verifyResponse(goPluginApiRequest.requestParameters());

            if (pluginProvider.authorize(pluginSettings, user)) {
                goApplicationAccessor.authenticateUser(user);
            }

            return JSONUtils.renderJSON(SC_MOVED_TEMPORARILY, ImmutableMap.of("Location", pluginSettings.getServerBaseURL()), null);
        } catch (Exception e) {
            LOGGER.error("Error occurred while OAuth authenticate.", e);
            return JSONUtils.renderJSON(SC_INTERNAL_SERVER_ERROR, null);
        } finally {
            goApplicationAccessor.deleteStoredAuthProvider(this.pluginProvider.getPluginId());
        }

    }


}

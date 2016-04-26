package com.tw.go.plugin.handle;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.tw.go.plugin.PluginSettings;
import com.tw.go.plugin.provider.PluginProvider;
import com.tw.go.plugin.provider.deliflow.DeliFlowAuthProvider;
import com.tw.go.plugin.util.JSONUtils;

import static com.thoughtworks.go.plugin.api.logging.Logger.getLoggerFor;
import static org.apache.commons.httpclient.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.commons.httpclient.HttpStatus.SC_MOVED_TEMPORARILY;

public class WebRequestIndexRequestHandler implements RequestHandler {
    public static final String WEB_REQUEST_INDEX = "index";
    private PluginProvider pluginProvider;
    private static com.thoughtworks.go.plugin.api.logging.Logger LOGGER = getLoggerFor(WebRequestIndexRequestHandler.class);

    public WebRequestIndexRequestHandler(PluginProvider pluginProvider) {

        this.pluginProvider = pluginProvider;
    }

    @Override
    public boolean canHandle(GoPluginApiRequest goPluginApiRequest) {
        return WEB_REQUEST_INDEX.equals(goPluginApiRequest.requestName());
    }

    @Override
    public GoPluginApiResponse handle(GoApplicationAccessorWarp goApplicationAccessor, GoPluginApiRequest goPluginApiRequest) {
        return handleSetupLoginWebRequest(goApplicationAccessor);
    }


    private GoPluginApiResponse handleSetupLoginWebRequest(GoApplicationAccessorWarp goApplicationAccessor) {
        try {
            PluginSettings pluginSettings = goApplicationAccessor.getPluginSettings(pluginProvider.getPluginId());
            final DeliFlowAuthProvider authProvider = new DeliFlowAuthProvider(pluginSettings);
            String redirectURL = authProvider.getLoginRedirectURL(getGoRedirectURL(pluginSettings.getServerBaseURL()));
            goApplicationAccessor.storeAuthProvider(this.pluginProvider.getPluginId(), authProvider);
            return JSONUtils.renderJSON(SC_MOVED_TEMPORARILY, ImmutableMap.of("Location", redirectURL), null);
        } catch (Exception e) {
            LOGGER.error("Error occurred while OAuth setup.", e);
            return JSONUtils.renderJSON(SC_INTERNAL_SERVER_ERROR, null);
        }
    }


    private String getGoRedirectURL(String serverBaseURL) {
        return String.format("%s/go/plugin/interact/%s/authenticate", serverBaseURL, pluginProvider.getPluginId());
    }

}

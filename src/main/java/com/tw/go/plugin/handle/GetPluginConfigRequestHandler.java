package com.tw.go.plugin.handle;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.tw.go.plugin.provider.PluginProvider;
import com.tw.go.plugin.util.JSONUtils;

import java.util.Map;

import static org.apache.commons.httpclient.HttpStatus.SC_OK;

public class GetPluginConfigRequestHandler implements RequestHandler {
    public static final String PLUGIN_CONFIGURATION = "go.authentication.plugin-configuration";
    private PluginProvider pluginProvider;

    public GetPluginConfigRequestHandler(PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
    }

    @Override
    public boolean canHandle(GoPluginApiRequest goPluginApiRequest) {
        return PLUGIN_CONFIGURATION.equals(goPluginApiRequest.requestName());
    }

    @Override
    public GoPluginApiResponse handle(GoApplicationAccessorWarp goApplicationAccessor, GoPluginApiRequest goPluginApiRequest) {
        return JSONUtils.renderJSON(SC_OK, getPluginConfiguration(pluginProvider));
    }

    public Map<String, ?> getPluginConfiguration(PluginProvider pluginProvider) {
        Map<String, ? extends Object> configuration = ImmutableMap.of("display-name", this.pluginProvider.getName(),
                "display-image-url", pluginProvider.getImageURL(),
                "supports-web-based-authentication", true,
                "supports-password-based-authentication", false);
        return configuration;
    }
}

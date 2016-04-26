package com.tw.go.plugin.handle;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.tw.go.plugin.PluginSettings;

import java.util.HashMap;
import java.util.Map;

import static com.tw.go.plugin.util.JSONUtils.renderJSON;
import static org.apache.commons.httpclient.HttpStatus.SC_OK;

public class GetSettingConfigRequestHandler implements RequestHandler {
    public static final String PLUGIN_SETTINGS_GET_CONFIGURATION = "go.plugin-settings.get-configuration";

    @Override
    public boolean canHandle(GoPluginApiRequest goPluginApiRequest) {
        return PLUGIN_SETTINGS_GET_CONFIGURATION.equals(goPluginApiRequest.requestName());
    }

    @Override
    public GoPluginApiResponse handle(GoApplicationAccessorWarp goApplicationAccessor, GoPluginApiRequest goPluginApiRequest) {
        return handleGetPluginSettingsConfiguration();
    }

    private GoPluginApiResponse handleGetPluginSettingsConfiguration() {
        ImmutableMap<String, Map<String, Object>> response = ImmutableMap.of(
                PluginSettings.PLUGIN_SETTINGS_SERVER_BASE_URL, createField("Server Base URL", null, true, false, "0"),
                PluginSettings.PLUGIN_SETTINGS_CONSUMER_KEY, createField("OAuth Client ID", null, true, false, "1"),
                PluginSettings.PLUGIN_SETTINGS_CONSUMER_SECRET, createField("OAuth Client Secret", null, true, false, "2")
        );

        return renderJSON(SC_OK, response);
    }

    private Map<String, Object> createField(String displayName, String defaultValue, boolean isRequired, boolean isSecure, String displayOrder) {
        final HashMap<String, Object> maps = Maps.newHashMap();
        maps.put("display-name", displayName);
        maps.put("default-value", defaultValue);
        maps.put("required", isRequired);
        maps.put("secure", isSecure);
        maps.put("display-order", displayOrder);
        return maps;
    }
}

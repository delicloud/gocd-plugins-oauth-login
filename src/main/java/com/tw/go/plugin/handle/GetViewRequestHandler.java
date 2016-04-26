package com.tw.go.plugin.handle;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.tw.go.plugin.util.JSONUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static org.apache.commons.httpclient.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.commons.httpclient.HttpStatus.SC_OK;


public class GetViewRequestHandler implements RequestHandler {
    public static final String PLUGIN_SETTINGS_GET_VIEW = "go.plugin-settings.get-view";
    public static final String PLUGIN_SETTINGS_TEMPLATE_HTML = "/plugin-settings.template.html";

    @Override
    public boolean canHandle(GoPluginApiRequest goPluginApiRequest) {
        return PLUGIN_SETTINGS_GET_VIEW.equals(goPluginApiRequest.requestName());
    }

    @Override
    public GoPluginApiResponse handle(GoApplicationAccessorWarp goApplicationAccessor, GoPluginApiRequest goPluginApiRequest) {
        try {
            return handleGetPluginSettingsView();
        } catch (IOException e) {
            return JSONUtils.renderJSON(SC_INTERNAL_SERVER_ERROR, String.format("Failed to find template: %s", e.getMessage()));
        }
    }

    private GoPluginApiResponse handleGetPluginSettingsView() throws IOException {
        final String html = IOUtils.toString(getClass().getResourceAsStream(PLUGIN_SETTINGS_TEMPLATE_HTML), "UTF-8");
        return JSONUtils.renderJSON(SC_OK, ImmutableMap.of("template", html));
    }
}

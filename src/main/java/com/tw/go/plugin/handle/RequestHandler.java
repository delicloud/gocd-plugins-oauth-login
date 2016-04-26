package com.tw.go.plugin.handle;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public interface RequestHandler {
    boolean canHandle(GoPluginApiRequest goPluginApiRequest);

    GoPluginApiResponse handle(GoApplicationAccessorWarp goApplicationAccessor, GoPluginApiRequest goPluginApiRequest);

}

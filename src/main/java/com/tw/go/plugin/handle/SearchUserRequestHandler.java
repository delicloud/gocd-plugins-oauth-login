package com.tw.go.plugin.handle;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.tw.go.plugin.PluginSettings;
import com.tw.go.plugin.User;
import com.tw.go.plugin.provider.PluginProvider;
import com.tw.go.plugin.util.JSONUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.httpclient.HttpStatus.SC_OK;

public class SearchUserRequestHandler implements RequestHandler {
    public static final String SEARCH_USER = "go.authentication.search-user";
    private PluginProvider pluginProvider;

    public SearchUserRequestHandler(PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
    }

    @Override
    public boolean canHandle(GoPluginApiRequest goPluginApiRequest) {
        return SEARCH_USER.equals(goPluginApiRequest.requestName());
    }

    @Override
    public GoPluginApiResponse handle(GoApplicationAccessorWarp goApplicationAccessor, GoPluginApiRequest goPluginApiRequest) {
        return handleSearchUserRequest(goApplicationAccessor, goPluginApiRequest);
    }

    private GoPluginApiResponse handleSearchUserRequest(GoApplicationAccessorWarp goApplicationAccessor, GoPluginApiRequest goPluginApiRequest) {
        Map<String, String> requestBodyMap = JSONUtils.fromJSON(goPluginApiRequest.requestBody());
        String searchTerm = requestBodyMap.get("search-term");
        PluginSettings pluginSettings = goApplicationAccessor.getPluginSettings(pluginProvider.getPluginId());
        List<User> users = pluginProvider.searchUser(pluginSettings, searchTerm);
        if (users == null || users.isEmpty()) {
            return JSONUtils.renderJSON(SC_OK, null);
        } else {
            List<Map> searchResults = new ArrayList<Map>();
            for (User user : users) {
                searchResults.add(user.getUserMap());
            }
            return JSONUtils.renderJSON(SC_OK, searchResults);
        }
    }

}

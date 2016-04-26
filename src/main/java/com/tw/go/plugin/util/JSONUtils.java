package com.tw.go.plugin.util;

import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Map;

public class JSONUtils {

    public static <T> T fromJSON(String json) {
        return (T) new GsonBuilder().create().fromJson(json, Object.class);
    }

    public static String toJSON(Object object) {
        return new GsonBuilder().create().toJson(object);
    }

    public static GoPluginApiResponse renderJSON(final int responseCode, final Map<String, String> responseHeaders, Object response) {
        final String json = response == null ? null : toJSON(response);
        return new GoPluginApiResponse() {
            @Override
            public int responseCode() {
                return responseCode;
            }

            @Override
            public Map<String, String> responseHeaders() {
                return responseHeaders;
            }

            @Override
            public String responseBody() {
                return json;
            }
        };
    }

    public static GoPluginApiResponse renderJSON(final int responseCode, Object response) {
        return renderJSON(responseCode, null, response);
    }
}

package com.tw.go.plugin.handle;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.tw.go.plugin.PluginSettings;
import com.tw.go.plugin.util.FieldValidator;
import com.tw.go.plugin.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.httpclient.HttpStatus.SC_OK;

public class SettingValdationRequestHandler implements RequestHandler {
    public static final String PLUGIN_SETTINGS_VALIDATE_CONFIGURATION = "go.plugin-settings.validate-configuration";

    @Override
    public boolean canHandle(GoPluginApiRequest goPluginApiRequest) {
        return PLUGIN_SETTINGS_VALIDATE_CONFIGURATION.equals(goPluginApiRequest.requestName());
    }

    @Override
    public GoPluginApiResponse handle(GoApplicationAccessorWarp goApplicationAccessor, GoPluginApiRequest goPluginApiRequest) {
        return handleValidatePluginSettingsConfiguration(goPluginApiRequest);
    }

    private GoPluginApiResponse handleValidatePluginSettingsConfiguration(GoPluginApiRequest goPluginApiRequest) {
        Map<String, Object> responseMap = JSONUtils.fromJSON(goPluginApiRequest.requestBody());
        final Map<String, String> configuration = keyValuePairs(responseMap, "plugin-settings");
        List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();

        validate(response, new FieldValidator() {
            @Override
            public void validate(Map<String, Object> fieldValidation) {
                validateRequiredField(configuration, fieldValidation, PluginSettings.PLUGIN_SETTINGS_SERVER_BASE_URL, "Server Base URL");
            }
        });

        validate(response, new FieldValidator() {
            @Override
            public void validate(Map<String, Object> fieldValidation) {
                validateRequiredField(configuration, fieldValidation, PluginSettings.PLUGIN_SETTINGS_CONSUMER_KEY, "OAuth Client ID");
            }
        });

        validate(response, new FieldValidator() {
            @Override
            public void validate(Map<String, Object> fieldValidation) {
                validateRequiredField(configuration, fieldValidation, PluginSettings.PLUGIN_SETTINGS_CONSUMER_SECRET, "OAuth Client Secret");
            }
        });

        validate(response, new FieldValidator() {
            @Override
            public void validate(Map<String, Object> fieldValidation) {
                validateRequiredField(configuration, fieldValidation, PluginSettings.PLUGIN_SETTINGS_USER_NAME, "DeliFlow User Namet");
            }
        });

        validate(response, new FieldValidator() {
            @Override
            public void validate(Map<String, Object> fieldValidation) {
                validateRequiredField(configuration, fieldValidation, PluginSettings.PLUGIN_SETTINGS_USER_PASSWORD, "DeliFlow Password");
            }
        });

        return JSONUtils.renderJSON(SC_OK, response);
    }

    private Map<String, String> keyValuePairs(Map<String, Object> map, String mainKey) {
        Map<String, String> keyValuePairs = new HashMap<String, String>();
        Map<String, Object> fieldsMap = (Map<String, Object>) map.get(mainKey);
        for (String field : fieldsMap.keySet()) {
            Map<String, Object> fieldProperties = (Map<String, Object>) fieldsMap.get(field);
            String value = (String) fieldProperties.get("value");
            keyValuePairs.put(field, value);
        }
        return keyValuePairs;
    }

    private void validate(List<Map<String, Object>> response, FieldValidator fieldValidator) {
        Map<String, Object> fieldValidation = new HashMap<String, Object>();
        fieldValidator.validate(fieldValidation);
        if (!fieldValidation.isEmpty()) {
            response.add(fieldValidation);
        }
    }

    private void validateRequiredField(Map<String, String> configuration, Map<String, Object> fieldMap, String key, String name) {
        if (configuration.get(key) == null || configuration.get(key).isEmpty()) {
            fieldMap.put("key", key);
            fieldMap.put("message", String.format("'%s' is a required field", name));
        }
    }
}

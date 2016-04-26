package com.tw.go.plugin;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.tw.go.plugin.handle.GoApplicationAccessorWarp;
import com.tw.go.plugin.handle.RequestHandlerDispenser;
import com.tw.go.plugin.provider.PluginProvider;

import java.lang.reflect.Constructor;
import java.util.Properties;

import static com.tw.go.plugin.handle.GoApplicationAccessorWarp.getGoPluginIdentifier;

@Extension
public class OAuthLoginPlugin implements GoPlugin {
    public static final String PLUGIN_PROPERTIES = "/defaults.properties";
    public static final String PROVIDER_KEY = "provider";
    private static Logger LOGGER = Logger.getLoggerFor(OAuthLoginPlugin.class);

    private final RequestHandlerDispenser requestHandlerDispenser;

    private final PluginProvider pluginProvider;
    private GoApplicationAccessorWarp goApplicationAccessor;

    public OAuthLoginPlugin() {
        try {
            pluginProvider = createPluginProvider();
            requestHandlerDispenser = new RequestHandlerDispenser(pluginProvider);
        } catch (Exception e) {
            throw new RuntimeException("could not create provider", e);
        }
    }

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        this.goApplicationAccessor = new GoApplicationAccessorWarp(goApplicationAccessor);
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest goPluginApiRequest) {
        try {
            return requestHandlerDispenser.handle(goApplicationAccessor, goPluginApiRequest);
        } catch (Exception e) {
            LOGGER.error(String.format("Handle request error for %s", goPluginApiRequest.requestName()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return getGoPluginIdentifier();
    }

    private PluginProvider createPluginProvider() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream(PLUGIN_PROPERTIES));
        Class<?> providerClass = Class.forName(properties.getProperty(PROVIDER_KEY));
        Constructor<?> constructor = providerClass.getConstructor();
        return (PluginProvider) constructor.newInstance();
    }


}
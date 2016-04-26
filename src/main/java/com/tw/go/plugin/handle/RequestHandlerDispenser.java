package com.tw.go.plugin.handle;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.tw.go.plugin.provider.PluginProvider;
import com.tw.go.plugin.util.JSONUtils;

import static org.apache.commons.httpclient.HttpStatus.SC_NOT_FOUND;

public class RequestHandlerDispenser {

    private final ImmutableList<RequestHandler> handlers;

    public RequestHandlerDispenser(PluginProvider pluginProvider) {
        handlers = ImmutableList.of(
                new GetSettingConfigRequestHandler(),
                new GetViewRequestHandler(),
                new SettingValdationRequestHandler(),
                new GetPluginConfigRequestHandler(pluginProvider),
                new SearchUserRequestHandler(pluginProvider),
                new WebRequestIndexRequestHandler(pluginProvider),
                new WebRequestAuthenticateRequestHandler(pluginProvider)
        );
    }

    public GoPluginApiResponse handle(GoApplicationAccessorWarp goApplicationAccessor, final GoPluginApiRequest goPluginApiRequest) {
        final Optional<RequestHandler> handlerOptional = FluentIterable.from(handlers).firstMatch(new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler requestHandler) {
                return requestHandler.canHandle(goPluginApiRequest);
            }
        });

        if (!handlerOptional.isPresent()) {
            return JSONUtils.renderJSON(SC_NOT_FOUND, null);
        }
        return handlerOptional.get().handle(goApplicationAccessor, goPluginApiRequest);
    }

}

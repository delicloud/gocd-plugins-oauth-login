package com.tw.go.plugin.provider;

import com.tw.go.plugin.User;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by yongliuli on 9/8/16.
 */
public interface OAuthProvider extends Serializable {
    User verifyResponse(Map<String, String> requestParams) throws Exception;

    String getLoginRedirectURL(String successUrl) throws Exception;
}

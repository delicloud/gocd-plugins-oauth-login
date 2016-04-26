/*
 ===========================================================================
 Copyright (c) 2010 BrickRed Technologies Limited

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sub-license, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ===========================================================================

 */
package com.tw.go.plugin.util;

import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpUtil {


    public static String buildParams(final Map<String, String> params)
            throws Exception {
        List<String> argList = new ArrayList<String>();

        for (String key : params.keySet()) {
            String val = params.get(key);
            if (val != null && val.length() > 0) {
                String arg = key + "=" + encodeURIComponent(val);
                argList.add(arg);
            }
        }
        Collections.sort(argList);
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < argList.size(); i++) {
            s.append(argList.get(i));
            if (i != argList.size() - 1) {
                s.append("&");
            }
        }
        return s.toString();
    }

    public static String encodeURIComponent(final String value)
            throws Exception {
        if (value == null) {
            return "";
        }

        try {
            return URLEncoder.encode(value, "utf-8")
                    .replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public static String postRequest(String urlStr, Map<String, String> data, String user, String pwd) throws Exception {
        final URL url = new URL(urlStr);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", basicAuthorizationValue(user, pwd));
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");
        con.setInstanceFollowRedirects(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.write(buildParams(data).getBytes("UTF-8"));
        out.flush();
        return IOUtils.toString(con.getInputStream());
    }

    private static String basicAuthorizationValue(String user, String pwd) {
        byte[] plainCredsBytes = String.format("%s:%s", user, pwd).getBytes();
        String base64CredsString = Base64.encodToString(plainCredsBytes);
        return String.format("Basic %s", base64CredsString);

    }

    public static String getRequest(String urlStr) throws Exception {
        return getRequest(urlStr, null, null);
    }

    public static String getRequest(String urlStr, String user, String pwd) throws Exception {
        final URL url = new URL(urlStr);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        if (user != null && pwd != null) {
            con.setRequestProperty("Authorization", basicAuthorizationValue(user, pwd));
        }
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("GET");
        con.setInstanceFollowRedirects(true);
        return IOUtils.toString(con.getInputStream());
    }
}

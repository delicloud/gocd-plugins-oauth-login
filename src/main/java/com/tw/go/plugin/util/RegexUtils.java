package com.tw.go.plugin.util;

public class RegexUtils {

    public static boolean matchesRegex(String str, String pattern) {
        if (isBlank(pattern)) {
            return true;
        }
        try {
            if (str.matches(pattern.trim())) {
                return true;
            }
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    public static boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }
}

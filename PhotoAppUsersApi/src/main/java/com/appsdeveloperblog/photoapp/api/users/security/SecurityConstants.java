package com.appsdeveloperblog.photoapp.api.users.security;

import com.appsdeveloperblog.photoapp.api.users.SpringApplicationContext;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKER_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    // public static final String TOKER_SECRET = "jf9i4jgu83nfl0"; // Must be unique
    private static String tokenSecret = null;

    public static String getTokenSecret() {
	if (tokenSecret == null) {
	    AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
	    tokenSecret = appProperties.getTokenSecret();
	}

	return tokenSecret;
    }
}

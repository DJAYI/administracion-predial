package com.agmdesarrollos.geopix.utils.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieService {

    @Value("${jwt.cookie.access-token-name}")
    private String accessTokenCookieName;

    @Value("${jwt.cookie.refresh-token-name}")
    private String refreshTokenCookieName;

    @Value("${jwt.access-token.expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token.expiration-ms}")
    private long refreshTokenExpirationMs;

    @Value("${jwt.cookie.secure}")
    private boolean secureCookie;

    @Value("${jwt.cookie.same-site}")
    private String sameSite;

    @Value("${jwt.cookie.domain:}")
    private String domain;

    @Value("${jwt.cookie.path}")
    private String cookiePath;

    /**
     * Adds the access_token and refresh_token as HttpOnly secure cookies to the response.
     */
    public void addTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessCookie = buildCookie(accessTokenCookieName, accessToken, accessTokenExpirationMs / 1000);
        ResponseCookie refreshCookie = buildCookie(refreshTokenCookieName, refreshToken, refreshTokenExpirationMs / 1000);

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    /**
     * Clears both token cookies by setting maxAge to 0.
     */
    public void clearTokenCookies(HttpServletResponse response) {
        ResponseCookie accessCookie = buildCookie(accessTokenCookieName, "", 0);
        ResponseCookie refreshCookie = buildCookie(refreshTokenCookieName, "", 0);

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    public String getAccessTokenCookieName() {
        return accessTokenCookieName;
    }

    public String getRefreshTokenCookieName() {
        return refreshTokenCookieName;
    }

    private ResponseCookie buildCookie(String name, String value, long maxAgeSeconds) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite(sameSite)
                .path(cookiePath)
                .maxAge(maxAgeSeconds);
        
        // NEVER set Domain - let browser use current host (most flexible)
        // Setting Domain causes mismatch when accessing via different hostnames
        
        return builder.build();
    }
}

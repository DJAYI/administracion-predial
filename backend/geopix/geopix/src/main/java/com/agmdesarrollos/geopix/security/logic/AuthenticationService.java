package com.agmdesarrollos.geopix.security.logic;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final TokenRedisService tokenRedisService;
    private final CookieService cookieService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public void authenticate(String username, String password, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        UserDetails user = userDetailsService.loadUserByUsername(username);
        
        // Sesión única: Revocar previos
        tokenRedisService.revokeAllUserTokens(username);
        
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenRedisService.saveAccessToken(username, accessToken);
        tokenRedisService.saveRefreshToken(username, refreshToken);

        cookieService.addAccessTokenCookie(response, accessToken);
        cookieService.addRefreshTokenCookie(response, refreshToken);
    }

    public void refreshToken(String refreshToken, HttpServletResponse response) {
        String username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(refreshToken, user) && 
                tokenRedisService.isRefreshTokenValid(username, refreshToken)) {
                
                String newAccessToken = jwtService.generateToken(user);
                tokenRedisService.saveAccessToken(username, newAccessToken);
                cookieService.addAccessTokenCookie(response, newAccessToken);
            } else {
                tokenRedisService.revokeAllUserTokens(username);
                cookieService.clearAuthCookies(response);
                throw new RuntimeException("Invalid refresh token");
            }
        }
    }
}

package com.agmdesarrollos.geopix.security;

import com.agmdesarrollos.geopix.security.dto.AuthResponse;
import com.agmdesarrollos.geopix.security.dto.LoginRequest;
import com.agmdesarrollos.geopix.security.jpa.RoleEntity;
import com.agmdesarrollos.geopix.security.jpa.UserEntity;
import com.agmdesarrollos.geopix.utils.jwt.CookieService;
import com.agmdesarrollos.geopix.utils.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final CustomUserDetailsService userDetailsService;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          CookieService cookieService,
                          CustomUserDetailsService userDetailsService,
                          TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request,
                                              HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserEntity user = (UserEntity) authentication.getPrincipal();

            // Issue a new token pair and persist both in the database
            TokenService.TokenPair pair = tokenService.issueTokenPair(user);

            cookieService.addTokenCookies(response, pair.accessToken(), pair.refreshToken());

            return ResponseEntity.ok(buildAuthResponse(user, "Login successful"));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, "Invalid credentials"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication authentication) {
        String username = authentication.getName();
        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(buildAuthResponse(user, null));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletRequest request,
                                               HttpServletResponse response) {
        // Attempt to identify the user from the access-token cookie to revoke DB tokens
        String accessToken = extractAccessTokenFromCookies(request);
        if (accessToken != null && jwtService.validateToken(accessToken)) {
            String username = jwtService.extractUsername(accessToken);
            UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(username);
            tokenService.revokeAllUserTokens(user);
        }

        cookieService.clearTokenCookies(response);
        return ResponseEntity.ok(new AuthResponse(null, null, null, "Logout successful"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request,
                                                HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookies(request);

        // 1. Validate JWT signature & expiration
        if (refreshToken == null || !jwtService.validateToken(refreshToken)) {
            cookieService.clearTokenCookies(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, "Invalid or expired refresh token"));
        }

        // 2. Verify it is a refresh token
        String tokenType = jwtService.extractTokenType(refreshToken);
        if (!"refresh".equals(tokenType)) {
            cookieService.clearTokenCookies(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, "Invalid token type"));
        }

        // 3. Verify the token is still active in the database (not revoked)
        if (!tokenService.isTokenActive(refreshToken)) {
            cookieService.clearTokenCookies(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, "Token has been revoked"));
        }

        // 4. Rotate: revoke old pair, issue new pair
        String username = jwtService.extractUsername(refreshToken);
        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(username);

        TokenService.TokenPair pair = tokenService.rotateTokens(user);

        cookieService.addTokenCookies(response, pair.accessToken(), pair.refreshToken());

        return ResponseEntity.ok(buildAuthResponse(user, "Token refreshed successfully"));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private AuthResponse buildAuthResponse(UserEntity user, String message) {
        List<String> roles = user.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toList());
        return new AuthResponse(user.getUsername(), user.getEmail(), roles, message);
    }

    private String extractAccessTokenFromCookies(HttpServletRequest request) {
        return extractCookie(request, cookieService.getAccessTokenCookieName());
    }

    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        return extractCookie(request, cookieService.getRefreshTokenCookieName());
    }

    private String extractCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}

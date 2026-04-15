package com.agmdesarrollos.geopix.security.filters;

import com.agmdesarrollos.geopix.security.TokenService;
import com.agmdesarrollos.geopix.utils.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    /** Paths that are public and should skip JWT validation entirely. */
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/auth/login",
            "/api/auth/refresh",
            "/actuator/health"
    );

    private final JwtService jwtService;
    private final TokenService tokenService;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository =
            new RequestAttributeSecurityContextRepository();

    @Value("${jwt.cookie.access-token-name}")
    private String accessTokenCookieName;

    public JwtAuthenticationFilter(JwtService jwtService, TokenService tokenService, org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Skip this filter entirely for public endpoints so it cannot
     * interfere with unauthenticated flows like login and refresh.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return PUBLIC_PATHS.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        // Debug: Log the full request info
        log.debug("Request: {} {} | Origin: {} | Referer: {}", 
                request.getMethod(), request.getRequestURI(), 
                request.getHeader("Origin"), request.getHeader("Referer"));

        String token = extractTokenFromCookies(request);

        if (token != null
                && jwtService.validateToken(token)
                && "access".equals(jwtService.extractTokenType(token))
                && tokenService.isTokenActive(token)) {

            String username = jwtService.extractUsername(token);
            List<String> roles = jwtService.extractRoles(token);

            org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Spring Security 7.x requires explicit context saving
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            log.debug("Authenticated user '{}' with roles {} for {} {}",
                    username, roles, request.getMethod(), request.getServletPath());
        } else if (token != null) {
            log.debug("JWT validation failed for {} {}", request.getMethod(), request.getServletPath());
        }

        chain.doFilter(request, response);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.warn("No cookies in request to {} {}", request.getMethod(), request.getRequestURI());
            return null;
        }
        log.debug("Cookies received: {}", Arrays.stream(cookies)
                .map(Cookie::getName)
                .collect(Collectors.joining(", ")));
        
        return Arrays.stream(cookies)
                .filter(cookie -> accessTokenCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}

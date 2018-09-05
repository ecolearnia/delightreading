package com.delightreading.authsupport;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class CookieAuthorizationFilter extends BasicAuthenticationFilter {

    public static final String COOKIE_NAME = "dr_token";

    JwtService jwtService;

    public CookieAuthorizationFilter(AuthenticationManager authManager, JwtService jwtService) {
        super(authManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            Optional<OAuth2AuthenticationToken> optAuth = obtainAuthentication(req);
            optAuth.ifPresent(
                    auth -> SecurityContextHolder.getContext().setAuthentication(auth)
            );
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("SecurityContextHolder not populated from Cookie, as it already contained: '"
                        + SecurityContextHolder.getContext().getAuthentication() + "'");
            }
        }

        chain.doFilter(req, res);
    }


    private Optional<OAuth2AuthenticationToken> obtainAuthentication(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }
        try {
            Optional<Cookie> cookie = getCookie(cookies, COOKIE_NAME);
            if (!cookie.isPresent()) {
                logger.debug("Cookie with name " + COOKIE_NAME + " not found");
                return Optional.empty();
            }
            var jwt = this.jwtService.parse(cookie.get().getValue());

            var oauthToken = OAuth2AuthenticationHelper.oauth2TokenFromJwt(jwt, this.getAuthenticationManager());

            return oauthToken;
        } catch (Exception e) {
            logger.error("Error processing Cookie", e);
        }
        return Optional.empty();
    }


    Optional<Cookie> getCookie(Cookie[] cookies, String name) {
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

}

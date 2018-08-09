package com.delightreading.authsupport;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        OAuth2AuthenticationToken authentication = obtainAuthentication(req );;
        //UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }


    private OAuth2AuthenticationToken obtainAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token == null) {
            return null;
        }
        try {
            // parse the token.
            SignedJWT signedJWT = SignedJWT.parse(token.replace(TOKEN_PREFIX, ""));
            JWSVerifier verifier = new MACVerifier("MySecret that is at least 32 length long");
            boolean good = signedJWT.verify(verifier);
            // assertTrue(signedJWT.verify(verifier));

            String user = signedJWT.getJWTClaimsSet().getSubject();

            if (user != null) {
                Set<GrantedAuthority> authorities = new HashSet<>();
                //Map<String, Object> authorityAttribs = new HashMap<>();
                //authorityAttribs.put("ROLE_USER", );
                authorities.add(new OAuth2UserAuthority(signedJWT.getJWTClaimsSet().getClaims()));
                OAuth2User oAuth2User = new DefaultOAuth2User(authorities, signedJWT.getJWTClaimsSet().getClaims(), "sub");
                return new OAuth2AuthenticationToken(oAuth2User, new ArrayList<>(), "local");
            }
        } catch (Exception e) {
            // log
            e.printStackTrace();
        }
        return null;
    }


    private Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token == null) {
            return null;
        }
        try {
            // parse the token.
            SignedJWT signedJWT = SignedJWT.parse(token.replace(TOKEN_PREFIX, ""));
            JWSVerifier verifier = new MACVerifier("MySecret that is at least 32 length long");
            // assertTrue(signedJWT.verify(verifier));

//            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
//                    .build()
//                    .verify(token.replace(TOKEN_PREFIX, ""))
//                    .getSubject();
            String user = signedJWT.getJWTClaimsSet().getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
        } catch (Exception e) {
            // log
            e.printStackTrace();
        }
        return null;
    }


}

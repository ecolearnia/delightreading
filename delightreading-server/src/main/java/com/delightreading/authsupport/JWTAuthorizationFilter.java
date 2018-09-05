package com.delightreading.authsupport;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    JwtService jwtService;

    public JWTAuthorizationFilter(AuthenticationManager authManager, JwtService jwtService) {
        super(authManager);
        this.jwtService = jwtService;
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

        Optional<OAuth2AuthenticationToken> optAuth = obtainAuthentication(req);
        optAuth.ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));

        chain.doFilter(req, res);
    }


    private Optional<OAuth2AuthenticationToken> obtainAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token == null) {
            return Optional.empty();
        }
        try {
            var jwt = this.jwtService.parse(token.replace(TOKEN_PREFIX, ""));

            return OAuth2AuthenticationHelper.oauth2TokenFromJwt(jwt, this.getAuthenticationManager());
        } catch (Exception e) {
            logger.error("Error processing Authorization header", e);
            e.printStackTrace();
        }
        return Optional.empty();
    }

/*
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
    */


}

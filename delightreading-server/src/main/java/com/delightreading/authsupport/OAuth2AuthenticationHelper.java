package com.delightreading.authsupport;

import com.nimbusds.jwt.JWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.text.ParseException;
import java.util.*;

public class OAuth2AuthenticationHelper {

    public static Optional<OAuth2AuthenticationToken> oauth2TokenFromJwt(JWT jwt, AuthenticationManager authentcationManager) throws ParseException {
        String user = jwt.getJWTClaimsSet().getSubject();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, "pwd");
        var authenticatedToken = authentcationManager.authenticate(authToken);

        if (user == null) {
            return Optional.empty();
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        //Map<String, Object> authorityAttribs = new HashMap<>();
        //authorityAttribs.put("ROLE_USER", );
        authorities.add(new OAuth2UserAuthority(jwt.getJWTClaimsSet().getClaims()));
        Map<String, Object> userAttribs = new HashMap<>() {{
            putAll(jwt.getJWTClaimsSet().getClaims());
        }};
        OAuth2User oAuth2User = new DefaultOAuth2User(authorities, userAttribs, "sub");
        var oauthToken = new OAuth2AuthenticationToken(oAuth2User, new ArrayList<>(), "local");

        oauthToken.setDetails(authenticatedToken.getPrincipal());
        return Optional.of(oauthToken);
    }
}

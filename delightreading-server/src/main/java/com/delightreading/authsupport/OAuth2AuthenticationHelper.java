package com.delightreading.authsupport;

import com.nimbusds.jwt.JWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.text.ParseException;
import java.util.*;

public class OAuth2AuthenticationHelper {

    /**
     * This obtains authenticationToken object given JWT.
     * Since JWT was created from trusted authentication providers such as Google, there
     * is no need to validate with password.
     * This method is used in {Cookie|JWT} AuthorizationFilters.
     *
     * @param jwt
     * @param authentcationManager
     * @return
     * @throws ParseException
     */
    public static Optional<OAuth2AuthenticationToken> oauth2TokenFromJwt(JWT jwt, AuthenticationManager authentcationManager) throws ParseException {
        String user = jwt.getJWTClaimsSet().getSubject();

        // TODO: Check JWT expiration
        return oauth2TokenFromUserPassword(user, "pwd", authentcationManager);
    }

    public static Optional<OAuth2AuthenticationToken> oauth2TokenFromUserPassword(String user, String password, AuthenticationManager authentcationManager) throws ParseException {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, password);
        var authenticatedToken = authentcationManager.authenticate(authToken);

        if (user == null) {
            return Optional.empty();
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        //authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // TODO: Set the role
        authorities.add(new OAuth2UserAuthority("ROLE_USER", new HashMap<>()));
        // No need to put attributes as oauthToken's detail is the UserAccount object.
        Map<String, Object> userAttribs = new HashMap<>();
        OAuth2User oAuth2User = new DefaultOAuth2User(authorities, userAttribs, "sub"); // sub --> subject
        var oauthToken = new OAuth2AuthenticationToken(oAuth2User, new ArrayList<>(), "local");

        // Set the UserAccount to token details
        oauthToken.setDetails(authenticatedToken.getPrincipal());
        return Optional.of(oauthToken);

    }
}
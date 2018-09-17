package com.delightreading.authsupport;

import com.delightreading.rest.UnauthorizedException;
import com.delightreading.user.model.UserAuthenticationEntity;
import com.delightreading.user.model.UserProfileEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Optional;

public class AuthenticationUtils {

    public static final Optional<OAuth2AuthenticationToken> getOAuth2AuthenticationToken() {
        Authentication authentic = SecurityContextHolder.getContext().getAuthentication();

        if (authentic != null && authentic instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentic;
            return Optional.of(oauthToken);
        }
        return Optional.empty();
    }

    public static final UserAuthenticationEntity getUserAuthenticationOrError() {
        Authentication authentic = SecurityContextHolder.getContext().getAuthentication();

        if (authentic != null && authentic instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentic;

            if (oauthToken.getDetails() instanceof UserAuthenticationEntity) {
                return (UserAuthenticationEntity) oauthToken.getDetails();
            }
        }
        throw new UnauthorizedException(UserProfileEntity.class.getSimpleName(), "");
    }
}

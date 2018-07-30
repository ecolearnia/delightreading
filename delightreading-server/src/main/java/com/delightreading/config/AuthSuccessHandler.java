package com.delightreading.config;

import com.delightreading.authsupport.GoogleProfileAdapter;
import com.delightreading.authsupport.LinkedInProfileAdapter;
import com.delightreading.authsupport.ProfileProviderAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

/**
 * This component handles the success authentication from OAuth provider.
 * It retrieves the full profile, stores it if hasn't been stored yet.
 * Then redirects to the default page based on the role.
 * <p>
 * The component is registered to the oauth2Login().successHandler(handler)
 */
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) auth;

        OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientService.loadAuthorizedClient(
                        oauthToken.getAuthorizedClientRegistrationId(),
                        oauthToken.getName());
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        if ("google".equals(authorizedClient.getClientRegistration().getRegistrationId())) {
            ProfileProviderAdapter ppa =  new GoogleProfileAdapter();
            ppa.fetchProfile(accessToken, null);

        } else if ("linkedIn".equals(authorizedClient.getClientRegistration().getRegistrationId())) {
            ProfileProviderAdapter ppa =  new LinkedInProfileAdapter();
            ppa.fetchProfile(accessToken, null);
        }
        // save the profile

        // Redirect to home
        res.sendRedirect("/");
    }
}

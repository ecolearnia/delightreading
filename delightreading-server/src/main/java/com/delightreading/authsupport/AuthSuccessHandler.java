package com.delightreading.authsupport;

import com.delightreading.user.UserAuthenticationEntity;
import com.delightreading.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.delightreading.authsupport.JWTAuthorizationFilter.HEADER_STRING;
import static com.delightreading.authsupport.JWTAuthorizationFilter.TOKEN_PREFIX;

/**
 * This component handles the success authentication from OAuth provider.
 * It retrieves the full profile, stores it if hasn't been stored yet.
 * Then redirects to the default page based on the role.
 * <p>
 * The component is registered to the oauth2Login().successHandler(handler)
 */
@Component
@Slf4j
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    ObjectMapper objectMapper;

    OAuth2AuthorizedClientService authorizedClientService;

    JwtService jwtService;

    UserService userService;

    Map<String, ProfileProviderAdapter> profileAdapters = new HashMap<>();

    public AuthSuccessHandler(ObjectMapper objectMapper,
                              OAuth2AuthorizedClientService authorizedClientService,
                              JwtService jwtService,
                              UserService userService) {
        this.objectMapper = objectMapper;
        this.authorizedClientService = authorizedClientService;
        this.jwtService = jwtService;
        this.userService = userService;

        this.initialize();
    }

    public void initialize() {
        profileAdapters.put("google", new GoogleProfileAdapter(objectMapper));
        profileAdapters.put("linkedIn", new LinkedInProfileAdapter(objectMapper));
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) auth;

        OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientService.loadAuthorizedClient(
                        oauthToken.getAuthorizedClientRegistrationId(),
                        oauthToken.getName());

        UserAuthenticationEntity authentication = this.findOrCreateNew(authorizedClient);

        String token = jwtService.createToken(authentication);

        resp.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        Cookie cookie = new Cookie(CookieAuthorizationFilter.COOKIE_NAME, token);
        cookie.setMaxAge( 24 * 60 * 60);// for 24 hrs
        cookie.setPath("/"); // this is the magic
        resp.addCookie(cookie);

        // Redirect to home
        resp.sendRedirect("/");
    }

    UserAuthenticationEntity findOrCreateNew(OAuth2AuthorizedClient authorizedClient) {
        var authentication = UserAuthenticationEntity.builder()
                .provider(authorizedClient.getClientRegistration().getRegistrationId())
                .providerAccountId(authorizedClient.getPrincipalName()).build();
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        // If authentication not found, create one
        Optional<UserAuthenticationEntity> existing = this.userService.findByProviderAndProviderAccountId(authentication.getProvider(), authentication.getProviderAccountId());

        if (existing.isPresent()) {
            authentication = existing.get();
            log.info("Authentication found from DB, authentication={}", authentication.toString());
        } else {
            log.info("New user logged [{}] with id={}", authentication.getProvider(), authentication.getProviderAccountId());
            ProfileProviderAdapter ppa = this.profileAdapters.get(authorizedClient.getClientRegistration().getRegistrationId());
            if (ppa == null) {
                throw new IllegalStateException("Provider " + authorizedClient.getClientRegistration().getRegistrationId() + " not found");
            }
            var profile = ppa.fetchProfile(authentication, accessToken);
            // save account, authentication and profile

            // At this point, authentication already includes account and profile
            authentication = this.userService.registerNew(authentication, profile);
        }
        return authentication;
    }


}

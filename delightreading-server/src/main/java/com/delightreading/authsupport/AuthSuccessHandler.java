package com.delightreading.authsupport;

import com.delightreading.authsupport.GoogleProfileAdapter;
import com.delightreading.authsupport.LinkedInProfileAdapter;
import com.delightreading.authsupport.ProfileProviderAdapter;
import com.delightreading.user.UserAuthenticationEntity;
import com.delightreading.user.UserAuthenticationRepository;
import com.delightreading.user.UserProfileEntity;
import com.delightreading.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
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
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    ObjectMapper objectMapper;

    OAuth2AuthorizedClientService authorizedClientService;

    UserService userService;

    Map<String, ProfileProviderAdapter> profileAdapters = new HashMap<>();

    public AuthSuccessHandler(ObjectMapper objectMapper,
                              OAuth2AuthorizedClientService authorizedClientService,
                              UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;

        this.initialize();
    }

    public void initialize() {
        profileAdapters.put("google", new GoogleProfileAdapter(objectMapper));
        profileAdapters.put("linkedIn", new LinkedInProfileAdapter(objectMapper));
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) auth;

        OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientService.loadAuthorizedClient(
                        oauthToken.getAuthorizedClientRegistrationId(),
                        oauthToken.getName());
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        // Create UserAuthentication
        var authentication = UserAuthenticationEntity.builder()
                .provider(authorizedClient.getClientRegistration().getRegistrationId())
                .providerAccountId(authorizedClient.getPrincipalName()).build();

        // If authentication not found, create one
        Optional<UserAuthenticationEntity> existing = this.userService.findByProviderAndProviderAccountId(authentication.getProvider(), authentication.getProviderAccountId());

        if (existing.isPresent()) {
            authentication = existing.get();
        } else {
            ProfileProviderAdapter ppa = this.profileAdapters.get(authorizedClient.getClientRegistration().getRegistrationId());
            if (ppa != null) {
                var profile = ppa.fetchProfile(authentication, accessToken);
            } else {
                throw new IllegalStateException("Provider " + authorizedClient.getClientRegistration().getRegistrationId() + " not found");
            }
            // save account, authentication and profile

        }

        try {
            JWSSigner signer = new MACSigner("MySecret that is at least 32 length long");

            // Prepare JWT with claims set
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(authentication.getAccount().getUsername())
                    .issuer("https://delightreading.com")
                    .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            String token;
            // Apply the HMAC protection
            signedJWT.sign(signer);

            // Serialize to compact form, produces something like
            // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
            token = signedJWT.serialize();
            res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        } catch (Exception e) {
            // log
        }

        // Redirect to home
        res.sendRedirect("/");
    }
}

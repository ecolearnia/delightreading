package com.delightreading.authsupport;

import com.delightreading.user.UserAuthenticationEntity;
import com.delightreading.user.UserProfileEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;

public class LinkedInProfileAdapter implements ProfileProviderAdapter {

    ObjectMapper objectMapper;

    public LinkedInProfileAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public UserProfileEntity fetchProfile(UserAuthenticationEntity authentication, String accessToken) {
        // Fetch profile from Google API. (How do we know it is Google?)
        // https://www.googleapis.com/plus/v1/people/{userId}?access_token={accessToken}
        ParameterizedTypeReference<HashMap<String, Object>> responseType =
                new ParameterizedTypeReference<>() {
                };

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        RequestEntity<HashMap<String, Object>> requestEntity = new RequestEntity<>(
                headers,
                HttpMethod.GET,
                URI.create("https://api.linkedin.com/v2/me")
        );

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HashMap<String, Object>> result = restTemplate.exchange(requestEntity, responseType);

        return null;
    }
}

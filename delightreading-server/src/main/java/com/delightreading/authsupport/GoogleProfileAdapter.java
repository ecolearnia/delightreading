package com.delightreading.authsupport;

import com.delightreading.user.UserProfile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;

public class GoogleProfileAdapter implements ProfileProviderAdapter {

    @Override
    public UserProfile fetchProfile(String accessToken, String subjectId) {
        // Fetch profile from Google API. (How do we know it is Google?)
        // https://www.googleapis.com/plus/v1/people/{userId}?access_token={accessToken}
        ParameterizedTypeReference<HashMap<String, Object>> responseType =
                new ParameterizedTypeReference<>() {
                };

        RequestEntity<HashMap<String, Object>> requestEntity = new RequestEntity<>(
                HttpMethod.GET,
                URI.create("https://www.googleapis.com/plus/v1/people/me?access_token=" + accessToken)
        );

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HashMap<String, Object>> result = restTemplate.exchange(requestEntity, responseType);

        return null;
    }
}

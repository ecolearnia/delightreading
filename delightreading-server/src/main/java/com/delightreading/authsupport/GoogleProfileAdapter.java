package com.delightreading.authsupport;

import com.delightreading.common.ObjectAccessor;
import com.delightreading.user.Experience;
import com.delightreading.user.UserAccountEntity;
import com.delightreading.user.UserAuthenticationEntity;
import com.delightreading.user.UserProfileEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleProfileAdapter implements ProfileProviderAdapter {

    ObjectMapper objectMapper;

    public GoogleProfileAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public UserProfileEntity fetchProfile(UserAuthenticationEntity authentication, String accessToken) {
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
        // ResponseEntity<HashMap<String, Object>> result = restTemplate.exchange(requestEntity, responseType);
        ResponseEntity<String> result = restTemplate.exchange(requestEntity, String.class);

        if (!result.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Google returned error status code: " + result.getStatusCodeValue());
        }

        try {
            HashMap<String, Object> resultObj = this.objectMapper.readValue(result.getBody(), new TypeReference<Map<String, Object>>() {
            });
            authentication.setRawProfile(result.getBody());
            UserAccountEntity account = new UserAccountEntity();
            account.setGivenName(ObjectAccessor.access(resultObj, "name.givenName", String.class).orElse(null));
            account.setPictureUri(ObjectAccessor.access(resultObj, "image.url", String.class).orElse(null));

            var newProfile = new UserProfileEntity();
            newProfile.setExpertise(ObjectAccessor.access(resultObj, "skills", String.class).orElse(null));
            newProfile.setGender(ObjectAccessor.access(resultObj, "gender", String.class).orElse(null));
            newProfile.setSynopsis(ObjectAccessor.access(resultObj, "braggingRights", String.class).orElse(null));
            newProfile.setOccupation(ObjectAccessor.access(resultObj, "occupation", String.class).orElse(null));

            List<Experience> experienceList = new ArrayList<>();
            ObjectAccessor.access(resultObj, "organizations", List.class).ifPresent(orgs -> {
                for (Object org : orgs) {
                    Experience exp = new Experience();
                    exp.setKind(ObjectAccessor.access(org, "type", String.class).orElse(""));
                    exp.setTitle(ObjectAccessor.access(org, "title", String.class).orElse(""));
                    exp.setFromDate(ObjectAccessor.access(org, "startDate", String.class).orElse(""));
                    exp.setToDate(ObjectAccessor.access(org, "endDate", String.class).orElse(""));
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException("Failed reading Google profile", e);
        }

        return null;
    }
}

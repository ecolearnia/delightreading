package com.delightreading.authsupport;

import com.delightreading.common.ObjectAccessor;
import com.delightreading.user.Experience;
import com.delightreading.user.UserAccountEntity;
import com.delightreading.user.UserAuthenticationEntity;
import com.delightreading.user.UserProfileEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GoogleProfileAdapter implements ProfileProviderAdapter {

    ObjectMapper objectMapper;

    public GoogleProfileAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public UserProfileEntity fetchProfile(UserAuthenticationEntity authentication, String accessToken) {
        // https://www.googleapis.com/plus/v1/people/{userId}?access_token={accessToken}
        RequestEntity<HashMap<String, Object>> requestEntity = new RequestEntity<>(
                HttpMethod.GET,
                URI.create("https://www.googleapis.com/plus/v1/people/me?access_token=" + accessToken)
        );

        RestTemplate restTemplate = new RestTemplate();
        // ParameterizedTypeReference<HashMap<String, Object>> responseType = new ParameterizedTypeReference<>() {};
        // ResponseEntity<HashMap<String, Object>> result = restTemplate.exchange(requestEntity, responseType);
        ResponseEntity<String> result = restTemplate.exchange(requestEntity, String.class);

        if (!result.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Google returned error status code: " + result.getStatusCodeValue());
        }

        UserProfileEntity profile = parseProfile(authentication, result.getBody());

        return profile;
    }

    public UserProfileEntity parseProfile(UserAuthenticationEntity authentication, String rawProfile) {
        try {
            HashMap<String, Object> resultObj = this.objectMapper.readValue(rawProfile, new TypeReference<Map<String, Object>>() {
            });
            authentication.setRawProfile(rawProfile);
            UserAccountEntity account = new UserAccountEntity();
            List<Map<String, String>> emails = ObjectAccessor.access(resultObj, "emails", List.class).orElse(null);
            if (emails != null && emails.size() > 0) {
                // If username is null, try to set from email
                if (account.getUsername() == null) {
                    var mainEmail = emails.stream()
                            .filter(entry -> "account".equalsIgnoreCase(entry.get("type")))
                            .findFirst().get();
                    account.setUsername(mainEmail != null ? mainEmail.get("value") : null);
                }

                List<String> emailList = emails.stream().map(entry -> entry.get("value")).collect(Collectors.toList());
                account.setEmails(emailList);
            }

            account.setGivenName(ObjectAccessor.access(resultObj, "name.givenName", String.class).orElse(null));
            account.setPictureUri(ObjectAccessor.access(resultObj, "image.url", String.class).orElse(null));
            authentication.setAccount(account);

            var newProfile = new UserProfileEntity();
            newProfile.setAccount(account);
            newProfile.setExpertise(ObjectAccessor.access(resultObj, "skills", String.class).orElse(null));
            newProfile.setGender(ObjectAccessor.access(resultObj, "gender", String.class).orElse(null));
            newProfile.setSynopsis(ObjectAccessor.access(resultObj, "braggingRights", String.class).orElse(null));
            newProfile.setOccupation(ObjectAccessor.access(resultObj, "occupation", String.class).orElse(null));

            List<Experience> experienceList = new ArrayList<>();
            ObjectAccessor.access(resultObj, "organizations", List.class).ifPresent(orgs -> {
                for (Object org : orgs) {
                    Experience exp = new Experience();
                    exp.setTitle(ObjectAccessor.access(org, "title", String.class).orElse(""));
                    exp.setFromDate(ObjectAccessor.access(org, "startDate", String.class).orElse(""));
                    exp.setToDate(ObjectAccessor.access(org, "endDate", String.class).orElse(""));

                    String type = ObjectAccessor.access(org, "type", String.class).orElse("");
                    if ("school".equalsIgnoreCase(type)) {
                        newProfile.addEducation(exp);
                    } else if ("work".equalsIgnoreCase(type)) {
                        newProfile.addWork(exp);
                    } else {
                        newProfile.addExperience(exp);
                    }
                }
            });
            account.setProfile(newProfile);
            return newProfile;
        } catch (Exception e) {
            throw new IllegalStateException("Failed reading Google profile", e);
        }
    }
}

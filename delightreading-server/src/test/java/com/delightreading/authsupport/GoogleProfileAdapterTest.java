package com.delightreading.authsupport;

import com.delightreading.TestHelper;
import com.delightreading.user.model.UserAuthenticationEntity;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class GoogleProfileAdapterTest {

    @Test
    public void parseProfile_whenValidInput_returnProfile() throws Exception {

        String googleRawProfile = new String(Files.readAllBytes(Paths.get(getClass().getResource("/testdata/oauth_profile1-google.json").toURI())));

        GoogleProfileAdapter profileAdapter = new GoogleProfileAdapter(TestHelper.getObjectMapper());

        UserAuthenticationEntity authentication = new UserAuthenticationEntity();
        var profile = profileAdapter.parseProfile(authentication, googleRawProfile);

        assertThat(profile).isNotNull();
        assertThat(authentication.getAccount()).isNotNull();
        assertThat(authentication.getAccount().getEmails()).containsExactlyInAnyOrder("test@testmail.com");
        assertThat(authentication.getAccount().getUsername()).isEqualTo("test@testmail.com");
        assertThat(authentication.getAccount().getGivenName()).isEqualTo("Reading");
    }
}

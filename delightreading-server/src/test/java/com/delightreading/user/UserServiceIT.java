package com.delightreading.user;

import com.delightreading.SpringApplicationContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {UserAuthenticationRepository.class, UserProfileRepository.class,
        UserService.class, SpringApplicationContextUtil.class})
@EnableAutoConfiguration
public class UserServiceIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserService userService;

    @Test
    public void register_succeeds() {
        // Given
        var account1 = UserAccountEntityRepositoryIT.buildEntity(null, "TEST-Username1", "TEST-givenName1", Arrays.asList("email1a@test.com", "email1b@test.com"));
        var profile1 = UserProfileRepositoryIT.buildEntity(Arrays.asList(Experience.builder().kind("exp-kind1").title("exp-title1").build()), Arrays.asList("math", "science"), "male", account1);
        //account1.setProfile(profile1);
        var auth1 = UserAuthenticationRepositoryIT.buildEntity(null, "TestProvider", "TestProvActID", "TestPwd", account1);

        // When
        var auth = userService.registerNew(auth1, profile1);

        // Then
        // var foundAuth = entityManager.find(UserAuthenticationEntity.class, auth.getSid());
        var foundAuth = userService.findByProviderAndProviderAccountId("TestProvider", "TestProvActID").get();
        var foundProfile = userService.findProfile(foundAuth.getAccount().getUid()).get();

        assertThat(foundAuth).isNotNull();
        assertThat(foundAuth.getAccount()).isNotNull();
        assertThat(foundAuth.getAccount().getUsername()).isEqualTo("TEST-Username1");
        assertThat(foundProfile).isNotNull();
        assertThat(foundProfile.getAccount()).isNotNull();
        assertThat(foundProfile.getAccount().getUsername()).isEqualTo("TEST-Username1");
        assertThat(foundProfile.getInterests()).containsExactlyInAnyOrder("math", "science");
    }


}

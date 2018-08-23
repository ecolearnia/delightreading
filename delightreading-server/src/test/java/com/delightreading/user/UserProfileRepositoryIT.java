package com.delightreading.user;

import com.delightreading.SpringApplicationContextUtil;
import org.junit.Before;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {SpringApplicationContextUtil.class})
@EnableAutoConfiguration
public class UserProfileRepositoryIT {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setup() {
        var account1 = UserAccountEntityRepositoryIT.buildEntity("TEST-UserUID1", "TEST-Username1", "TEST-givenName1", Arrays.asList("email1a@test.com", "email1b@test.com"));
        entityManager.persistAndFlush(account1);
        var profile1 = UserProfileRepositoryIT.buildEntity(Arrays.asList(Experience.builder().kind("exp-kind1").title("exp-title1").build()), Arrays.asList("math", "science"), "male", account1);
        entityManager.persistAndFlush(profile1);

        var account2 = UserAccountEntityRepositoryIT.buildEntity(null, "TEST-Username2", "TEST-givenName2", Arrays.asList("email2a@test.com", "email2b@test.com"));
        entityManager.persistAndFlush(account2);
        var profile2 = UserProfileRepositoryIT.buildEntity(Arrays.asList(Experience.builder().kind("exp-kind2").title("exp-title2").build()), Arrays.asList("literature", "science"), "female", account2);
        entityManager.persistAndFlush(profile2);
    }

    @Test
    public void crud_simple() {

        //var match = userProfileRepository.findByGender("male").get(0);
        var match = userProfileRepository.findByAccountUsername("TEST-Username1").get();

        assertThat (match).isNotNull();
        assertThat(match.getGender()).isEqualTo("male");
        assertThat(match.getAccount()).isNotNull();
        assertThat(match.getAccount().getUsername()).isEqualTo("TEST-Username1");
        assertThat(match.getInterests()).containsExactlyInAnyOrder("math", "science");
        assertThat(match.getEducation()).hasSize(1);
        assertThat(match.getEducation().get(0).getKind()).isEqualTo("exp-kind1");
    }

    public static UserProfileEntity buildEntity(List<Experience> education, List<String> interests, String gender, UserAccountEntity account) {
        UserProfileEntity authEntity = UserProfileEntity.builder()
                .education(education)
                .interests(interests)
                .gender(gender)
                .account(account)
                .build();

        return authEntity;
    }
}

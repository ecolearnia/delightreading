package com.delightreading.user;

import com.delightreading.user.model.UserAccountEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration
public class UserAccountEntityRepositoryIT {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setup() {
        var userAccount1 = UserAccountEntityRepositoryIT.buildEntity("TEST-UID1", "TEST-Username1", "TEST-givenName1", Arrays.asList("email1a@test.com", "email1b@test.com"));
        entityManager.persistAndFlush(userAccount1);

        var userAccount2 = UserAccountEntityRepositoryIT.buildEntity("TEST-UID2", "TEST-Username2", "TEST-givenName2", Arrays.asList("email2a@test.com", "email2b@test.com"));
        entityManager.persistAndFlush(userAccount2);

    }

    @Test
    public void crud_simple() {

        var newUserAccount = UserAccountEntityRepositoryIT.buildEntity("TEST-UID", "TEST-Username", "TEST-givenName", Arrays.asList("emaila@test.com", "emailb@test.com"));
        var userAccount = this.userAccountRepository.save(newUserAccount);

        UserAccountEntity match1 = userAccountRepository.findByUid(userAccount.getUid()).get();

        UserAccountEntity match2 = userAccountRepository.findByUsername("TEST-Username1").get();

        assertThat (match1).isNotNull();
        assertThat(match1.getEmails()).containsExactlyInAnyOrder("emaila@test.com", "emailb@test.com");

        assertThat (match2).isNotNull();
        assertThat(match2.getEmails()).containsExactlyInAnyOrder("email1a@test.com", "email1b@test.com");
    }

    public static UserAccountEntity buildEntity(String uid, String username, String givenName, List<String> emails) {
        UserAccountEntity userAccountEntity = UserAccountEntity.builder()
                .username(username)
                .givenName(givenName)
                .emails(emails)
                .build();
        userAccountEntity.setUid(uid);

        return userAccountEntity;
    }
}

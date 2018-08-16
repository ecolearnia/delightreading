package com.delightreading.user;

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
        var userAccount1 = this.buildEntity(1L, "TEST-UID1", "TEST-Username1", "TEST-givenName1", Arrays.asList("email1a@test.com", "email1b@test.com"));
        entityManager.persistAndFlush(userAccount1);

        var userAccount2 = this.buildEntity(2L, "TEST-UID2", "TEST-Username2", "TEST-givenName2", Arrays.asList("email2a@test.com", "email2b@test.com"));
        entityManager.persistAndFlush(userAccount2);

    }

    @Test
    public void crud_simple() {

        UserAccountEntity match = userAccountRepository.findByUsername("TEST-Username1").get();

        assertThat (match).isNotNull();
        assertThat(match.getEmails()).containsExactlyInAnyOrder("email1a@test.com", "email1b@test.com");
    }

    UserAccountEntity buildEntity(Long sid, String uid, String username, String givenName, List<String> emails) {
        UserAccountEntity userAccountEntity = UserAccountEntity.builder()
                .username(username)
                .givenName(givenName)
                .emails(emails)
                .build();
        userAccountEntity.setUid(uid);

        return userAccountEntity;
    }
}

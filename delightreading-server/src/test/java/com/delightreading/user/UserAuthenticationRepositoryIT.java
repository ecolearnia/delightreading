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
public class UserAuthenticationRepositoryIT {

    @Autowired
    UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setup() {
        var account = UserAccountEntityRepositoryIT.buildEntity("TEST-UserUID1", "TEST-Username1", "TEST-givenName1", Arrays.asList("email1a@test.com", "email1b@test.com"));
        var auth1 = this.buildEntity("TEST-UID1", "TEST-Foogle", "TEST-F1", "pwd1", account);
        entityManager.persistAndFlush(auth1);

        var auth2 = this.buildEntity("TEST-UID2", "TEST-Foogle", "TEST-F2", "pwd2", account);
        entityManager.persistAndFlush(auth2);

    }

    @Test
    public void crud_simple() {

        UserAuthenticationEntity match = userAuthenticationRepository.findByProviderAndProviderAccountId("TEST-Foogle", "TEST-F1").get();

        assertThat (match).isNotNull();
        assertThat(match.getPassword()).isEqualTo("pwd1");
        assertThat(match.getAccount()).isNotNull();
        assertThat(match.getAccount().getUsername()).isEqualTo("TEST-Username1");
    }

    public static UserAuthenticationEntity buildEntity(String uid, String provider, String providerAccountId, String password, UserAccountEntity account) {
        UserAuthenticationEntity authEntity = UserAuthenticationEntity.builder()
                .provider(provider)
                .providerAccountId(providerAccountId)
                .password(password)
                .account(account)
                .build();
        authEntity.setUid(uid);

        return authEntity;
    }
}

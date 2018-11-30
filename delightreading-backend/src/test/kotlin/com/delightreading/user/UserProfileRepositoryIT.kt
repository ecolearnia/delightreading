package com.delightreading.user

import com.delightreading.SpringApplicationContextUtil
import com.delightreading.user.model.Experience
import com.delightreading.user.model.UserAccountEntity
import com.delightreading.user.model.UserProfileEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@Transactional
@SpringBootTest(
    classes = arrayOf(
        TestEntityManager::class,
        SpringApplicationContextUtil::class
    )
)
@EnableAutoConfiguration
class UserProfileRepositoryIT(
    @Autowired val entityManager: TestEntityManager,
    @Autowired val userProfileRepository: UserProfileRepository

) {

    @BeforeEach
    fun setup() {
        val account1 = UserAccountEntity(
            uid = "TEST-UserUID1",
            username = "TEST-Username1",
            givenName = "TEST-givenName1",
            emails = Arrays.asList("email1a@test.com", "email1b@test.com")
        )
        entityManager.persistAndFlush(account1)
        val profile1 = UserProfileEntity(
            education =  Arrays.asList(
                Experience(kind = "exp-kind1", title = "exp-title1")
            ),
            interests = Arrays.asList("math", "science"),
            gender = "male",
            account = account1
        )
        entityManager.persistAndFlush<UserProfileEntity>(profile1)

        val account2 = UserAccountEntity(
            uid = null,
            username = "TEST-Username2",
            givenName = "TEST-givenName2",
            emails = Arrays.asList("email2a@test.com", "email2b@test.com")
        )
        entityManager.persistAndFlush(account2)
        val profile2 = UserProfileEntity(
            education =  Arrays.asList(Experience(kind = "exp-kind2", title = "exp-title2")),
            interests = Arrays.asList("literature", "science"),
            gender = "female",
            account = account2
        )
        entityManager.persistAndFlush<UserProfileEntity>(profile2)
    }

    @Test
    fun crud_simple() {

        //var match = userProfileRepository.findByGender("male").get(0);
        val match = userProfileRepository.findByAccountUsername("TEST-Username1")

        assertThat(match).isNotNull()
        assertThat(match!!.gender).isEqualTo("male")
        assertThat(match!!.account).isNotNull()
        assertThat(match!!.account!!.username).isEqualTo("TEST-Username1")
        assertThat(match!!.interests).containsExactlyInAnyOrder("math", "science")
        assertThat(match!!.education).hasSize(1)
        assertThat(match!!.education!![0].kind).isEqualTo("exp-kind1")
    }

}
package com.delightreading.user

import com.delightreading.SpringApplicationContextUtil
import com.delightreading.user.model.Experience
import com.delightreading.user.model.UserAccountEntity
import com.delightreading.user.model.UserAuthenticationEntity
import com.delightreading.user.model.UserProfileEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@AutoConfigureJsonTesters
@Transactional
@SpringBootTest(
    classes = arrayOf(
        BCryptPasswordEncoder::class,
        PasswordEncoder::class,
//        UserAuthenticationRepository::class,
//        UserProfileRepository::class,
        UserService::class,
        SpringApplicationContextUtil::class
    )
)
@EnableAutoConfiguration

//@ContextConfiguration(
//    classes = arrayOf(
//        BCryptPasswordEncoder::class,
//        PasswordEncoder::class,
//        UserAuthenticationRepository::class,
//        UserProfileRepository::class,
//        UserService::class,
//        SpringApplicationContextUtil::class
//    )
//)
class UserServiceIT(
//    @Autowired val entityManager: TestEntityManager,
    @Autowired val userService: UserService
) {

    @Test
    fun register_when_ValidValues_then_Succeeds() {
        // Given
        val account1 = UserAccountEntity(
            username = "TEST-Username1",
            givenName = "TEST-givenName1",
            emails = Arrays.asList("email1a@test.com", "email1b@test.com")
        )
        val profile1 = UserProfileEntity(
            experiences = Arrays.asList(Experience(kind = "exp-kind1", title = "exp-title1")),
            interests = Arrays.asList("math", "science"),
            gender = "male",
            account = account1
        )
        //account1.setProfile(profile1);
        val auth1 =
            UserAuthenticationEntity(
                provider = "TestProvider",
                providerAccountId = "TestProvActID",
                pwd = "TestPwd",
                account = account1
            )

        // When
        val userProfile = userService!!.registerNew(
            auth1,
            profile1
        )

        // Then
        // var foundAuth = entityManager.find(UserAuthenticationEntity.class, auth.getSid());
        val foundAuth = userService!!.findByProviderAndProviderAccountId("TestProvider", "TestProvActID")
        val foundProfile = userService!!.findProfile(foundAuth!!.account!!.uid!!)

        assertThat(foundAuth).isNotNull()
        assertThat(foundAuth!!.account).isNotNull()
        assertThat(foundAuth!!.account!!.username).isEqualTo("TEST-Username1")
        assertThat(foundProfile).isNotNull()
        assertThat(foundProfile!!.account).isNotNull()
        assertThat(foundProfile!!.account!!.username).isEqualTo("TEST-Username1")
        assertThat(foundProfile!!.interests).containsExactlyInAnyOrder("math", "science")
    }
}
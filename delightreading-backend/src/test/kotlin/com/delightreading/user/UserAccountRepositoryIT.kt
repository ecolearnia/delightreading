package com.delightreading.user

import com.delightreading.user.model.UserAccountEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@DataJpaTest
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@EnableAutoConfiguration
class UserAccountRepositoryIT(
    @Autowired val entityManager: TestEntityManager,
    @Autowired val userAccountRepo: UserAccountRepository
) {
    @BeforeEach
    fun setup() {
        val userAccount1 = UserAccountRepositoryIT.buildEntity(
            "TEST-UID1",
            "TEST-Username1",
            "TEST-givenName1",
            Arrays.asList("email1a@test.com", "email1b@test.com")
        )
        entityManager.persistAndFlush<UserAccountEntity>(userAccount1)

        val userAccount2 = UserAccountEntity(
            uid = "TEST-UID2",
            username = "TEST-Username2",
            givenName = "TEST-givenName2",
            emails = Arrays.asList("email2a@test.com", "email2b@test.com")
        )
        entityManager.persistAndFlush<UserAccountEntity>(userAccount2)

    }

    @Test
    fun `CRUD operations`() {

        val newUserAccount = UserAccountEntity(
            uid =  "TEST-UID",
            username = "TEST-Username",
            givenName = "TEST-givenName",
            emails = Arrays.asList("emaila@test.com", "emailb@test.com")
        )
        val savedAccount = this.userAccountRepo.save(newUserAccount)

        val matchByUid = userAccountRepo.findByUid(savedAccount.uid!!)

        val matchByUsername = userAccountRepo.findByUsername("TEST-Username1")

        assertThat(matchByUid).isNotNull()
        assertThat(matchByUid!!.emails).containsExactlyInAnyOrder("emaila@test.com", "emailb@test.com")

        assertThat(matchByUsername).isNotNull()
        assertThat(matchByUsername!!.emails).containsExactlyInAnyOrder("email1a@test.com", "email1b@test.com")
    }

    companion object {
        fun buildEntity(
            uid: String,
            username: String,
            givenName: String,
            emails: List<String>
        ): UserAccountEntity =
            UserAccountEntity(
                uid = uid,
                username = username,
                givenName = givenName,
                emails = emails
            )
    }


}
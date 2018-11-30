package com.delightreading.user

import com.delightreading.common.UnauthorizedException
import com.delightreading.user.model.UserAccountEntity
import com.delightreading.user.model.UserAuthenticationEntity
import com.delightreading.user.model.UserAuthenticationEntity.Companion.LOCAL_PROVIDER
import com.delightreading.user.model.UserProfileEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import javax.transaction.Transactional

@Service
class UserService(
    @Autowired var userAccountRepository: UserAccountRepository,
    @Autowired var userAuthenticationRepository: UserAuthenticationRepository,
    @Autowired var userProfileRepository: UserProfileRepository,
    @Autowired var passwordEncoder: PasswordEncoder
) {
    companion object {
        val USERNAME = "username"
        val PASSWORD = "password"
        val GIVEN_NAME = "givenName"
        val EMAIL = "email"
    }


    fun findAccountByUid(uid: String): UserAccountEntity? {
        return this.userAccountRepository.findByUid(uid)
    }

    fun findAccountByUsername(username: String): UserAccountEntity? {
        return this.userAccountRepository.findByUsername(username)
    }

    fun findAuthenticationByUid(uid: String): UserAuthenticationEntity? {
        return this.userAuthenticationRepository.findByUid(uid)
    }

    fun findByProviderAndProviderAccountId(
        provider: String,
        providerAccountId: String
    ): UserAuthenticationEntity? {
        return this.userAuthenticationRepository.findByProviderAndProviderAccountId(provider, providerAccountId)
    }

    fun findByProviderAndProviderAccountId(
        provider: String,
        providerAccountId: String,
        password: String
    ): UserAuthenticationEntity {

        val optAuth = this.userAuthenticationRepository.findByProviderAndProviderAccountId(provider, providerAccountId)
            ?: throw UnauthorizedException("login", providerAccountId)

        if (!passwordEncoder.matches(password, optAuth.password)) {
            throw UnauthorizedException("login", providerAccountId)
        }

        return optAuth
    }

    fun findProfile(accountUId: String): UserProfileEntity? {
        return this.userProfileRepository.findByAccountUid(accountUId)
    }

    fun save(profile: UserProfileEntity): UserProfileEntity {
        return this.userProfileRepository.save(profile)
    }

    /**
     * Saves Account, Profile and Authentication
     * @param authentication The authentication object to be saved
     * @param profile
     * @return Saved authentication entity
     */
    @Transactional
    fun registerNew(authentication: UserAuthenticationEntity, profile: UserProfileEntity): UserAuthenticationEntity {
//        log.info("Registering new authentication={}, profile={}", authentication.toString(), profile.toString())
        if (authentication.account == null) {
            throw IllegalStateException("NullAccount")
        }
        if (LOCAL_PROVIDER.equals(authentication.provider) && StringUtils.isEmpty(authentication.password)) {
            throw IllegalStateException("Password cannot be empty")
        }
        val authenticationWithEncodedPwd = authentication.copy(pwd = passwordEncoder.encode(authentication.password))
        val act = this.userAccountRepository.save(authenticationWithEncodedPwd.account!!)
        val profileWithAccount = profile.copy(account = act)

        this.userProfileRepository.save(profileWithAccount)
        return this.userAuthenticationRepository.save(authenticationWithEncodedPwd)
    }

    fun buildAuthentication(registInput: Map<String, String>): UserAuthenticationEntity {
        val username = registInput[USERNAME]
        val password = registInput[PASSWORD]
        val givenName = registInput[GIVEN_NAME]
        val email = registInput[EMAIL]

        return buildAuthentication(username, password, email, givenName)
    }

    fun buildAuthentication(
        username: String?,
        password: String?,
        email: String?,
        givenName: String?
    ): UserAuthenticationEntity {
        val givenName = givenName ?: username

        val userAccount = UserAccountEntity(
            username = username,
            givenName = givenName
        )

        if (!StringUtils.isEmpty(email)) {
            userAccount.emails = listOf(email!!)
        }

        val userAuth = UserAuthenticationEntity(
            provider = LOCAL_PROVIDER,
            providerAccountId = username,
            pwd = password,
            account = userAccount
        )

        if (StringUtils.isEmpty(userAuth.account!!.username) || StringUtils.isEmpty(userAuth.getPassword())) {
            throw IllegalArgumentException("Username or password is empty")
        }

        return userAuth
    }
}
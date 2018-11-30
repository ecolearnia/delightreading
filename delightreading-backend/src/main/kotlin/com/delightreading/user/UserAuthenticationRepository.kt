package com.delightreading.user

import com.delightreading.user.model.UserAuthenticationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAuthenticationRepository : JpaRepository<UserAuthenticationEntity, Long> {

    fun findByUid(uid: String): UserAuthenticationEntity?

    fun findByProviderAndProviderAccountId(
        provider: String,
        providerAccountId: String
    ): UserAuthenticationEntity?

}
package com.delightreading.user

import com.delightreading.user.model.UserAccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAccountRepository : JpaRepository<UserAccountEntity, Long> {
    fun findByUid(uid: String): UserAccountEntity?

    fun findByUsername(username: String): UserAccountEntity?
}
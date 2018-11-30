package com.delightreading.user

import com.delightreading.user.model.UserProfileEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserProfileRepository: JpaRepository<UserProfileEntity, Long> {
    fun findByUid(uid: String): UserProfileEntity?

    fun findByGender(gender: String): List<UserProfileEntity>

    fun findByAccountUid(accountUid: String): UserProfileEntity?

    @Query("select p from UserProfileEntity p where p.account.username = :username")
    fun findByAccountUsername(@Param("username") accountUsername: String): UserProfileEntity?
}
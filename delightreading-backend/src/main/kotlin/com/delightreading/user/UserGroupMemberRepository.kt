package com.delightreading.user

import com.delightreading.user.model.UserGroupMemberEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserGroupMemberRepository: JpaRepository<UserGroupMemberEntity, Long> {

    fun findByUid(uid: String): UserGroupMemberEntity?

    fun findByGroupUid(groupUid: String): LinkedHashSet<UserGroupMemberEntity>

    fun findByGroupUid(groupUid: String, pageable: Pageable): Page<UserGroupMemberEntity>

}
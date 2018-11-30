package com.delightreading.user

import com.delightreading.user.model.UserGroupEntity
import com.delightreading.user.model.UserGroupType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserGroupRepository : JpaRepository<UserGroupEntity, Long> {

    fun findByUid(uid: String): UserGroupEntity?

    fun findByTypeIn(type: Array<UserGroupType>): UserGroupEntity?

    fun findByNameLike(nameLike: String, pageable: Pageable): Page<UserGroupEntity>

    fun findByTypeInAndNameLike(
        type: Array<UserGroupType>,
        nameLike: String,
        pageable: Pageable
    ): Page<UserGroupEntity>

    @Query(
        value = "select * from user_group ug join user_group_member ugm " +
                "on ug.uid = group_uid " +
                "where ug.type in (:type) and ugm.account_uid = :memberUid", nativeQuery = true
    )
    fun findByTypeInAndMember(
        @Param("type") type: Array<String>,
        @Param("memberUid") memberUid: String, pageable: Pageable
    ): Page<UserGroupEntity>

    @Query(
        value = "select * from user_group ug join user_group_member ugm " +
                "on ug.uid = group_uid " +
                "where ug.type = :type and ugm.account_uid = :memberUid and ugm.role = :memberRole", nativeQuery = true
    )
    fun findByTypeAndMember(
        @Param("type") type: String,
        @Param("memberUid") memberUid: String,
        @Param("memberRole") memberRole: String
    ): List<UserGroupEntity>
}
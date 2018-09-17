package com.delightreading.user;

import com.delightreading.user.model.UserGroupEntity;
import com.delightreading.user.model.UserGroupType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Long> {

    Optional<UserGroupEntity> findByUid(String uid);

    Page<UserGroupEntity> findByNameLike(String nameLike, Pageable pageable);

    Page<UserGroupEntity> findByTypeInAndNameLike(UserGroupType[] type, String nameLike, Pageable pageable);

    @Query(value="select * from user_group ug join user_group_member ugm " +
            "on ug.uid = group_uid " +
            "where ug.type in (:type) and ugm.account_uid = :memberUid",
            nativeQuery = true)
    Page<UserGroupEntity> findByTypeInAndMember(@Param("type") String[] type,
                                                @Param("memberUid") String memberUid, Pageable pageable);
}

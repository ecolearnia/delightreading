package com.delightreading.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;

@Repository
public interface UserGroupMemberRepository extends JpaRepository<UserGroupMemberEntity, Long> {

    Optional<UserGroupMemberEntity> findByUid(String uid);

    LinkedHashSet<UserGroupMemberEntity> findByGroupUid(String groupUid);

}

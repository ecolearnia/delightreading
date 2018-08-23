package com.delightreading.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Long> {

    Optional<UserGroupEntity> findByUid(String uid);

    Page<UserGroupEntity> findByNameLike(String nameLike, Pageable pageable);

    Page<UserGroupEntity> findByTypeInAndNameLike(UserGroupType[] type, String nameLike, Pageable pageable);
}

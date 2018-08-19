package com.delightreading.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    
    Optional<UserProfileEntity> findByUid(String uid);

    List<UserProfileEntity> findByGender(String gender);

    Optional<UserProfileEntity> findByAccountUid(String accountUid);

    @Query("select p from UserProfileEntity p where p.account.username = :username")
    Optional<UserProfileEntity> findByAccountUsername(@Param("username") String accountUsername);
}

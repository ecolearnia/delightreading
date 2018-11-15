package com.delightreading.user;

import com.delightreading.user.model.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {

    Optional<UserAccountEntity> findByUid(String uid);

    Optional<UserAccountEntity> findByUsername(String username);
}

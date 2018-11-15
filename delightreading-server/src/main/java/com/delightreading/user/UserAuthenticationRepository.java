package com.delightreading.user;

import com.delightreading.user.model.UserAuthenticationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthenticationEntity, Long> {

    Optional<UserAuthenticationEntity> findByUid(String uid);

    Optional<UserAuthenticationEntity> findByProviderAndProviderAccountId(String provider, String providerAccountId);
}

package com.delightreading.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserAuthenticationRepository extends JpaRepository<UserAuthenticationEntity, Long> {

    Optional<UserAuthenticationEntity> findByUid(String uid);

    Optional<UserAuthenticationEntity> findByProviderAndProviderAccountId(String provider, String providerAccountId);
}

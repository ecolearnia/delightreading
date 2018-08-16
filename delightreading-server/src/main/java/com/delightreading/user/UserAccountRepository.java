package com.delightreading.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {
//    public UserAccountEntity findByUsername(String username)
//    {
//        return UserAccountEntity.builder()
//                .emails(Arrays.asList("test@mail.net"))
//                .givenName("Tino")
//                .familyName("Zhero").build();
//    }

    Optional<UserAccountEntity> findByUid(String uid);

    Optional<UserAccountEntity> findByUsername(String username);
}

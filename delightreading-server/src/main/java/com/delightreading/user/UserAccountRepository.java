package com.delightreading.user;

import com.delightreading.user.model.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {
//    public UserAccountEntity findByAccountUsername(String username)
//    {
//        return UserAccountEntity.builder()
//                .emails(Arrays.asList("test@mail.net"))
//                .givenName("Tino")
//                .familyName("Zhero").build();
//    }

    Optional<UserAccountEntity> findByUid(String uid);

    Optional<UserAccountEntity> findByUsername(String username);
}

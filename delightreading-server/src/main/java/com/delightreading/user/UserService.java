package com.delightreading.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    UserAccountRepository userAccountRepository;
    UserAuthenticationRepository userAuthenticationRepository;
    UserProfileRepository userProfileRepository;

    public UserService(
            UserAccountRepository userAccountRepository,
            UserAuthenticationRepository userAuthenticationRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.userAccountRepository = userAccountRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public Optional<UserAccountEntity> findAccountByUsername(String username) {
        return this.userAccountRepository.findByUsername(username);
    }

    public Optional<UserAuthenticationEntity> findByProviderAndProviderAccountId(String provider, String providerAccountId) {
        return this.userAuthenticationRepository.findByProviderAndProviderAccountId(provider, providerAccountId);
    }

    public Optional<UserProfileEntity> findProfile(String accountUId) {
        return this.userProfileRepository.findByAccountUid(accountUId);
    }

    /**
     * Saves Account, Profile and Authentication
     * @param authentication The authentication object to be saved
     * @param profile
     * @return Saved authentication entity
     */
    @Transactional
    public UserAuthenticationEntity registerNew(UserAuthenticationEntity authentication, UserProfileEntity profile) {
        log.info("Registering new authentication={}, profile={}", authentication.toString(), profile.toString());
        var act = this.userAccountRepository.save(authentication.getAccount());
        profile.setAccount(act);
        this.userProfileRepository.save(profile);
        return this.userAuthenticationRepository.save(authentication);
    }


}

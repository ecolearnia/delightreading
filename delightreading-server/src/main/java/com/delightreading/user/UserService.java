package com.delightreading.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    UserAccountRepository userAccountRepository;

    UserAuthenticationRepository userAuthenticationRepository;


    public UserService(
            UserAccountRepository userAccountRepository,
            UserAuthenticationRepository userAuthenticationRepository
    ) {
        this.userAccountRepository = userAccountRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    public Optional<UserAuthenticationEntity> findByProviderAndProviderAccountId(String provider, String providerAccountId) {
        return this.userAuthenticationRepository.findByProviderAndProviderAccountId(provider, providerAccountId);
    }

    public void registerNew(UserAuthenticationEntity authentication) {
        this.userAccountRepository.save(authentication.getAccount());
        this.userAuthenticationRepository.save(authentication);
    }


}

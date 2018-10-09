package com.delightreading.user;

import com.delightreading.user.model.UserAccountEntity;
import com.delightreading.user.model.UserAuthenticationEntity;
import com.delightreading.user.model.UserProfileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String GIVEN_NAME = "givenName";
    public static final String EMAIL = "email";

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

    public Optional<UserAccountEntity> findAccountByUid(String uid) {
        return this.userAccountRepository.findByUid(uid);
    }

    public Optional<UserAccountEntity> findAccountByUsername(String username) {
        return this.userAccountRepository.findByUsername(username);
    }

    public Optional<UserAuthenticationEntity> findAuthenticationByUid(String uid) {
        return this.userAuthenticationRepository.findByUid(uid);
    }

    public Optional<UserAuthenticationEntity> findByProviderAndProviderAccountId(String provider, String providerAccountId) {
        return this.userAuthenticationRepository.findByProviderAndProviderAccountId(provider, providerAccountId);
    }

    public Optional<UserProfileEntity> findProfile(String accountUId) {
        return this.userProfileRepository.findByAccountUid(accountUId);
    }

    public UserProfileEntity save(UserProfileEntity profile) {
        return this.userProfileRepository.save(profile);
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
        if (authentication.getAccount() == null) {
            throw new IllegalStateException("NullAccount");
        }
        var act = this.userAccountRepository.save(authentication.getAccount());
        profile.setAccount(act);
        this.userProfileRepository.save(profile);
        return this.userAuthenticationRepository.save(authentication);
    }

    public UserAuthenticationEntity buildAuthentication(Map<String, String> registInput) {
        String username = registInput.get(USERNAME);
        String password = registInput.get(PASSWORD);
        String givenName = registInput.get(GIVEN_NAME);
        String email = registInput.get(EMAIL);

        return buildAuthentication(username, password, email, givenName);
    }

    public UserAuthenticationEntity buildAuthentication(String username, String password, String email, String givenName) {

        if (givenName == null) {
            givenName = username;
        }
        UserAccountEntity userAccount = UserAccountEntity.builder()
                .username(username)
                .givenName(givenName)
                .build();
        if (!StringUtils.isEmpty(email)) {
            userAccount.setEmails(List.of(email));
        }

        UserAuthenticationEntity userAuth =  UserAuthenticationEntity.builder()
                .provider(UserAuthenticationEntity.LOCAL_PROVIDER)
                .providerAccountId(username)
                .password(password)
                .account(userAccount)
                .build();

        if (StringUtils.isEmpty(userAuth.getAccount().getUsername())
                || StringUtils.isEmpty((userAuth.getPassword()))) {
            throw new IllegalArgumentException("Username or password is empty");
        }

        return userAuth;
    }

}

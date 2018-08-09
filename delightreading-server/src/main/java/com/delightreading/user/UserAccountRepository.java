package com.delightreading.user;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserAccountRepository {
    public UserAccount findByUsername(String username)
    {
        return UserAccount.builder()
                .emails(Arrays.asList("test@mail.net"))
                .givenName("Tino")
                .familyName("Zhero").build();
    }
}

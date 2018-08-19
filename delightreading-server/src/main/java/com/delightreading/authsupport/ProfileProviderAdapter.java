package com.delightreading.authsupport;

import com.delightreading.user.UserAuthenticationEntity;
import com.delightreading.user.UserProfileEntity;

public interface ProfileProviderAdapter {
    UserProfileEntity fetchProfile(UserAuthenticationEntity authentication, String accessToken);
}

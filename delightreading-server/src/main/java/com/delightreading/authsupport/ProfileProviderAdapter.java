package com.delightreading.authsupport;

import com.delightreading.user.model.UserAuthenticationEntity;
import com.delightreading.user.model.UserProfileEntity;

public interface ProfileProviderAdapter {
    UserProfileEntity fetchProfile(UserAuthenticationEntity authentication, String accessToken);
}

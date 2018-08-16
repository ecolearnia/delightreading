package com.delightreading.authsupport;

import com.delightreading.user.UserProfileEntity;

public interface ProfileProviderAdapter {
    public UserProfileEntity fetchProfile(String accessToken, String subjectId);
}

package com.delightreading.authsupport;

import com.delightreading.user.UserProfile;

public interface ProfileProviderAdapter {
    public UserProfile fetchProfile(String accessToken, String subjectId);
}

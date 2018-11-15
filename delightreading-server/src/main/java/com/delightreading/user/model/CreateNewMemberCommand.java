package com.delightreading.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateNewMemberCommand {
    String groupUid;
    String memberRole;
    String username;
    String password;
    String email;
    String givenName;
    String familyName;
    UserProfileEntity profile = new UserProfileEntity();
}

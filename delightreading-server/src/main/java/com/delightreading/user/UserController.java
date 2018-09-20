package com.delightreading.user;

import com.delightreading.authsupport.AuthenticationUtils;
import com.delightreading.authsupport.JwtService;
import com.delightreading.rest.UnauthorizedException;
import com.delightreading.user.model.UserAccountEntity;
import com.delightreading.user.model.UserAuthenticationEntity;
import com.delightreading.user.model.UserProfileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/api/users/v1")
public class UserController {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";


    JwtService jwtService;
    UserService userService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, String> loginInput) {

        if (!loginInput.containsKey(USERNAME) || !loginInput.containsKey(PASSWORD) ) {
            throw new IllegalArgumentException("param username or password not found");
        }
        String username = loginInput.get(USERNAME);
        String password = loginInput.get(PASSWORD);
        log.info("Attempt to login with [{}]", username);

        var optAuth = userService.findByProviderAndProviderAccountId(UserAuthenticationEntity.LOCAL_PROVIDER, username);
        if (!optAuth.isPresent()) {
            // Return 403
            throw new UnauthorizedException("login", "");
        }
        // TODO: encrypt password
        if (!password.equals(optAuth.get().getPassword())) {
            // Return 403
            throw new UnauthorizedException("login", "");
        }
        String token = jwtService.createToken(optAuth.get());

        Map<String, Object> response = new HashMap<>() {{
            put("token", token);
        }};
        return response;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> register(@RequestBody Map<String, String> registInput) {

        if (!registInput.containsKey(USERNAME)
                || !registInput.containsKey(PASSWORD)) {
            throw new IllegalArgumentException("param username or password not found");
        }
        String username = registInput.get(USERNAME);
        String password = registInput.get(PASSWORD);
        String email = registInput.get(EMAIL);

        UserAccountEntity userAccount = UserAccountEntity.builder()
                .username(username)
                .givenName(username)
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

        UserProfileEntity userProfile = UserProfileEntity.builder()
                .account(userAccount)
                .build();

        UserAuthenticationEntity registeredUserAuth =userService.registerNew(userAuth, userProfile);

        String token = jwtService.createToken(registeredUserAuth);

        Map<String, Object> response = new HashMap<>() {{
            put("token", token);
        }};
        return response;
    }


    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserAccountEntity me() {
        Optional<OAuth2AuthenticationToken> oauthTokenOpt = AuthenticationUtils.getOAuth2AuthenticationToken();
        if (!oauthTokenOpt.isPresent()) {
            throw new UnauthorizedException(UserProfileEntity.class.getSimpleName(), "");
        }
        UserAccountEntity userAccount = null;
        if (oauthTokenOpt.get().getDetails() instanceof UserAuthenticationEntity) {
            userAccount = ((UserAuthenticationEntity)oauthTokenOpt.get().getDetails()).getAccount();
        }
        return userAccount;
    }

    @GetMapping(value = "/me/profile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserProfileEntity myProfile() {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        Optional<UserProfileEntity> userProfileOpt = userService.findProfile(userAuth.getAccount().getUid());

        return userProfileOpt.get();
    }

    @PutMapping(value = "/me/profile", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserProfileEntity updateMyProfile(@RequestBody UserProfileEntity userProfile) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        Optional<UserProfileEntity> userProfileOpt = userService.findProfile(userAuth.getAccount().getUid());
        UserProfileEntity profileFound = userProfileOpt.get();
        profileFound.getAccount().setNickname(userProfile.getAccount().getNickname());
        profileFound.getAccount().setFamilyName(userProfile.getAccount().getFamilyName());
        profileFound.getAccount().setGivenName(userProfile.getAccount().getGivenName());
        profileFound.setGender(userProfile.getGender());
        profileFound.setExpertise(userProfile.getExpertise());
        profileFound.setEducation(userProfile.getEducation());
        profileFound.setOccupation(userProfile.getOccupation());
        userService.save(profileFound);

        return profileFound;
    }

}

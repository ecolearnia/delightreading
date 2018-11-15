package com.delightreading.user;

import com.delightreading.authsupport.AuthenticationUtils;
import com.delightreading.authsupport.JwtService;
import com.delightreading.authsupport.OAuth2AuthenticationHelper;
import com.delightreading.rest.UnauthorizedException;
import com.delightreading.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/api/users/v1")
public class UserController {

    public static final String TYPE_PREFIX = "type:";

    JwtService jwtService;
    UserService userService;

    public UserController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, String> loginInput) {

        if (!loginInput.containsKey(UserService.USERNAME) || !loginInput.containsKey(UserService.PASSWORD)) {
            throw new IllegalArgumentException("param username or password not found");
        }
        String username = loginInput.get(UserService.USERNAME);
        String password = loginInput.get(UserService.PASSWORD);
        log.info("login/start, username=[{}]", username);

        var optAuth = userService.findByProviderAndProviderAccountId(UserAuthenticationEntity.LOCAL_PROVIDER, username, password);

        String token = jwtService.createToken(optAuth.get());

        Map<String, Object> response = new HashMap<>() {{
            put("token", token);
        }};
        log.debug("login/complete, token={}", token);
        return response;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> registerUser(@RequestBody Map<String, String> registInput) {

        UserAuthenticationEntity userAuth = userService.buildAuthentication(registInput);
        log.debug("registerUser/start, username={}", userAuth.getAccount().getUsername());

        UserProfileEntity userProfile = UserProfileEntity.builder()
                .account(userAuth.getAccount())
                .build();

        UserAuthenticationEntity registeredUserAuth = userService.registerNew(userAuth, userProfile);

        String token = jwtService.createToken(registeredUserAuth);

        Map<String, Object> response = new HashMap<>() {{
            put("token", token);
        }};
        log.debug("registerUser/complete, token={}", token);
        return response;
    }


    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserAccountEntity me() {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();
        log.debug("me/start, accountUid={}", userAuth.getAccount().getUid());

        return userAuth.getAccount();
    }

    @GetMapping(value = "/me/profile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserProfileEntity myProfile() {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();
        log.debug("myProfile/start, accountUid={}", userAuth.getAccount().getUid());

        Optional<UserProfileEntity> userProfileOpt = userService.findProfile(userAuth.getAccount().getUid());

        return userProfileOpt.get();
    }

    @PutMapping(value = "/me/profile", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserProfileEntity updateMyProfile(@RequestBody UserProfileEntity userProfile) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();
        log.debug("POST/myProfile/start, accountUid={}", userAuth.getAccount().getUid());

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



    //</editor-fold>
}

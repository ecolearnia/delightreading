package com.delightreading.user;

import com.delightreading.authsupport.AuthenticationUtils;
import com.delightreading.authsupport.JwtService;
import com.delightreading.rest.UnauthorizedException;
import com.delightreading.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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
    UserGroupService userGroupService;

    public UserController(JwtService jwtService, UserService userService, UserGroupService userGroupService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userGroupService = userGroupService;
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


    //<editor-fold desc="UserGroups">

    @GetMapping(value = "/groups", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<UserGroupEntity> listGroups(@RequestParam Map<String, String> requestParams, Pageable pageable) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        UserGroupType[] types = {UserGroupType.FAMILY, UserGroupType.ACADEMIC, UserGroupType.CLUB};
        if (requestParams.containsKey("type")) {
            types = new UserGroupType[1];
            types[0] = UserGroupType.valueOf(requestParams.get("type"));
        }
        boolean fetchMembers = requestParams.containsKey("fetchMembers");
        Page<UserGroupEntity> result = this.userGroupService.findGroupsByTypeInAndMember(
                types, userAuth.getAccount().getUid(), fetchMembers, pageable);

        return result;
    }


    @GetMapping(value = "/groups/family", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Optional<UserGroupEntity> myFamilyGroup() {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        var familyGroup = this.userGroupService.findGroupsByTypeAndMemberWithRole(
                UserGroupType.FAMILY, userAuth.getAccount().getUid(), UserGroupMemberEntity.ROLE_GUARDIAN, true);

        if (familyGroup.size() == 1) {
            return Optional.of(familyGroup.get(0));
        }
        return Optional.empty();
    }

    /**
     * Creates a group and adds the requester as admin member
     *
     * @param inputUserGroup
     * @return
     */
    @PostMapping(value = "/groups", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserGroupEntity createMyGroup(@RequestBody UserGroupEntity inputUserGroup) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        return this.userGroupService.createGroup(inputUserGroup, userAuth.getAccount());
    }


    /**
     * Finds a family if found otherwise create one
     *
     * @param inputUserGroup
     * @return
     */
    @PutMapping(value = "/groups/family", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserGroupEntity getOrCreateMyFamilyGroup(@RequestBody UserGroupEntity inputUserGroup) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        if (StringUtils.isEmpty(inputUserGroup.getName())) {
            var familyGroupName = !StringUtils.isEmpty(userAuth.getAccount().getFamilyName()) ? userAuth.getAccount().getFamilyName() : userAuth.getAccount().getUsername();
            inputUserGroup.setName(familyGroupName);
        }
        inputUserGroup.setType(UserGroupType.FAMILY);

        var family = this.userGroupService.findOrCreateGroupByType(inputUserGroup, userAuth.getAccount());

        return family.get(0);
    }


    @GetMapping(value = "/groups/{groupUid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<UserGroupMemberEntity> getGroup(@PathVariable("groupUid") String groupUid, Pageable pageable) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        // TODO constraint to user's groups only
        return this.userGroupService.findMembers(groupUid, pageable);
    }

    @GetMapping(value = "/groups/{groupUid}/members", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<UserGroupMemberEntity> getMembers(@PathVariable("groupUid") String groupUid, Pageable pageable) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        // TODO constraint to user's groups only
        return this.userGroupService.findMembers(groupUid, pageable);
    }


    @PostMapping(value = "/groups/{groupUid}/members/account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserGroupMemberEntity addNewAccountMember(@PathVariable("groupUid") String groupUid, @RequestBody Map<String, String> memberAccountDetails) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        // TODO verify that the user in session has admin permission to add member
        String memberRole = memberAccountDetails.get("memberRole");
        String username = memberAccountDetails.get(UserService.USERNAME);
        String password = memberAccountDetails.get(UserService.PASSWORD);
        String email = memberAccountDetails.get(UserService.EMAIL);
        String givenName= memberAccountDetails.get(UserService.GIVEN_NAME);

        return this.userGroupService.createAccountAndAddAsMember(groupUid, memberRole, username, password, email, givenName);
    }


    @PostMapping(value = "/groups/{groupUid}/members", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserGroupMemberEntity addMember(@PathVariable("groupUid") String groupUid, @RequestBody UserGroupMemberEntity memberDetails, Pageable pageable) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        // TODO verify that the user in session has admin permission to add member
        memberDetails.setAccount(userAuth.getAccount());
        return this.userGroupService.addMember(groupUid, memberDetails);
    }

    //</editor-fold>
}

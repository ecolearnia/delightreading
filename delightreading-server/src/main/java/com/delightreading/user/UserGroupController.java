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
public class UserGroupController {

    public static final String TYPE_PREFIX = "type:";

    UserGroupService userGroupService;

    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
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
    public UserGroupMemberEntity addNewAccountMember(@PathVariable("groupUid") String groupUid, @RequestBody CreateNewMemberCommand memberAccountDetails) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        memberAccountDetails.setGroupUid(groupUid);

        return this.userGroupService.createAccountAndAddAsMember(memberAccountDetails);
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

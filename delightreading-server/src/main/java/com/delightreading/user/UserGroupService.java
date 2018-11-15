package com.delightreading.user;

import com.delightreading.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserGroupService {

    UserService userService;
    UserGroupRepository userGroupRepository;
    UserGroupMemberRepository userGroupMemberRepository;

    public UserGroupService(
            UserService userService,
            UserGroupRepository userGroupRepository,
            UserGroupMemberRepository userGroupMemberRepository
    ) {
        this.userService = userService;
        this.userGroupRepository = userGroupRepository;
        this.userGroupMemberRepository = userGroupMemberRepository;
    }

    /**
     * Creates a group with the member as the first member
     * @return
     */
    @Transactional
    public UserGroupEntity createGroup(UserGroupEntity inputUserGroup, UserAccountEntity memberAccount) {

        String memberRole = UserGroupMemberEntity.ROLE_ADMIN;
        if (inputUserGroup.getType() == UserGroupType.FAMILY) {
            // Family can only be one
            memberRole = UserGroupMemberEntity.ROLE_GUARDIAN;
            List<UserGroupEntity> foundGroups = this.findGroupsByTypeAndMemberWithRole(inputUserGroup.getType(), memberAccount.getUid(), memberRole, false);
            if (foundGroups.size() > 0) {
                // return 409
                return null;
            }
        }

        var foundMemberAccountOpt = userService.findAccountByUid(memberAccount.getUid());
        if (!foundMemberAccountOpt.isPresent()) {
            throw new IllegalArgumentException(("ResourceNotFound: accountUid=" + memberAccount.getUid()));
        }

        var savedGroup = userGroupRepository.save(inputUserGroup);
        var member = UserGroupMemberEntity.builder()
                .group(savedGroup)
                .account(foundMemberAccountOpt.get())
                .role(memberRole)
                .sinceDate(Instant.now())
                .memberStatus(UserGroupMemberEntity.STATUS_ACTIVE)
                .build();
        this.userGroupMemberRepository.save(member);
        return savedGroup;
    }


    /**
     * Find a group to which the user belongs to as admin
     * @param inputUserGroup
     * @param adminAccount
     * @return
     */
    @Transactional
    public List<UserGroupEntity> findOrCreateGroupByType(UserGroupEntity inputUserGroup, UserAccountEntity adminAccount) {
        String memberRole = UserGroupMemberEntity.ROLE_ADMIN;
        if (inputUserGroup.getType() == UserGroupType.FAMILY) {
            memberRole = UserGroupMemberEntity.ROLE_GUARDIAN;
        }
        List<UserGroupEntity> foundGroups = this.findGroupsByTypeAndMemberWithRole(inputUserGroup.getType(), adminAccount.getUid(), memberRole, false);
        if (foundGroups.size() == 0) {
            var newGroup = this.createGroup(inputUserGroup, adminAccount);
            foundGroups = List.of(newGroup);
        }

        return foundGroups;
    }


    public Optional<UserGroupEntity> findGroup(String groupId, boolean fetchMembers) {
        var match = this.userGroupRepository.findByUid(groupId);
        if (match.isPresent() && fetchMembers) {
            // TODO: optimize by using select where group_id in
            log.info("Retrieving members for groupId:{}", match.get().getUid());
            var members = this.userGroupMemberRepository.findByGroupUid(match.get().getUid());
            match.get().setMembers(members);
        }
        return match;
    }

    public Optional<UserGroupEntity> findGroups(UserGroupType[] typeIn, boolean fetchMembers) {
        var result = this.userGroupRepository.findByTypeIn(typeIn);

        return result;
    }

    public Page<UserGroupEntity> findAllGroups(Pageable pageable) {
        return this.userGroupRepository.findAll(pageable);
    }

    public Page<UserGroupEntity> findGroupsByTypeInAndMember(UserGroupType[] types, String memberUid, boolean fetchMembers, Pageable pageable) {

        String[] typeNames = Arrays.stream(types).map( type -> type.toString()).toArray(String[]::new);
        var result = this.userGroupRepository.findByTypeInAndMember(typeNames, memberUid, pageable);
        if (fetchMembers) {
            fetchMembersOf(result.getContent());
        }
        return result;
    }


    public List<UserGroupEntity> findGroupsByTypeAndMemberWithRole(UserGroupType type, String memberUid, String memberRole, boolean fetchMembers) {
        var result = this.userGroupRepository.findByTypeAndMember(type.toString(), memberUid, memberRole);
        if (fetchMembers) {
            fetchMembersOf(result);
        }
        return result;
    }

    public Page<UserGroupEntity> findGroupsByTypeAndNameLike(UserGroupType[] typeIn, String nameLike, boolean fetchMembers, Pageable pageable) {
        var result = this.userGroupRepository.findByTypeInAndNameLike(typeIn, nameLike, pageable);
        if (fetchMembers) {
            fetchMembersOf(result.getContent());
        }
        return result;
    }

    void fetchMembersOf(List<UserGroupEntity> userGroups) {
        userGroups.stream().forEach(entry -> {
            log.info("Retrieving members for groupId:{}", entry.getUid());
            var members = this.userGroupMemberRepository.findByGroupUid(entry.getUid());
            entry.setMembers(members);
        });
    }

    // Members


    @Transactional
    public UserGroupMemberEntity createAccountAndAddAsMember(CreateNewMemberCommand command) {
        var matchGroupOpt = this.userGroupRepository.findByUid(command.getGroupUid());
        if (!matchGroupOpt.isPresent()) {
            throw new IllegalStateException("ResourceNotFound");
        }

        var memberUserAuth = userService.buildAuthentication(command.getUsername(), command.getPassword(), command.getEmail(), command.getGivenName());
        command.getProfile().setAccount(memberUserAuth.getAccount());
        UserAuthenticationEntity registeredMemberUserAuth = userService.registerNew(memberUserAuth, command.getProfile());

        UserGroupMemberEntity newMember = UserGroupMemberEntity.builder()
                .role(command.getMemberRole())
                .account(registeredMemberUserAuth.getAccount()).build();

        return this.addMember(command.getGroupUid(), newMember);

    }

    public UserGroupMemberEntity addMember(String groupUid, UserGroupMemberEntity member) {
        var matchGroupOpt = this.userGroupRepository.findByUid(groupUid);
        if (!matchGroupOpt.isPresent()) {
            throw new IllegalStateException("ResourceNotFound");
        }
        if (member.getAccount() == null) {
            throw new IllegalStateException("AccountNotSet");
        }
        member.setGroup(matchGroupOpt.get());
        member.setSinceDate(Instant.now());
        member.setMemberStatus(UserGroupMemberEntity.STATUS_ACTIVE);
        return this.userGroupMemberRepository.save(member);
    }

    public Page<UserGroupMemberEntity> findMembers(String groupUid, Pageable pageable) {
        return this.userGroupMemberRepository.findByGroupUid(groupUid, pageable);
    }
}

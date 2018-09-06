package com.delightreading.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class UserGroupService {

    UserGroupRepository userGroupRepository;
    UserGroupMemberRepository userGroupMemberRepository;

    public UserGroupService(
            UserGroupRepository userGroupRepository,
            UserGroupMemberRepository userGroupMemberRepository
    ) {
        this.userGroupRepository = userGroupRepository;
        this.userGroupMemberRepository = userGroupMemberRepository;
    }

    /**
     * Creates a group with the member as the first member
     * @param group
     * @param memberAccount
     * @param memberRole
     * @return
     */
    @Transactional
    public UserGroupEntity createGroup(UserGroupEntity group, UserAccountEntity memberAccount, String memberRole) {
        var savedGroup = userGroupRepository.save(group);
        var member = UserGroupMemberEntity.builder()
                .group(savedGroup)
                .account(memberAccount)
                .role(memberRole)
                .sinceDate(Instant.now())
                .memberStatus(UserGroupMemberEntity.STATUS_ACTIVE)
                .build();
        this.userGroupMemberRepository.save(member);
        return savedGroup;
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

    public Page<UserGroupEntity> findAllGroups(Pageable pageable) {
        return this.userGroupRepository.findAll(pageable);
    }

    public Page<UserGroupEntity> findGroupsByTypeWithMember(String[] typeIn, String memberUid, Pageable pageable, boolean fetchMembers) {
        var result = this.userGroupRepository.findByTypeInAndMember(typeIn, memberUid, pageable);
        if (fetchMembers) {
            result.getContent().stream().forEach(entry -> {
                log.info("Retrieving members for groupId:{}", entry.getUid());
                var members = this.userGroupMemberRepository.findByGroupUid(entry.getUid());
                entry.setMembers(members);
            });
        }
        return result;
    }

    public Page<UserGroupEntity> findGroupsByTypeAndNameLike(UserGroupType[] typeIn, String nameLike, Pageable pageable, boolean fetchMembers) {
        var result = this.userGroupRepository.findByTypeInAndNameLike(typeIn, nameLike, pageable);
        if (fetchMembers) {
            result.getContent().stream().forEach(entry -> {
                log.info("Retrieving members for groupId:{}", entry.getUid());
                var members = this.userGroupMemberRepository.findByGroupUid(entry.getUid());
                entry.setMembers(members);
            });
        }
        return result;
    }


}

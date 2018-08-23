package com.delightreading.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Optional<UserGroupEntity> findGroup(String groupId, boolean withUser) {
        var match = this.userGroupRepository.findByUid(groupId);
        if (match.isPresent() && withUser) {
            log.info("Retrieving members for groupId:{}", match.get().getUid());
            var members = this.userGroupMemberRepository.findByGroupUid(match.get().getUid());
            match.get().setMembers(members);
        }
        return match;
    }


    public Page<UserGroupEntity> findAllGroups(Pageable pageable) {
        return this.userGroupRepository.findAll(pageable);
    }

    public Page<UserGroupEntity> findGroupByTypeAndNameLike(UserGroupType[] typeIn, String nameLike, Pageable pageable, boolean withUser) {
        var result = this.userGroupRepository.findByTypeInAndNameLike(typeIn, nameLike, pageable);
        if (withUser) {
            result.getContent().stream().forEach(entry -> {
                log.info("Retrieving members for groupId:{}", entry.getUid());
                var members = this.userGroupMemberRepository.findByGroupUid(entry.getUid());
                entry.setMembers(members);
            });
        }
        return result;
    }


}

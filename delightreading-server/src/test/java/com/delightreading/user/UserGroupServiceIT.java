package com.delightreading.user;

import com.delightreading.SpringApplicationContextUtil;
import com.delightreading.user.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {UserAuthenticationRepository.class,
        UserProfileRepository.class, UserService.class,
        UserGroupRepository.class, UserGroupMemberRepository.class,
        UserGroupService.class, SpringApplicationContextUtil.class})
@EnableAutoConfiguration
public class UserGroupServiceIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserGroupService userGroupService;

    List<UserAccountEntity> accounts;

    @Test
    public void createGroup_whenNew_returnGroup() {

        // Given
        var auths = buidFullAuthsAndPersist("test", 1);

        var group = UserGroupEntity.builder()
                .name("TestGroup")
                .type(UserGroupType.FAMILY)
                .groupStatus(UserGroupEntity.STATUS_ACTIVE)
                .category("TestCate")
                .build();

        // When
        var createdGroup = userGroupService.createGroup(group, auths.get(0).getAccount());

        //UserGroupType[] groupTypes = {UserGroupType.FAMILY, UserGroupType.ACADEMIC};
        UserGroupType[] groupTypes = {UserGroupType.FAMILY, UserGroupType.ACADEMIC};
        var foundGroups = userGroupService.findGroupsByTypeInAndMember(groupTypes, auths.get(0).getAccount().getUid(), true, PageRequest.of(0, 10));


        // Then
        assertThat(createdGroup).isNotNull();

        assertThat(foundGroups).hasSize(1);
    }

    @Test
    public void findOrCreateGroupByType_whenCalledTwice_returnsSame() {

        // Given
        var auths = buidFullAuthsAndPersist("test2", 1);

        var group = UserGroupEntity.builder()
                .name("TestGroup")
                .type(UserGroupType.FAMILY)
                .groupStatus(UserGroupEntity.STATUS_ACTIVE)
                .category("TestCate")
                .build();

        // When called once
        var createdFamilyGroup = userGroupService.findOrCreateGroupByType(group, auths.get(0).getAccount());

        // Then new group is crated
        assertThat(createdFamilyGroup).isNotNull();

        // When find by criteria
        UserGroupType groupType = UserGroupType.FAMILY;
        var foundGroups = userGroupService.findGroupsByTypeAndMemberWithRole(groupType, auths.get(0).getAccount().getUid(), UserGroupMemberEntity.ROLE_GUARDIAN, false);

        // Then newly created group is returned
        assertThat(foundGroups).hasSize(1);
        assertThat(foundGroups.get(0).getUid()).isEqualTo(createdFamilyGroup.get(0).getUid());

        // When called again
        var foundFamilyGroup = userGroupService.findOrCreateGroupByType(group, auths.get(0).getAccount());

        // Then
        assertThat(foundFamilyGroup).hasSize(1);
        assertThat(foundFamilyGroup.get(0).getUid()).isEqualTo(createdFamilyGroup.get(0).getUid());

    }

    @Test
    public void findGroupByTypeAndNameLike_whenSearchByFamilyWithMember_returnTwo() {

        // Given
        prep(6);

        // When
        UserGroupType[] groupTypes = {UserGroupType.FAMILY, UserGroupType.ACADEMIC};
        var foundGroups = userGroupService.findGroupsByTypeAndNameLike(groupTypes, "%Family", true, PageRequest.of(0, 10));

        var allGroups = userGroupService.findAllGroups(PageRequest.of(0, 10));

        // Then
        assertThat(foundGroups).hasSize(2);
    }


    @Test
    public void findGroupByTypeAndNameLike_whenSearchByAcademicType_returnOne() {

        // Given
        prep(6);

        // When
        UserGroupType[] groupTypes = {UserGroupType.ACADEMIC};
        var foundGroups = userGroupService.findGroupsByTypeAndNameLike(groupTypes, "%", true, PageRequest.of(0, 10));

        // Then
        assertThat(foundGroups).hasSize(1);
    }


    @Test
    public void findGroupsByTypeWithMember_whenSearchByFamily_returnTwo() {

        // Given
        var auths = buidFullAuthsAndPersist("GroupTest", 1);

        List<UserGroupEntity> groups = buildGroups();
        for (UserGroupEntity group: groups) {
            var savedGroup = entityManager.persist(group);
            UserGroupMemberEntity member = UserGroupMemberEntity.builder().memberStatus("new")
                    .account(auths.get(0).getAccount()).group(savedGroup).role("trole").build();
            entityManager.persist(member);
        }

        // When
        UserGroupType[] groupTypes = {UserGroupType.FAMILY, UserGroupType.CLUB};
        var foundGroups = userGroupService.findGroupsByTypeInAndMember(groupTypes, auths.get(0).getAccount().getUid(), true, PageRequest.of(0, 10));

        // Then
        assertThat(foundGroups).hasSize(2);
        assertThat(foundGroups.stream().map(UserGroupEntity::getType).toArray()).containsOnly(UserGroupType.FAMILY);
    }

    @Test
    public void createAccountAndAddAsMember_whenCalled_createMember() {

        // Given
        var auths = buidFullAuthsAndPersist("test", 1);

        List<UserGroupEntity> groups = buildGroups();
        var savedGroup = entityManager.persist(groups.get(0));

        // When
        String username = "NEWMEMTEST";
        var member = userGroupService.createAccountAndAddAsMember(savedGroup.getUid(), UserGroupMemberEntity.ROLE_MEMBER, username, "newmempwd", null, null);

        // Then new auth is found
        UserAuthenticationEntity foundAuth = entityManager.getEntityManager().createQuery("FROM UserAuthenticationEntity where providerAccountId = :providerAccountId", UserAuthenticationEntity.class)
                .setParameter("providerAccountId", username).getSingleResult();
        assertThat(foundAuth).isNotNull();

        // And its account is part of members
        var members = userGroupService.findMembers(savedGroup.getUid(), PageRequest.of(0, 100));
        assertThat(members).hasSize(1);
        assertThat(members.getContent().get(0).getAccount().getUid()).isEqualTo(foundAuth.getAccount().getUid());

    }

    public List<UserAuthenticationEntity> buidFullAuthsAndPersist(String idPrefix, int count) {
        List<UserAuthenticationEntity> auths = new ArrayList<>();
        for (int i=0; i < count; i++) {
            String userName = idPrefix + "-" + String.valueOf(i);
            String givenName = idPrefix + "Name-" + String.valueOf(i);
            String email =  idPrefix + String.valueOf(i) + "@supertest.com";
            String providerActId =  idPrefix + "ProvActID" + String.valueOf(i);
            var account1 = UserAccountRepositoryIT.buildEntity(null, userName, givenName, Arrays.asList(email));
            entityManager.persist(account1);
            var profile1 = UserProfileRepositoryIT.buildEntity(Arrays.asList(Experience.builder().kind("exp-kind1").title("exp-title1").build()), Arrays.asList("math", "science"), "male", account1);
            entityManager.persist(profile1);
            var auth1 = UserAuthenticationRepositoryIT.buildEntity(null, "TestProvider", providerActId, "TestPwd", account1);
            entityManager.persist(auth1);
            auths.add(auth1);
        }

        return auths;
    }

    public List<UserGroupEntity> buildGroups() {

        List<UserGroupEntity> groups = new ArrayList<>();

        groups.addAll(
                Arrays.asList(
                        UserGroupEntity.builder().name("SuperFamily").type(UserGroupType.FAMILY).build(),
                        UserGroupEntity.builder().name("MegaFamily").type(UserGroupType.FAMILY).build(),
                        UserGroupEntity.builder().name("3rd Grade").type(UserGroupType.ACADEMIC).build())
        );
        return groups;
    }


    /**
     * Creates and insert three groups
     */
    public List<UserAuthenticationEntity> prep(int userCount) {

        var auths = buidFullAuthsAndPersist("GroupTest", userCount);

        List<UserGroupEntity> groups = buildGroups();

        int count = auths.size() / groups.size();
        int authIdx = 0;
        int groupIdx = 0;
        UserGroupEntity savedGroup = null;
        for (UserAuthenticationEntity auth: auths) {
            if ( (authIdx++ % count) == 0 ) {
                UserGroupEntity group = groups.get(groupIdx++);
                savedGroup = entityManager.persist(group);
            }
            UserGroupMemberEntity member = UserGroupMemberEntity.builder().memberStatus("new")
                    .account(auth.getAccount()).group(savedGroup).role("trole").build();
            entityManager.persist(member);
        }

        return auths;
    }

}

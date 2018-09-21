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
@ContextConfiguration(classes = {UserAuthenticationRepository.class, UserProfileRepository.class,
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
        var auths = buidFullAuths("test", 1);

        var group = UserGroupEntity.builder()
                .name("TestGroup")
                .type(UserGroupType.FAMILY)
                .groupStatus(UserGroupEntity.STATUS_ACTIVE)
                .category("TestCate")
                .build();

        // When
        var createdGroup = userGroupService.createGroup(group, auths.get(0).getAccount(), "admin");

        //UserGroupType[] groupTypes = {UserGroupType.FAMILY, UserGroupType.ACADEMIC};
        String[] groupTypes = {UserGroupType.FAMILY.name(), UserGroupType.ACADEMIC.name()};
        var foundGroups = userGroupService.findGroupsByTypeWithMember(groupTypes, auths.get(0).getAccount().getUid(), PageRequest.of(0, 10), true);


        // Then
        assertThat(createdGroup).isNotNull();

        assertThat(foundGroups).hasSize(1);
    }

    @Test
    public void findGroupByTypeAndNameLike_whenSearchByFamilyWithMember_returnTwo() {

        // Given
        prep(6);

        // When
        UserGroupType[] groupTypes = {UserGroupType.FAMILY, UserGroupType.ACADEMIC};
        var foundGroups = userGroupService.findGroupsByTypeAndNameLike(groupTypes, "%Family", PageRequest.of(0, 10), true);

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
        var foundGroups = userGroupService.findGroupsByTypeAndNameLike(groupTypes, "%", PageRequest.of(0, 10), true);

        // Then
        assertThat(foundGroups).hasSize(1);
    }


    @Test
    public void findGroupsByTypeWithMember_whenSearchByFamily_returnTwo() {

        // Given
        var auths = buidFullAuths("GroupTest", 1);

        List<UserGroupEntity> groups = buildGroups();
        for (UserGroupEntity group: groups) {
            var savedGroup = entityManager.persist(group);
            UserGroupMemberEntity member = UserGroupMemberEntity.builder().memberStatus("new")
                    .account(auths.get(0).getAccount()).group(savedGroup).role("trole").build();
            entityManager.persist(member);
        }

        // When
        String[] groupTypes = {UserGroupType.FAMILY.name(), UserGroupType.CLUB.name()};
        var foundGroups = userGroupService.findGroupsByTypeWithMember(groupTypes, auths.get(0).getAccount().getUid(), PageRequest.of(0, 10), true);

        // Then
        assertThat(foundGroups).hasSize(2);
        assertThat(foundGroups.stream().map(UserGroupEntity::getType).toArray()).containsOnly(UserGroupType.FAMILY);
    }

    public List<UserAuthenticationEntity> buidFullAuths(String idPrefix, int count) {
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

        var auths = buidFullAuths("GroupTest", userCount);

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

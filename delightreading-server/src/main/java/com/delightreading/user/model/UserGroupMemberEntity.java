package com.delightreading.user.model;

import com.delightreading.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_group_member")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserGroupMemberEntity extends BaseEntity {

    public static String STATUS_ACTIVE = "active";

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="group_uid", referencedColumnName = "uid")
    UserGroupEntity group;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="account_uid", referencedColumnName = "uid")
    UserAccountEntity account;


    // student, guardian, teacher, club-admin
    @Column(name = "role")
    String role;

    @Column(name = "since_date")
    Instant sinceDate;

    @Column(name = "until_date")
    Instant untilDate;

    @Column(name = "member_status")
    String memberStatus;

}

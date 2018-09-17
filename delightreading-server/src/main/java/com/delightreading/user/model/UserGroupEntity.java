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
import java.util.LinkedHashSet;

@Entity
@Table(name = "user_group")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserGroupEntity extends BaseEntity {

    public static final String STATUS_ACTIVE = "active";


    // family, academic, club, etc.
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    UserGroupType type;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "picture_uri")
    String pictureUri;

    @Column(name = "cover_image_uri")
    String coverImageUri;

    @Column(name = "category")
    String category;

    @Column(name = "rules")
    String rules;

    @Column(name = "website")
    String website;

    @Column(name = "start_date")
    Instant startDate;

    @Column(name = "close_date")
    Instant closeDate;

    @Column(name = "group_status")
    String groupStatus;

    // Integer memberCount;

    @Transient
//    @OneToMany(
//            mappedBy = "user_group",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
    LinkedHashSet<UserGroupMemberEntity> members = new LinkedHashSet<>();

    public void addMember(UserGroupMemberEntity member) {
        members.add(member);
    }
}

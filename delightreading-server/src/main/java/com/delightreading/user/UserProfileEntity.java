package com.delightreading.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user_profile")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserProfileEntity implements Serializable {


    ////////// Base {{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long sid;

    @Column(name = "uid")
    String uid;

    @Column(name = "status")
    String status;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "updated_by")
    String updatedBy;

    @Column(name = "updated_at")
    Instant updatedAt;


    @PrePersist
    public void prePersist() {
        if (uid == null) {
            uid = UUID.randomUUID().toString();
        }
        createdAt = Instant.now();
        // createdBy = LoggedUser.get();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
        // updatedBy = LoggedUser.get();
    }
    ////////// }} Base

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_uid", referencedColumnName = "uid")
    UserAccountEntity account;

    @Column(name = "synopsis")
    String synopsis;

    @Column(name = "hometown")
    String hometown;

    @Column(name = "gender")
    String gender;

    @Column(name = "expertise")
    String expertise;

    @Column(name = "occupation")
    String occupation;

    @Column(name = "style")
    String style;

    @Column(name = "interests")
    @Type(type = "com.delightreading.common.HibernateStringListUserType")
    List<String> interests;

    @Column(name = "languages")
    @Type(type = "com.delightreading.common.HibernateStringListUserType")
    List<String> languages;

    @Column(name = "websites")
    @Type(type = "com.delightreading.common.HibernateStringListUserType")
    List<String> websites;

    @Column(name = "education_json")
    @Type(type = "com.delightreading.user.hibernate.ExperienceListUserType")
    List<Experience> education;

    @Column(name = "work_json")
    @Type(type = "com.delightreading.user.hibernate.ExperienceListUserType")
    List<Experience> work;

    @Column(name = "experiences_json")
    @Type(type = "com.delightreading.user.hibernate.ExperienceListUserType")
    List<Experience> experiences;

    @Column(name = "accomplishments_json")
    @Type(type = "com.delightreading.user.hibernate.ExperienceListUserType")
    List<Experience> accomplishments;


    public void addEducation(Experience educationItem) {
        if (education == null) {
            education = new ArrayList<>();
        }
        this.education.add(educationItem);
    }

    public void addWork(Experience experience) {
        if (work == null) {
            work = new ArrayList<>();
        }
        this.work.add(experience);
    }

    public void addExperience(Experience experience) {
        if (experiences == null) {
            experiences = new ArrayList<>();
        }
        this.experiences.add(experience);
    }

    public void addAccomplishment(Experience experience) {
        if (accomplishments == null) {
            accomplishments = new ArrayList<>();
        }
        this.accomplishments.add(experience);
    }

}

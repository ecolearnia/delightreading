package com.delightreading.user;

import com.delightreading.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserProfileEntity extends BaseEntity {

    @Column(name = "experiences_json")
    UserAccountEntity account;

    @Column(name = "experiences_json")
    List<String> emails;

    @Column(name = "experiences_json")
    String synopsis;

    @Column(name = "experiences_json")
    String hometown;

    @Column(name = "experiences_json")
    String education;

    @Column(name = "experiences_json")
    String expertise;

    @Column(name = "experiences_json")
    @Type(type = "com.delightreading.user.hibernate.ExperienceUserType")
    List<Experience> experiences;
    List<Experience> accomplishments;
    String style;
    List<String> interests;
    List<String> languages;
    String gender;
    List<String> websites;

}

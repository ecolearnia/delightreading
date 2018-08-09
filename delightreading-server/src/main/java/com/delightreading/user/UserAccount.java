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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user_account")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserAccount extends BaseEntity {

    @Column(name = "username")
    String username;

    @Column(name = "emails")
    List<String> emails;

    @Column(name = "nickname")
    String nickname;

    @Column(name = "given_name")
    String givenName;

    @Column(name = "family_name")
    String familyName;

    @Column(name = "middle_name")
    String middleName;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Column(name = "picture_uri")
    String pictureUri;

    @Column(name = "locale")
    String locale;

    @Column(name = "timezone")
    String timezone;

}

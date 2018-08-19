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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
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
public class UserAccountEntity extends BaseEntity {

    @Column(name = "username")
    String username;

    @ElementCollection
    @CollectionTable(name = "user_account_emails", joinColumns = @JoinColumn(name = "account_uid", referencedColumnName = "uid"))
    //@Fetch(FetchMode.SUBSELECT)
    @Fetch(FetchMode.JOIN)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @Column(name = "email")
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

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="profile_uid")
    UserProfileEntity profile;

}

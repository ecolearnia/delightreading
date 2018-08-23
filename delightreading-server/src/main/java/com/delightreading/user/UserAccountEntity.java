package com.delightreading.user;

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
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user_account")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserAccountEntity implements Serializable {

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

//    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name="profile_uid", referencedColumnName = "uid")
//    UserProfileEntity profile;

}

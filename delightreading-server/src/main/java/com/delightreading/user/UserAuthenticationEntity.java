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
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user_authentication")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserAuthenticationEntity implements Serializable {


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

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="account_uid", referencedColumnName = "uid")
    UserAccountEntity account;

    @Column(name = "provider_id")
    String provider;

    @Column(name = "provider_account_id")
    String providerAccountId;

    @Column(name = "password")
    String password;

    @Column(name = "access_token")
    String accessToken;

    @Column(name = "refresh_token")
    String refreshToken;

    @Column(name = "expiration")
    Instant expiration;

    @Column(name = "raw_profile", length = 65535, columnDefinition="TEXT")
    @Type(type="text")
    String rawProfile;

}

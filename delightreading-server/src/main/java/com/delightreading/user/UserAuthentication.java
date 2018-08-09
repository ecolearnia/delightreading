package com.delightreading.user;

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
public class UserAuthentication {

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="account_uid")
    UserAccount account;
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
    @Column(name = "raw_profile")
    String rawProfile;

}

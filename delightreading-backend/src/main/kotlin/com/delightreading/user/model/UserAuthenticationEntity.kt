package com.delightreading.user.model

import org.hibernate.annotations.Type
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import java.io.Serializable
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_authentication")
data class UserAuthenticationEntity(

    //<editor-fold desc="Common Entity Base">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var sid: Long? = null,

    @Column(name = "uid")
    var uid: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: UserAccountStatus = UserAccountStatus.ACTIVE,

    @Column(name = "created_by")
    var createdBy: String? = null,

    @Column(name = "created_at")
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_by")
    var updatedBy: String? = null,

    @Column(name = "updated_at")
    var updatedAt: Instant? = null,
    //</editor-fold>

    @ManyToOne(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "account_uid", referencedColumnName = "uid")
    var account: UserAccountEntity? = null,

    @Column(name = "provider_id")
    var provider: String? = null,

    @Column(name = "provider_account_id")
    var providerAccountId: String? = null,

    @Column(name = "password")
    var pwd: String? = null,

    @Column(name = "access_token")
    var accessToken: String? = null,

    @Column(name = "refresh_token")
    var refreshToken: String? = null,

    @Column(name = "expiration")
    var expiration: Instant? = null,

    @Column(name = "raw_profile", length = 65535, columnDefinition = "TEXT")
    @Type(type = "text")
    var rawProfile: String? = null

) : UserDetails, Serializable {

    //<editor-fold desc="Common Entity Base">
    @PrePersist
    fun prePersist() {
        if (uid == null) {
            uid = UUID.randomUUID().toString()
        }
        createdAt = Instant.now()
        // createdBy = LoggedUser.get();
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = Instant.now()
        // updatedBy = LoggedUser.get();
    }
    //</editor-fold>

    companion object {
        val LOCAL_PROVIDER = "local"
    }

    override fun getAuthorities(): Collection<GrantedAuthority>? {
        return null
    }

    override fun getUsername(): String? {
        return this.account?.username
    }

    override fun isAccountNonExpired(): Boolean {
        return expiration == null || expiration!!.isBefore(Instant.now())
    }

    override fun isAccountNonLocked(): Boolean {
        return this.account?.status != UserAccountStatus.LOCKED
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return this.account?.status != UserAccountStatus.DISABLED
    }

    override fun getPassword(): String? {
        return this.pwd
    }
}
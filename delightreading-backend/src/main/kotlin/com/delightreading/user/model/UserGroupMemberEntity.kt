package com.delightreading.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_group_member")
data class UserGroupMemberEntity(

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

    @JsonIgnore
    @ManyToOne(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "group_uid", referencedColumnName = "uid")
    internal var group: UserGroupEntity? = null,

    @ManyToOne(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "account_uid", referencedColumnName = "uid")
    internal var account: UserAccountEntity? = null,


    // student, guardian, teacher, club-admin
    @Column(name = "role")
    internal var role: String? = null,

    @Column(name = "since_date")
    internal var sinceDate: Instant? = null,

    @Column(name = "until_date")
    internal var untilDate: Instant? = null,

    @Column(name = "member_status")
    internal var memberStatus: String? = null

) : Serializable {

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

}
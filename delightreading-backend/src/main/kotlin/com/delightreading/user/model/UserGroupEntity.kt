package com.delightreading.user.model

import java.io.Serializable
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_group")
data class UserGroupEntity(

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


    // family, academic, club, etc.
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    var type: UserGroupType? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "picture_uri")
    var pictureUri: String? = null,

    @Column(name = "cover_image_uri")
    var coverImageUri: String? = null,

    @Column(name = "category")
    var category: String? = null,

    @Column(name = "rules")
    var rules: String? = null,

    @Column(name = "website")
    var website: String? = null,

    @Column(name = "start_date")
    var startDate: Instant? = null,

    @Column(name = "close_date")
    var closeDate: Instant? = null,

    @Column(name = "group_status")
    var groupStatus: String? = null,

// Integer memberCount;

    @Transient
//    @OneToMany(
//            mappedBy = "user_group",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
    var members: LinkedHashSet<UserGroupMemberEntity> = LinkedHashSet<UserGroupMemberEntity>()//
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
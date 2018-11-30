package com.delightreading.user.model

import org.hibernate.annotations.Type
import java.io.Serializable
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_profile")
data class UserProfileEntity(

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_uid", referencedColumnName = "uid")
    internal var account: UserAccountEntity? = null,

    @Column(name = "synopsis")
    internal var synopsis: String? = null,

    @Column(name = "hometown")
    internal var hometown: String? = null,

    @Column(name = "gender")
    internal var gender: String? = null,

    @Column(name = "expertise")
    internal var expertise: String? = null,

    @Column(name = "occupation")
    internal var occupation: String? = null,

    @Column(name = "style")
    internal var style: String? = null,

    @Column(name = "interests")
    @Type(type = "com.delightreading.common.HibernateStringListUserType")
    internal var interests: List<String>? = null,

    @Column(name = "languages")
    @Type(type = "com.delightreading.common.HibernateStringListUserType")
    internal var languages: List<String>? = null,

    @Column(name = "websites")
    @Type(type = "com.delightreading.common.HibernateStringListUserType")
    internal var websites: List<String>? = null,

    @Column(name = "education_json")
    @Type(type = "com.delightreading.user.hibernate.ExperienceListUserType")
    internal var education: MutableList<Experience>? = null,

    @Column(name = "work_json")
    @Type(type = "com.delightreading.user.hibernate.ExperienceListUserType")
    internal var work: MutableList<Experience>? = null,

    @Column(name = "experiences_json")
    @Type(type = "com.delightreading.user.hibernate.ExperienceListUserType")
    internal var experiences: MutableList<Experience>? = null,

    @Column(name = "accomplishments_json")
    @Type(type = "com.delightreading.user.hibernate.ExperienceListUserType")
    internal var accomplishments: MutableList<Experience>? = null

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
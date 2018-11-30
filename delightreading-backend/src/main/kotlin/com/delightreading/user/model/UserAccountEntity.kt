package com.delightreading.user.model

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.io.Serializable
import java.time.Instant
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_account")
data class UserAccountEntity(

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

    @Column(name = "username")
    var username: String? = null,

    @ElementCollection
    @CollectionTable(
        name = "user_account_emails",
        joinColumns = arrayOf(JoinColumn(name = "account_uid", referencedColumnName = "uid"))
    )
    //@Fetch(FetchMode.SUBSELECT)
    @Fetch(FetchMode.JOIN)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @Column(name = "email")
    internal var emails: List<String>? = null,

    @Column(name = "nickname")
    internal var nickname: String? = null,

    @Column(name = "given_name")
    internal var givenName: String? = null,

    @Column(name = "family_name")
    internal var familyName: String? = null,

    @Column(name = "middle_name")
    internal var middleName: String? = null,

    @Column(name = "date_of_birth")
    internal var dateOfBirth: LocalDate? = null,

    @Column(name = "picture_uri")
    internal var pictureUri: String? = null,

    @Column(name = "locale")
    internal var locale: String? = null,

    @Column(name = "timezone")
    internal var timezone: String? = null

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
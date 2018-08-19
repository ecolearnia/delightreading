package com.delightreading.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long sid;

    @Column(name = "uid")
    private String uid;

    @Column(name = "status")
    private String status;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

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
}

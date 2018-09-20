package com.delightreading.reading.model;

import com.delightreading.common.BaseEntity;
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

/**
 * This entity represents an instance of the user reading the book in full
 * NOTE: A user may read a book more than once. E.g. If user read the book
 * twice, there will be two records.
 */
@Entity
@Table(name = "completion_log")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CompletionLogEntity extends BaseEntity {

    @Column(name = "account_uid")
    String accountUid;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="literature_uid", referencedColumnName = "uid")
    LiteratureEntity literature;

    @Column(name = "start_date")
    Instant startDate;

    @Column(name = "end_date")
    Instant endDate;

    @Column(name = "percentage_complete")
    Integer percentageComplete;

    @Column(name = "post_emotion")
    String postEmotion;

    @Column(name = "review")
    String review;

    @Column(name = "synopsis")
    String synopsis;

    // activityStat
    @Transient
    Integer totalDuration;

    @Transient
    Integer totalCount;

}

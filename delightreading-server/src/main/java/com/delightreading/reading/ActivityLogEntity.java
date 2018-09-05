package com.delightreading.reading;

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

@Entity
@Table(name = "activity_log")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ActivityLogEntity extends BaseEntity {

    @Column(name = "account_uid")
    String accountUid;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="literature_uid", referencedColumnName = "uid")
    LiteratureEntity literature;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="completionlog_uid", referencedColumnName = "uid")
    CompletionLogEntity completionLog;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="goal_uid", referencedColumnName = "uid")
    GoalEntity goal;

    @Column(name = "activity")
    String activity;

    @Column(name = "log_timestamp")
    Instant logTimestamp;

    @Column(name = "quantity")
    Float quantity;

    // Time spent with the activity (in minutes)
    @Column(name = "duration")
    Integer duration;

    @Column(name = "current_page")
    Integer currentPage;

    @Column(name = "percentage_complete")
    Integer percentageComplete;

    @Column(name = "post_emotion")
    String postEmotion;

    // E.g. in the livingroom
    @Column(name = "situation")
    String situation;

    // E.g. What did you learn?
    @Column(name = "feed_context")
    String feedContext;

    // E.g. "How children succeed and fail."
    @Column(name = "feed_body")
    String feedBody;

    @Column(name = "retrospective")
    String retrospective;

    @Column(name = "approved_by_uid")
    String approvedByUid;

    @Column(name = "approved_at")
    Instant approvedAt;

}

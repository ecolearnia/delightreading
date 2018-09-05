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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

/**
 * This entity represents an instance of the user reading the book in full
 * NOTE: A user may read a book more than once. E.g. If user read the book
 * twice, there will be two records.
 */
@Entity
@Table(name = "goal")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoalEntity extends BaseEntity {

    @Column(name = "account_uid")
    String accountUid;

    @Column(name = "title")
    String title;

    @Column(name = "activity")
    String activity;

    @Column(name = "start_date")
    Instant startDate;

    @Column(name = "end_date")
    Instant endDate;

    @Column(name = "actual_completion_date")
    Instant actualCompletionDate;

    @Column(name = "quantity")
    Integer quantity;

    // book, minutes
    @Column(name = "quantity_unit")
    String quantityUnit;

    // every day, week, month, year
    @Column(name = "time_period")
    String timePeriod;

    @Column(name = "retrospective")
    String retrospective;

}

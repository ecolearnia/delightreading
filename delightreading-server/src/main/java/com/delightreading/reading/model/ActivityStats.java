package com.delightreading.reading.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
// @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ActivityStats {

    @Id
    Long sid;

    @Column(name = "range_type")
    String rangeType;

    @Column(name = "period_start")
    Date periodStart;

    @Column(name = "period_end")
    Date periodEnd;

    @Column(name = "activity_duration")
    Float activityDuration;

    @Column(name = "activity_count")
    Float activityCount;
}

package com.delightreading.reading.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ActivityStats {

    String rangeType;

    Instant periodStart;
    Instant periodEnd;
    Float activityDuration;
    Float activityCount;
}

package com.delightreading.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Experience {
    String kind; // education, project, competition, hackathon,
    String institution; // Weekend Hackaton Org.
    String location; // Boston
    String title; // E.g. Participated in 12th Weekend Hackhaton
    String subject; // E.g. Entrepreneurship
    String description;
    String achievements; // 1st place.
    LocalDate fromDate; // 20150200 some day in February 2015
    LocalDate toDate;
}

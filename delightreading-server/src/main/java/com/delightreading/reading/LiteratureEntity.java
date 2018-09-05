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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "literature")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LiteratureEntity extends BaseEntity {

    @Column(name = "source_uri")
    String sourceUri;

    @Column(name = "title")
    String title;

    @ElementCollection
    @CollectionTable(name = "literature_authornames", joinColumns = @JoinColumn(name = "author_uid", referencedColumnName = "uid"))
    //@Fetch(FetchMode.SUBSELECT)
    @Fetch(FetchMode.JOIN)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @Column(name = "author_name")
    List<String> authorNames;

    @Column(name = "publisher")
    String publisher;

    @Column(name = "published_date")
    String publishedDate;

    @Column(name = "description")
    String description;

    @Column(name = "synopsis")
    String synopsis;

    // [ {"type":"isbn", "identifier": "ABC"} ]
    @Column(name = "identifiers")
    @Type(type = "com.delightreading.common.HibernateMapListUserType")
    List<Map<String, String>> identifiers;

    @Column(name = "page_count")
    Integer pageCount;

    // ["fiction/children", "education"]
    @Column(name = "categories")
    @Type(type = "com.delightreading.common.HibernateStringListUserType")
    List<String> categories;

    @Column(name = "ratings_count")
    Integer ratingsCount;

    @Column(name = "average_rating")
    Float averageRating;

    @Column(name = "maturity_rating")
    String maturityRating;

    @Column(name = "language")
    String language;

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "thumbnail_image_url")
    String thumbnailImageUrl;

    // ["Newbery"]
    @Column(name = "awards")
    @Type(type = "com.delightreading.common.HibernateStringListUserType")
    List<String> awards;

}

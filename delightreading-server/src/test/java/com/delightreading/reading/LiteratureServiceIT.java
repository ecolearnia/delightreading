package com.delightreading.reading;

import com.delightreading.TestHelper;
import com.delightreading.reading.model.LiteratureEntity;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LiteratureServiceIT {

    LiteratureService literatureService;

    @Before
    public void setup() {
        literatureService = new LiteratureService(TestHelper.getObjectMapper(), null);
    }

    @Test
    public void fetchFromSource_whenHoleRequested_return() {
        LiteratureEntity result = literatureService.fetchFromSource("https://www.googleapis.com/books/v1/volumes/U_zINMa9cAAC");
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Holes");
        assertThat(result.getAuthorNames()).contains("Louis Sachar");
        assertThat(result.getPublishedDate()).isEqualTo("2011-06-01");
        assertThat(result.getPageCount()).isEqualTo(272);
        assertThat(result.getMaturityRating()).isEqualTo("NOT_MATURE");
        // assertThat(result.getRatingsCount()).isGreaterThan(412); Rating changes..
        assertThat(result.getAwards()).containsExactlyInAnyOrder("Newbery", "National Book Award");
    }

    @Test
    public void fetchFromSource_whenKoreanBOokRequested_return() {
        // 스무고개탐정
        LiteratureEntity result = literatureService.fetchFromSource("https://www.googleapis.com/books/v1/volumes/yD79ngEACAAJ");
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("스무 고개 탐정 과 마술사");
        assertThat(result.getAuthorNames()).contains("허교범");
        assertThat(result.getPublishedDate()).isEqualTo("2013");
        assertThat(result.getPageCount()).isEqualTo(184);
        assertThat(result.getMaturityRating()).isEqualTo("NOT_MATURE");
        assertThat(result.getLanguage()).isEqualTo("ko");
        assertThat(result.getRatingsCount()).isNull();
        assertThat(result.getAwards()).isEmpty();
    }
}

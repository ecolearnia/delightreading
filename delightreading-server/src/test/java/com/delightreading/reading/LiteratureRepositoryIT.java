package com.delightreading.reading;

import com.delightreading.SpringApplicationContextUtil;
import com.delightreading.reading.model.LiteratureEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {SpringApplicationContextUtil.class})
@EnableAutoConfiguration
public class LiteratureRepositoryIT {

    @Autowired
    LiteratureRepository literatureRepository;

    @Autowired
    private TestEntityManager entityManager;

    public static LiteratureEntity buildEntity(String title, String author, String language, Float avgRating, String sourceUri) {
        return LiteratureEntity.builder()
                .title(title)
                .authorNames(List.of(author))
                .awards(List.of("MegaAward"))
                .categories(List.of("super/interesting/category", "test/category"))
                .identifiers(List.of(Map.of("MyID", "ID1")))
                .language(language)
                .averageRating(avgRating)
                .sourceUri(sourceUri)
                .build();
    }

    @Before
    public void setup() {
        var Literature1 = LiteratureRepositoryIT.buildEntity("TEST-Title1", "TEST-Author", "en", 4.5F, "b-1");
        entityManager.persistAndFlush(Literature1);

        var Literature2 = LiteratureRepositoryIT.buildEntity("TEST-Title2", "TEST-Author2", "en", 4.0F, "b-2");
        entityManager.persistAndFlush(Literature2);

        var Literature3 = LiteratureRepositoryIT.buildEntity("TEST-Title3", "TEST-Author3", "es", 3.9F, "b-3");
        entityManager.persistAndFlush(Literature3);

    }

    @Test
    public void crud_simple() {

        var newLiterature = LiteratureRepositoryIT.buildEntity("TEST-Titulo", "TEST-Autor", "es", 4.1F, "t1");
        var Literature = this.literatureRepository.save(newLiterature);

        LiteratureEntity match1 = literatureRepository.findByUid(Literature.getUid()).get();

        assertThat(match1).isNotNull();
        assertThat(match1.getTitle()).isEqualTo("TEST-Titulo");

        assertThat(match1.getAuthorNames()).containsExactlyInAnyOrder("TEST-Autor");
    }

    @Test
    public void findOrderByAverageRating_returnInCorrectOrder() {

        var literatures = this.literatureRepository.findAllByOrderByAverageRatingDesc(PageRequest.of(0, 100));

        assertThat(literatures.getSize()).isGreaterThan(2);
        Iterator<LiteratureEntity> it = literatures.getContent().iterator();
        LiteratureEntity prev = it.next();
        while(it.hasNext()) {
            var curr = it.next();
            assertThat(prev.getAverageRating()).isGreaterThanOrEqualTo(curr.getAverageRating());
        }
    }
}

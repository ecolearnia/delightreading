package com.delightreading.reading;

import com.delightreading.common.ObjectAccessor;
import com.delightreading.reading.model.LiteratureEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class LiteratureService {

    public static final String[] AWARDS = {"Newbery", "National Book Award", "Caldecott"};
    public static final String RATING_NOT_MATURE = "NOT_MATURE";
    public static final String RATING_MATURE = "MATURE";
    public static final String[] NWORDS = {
            "violence", "sex", "erotic", "sensual", "fetish", "f**k", "fuck", "s**t", "shit"
    };

    LiteratureRepository literatureRepository;

    public LiteratureService(LiteratureRepository literatureRepository) {
        this.literatureRepository = literatureRepository;
    }

    public LiteratureEntity findBySourceUri(String sourceUri, boolean fetchFromSource) {
        Optional<LiteratureEntity> optLiterature = literatureRepository.findBySourceUri(sourceUri);
        if (!optLiterature.isPresent() && fetchFromSource) {
            // fetch from GoogleBooks

        }
        return optLiterature.get();
    }

    /*
    public LiteratureEntity buildLiterature(Map<String, Object> gbookVol) {

        List<String> awards;
        ObjectAccessor.access(gbookVol, "volumeInfo.description", String.class).isPresent( description -> {
            for (int i = 0; i < AWARDS.length; i++) {
                if ( description.indexOf(AWARDS[i]) >= 0) {
                    awards.add(AWARDS[i]);
                }
            }

            // Double check for NOT_MATURE labeled content for words
            String maturity = ObjectAccessor.access(gbookVol, "volumeInfo.maturityRating", String.class).get();
            for (int i = 0; i < NWORDS.length; i++) {
                if (description.indexOf(GoogleBooksClient.NWORDS[i]) != = -1) {
                    gbookVol.volumeInfo.maturityRating = RATING_MATURE;
                }
            }

        }

        return new Reference({
                sourceUri:gbookVol.selfLink,
                title:gbookVol.volumeInfo.title,
                authors:gbookVol.volumeInfo.authors,
                publisher:gbookVol.volumeInfo.publisher,
                publishedDate:gbookVol.volumeInfo.publishedDate,
                description:gbookVol.volumeInfo.description,
                synopsys:gbookVol.searchInfo && gbookVol.searchInfo.textSnippet,
                identifiers:gbookVol.volumeInfo.industryIdentifiers,
                pageCount:gbookVol.volumeInfo.pageCount,
                categories:gbookVol.volumeInfo.categories,
                averageRating:gbookVol.volumeInfo.averageRating,
                ratingsCount:gbookVol.volumeInfo.ratingsCount,
                maturityRating:gbookVol.volumeInfo.maturityRating,
                language:gbookVol.volumeInfo.language,
                imageUrl:gbookVol.volumeInfo.imageLinks && gbookVol.volumeInfo.imageLinks.small,
                thumbnailImageUrl:gbookVol.volumeInfo.imageLinks && gbookVol.volumeInfo.imageLinks.thumbnail,
                awards:awards
        });

    }
    */
}

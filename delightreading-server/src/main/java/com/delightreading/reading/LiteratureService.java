package com.delightreading.reading;

import com.delightreading.common.ObjectAccessor;
import com.delightreading.reading.model.LiteratureEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

@Service
@Slf4j
public class LiteratureService {

    public static final String[] AWARDS = {"Newbery", "National Book Award", "Caldecott"};
    public static final String RATING_NOT_MATURE = "NOT_MATURE";
    public static final String RATING_MATURE = "MATURE";
    public static final String[] NWORDS = {
            "violence", "sex", "erotic", "sensual", "fetish", "f**k", "fuck", "s**t", "shit"
    };

    ObjectMapper objectMapper;
    LiteratureRepository literatureRepository;

    public LiteratureService(ObjectMapper objectMapper, LiteratureRepository literatureRepository) {
        this.objectMapper = objectMapper;
        this.literatureRepository = literatureRepository;
    }

    public Page<LiteratureEntity> findAll(Pageable pageable) {
        return literatureRepository.findAll(pageable);
    }

    public LiteratureEntity findBySourceUri(String sourceUri, boolean fetchFromSource) {
        log.debug("Retrieving literature, uri=[{}]", sourceUri);
        Optional<LiteratureEntity> optLiterature = literatureRepository.findBySourceUri(sourceUri);
        if (!optLiterature.isPresent() && fetchFromSource) {
            // fetch from GoogleBooks

            LiteratureEntity gBook = fetchFromSource(sourceUri);
            literatureRepository.save(gBook);
            return gBook;
        }
        return optLiterature.get();
    }

    public LiteratureEntity fetchFromSource(String bookUri) {
        log.debug("Fetching literature, uri=[{}]", bookUri);

        RequestEntity<HashMap<String, Object>> requestEntity = new RequestEntity<>(
                HttpMethod.GET,
                URI.create(bookUri)
        );

        RestTemplate restTemplate = new RestTemplate();
        // ParameterizedTypeReference<HashMap<String, Object>> responseType = new ParameterizedTypeReference<>() {};
        // ResponseEntity<HashMap<String, Object>> result = restTemplate.exchange(requestEntity, responseType);
        ResponseEntity<String> result = restTemplate.exchange(requestEntity, String.class);

        if (!result.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Google returned error status code: " + result.getStatusCodeValue());
        }

        log.info("Literature fetched, ", result.getBody());
        return buildLiterature(result.getBody());
    }

    public LiteratureEntity buildLiterature(String rawBookInfo) {
        try {
            HashMap<String, Object> gbookVol = this.objectMapper.readValue(rawBookInfo, new TypeReference<Map<String, Object>>() {
            });

            List<String> awards = new ArrayList<>();
            Optional<String> descriptionOpt = ObjectAccessor.access(gbookVol, "volumeInfo.description", String.class);
            if (descriptionOpt.isPresent()) {
                for (int i = 0; i < AWARDS.length; i++) {
                    if (descriptionOpt.get().contains(AWARDS[i])) {
                        awards.add(AWARDS[i]);
                    }
                }

                // Double check for NOT_MATURE labeled content for words
                // String maturityOpt = ObjectAccessor.access(gbookVol, "volumeInfo.maturityRating", String.class).get();
                for (int i = 0; i < NWORDS.length; i++) {
                    if (descriptionOpt.get().contains(NWORDS[i])) {
                        ObjectAccessor.set(gbookVol, "volumeInfo.maturityRating", RATING_MATURE);
                    }
                }
            }

            return LiteratureEntity.builder()
                    .sourceUri(ObjectAccessor.access(gbookVol, "selfLink", String.class).get())
                    .title(ObjectAccessor.access(gbookVol, "volumeInfo.title", String.class).get())
                    .authorNames(ObjectAccessor.access(gbookVol, "volumeInfo.authors", List.class).get())
                    .publisher(ObjectAccessor.access(gbookVol, "volumeInfo.publisher", String.class).get())
                    .publishedDate(ObjectAccessor.access(gbookVol, "volumeInfo.publishedDate", String.class).get())
                    .description(ObjectAccessor.access(gbookVol, "volumeInfo.description", String.class).orElse(""))
                    .synopsis(ObjectAccessor.access(gbookVol, "volumeInfo.searchInfo.textSnippet", String.class).orElse(""))
                    .identifiers(ObjectAccessor.access(gbookVol, "volumeInfo.industryIdentifiers", List.class).get())
                    .pageCount(ObjectAccessor.access(gbookVol, "volumeInfo.pageCount", Integer.class).get())
                    .categories(ObjectAccessor.access(gbookVol, "volumeInfo.categories", List.class).orElse(null))
                    .averageRating(ObjectAccessor.access(gbookVol, "volumeInfo.averageRating", Double.class).orElse(new Double(0)).floatValue())
                    .ratingsCount(ObjectAccessor.access(gbookVol, "volumeInfo.ratingsCount", Integer.class).orElse(null))
                    .maturityRating(ObjectAccessor.access(gbookVol, "volumeInfo.maturityRating", String.class).get())
                    .language(ObjectAccessor.access(gbookVol, "volumeInfo.language", String.class).get())
                    .imageUrl(ObjectAccessor.access(gbookVol, "volumeInfo.imageLinks.small", String.class).orElse(""))
                    .thumbnailImageUrl(ObjectAccessor.access(gbookVol, "volumeInfo.imageLinks.thumbnail", String.class).orElse(""))
                    .awards(awards)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("Failed reading Google books", e);
        }
    }
}

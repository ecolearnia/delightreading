package com.delightreading.reading;

import com.delightreading.reading.model.LiteratureEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiteratureRepository extends JpaRepository<LiteratureEntity, Long> {

    Optional<LiteratureEntity> findByUid(String uid);

    Optional<LiteratureEntity> findBySourceUri(String sourceUri);

    List<LiteratureEntity> findByTitleLike(String titleLike);

    Page<LiteratureEntity> findAllByOrderByAverageRatingDesc(Pageable pageable);
}

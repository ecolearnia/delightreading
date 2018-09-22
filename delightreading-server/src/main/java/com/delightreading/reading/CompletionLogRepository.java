package com.delightreading.reading;

import com.delightreading.reading.model.CompletionLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompletionLogRepository extends JpaRepository<CompletionLogEntity, Long> {

    Optional<CompletionLogEntity> findByUid(String uid);

    Optional<CompletionLogEntity> findByAccountUidAndLiteratureUid(String accountUid, String literatureUid);

    Page<CompletionLogEntity> findByAccountUid(String accountUid, Pageable pageable);
}

package com.delightreading.reading;

import com.delightreading.reading.model.ActivityLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity, Long> {

    Page<ActivityLogEntity> findByAccountUid(String accountUid, Pageable pageable);
}

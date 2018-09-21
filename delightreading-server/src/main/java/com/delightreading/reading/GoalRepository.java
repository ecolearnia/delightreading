package com.delightreading.reading;

import com.delightreading.reading.model.GoalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, Long> {

    Optional<GoalEntity> findByUid(String uid);

    Page<GoalEntity> findByAccountUid(String accountUid, Pageable pageable);
}

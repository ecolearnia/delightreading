package com.delightreading.reading;

import com.delightreading.reading.model.ActivityLogEntity;
import com.delightreading.reading.model.ActivityStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity, Long> {

    Optional<ActivityLogEntity> findByUid(String uid);
    void deleteByUid(String uid);

    Page<ActivityLogEntity> findByAccountUid(String accountUid, Pageable pageable);

    // TODO: Fix so that the return type is List<ActivityStats>
    // NOTE: '::::' is escaped and converted into '::' which in Pgsql is type casting.
    @Query(value="WITH periods as ( \n" +
            "   SELECT period \n" +
            "   FROM generate_series(:fromDate ::::date, :toDate ::::date, :period ::::interval) period \n" +
            ") \n" +
            "SELECT periods.period as period_start, (periods.period + :period ::::interval) as period_end, \n" +
            "   SUM(duration) AS activity_duration,\n" +
            "   COUNT(activity_log.sid) AS activity_count \n" +
            "FROM periods\n" +
            "LEFT JOIN activity_log \n" +
            "   ON activity_log.account_uid = :accountUid " +
            "       AND activity_log.log_timestamp >  periods.period " +
            "       AND activity_log.log_timestamp <= (periods.period + :period ::::interval) \n" +
            "   GROUP BY periods.period \n" +
            "   ORDER BY periods.period", nativeQuery = true)
    List<Map<String, Object>> timeSeries(@Param("accountUid") String accountUid, @Param("period") String period,
                                         @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);
}

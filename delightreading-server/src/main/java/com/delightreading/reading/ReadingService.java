package com.delightreading.reading;

import com.delightreading.reading.model.ActivityLogEntity;
import com.delightreading.reading.model.ActivityStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReadingService {

    ActivityLogRepository activityLogRepository;

    public ReadingService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public Page<ActivityLogEntity> findAllActivityLogs(String accountUid, Pageable pageable) {
        return this.activityLogRepository.findByAccountUid(accountUid, pageable);
    }

    public Map<String, ActivityStats> getActivityStats(String accountUid) {
        var result = new HashMap<String, ActivityStats>();
        // TODO: remove TEST
        result.put("month", ActivityStats.builder()
                .periodStart(Instant.now())
                .activityCount(3F)
                .build() );
        return result;
    }
}

package com.delightreading.reading;

import com.delightreading.reading.model.ActivityLogEntity;
import com.delightreading.reading.model.ActivityStats;
import com.delightreading.reading.model.CompletionLogEntity;
import com.delightreading.reading.model.LiteratureEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ReadingService {

    ActivityLogRepository activityLogRepository;
    CompletionLogRepository completionLogRepository;
    LiteratureService literatureService;

    public ReadingService(ActivityLogRepository activityLogRepository, CompletionLogRepository completionLogRepository, LiteratureService literatureService) {
        this.activityLogRepository = activityLogRepository;
        this.completionLogRepository = completionLogRepository;
        this.literatureService = literatureService;
    }

    @Transactional
    public ActivityLogEntity logActivity(String accountUid, ActivityLogEntity activityLog) {

        activityLog.setLogTimestamp(Instant.now());
        activityLog.setAccountUid(accountUid);

        LiteratureEntity literature = literatureService.findBySourceUri(activityLog.getLiterature().getSourceUri(), true);
        activityLog.setLiterature(literature);

        var completionLog = this.createOrUpdateCompletionLog(accountUid, literature, activityLog.getPercentageComplete());

        activityLog.setCompletionLog(completionLog);

        return this.save(activityLog);
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
                .build());
        return result;
    }


    public ActivityLogEntity save(ActivityLogEntity activityLog) {
        return this.activityLogRepository.save(activityLog);
    }

    ////////// CompletionLog {{

    public Page<CompletionLogEntity> findAllCompletionLogs(String accountUid, Pageable pageable) {
        return this.completionLogRepository.findByAccountUid(accountUid, pageable);
    }

    public CompletionLogEntity createOrUpdateCompletionLog(String accountUid, LiteratureEntity literature, Integer percentComplete) {
        CompletionLogEntity completionLog;
        Optional<CompletionLogEntity> completionLogOpt = this.completionLogRepository.findByAccountUidAndLiteratureUid(accountUid, literature.getUid());
        if (completionLogOpt.isPresent()) {
            completionLog = completionLogOpt.get();
            completionLog.setPercentageComplete(percentComplete);
        } else {
            completionLog = CompletionLogEntity.builder()
                    .literature(literature)
                    .accountUid(accountUid)
                    .percentageComplete(percentComplete)
                    .build();
        }
        if (completionLog.getPercentageComplete() != null && completionLog.getPercentageComplete() == 100) {
            completionLog.setEndDate(Instant.now());
        }
        return this.save(completionLog);
    }


    public CompletionLogEntity save(CompletionLogEntity completionLog) {
        return this.completionLogRepository.save(completionLog);
    }
}

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
import java.util.*;

import static org.apache.commons.beanutils.BeanUtils.populate;

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

    @Transactional
    public Optional<ActivityLogEntity> updateFeed(String accountUid, ActivityLogEntity feed) {
        Optional<ActivityLogEntity> activityLog = this.activityLogRepository.findByUid(feed.getUid());
        if (activityLog.isPresent()) {
            activityLog.get().setFeedBody(feed.getFeedBody());
            activityLog.get().setFeedContext(feed.getFeedContext());
            activityLog.get().setPostEmotion(feed.getPostEmotion());
            activityLog.get().setPercentageComplete(feed.getPercentageComplete());

            Optional<CompletionLogEntity> completionLogOpt = this.completionLogRepository.findByUid(activityLog.get().getCompletionLog().getUid());
            if (completionLogOpt.isPresent()) {
                completionLogOpt.get().updatePercentageComplete(activityLog.get().getPercentageComplete());
                this.completionLogRepository.save(completionLogOpt.get());
            }

            return Optional.of(activityLogRepository.save(activityLog.get()));
        }
        return Optional.empty();
    }

    public Page<ActivityLogEntity> findAllActivityLogs(String accountUid, Pageable pageable) {
        return this.activityLogRepository.findByAccountUid(accountUid, pageable);
    }

    public Map<String, ActivityStats> getActivityStats(String accountUid) {
        var result = new HashMap<String, ActivityStats>();
        // TODO: remove TEST
        result.put("month", ActivityStats.builder()
                //.periodStart(Instant.now())
                .periodStart(new Date())
                .activityCount(3F)
                .build());
        result.put("week", ActivityStats.builder()
                .periodStart(new Date())
                .activityCount(3F)
                .build());
        result.put("day", ActivityStats.builder()
                .periodStart(new Date())
                .activityCount(3F)
                .build());
        return result;
    }

    public ActivityLogEntity save(ActivityLogEntity activityLog) {
        return this.activityLogRepository.save(activityLog);
    }

    public Optional<ActivityLogEntity> delete(String activityLogUid) {
        Optional<ActivityLogEntity> foundOpt = this.activityLogRepository.findByUid(activityLogUid);
        if (foundOpt.isPresent()) {
            this.activityLogRepository.delete(foundOpt.get());
        }
        return foundOpt;
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
            completionLog.updatePercentageComplete(percentComplete);
        } else {
            completionLog = CompletionLogEntity.builder()
                    .startDate(Instant.now())
                    .literature(literature)
                    .accountUid(accountUid)
                    .percentageComplete(percentComplete)
                    .build();
        }

        return this.completionLogRepository.save(completionLog);
    }

    public Optional<CompletionLogEntity> updateCompletionLog(String accountUid, CompletionLogEntity completionLog) {
        Optional<CompletionLogEntity> completionLogOpt = this.completionLogRepository.findByUid(completionLog.getUid());
        if (completionLogOpt.isPresent()) {
            completionLogOpt.get().updatePercentageComplete(completionLog.getPercentageComplete());
            completionLogOpt.get().setPostEmotion(completionLog.getPostEmotion());

            return Optional.of(this.completionLogRepository.save(completionLogOpt.get()));
        }
        return Optional.empty();
    }


    public Optional<CompletionLogEntity> patchCompletionLog(String accountUid, String uid, Map<String, Object> fields) {

        Optional<CompletionLogEntity> completionLogOpt = this.completionLogRepository.findByUid(uid);
        if (completionLogOpt.isPresent()) {

            String[] removefields = {"sid", "uid", "createdBy", "createdAt", "accountUid", "literature"};
            Arrays.stream(removefields).forEach(fieldName -> fields.remove(fieldName));
            try {
                populate(completionLogOpt.get(), fields);
            } catch (Exception e) {
                log.warn("Error populating CompletionLog", e);
            }

            return Optional.of(this.completionLogRepository.save(completionLogOpt.get()));
        }
        return Optional.empty();
    }

    public Optional<CompletionLogEntity> deleteCompletionLog(String uid) {
        Optional<CompletionLogEntity> foundOpt = this.completionLogRepository.findByUid(uid);
        if (foundOpt.isPresent()) {
            this.completionLogRepository.delete(foundOpt.get());
        }
        return foundOpt;
    }


}

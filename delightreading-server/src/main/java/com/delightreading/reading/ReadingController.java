package com.delightreading.reading;

import com.delightreading.authsupport.AuthenticationUtils;
import com.delightreading.authsupport.JwtService;
import com.delightreading.reading.model.ActivityLogEntity;
import com.delightreading.reading.model.ActivityStats;
import com.delightreading.reading.model.CompletionLogEntity;
import com.delightreading.reading.model.LiteratureEntity;
import com.delightreading.rest.UnauthorizedException;
import com.delightreading.user.UserService;
import com.delightreading.user.model.UserAccountEntity;
import com.delightreading.user.model.UserAuthenticationEntity;
import com.delightreading.user.model.UserProfileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/api/reading/v1")
public class ReadingController {

    LiteratureService literatureService;
    ReadingService readingService;

    public ReadingController(LiteratureService literatureService, ReadingService readingService) {
        this.literatureService = literatureService;
        this.readingService = readingService;
    }

    @GetMapping(value = "/activitylogs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<ActivityLogEntity> myActivityLogs(Pageable pageable) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        Page<ActivityLogEntity> result = readingService.findAllActivityLogs(userAuth.getAccount().getUid(), pageable);

        return result;
    }

    @PostMapping(value = "/activitylogs", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ActivityLogEntity addMyActivityLog(@RequestBody ActivityLogEntity activityLog) {

        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        return this.readingService.logActivity(userAuth.getAccount().getUid(), activityLog);
    }

    @GetMapping(value = "/activitylogs/stats", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, ActivityStats> myActivityStats(Pageable pageable) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        Map<String, ActivityStats> result = readingService.getActivityStats(userAuth.getAccount().getUid());

        return result;
    }

    @GetMapping(value = "/completionlogs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<CompletionLogEntity> myCompletionLogs(Pageable pageable) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        Page<CompletionLogEntity> result = readingService.findAllCompletionLogs(userAuth.getAccount().getUid(), pageable);

        return result;
    }

    ///// Literature {{
    @GetMapping(value = "/literatures", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<LiteratureEntity> getLiteratures(Pageable pageable) {
        Page<LiteratureEntity> result = literatureService.findAll(pageable);

        return result;
    }
}

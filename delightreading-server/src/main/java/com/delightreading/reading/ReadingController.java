package com.delightreading.reading;

import com.delightreading.authsupport.AuthenticationUtils;
import com.delightreading.authsupport.JwtService;
import com.delightreading.reading.model.ActivityLogEntity;
import com.delightreading.reading.model.ActivityStats;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/api/reading/v1")
public class ReadingController {

    ReadingService readingService;

    public ReadingController(ReadingService readingService) {
        this.readingService = readingService;
    }

    @GetMapping(value = "/activitylogs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Page<ActivityLogEntity> myActivityLogs(Pageable pageable) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        Page<ActivityLogEntity> result = readingService.findAllActivityLogs(userAuth.getAccount().getUid(), pageable);

        return result;
    }


    @GetMapping(value = "/activitylogs/stats", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, ActivityStats> myActivityStats(Pageable pageable) {
        UserAuthenticationEntity userAuth = AuthenticationUtils.getUserAuthenticationOrError();

        Map<String, ActivityStats> result = readingService.getActivityStats(userAuth.getAccount().getUid());

        return result;
    }

}

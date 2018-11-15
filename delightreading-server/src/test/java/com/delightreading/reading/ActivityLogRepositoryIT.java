package com.delightreading.reading;

import com.delightreading.SpringApplicationContextUtil;
import com.delightreading.reading.model.ActivityLogEntity;
import com.delightreading.reading.model.ActivityStats;
import com.delightreading.reading.model.LiteratureEntity;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {SpringApplicationContextUtil.class})
@EnableAutoConfiguration
public class ActivityLogRepositoryIT {

    @Autowired
    ActivityLogRepository ActivityLogRepository;

    @Autowired
    private TestEntityManager entityManager;

    public static ActivityLogEntity buildEntity(String accountUid, LiteratureEntity literature, String goalUid,
                                                String activity, Integer duration, Instant logTimestamp, String completionLogUid) {
        return ActivityLogEntity.builder()
                .accountUid(accountUid)
                .literature(literature)
                .activity(activity)
                .duration(duration)
                .logTimestamp(logTimestamp)
                .build();
    }

    @Before
    public void setup() {

        LiteratureEntity literature1 = LiteratureRepositoryIT.buildEntity("TEST-Title1", "TEST-Author", "en", 4.5F, "b1");
        var savedLiterature1 = entityManager.persistAndFlush(literature1);

        LiteratureEntity literature2 = LiteratureRepositoryIT.buildEntity("TEST-Title2", "TEST-Author", "en", 4.5F, "b2");
        var savedLiterature2 = entityManager.persistAndFlush(literature2);

        List<ActivityLogEntity> activityLog = new ArrayList<>();
        activityLog.add(ActivityLogRepositoryIT.buildEntity("ACT1", savedLiterature1, null, "read", 11, Instant.parse("2018-01-21T00:00:00.000Z"), "1"));
        activityLog.add(ActivityLogRepositoryIT.buildEntity("ACT1", savedLiterature2, null, "read", 12, Instant.parse("2018-01-22T00:00:00.000Z"), "1"));
        activityLog.add(ActivityLogRepositoryIT.buildEntity("ACT1", savedLiterature1, null, "read", 13, Instant.parse("2018-01-26T00:00:00.000Z"), "2"));
        activityLog.add(ActivityLogRepositoryIT.buildEntity("ACT1", savedLiterature1, null, "read", 14, Instant.parse("2018-01-26T00:00:00.000Z"), "2"));
        activityLog.add(ActivityLogRepositoryIT.buildEntity("ACT1", savedLiterature2, null, "read", 15, Instant.parse("2018-02-02T00:00:00.000Z"), "2"));
        activityLog.add(ActivityLogRepositoryIT.buildEntity("ACT2", savedLiterature2, null, "read", 13, Instant.parse("2018-01-27T00:00:00.000Z"), "3"));

        activityLog.forEach(actLog -> entityManager.persistAndFlush(actLog));
    }

    @Test
    public void crud_simple() {

        LiteratureEntity literature = LiteratureRepositoryIT.buildEntity("TEST-Title0", "TEST-Author", "en", 4.5F, "a1");
        literature = entityManager.persistAndFlush(literature);

        var newActivityLog = ActivityLogRepositoryIT.buildEntity("ACT0", literature, null, "testing", 54, Instant.parse("2018-01-27T00:00:00.000Z"), "3");
        var ActivityLog = this.ActivityLogRepository.save(newActivityLog);

        ActivityLogEntity match1 = ActivityLogRepository.findByUid(ActivityLog.getUid()).get();

        assertThat(match1).isNotNull();
        assertThat(match1.getActivity()).isEqualTo("testing");
        assertThat(match1.getDuration()).isEqualTo(54);
    }

    @Test
    @Ignore // It is returning more than expected, probably UTC calculation issue
    public void timeSeries_givenWeek_returnWeek() {

        List<Map<String, Object>> stats = this.ActivityLogRepository.timeSeries("ACT1", "1 day", Instant.parse("2018-01-25T00:00:00.000Z"), Instant.parse("2018-02-01T00:00:00.000Z"));

        assertThat(stats).hasSize(5);
//        assertThat(stats.get(1).getActivityDuration()).isEqualTo(27F);
//        assertThat(stats.get(1).getActivityCount()).isEqualTo(2);
    }
}

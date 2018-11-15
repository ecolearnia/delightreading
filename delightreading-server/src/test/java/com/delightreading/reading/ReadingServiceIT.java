package com.delightreading.reading;

import com.delightreading.SpringApplicationContextUtil;
import com.delightreading.reading.model.CompletionLogEntity;
import com.delightreading.user.UserAccountRepositoryIT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {ActivityLogRepository.class, CompletionLogRepository.class,
        LiteratureService.class, ReadingService.class, SpringApplicationContextUtil.class})
@EnableAutoConfiguration
public class ReadingServiceIT {


    @Autowired
    ReadingService readingService;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void patchCompletionLog_whenPatchMhyRating_return() {

        CompletionLogEntity completionLog = createCompletionLog();
        var saved = entityManager.persistAndFlush(completionLog);

        var props = new HashMap<String, Object>() {{
            put("accountUid", "XYZ");
            put("myRating", 4);
        }};
        var updated = readingService.patchCompletionLog("ABC", saved.getUid(), props);

        assertThat(updated.get().getAccountUid()).isEqualTo("ABC");
        assertThat(updated.get().getMyRating()).isEqualTo(4);
    }

    public CompletionLogEntity createCompletionLog() {
        var account1 = UserAccountRepositoryIT.buildEntity(null, "TEST-Username1", "TEST-givenName1", Arrays.asList("email1a@test.com", "email1b@test.com"));
        var literature1 = LiteratureRepositoryIT.buildEntity("TEST-Title1", "TEST-Author", "en", 4.5F, "b1");
        entityManager.persistAndFlush(literature1);

        var completionLog = CompletionLogEntity.builder()
                .accountUid("ABC")
                .literature(literature1)
                .percentageComplete(50)
                .build();

        return completionLog;
    }
}

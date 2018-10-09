package com.delightreading.user.web;

import com.delightreading.SpringApplicationContextUtil;
import com.delightreading.authsupport.JwtService;
import com.delightreading.user.*;
import com.delightreading.user.model.UserAuthenticationEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@EnableWebMvc
@DataJpaTest
@EnableJpaRepositories(
        basePackages = "com.delightreading.user"
)
@EntityScan(basePackages = "com.delightreading.user", basePackageClasses = {Jsr310JpaConverters.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = {
        UserAccountRepository.class,
        UserAuthenticationRepository.class,
        UserProfileRepository.class,
        UserController.class,
        UserService.class,
        UserGroupService.class,
        JwtService.class,
        SpringApplicationContextUtil.class
}, properties = "classpath:application.yaml")
@AutoConfigureJsonTesters
@EnableAutoConfiguration
public class UserControllerIT {

    @Autowired
    UserService userService;
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;
    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        var account = UserAccountRepositoryIT.buildEntity("TEST-UserUID1", "TEST-Username1", "TEST-givenName1", Arrays.asList("email1a@test.com", "email1b@test.com"));
        var auth = UserAuthenticationRepositoryIT.buildEntity("TEST-UID1", UserAuthenticationEntity.LOCAL_PROVIDER, "TEST-F1", "pwd1", account);
        entityManager.persistAndFlush(auth);
    }

    @Test
    public void login_whenValidCredentials_returnSuccess() throws Exception {
        mockMvc.perform(
                post("/api/users/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"TEST-F1\", \"password\":\"pwd1\"}")
        ).andExpect(status().isOk()
        ).andDo(print()
        ).andExpect(
                jsonPath("$.token").isNotEmpty()
        );
    }
}

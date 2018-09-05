package com.delightreading.user.web;

import com.delightreading.authsupport.JwtService;
import com.delightreading.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/api/users/v1")
public class UserController {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";


    JwtService jwtService;
    UserService userService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, String> loginInput) {

        if (!loginInput.containsKey(USERNAME) || !loginInput.containsKey(PASSWORD) ) {
            throw new IllegalArgumentException("param username or password not found");
        }
        String username = loginInput.get(USERNAME);
        String password = loginInput.get(PASSWORD);

        var optAuth = userService.findByProviderAndProviderAccountId(UserService.LOCAL_PROVIDER, username);
        if (!optAuth.isPresent()) {
            // Return 403
            throw new IllegalStateException("Bad username or password");
        }
        if (password.equals(optAuth.get().getPassword())) {
            // Return 403
            throw new IllegalStateException("Bad username or password");
        }
        String token = jwtService.createToken(optAuth.get());

        Map<String, Object> response = new HashMap<>() {{
            put("token", token);
        }};
        return response;
    }
}

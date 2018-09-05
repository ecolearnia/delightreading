package com.delightreading.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Based on https://github.com/jgrandja/springone2017-demo
@Controller
public class MainController {
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/")
    public String index(Model model, OAuth2AuthenticationToken authentication) {
        Authentication authentic = SecurityContextHolder.getContext().getAuthentication();

        if (authentic != null && authentic instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentic;

            model.addAttribute("user", oauthToken.getDetails());
        }
        model.addAttribute("message", "Hello");
        return "index";
    }

}

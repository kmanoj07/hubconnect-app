package com.kumarmanoj.hubconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
public class HubConnectApplication {
    public static void main(String[] args) {
        SpringApplication.run(HubConnectApplication.class, args);
    }
    @RequestMapping(path="/user", method = RequestMethod.GET)
    public String user(@AuthenticationPrincipal OAuth2User principal){
        System.out.println(principal);
        return principal.getAttribute("name");
    }
}

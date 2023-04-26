package com.kumarmanoj.hubconnect;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.kumarmanoj.hubconnect.folders.Folder;
import com.kumarmanoj.hubconnect.folders.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;

@SpringBootApplication
@RestController
public class HubConnectApplication {

    @Autowired
    private FolderRepository folderRepository;

    public static void main(String[] args) {
        SpringApplication.run(HubConnectApplication.class, args);
    }

//    @RequestMapping("/user")
//    @ResponseBody
//    public String user(@AuthenticationPrincipal OAuth2User principal) {
//        System.out.println(principal);
//        return principal.getAttribute("name");
//    }

    /*
    connect to Astra-db via secure bundle
     */
    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        System.out.println(astraProperties);
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        System.out.println(bundle);
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

    @PostConstruct
    public void init() {
        folderRepository.save(new Folder("koushikkothagal", "HubConnect","red"));
        folderRepository.save(new Folder("manojCode94", "sent", "green"));
        folderRepository.save (new Folder("manojCode94", "Important", "yellow"));
    }


}

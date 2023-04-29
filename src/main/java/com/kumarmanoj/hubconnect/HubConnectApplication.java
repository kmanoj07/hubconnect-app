package com.kumarmanoj.hubconnect;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.kumarmanoj.hubconnect.email.Email;
import com.kumarmanoj.hubconnect.email.EmailRepository;
import com.kumarmanoj.hubconnect.email.EmailService;
import com.kumarmanoj.hubconnect.emaillist.EmailListItem;
import com.kumarmanoj.hubconnect.emaillist.EmailListItemKey;
import com.kumarmanoj.hubconnect.emaillist.EmailListItemRepository;
import com.kumarmanoj.hubconnect.folders.Folder;
import com.kumarmanoj.hubconnect.folders.FolderRepository;
import com.kumarmanoj.hubconnect.folders.UnreadEmailStatsRepository;
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
    @Autowired
    private EmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(HubConnectApplication.class, args);
    }

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
        folderRepository.save(new Folder("manojCode94", "Work","red"));
        folderRepository.save(new Folder("manojCode94", "Home", "green"));
        folderRepository.save (new Folder("manojCode94", "Family", "yellow"));

        for(int i=0;i<10;i++){
            emailService.sendEmail("manojCode94", Arrays.asList("manojCode94", "abc"), "Hello" + i, "body " + i);
        }

        emailService.sendEmail("abc", Arrays.asList("def", "abc"), "hello", "body");
    }


}

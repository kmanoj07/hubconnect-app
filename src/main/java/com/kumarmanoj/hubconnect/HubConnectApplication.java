package com.kumarmanoj.hubconnect;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.kumarmanoj.hubconnect.emaillist.EmailListItem;
import com.kumarmanoj.hubconnect.emaillist.EmailListItemKey;
import com.kumarmanoj.hubconnect.emaillist.EmailListItemRepository;
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
    @Autowired
    private EmailListItemRepository listItemRepository;

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
        folderRepository.save(new Folder("manojCode94", "MailBox","red"));
        folderRepository.save(new Folder("manojCode94", "Sent", "green"));
        folderRepository.save (new Folder("manojCode94", "Important", "yellow"));

        for(int i=0;i<10;i++){
            EmailListItemKey key = new EmailListItemKey();
            key.setId("manojCode94");
            key.setLabel("MailBox");
            key.setTimeUUID(Uuids.timeBased());

            EmailListItem item = new EmailListItem();
            item.setKey(key);
            item.setTo(Arrays.asList("manojCode94"));
            item.setSubject("Subject" + i);
            item.setUnread(true);

            //persist
            listItemRepository.save(item);
        }
    }


}

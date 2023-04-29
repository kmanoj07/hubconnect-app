package com.kumarmanoj.hubconnect.controllers;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.kumarmanoj.hubconnect.emaillist.EmailListItem;
import com.kumarmanoj.hubconnect.emaillist.EmailListItemRepository;
import com.kumarmanoj.hubconnect.folders.*;
import io.netty.util.internal.StringUtil;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class HubConnectController {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailListItemRepository listItemRepository;

    // check if the user is authenticated - decide page rendering
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(@RequestParam(required = false) String folder,
                       @AuthenticationPrincipal OAuth2User principal,
                       Model model) {
        if(principal == null  || !StringUtils.hasText(principal.getAttribute("name"))){
            return "index";
        }
        //user logged in
        String userId = principal.getAttribute("login");
        // Folder for specific user
        List<Folder> userFolders = folderRepository.findAllById(userId);
        //put the userFolders in model to access on  template
        model.addAttribute("userFolders", userFolders);
        //Default Folder for every user
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        //put the userFolders in model to access on  template
        model.addAttribute("defaultFolders", defaultFolders);
        //read unreadcount
        model.addAttribute("unreadStats", folderService.mapCountToLabels(userId));
        model.addAttribute("user", principal.getAttribute("name"));


        // Fetch emails by user's folder
        if(!StringUtils.hasText(folder)) {
            folder = "Inbox";
        }
        List<EmailListItem>  emailListItems =  listItemRepository.findAllByKey_IdAndKey_Label(userId, folder);
        PrettyTime prettyTime = new PrettyTime();

        emailListItems.stream().forEach(emailItem -> {
            UUID timeUuID = emailItem.getKey().getTimeUUID();
            Date emailDateTime = new Date(Uuids.unixTimestamp(timeUuID));
            emailItem.setAgoTimeString(prettyTime.format(emailDateTime));
        });

        model.addAttribute("emailLists", emailListItems);
        model.addAttribute("folderName", folder);

       return "hubconnect-page";
    }
}

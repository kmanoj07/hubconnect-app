package com.kumarmanoj.hubconnect.controllers;

import com.kumarmanoj.hubconnect.email.Email;
import com.kumarmanoj.hubconnect.email.EmailRepository;
import com.kumarmanoj.hubconnect.emaillist.EmailListItem;
import com.kumarmanoj.hubconnect.emaillist.EmailListItemKey;
import com.kumarmanoj.hubconnect.emaillist.EmailListItemRepository;
import com.kumarmanoj.hubconnect.folders.Folder;
import com.kumarmanoj.hubconnect.folders.FolderRepository;
import com.kumarmanoj.hubconnect.folders.FolderService;
import com.kumarmanoj.hubconnect.folders.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EmailViewController {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired private EmailListItemRepository emailListItemRepository;
    @Autowired private UnreadEmailStatsRepository emailStatsRepository;

    @RequestMapping(path = "/emails/{id}", method = RequestMethod.GET)
    public String emailView(
            @RequestParam String folder,
            @AuthenticationPrincipal OAuth2User principal,
                            @PathVariable UUID id,
                            Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("name"))) {
            return "index";
        }
//        System.out.println(principal);
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

        //Get Emails
        Optional<Email> optionalEmail= emailRepository.findById(id);
        if(optionalEmail.isEmpty()){
            return "hubconnect-page";
        }
//        System.out.println("Email " + optionalEmail.get());
        Email email = optionalEmail.get();
        String toIds = String.join(", " , email.getTo());

        //check if user is allowed to see the email
        if(!userId.equals(email.getFrom()) && !email.getTo().contains(userId)){
            return "redirect:/";
        }

        model.addAttribute("email", email);
        model.addAttribute("toIds", toIds);
        model.addAttribute("user", principal.getAttribute("name"));

        EmailListItemKey key = new EmailListItemKey();
        key.setId(userId);
        key.setLabel(folder);
        key.setTimeUUID(email.getTimeUUID());

        Optional<EmailListItem> optionalEmailListItem = emailListItemRepository.findById(key);
        if(optionalEmailListItem.isPresent()){
            EmailListItem emailListItem =  optionalEmailListItem.get();
            if(emailListItem.isUnread()){
                emailListItem.setUnread(false);
                emailListItemRepository.save(emailListItem);
                emailStatsRepository.decrementUnreadCount(userId, folder);
            }
        }
        model.addAttribute("unreadStats", folderService.mapCountToLabels(userId));

        return "email-page";
    }
}

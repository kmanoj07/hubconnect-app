package com.kumarmanoj.hubconnect.controllers;

import com.kumarmanoj.hubconnect.email.Email;
import com.kumarmanoj.hubconnect.email.EmailRepository;
import com.kumarmanoj.hubconnect.emaillist.EmailListItemRepository;
import com.kumarmanoj.hubconnect.folders.Folder;
import com.kumarmanoj.hubconnect.folders.FolderRepository;
import com.kumarmanoj.hubconnect.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(path = "/emails/{id}", method = RequestMethod.GET)
    public String emailView(@AuthenticationPrincipal OAuth2User principal,
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
        model.addAttribute("email", email);
        model.addAttribute("toIds", toIds);
        return "email-page";
    }
}

package com.kumarmanoj.hubconnect.controllers;

import com.kumarmanoj.hubconnect.email.Email;
import com.kumarmanoj.hubconnect.email.EmailRepository;
import com.kumarmanoj.hubconnect.email.EmailService;
import com.kumarmanoj.hubconnect.folders.Folder;
import com.kumarmanoj.hubconnect.folders.FolderRepository;
import com.kumarmanoj.hubconnect.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ComposeController {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailService emailService;
    @Autowired private EmailRepository emailRepository;

    @RequestMapping(path = "/compose", method = RequestMethod.GET)
    public String getComposePage(
            @RequestParam(required = false) String to,
            @RequestParam(required = false) UUID id,
            @AuthenticationPrincipal OAuth2User principal,
            Model model) {
        if(principal == null  || !StringUtils.hasText(principal.getAttribute("name"))){
            return "index";
        }
        //System.out.println(principal);
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
        model.addAttribute("unreadStats", folderService.mapCountToLabels(userId));
        model.addAttribute("user", principal.getAttribute("name"));

        List<String> uniqueToIds = getStrings(to);
        model.addAttribute("toIds", String.join(", ", uniqueToIds));

        //Get Emails
        Optional<Email> optionalEmail= emailRepository.findById(id);
        if(optionalEmail.isPresent()){
            Email email = optionalEmail.get();
            if(emailService.doesHaveAccess(email, userId)) {
                model.addAttribute("subject", emailService.getReplySubject(email.getSubject()));
                model.addAttribute("body", emailService.getReplyBody(email));
            }
        }

        return "compose-page";
    }

    private static List<String> getStrings(String to) {
        if(!StringUtils.hasText(to)) {
            return new ArrayList<String>();
        }
        String[] splitIds = to.split(",");
        List<String> uniqueToIds = Arrays.asList(splitIds)
                .stream()
                .map(id -> StringUtils.trimWhitespace(id))
                .filter(id -> StringUtils.hasText(id))
                .distinct()
                .collect(Collectors.toList());
        return uniqueToIds;
    }

    @PostMapping(path = "/sendEmail")
    public ModelAndView sendEmail(@AuthenticationPrincipal OAuth2User principal,
                                  @RequestBody MultiValueMap<String, String> formData)
    {
        if(principal == null  || !StringUtils.hasText(principal.getAttribute("name"))){
            return new ModelAndView("redirect:/"); // redirect to inbox or home
        }
        String from = principal.getAttribute("login");
        List<String> toIds = getStrings(formData.getFirst("toIds"));
        String subject = formData.getFirst("subject");
        String body = formData.getFirst("body");

        //send out an email
        // A new Email entry (emailRepository)
        // That messages shows each recipient in Inbox
        emailService.sendEmail(from, toIds, subject, body);
        return new ModelAndView("redirect:/");

    }

}

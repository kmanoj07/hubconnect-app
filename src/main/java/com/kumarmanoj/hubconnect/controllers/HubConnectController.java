package com.kumarmanoj.hubconnect.controllers;

import com.kumarmanoj.hubconnect.folders.Folder;
import com.kumarmanoj.hubconnect.folders.FolderRepository;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.List;
import java.util.Objects;

@Controller
public class HubConnectController {
    @Autowired
    private FolderRepository folderRepository;
    // check if the user is authenticated - decide page rendering
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if(principal == null  || !StringUtils.hasText(principal.getAttribute("name"))){
            return "index";
        }
        System.out.println(principal);
        //user logged in
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        //put the userFolders in model to access on  template
        model.addAttribute("userFolders", userFolders);
       return "hubconnect-page";
    }
}

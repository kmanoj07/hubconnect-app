package com.kumarmanoj.hubconnect.folders;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FolderService {
    public List<Folder> fetchDefaultFolders(String userId) {
        // Always return the default folder
        return Arrays.asList(
                new Folder(userId, "Inobx", "white"),
                new Folder(userId, "Sent Items", "green"),
                new Folder(userId, "Important", "blue")
        );
    }
}

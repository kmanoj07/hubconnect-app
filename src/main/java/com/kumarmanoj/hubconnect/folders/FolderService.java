package com.kumarmanoj.hubconnect.folders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FolderService {
    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    public List<Folder> fetchDefaultFolders(String userId) {
        // Always return the default folder
        return Arrays.asList(
                new Folder(userId, "Inbox", "blue"),
                new Folder(userId, "Sent", "green"),
                new Folder(userId, "Important", "blue")
        );
    }
    public Map<String, Integer> mapCountToLabels(String userId) {
        List<UnreadEmailStats> stats = unreadEmailStatsRepository.findAllById(userId);
        return stats.stream()
                .collect(Collectors.toMap(UnreadEmailStats::getLabel, UnreadEmailStats::getUnreadCount));
    }
}

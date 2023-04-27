package com.kumarmanoj.hubconnect.email;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.kumarmanoj.hubconnect.emaillist.EmailListItem;
import com.kumarmanoj.hubconnect.emaillist.EmailListItemKey;
import com.kumarmanoj.hubconnect.emaillist.EmailListItemRepository;
import com.kumarmanoj.hubconnect.folders.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired private EmailRepository emailRepository;
    @Autowired private EmailListItemRepository emailListItemRepository;
    @Autowired private UnreadEmailStatsRepository emailStatsRepository;


    public void sendEmail(String from, List<String> to, String subject, String body) {
        // create a new email
        Email email = new Email();
        email.setFrom(from);
        email.setTo(to);
        email.setSubject(subject);
        email.setBody(body);
        email.setTimeUUID(Uuids.timeBased());
        // persisted a new email
        emailRepository.save(email);

        // Email for each recipient inbox
        to.forEach(toId -> {
           EmailListItem item = createEmailListItem(to, subject, email, toId, "Inbox");
           emailListItemRepository.save(item);
           emailStatsRepository.incrementUnreadCount(toId, "Inbox");
        });

        EmailListItem sentItemEntry =  createEmailListItem(to, subject, email, from, "Sent");
        sentItemEntry.setUnread(false);
        emailListItemRepository.save(sentItemEntry);
    }

    private EmailListItem createEmailListItem(List<String> to, String subject, Email email, String toId, String folder) {
        EmailListItemKey key = new EmailListItemKey();
        key.setId(toId);
        key.setLabel(folder);
        key.setTimeUUID(email.getTimeUUID());
        EmailListItem item = new EmailListItem();
        item.setKey(key);
        item.setTo(to);
        item.setSubject(subject);
        item.setUnread(true);
        return  item;
    }
}

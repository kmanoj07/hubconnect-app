package com.kumarmanoj.hubconnect.folders;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnreadEmailStatsRepository extends CassandraRepository<UnreadEmailStats, String> {
    List<UnreadEmailStats> findAllById(String id);
    @Query("UPDATE unread_email_stats SET unreadCount = unreadCount + 1 WHERE user_id = ?0 AND label = ?1")
    void incrementUnreadCount(String userId, String label);
    @Query("UPDATE unread_email_stats SET unreadCount = unreadCount - 1 WHERE user_id = ?0 AND label = ?1")
    void decrementUnreadCount(String userId, String label);
}

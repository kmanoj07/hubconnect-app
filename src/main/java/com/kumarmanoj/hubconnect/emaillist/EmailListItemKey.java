package com.kumarmanoj.hubconnect.emaillist;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@PrimaryKeyClass
public class EmailListItemKey {
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;
    @PrimaryKeyColumn(name = "label", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String label;
    @PrimaryKeyColumn(name = "created_time_uuid", ordinal = 0, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID timeUUID;


    public String getId() {
        return id;
    }

    public EmailListItemKey() {
    }

    public EmailListItemKey(String id, String label, UUID timeUUID) {
        this.id = id;
        this.label = label;
        this.timeUUID = timeUUID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public UUID getTimeUUID() {
        return timeUUID;
    }

    public void setTimeUUID(UUID timeUUID) {
        this.timeUUID = timeUUID;
    }
}

package com.kumarmanoj.hubconnect.emaillist;

import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface EmailListItemRepository extends CassandraRepository<EmailListItem, EmailListItemKey> {

    //List<EmailListItem> findAllById(EmailListItemKey id);


    List<EmailListItem> findAllByKey_IdAndKey_Label(String id, String label);
}

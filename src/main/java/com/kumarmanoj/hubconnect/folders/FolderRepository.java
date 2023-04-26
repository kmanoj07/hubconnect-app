package com.kumarmanoj.hubconnect.folders;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends CassandraRepository<Folder, String> {

    //spring data library to get multiple records on id
    List<Folder> findAllById(String id);
}

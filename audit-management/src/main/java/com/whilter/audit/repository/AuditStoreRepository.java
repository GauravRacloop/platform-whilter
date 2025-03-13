package com.whilter.audit.repository;


import com.whilter.audit.model.AuditStore;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditStoreRepository extends MongoRepository<AuditStore, String> {
}

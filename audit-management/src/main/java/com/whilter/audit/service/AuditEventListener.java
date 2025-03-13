package com.whilter.audit.service;


import com.whilter.audit.model.AuditStore;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditEventListener {

//    @Autowired
//    private AuditStoreService auditStoreService;
    @Autowired
    private Gson gson;

    @KafkaListener(topics = "${application.topic.audit.queue}",
            groupId = "${application.topic.audit.group-id}",
            containerFactory = "auditEventConcurrentKafkaListenerContainerFactory")
    public void listenAuditEventTopic(String auditEventString) {
        log.info("Listening message from topic : audit-event");
        log.info("message received : " + auditEventString);
        try {
            AuditStore auditStore = gson.fromJson(auditEventString, AuditStore.class);
//            auditStoreService.createAudit(auditStore);
        } catch (Exception e) {
            log.error("Error processing event : " + auditEventString, e.getMessage(), e);
        }
    }
}

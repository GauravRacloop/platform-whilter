package com.whilter.audit.service;

import com.whilter.audit.model.AuditStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditEventKafkaPublisher {

    private static final ThreadLocal<ObjectMapper> OBJECT_MAPPER_THREAD_LOCAL = ThreadLocal.withInitial(ObjectMapper::new);

    @Value("${application.topic.audit.queue}")
    private String auditEventTopic;

    @Autowired
    private KafkaTemplate<String, String> auditEventKafkaTemplate;

    public void sendAuditEvent(AuditStore auditStore) {
        log.info("Sending message to topic : " + auditEventTopic);
        try {
            String json = OBJECT_MAPPER_THREAD_LOCAL.get().writeValueAsString(auditStore);
            auditEventKafkaTemplate.send(auditEventTopic, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}

package com.whilter.audit.controller;

import com.whilter.audit.model.*;


import com.whilter.audit.service.AuditEventKafkaPublisher;
import com.whilter.audit.service.AuditStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@Slf4j
@RequestMapping("/v3/api/am/audit")
public class AuditStoreController {

    @Autowired
    private AuditEventKafkaPublisher auditEventKafkaPublisher;

    private final AuditStoreService auditStoreService;

    public AuditStoreController(AuditStoreService auditStoreService) {
        this.auditStoreService = auditStoreService;
    }
//
//    @GetMapping("/getAllAuditMetaFiltered")
    public List<AuditStore> getAllAuditMetaFiltered(@RequestBody AuditStoreFilter auditStoreFilter) {
        return auditStoreService.getAllAuditFiltered(auditStoreFilter, false);
    }
//
//    @GetMapping("/getAllAuditFiltered")
//    public List<AuditStore> getAllAuditFiltered(@BeanParam AuditStoreMetaMapFilter auditStoreMetaMapFilter) {
//        AuditStoreFilter auditStoreFilter = new AuditStoreFilter(auditStoreMetaMapFilter);
//        return auditStoreService.getAllAuditFiltered(auditStoreFilter, false);
//    }
//
//    @GetMapping("/getDeviceStatusUpdateAudit")
//    public List<DeviceStatusAuditDto> getDeviceStatusUpdateAudit(@BeanParam AuditStoreDeviceStatusFilter auditStoreDeviceStatusFilter) {
//        AuditStoreFilter auditStoreFilter = auditStoreService.getAuditStoreDeviceStatusFilter(auditStoreDeviceStatusFilter);
//        return auditStoreService.getDeviceStatusUpdateAudit(auditStoreFilter, false);
//    }
//
//    @GetMapping("/exportDeviceStatusUpdateAudit")
//    public void exportDeviceStatusUpdateAudit(@BeanParam AuditStoreDeviceStatusFilter auditStoreDeviceStatusFilter,
//                                              HttpServletResponse response) throws IOException {
//        AuditStoreFilter auditStoreFilter = auditStoreService.getAuditStoreDeviceStatusFilter(auditStoreDeviceStatusFilter);
//        auditStoreService.exportDeviceStatusUpdateAudit(auditStoreFilter, response);
//    }
//
//    @GetMapping("/getVehicleTrackingAudit")
//    public List<VehicleTrackingAuditDto> getVehicleTrackingAudit(@BeanParam AuditStoreVehicleTrackingFilter auditStoreVehicleTrackingFilter) {
//        AuditStoreFilter auditStoreFilter = auditStoreService.getAuditStoreVehicleTrackingFilter(auditStoreVehicleTrackingFilter);
//        return auditStoreService.getVehicleTrackingAudit(auditStoreFilter, false);
//    }
//
//    @GetMapping("/getLatestVehicleTrackingAudit")
//    public VehicleTrackingAuditDto getLatestVehicleTrackingAudit(@BeanParam AuditStoreVehicleTrackingFilter auditStoreVehicleTrackingFilter) {
//        AuditStoreFilter auditStoreFilter = auditStoreService.getAuditStoreVehicleTrackingFilter(auditStoreVehicleTrackingFilter);
//        return auditStoreService.getLatestVehicleTrackingAudit(auditStoreFilter, false);
//    }
//
//    @GetMapping("/exportVehicleTrackingAudit")
//    public void exportVehicleTrackingAudit(@BeanParam AuditStoreVehicleTrackingFilter auditStoreVehicleTrackingFilter,
//                                           HttpServletResponse response) throws IOException {
//        AuditStoreFilter auditStoreFilter = auditStoreService.getAuditStoreVehicleTrackingFilter(auditStoreVehicleTrackingFilter);
//        auditStoreService.exportVehicleTrackingAudit(auditStoreFilter, response);
//    }
//
//    /* @GetMapping("/getAll")
//    public List<AuditStore> getAllAudit() {
//        return auditStoreService.getAllAudit();
//    }*/
//
//    /*@PostMapping("/createAudit")
//    public AuditStore createAudit(@RequestBody AuditStore auditStore) {
//        return auditStoreService.createAudit(auditStore);
//    }*/
//
//    /*@GetMapping("/createAudit")
//    public void createAudit() {
//        AuditStore auditStore = new AuditStore();
//        auditStore.setEntity("USER");
//        auditEventKafkaPublisher.sendAuditEvent(auditStore);
//    }*/


}

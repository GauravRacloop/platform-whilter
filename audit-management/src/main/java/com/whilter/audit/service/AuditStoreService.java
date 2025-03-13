package com.whilter.audit.service;


import com.whilter.audit.dto.DeviceStatusAuditDto;
import com.whilter.audit.exception.BadClientDataException;
import com.whilter.audit.model.AuditStore;
import com.whilter.audit.model.AuditStoreDeviceStatusFilter;
import com.whilter.audit.model.AuditStoreFilter;
import com.whilter.audit.repository.AuditStoreRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class AuditStoreService {

    private final MongoTemplate mongoTemplate;
    private final AuditStoreRepository auditStoreRepository;

    @Value("${application.audit.period}")
    private long auditPeriod;

    public AuditStoreService(MongoTemplate mongoTemplate, AuditStoreRepository auditStoreRepository) {
        this.mongoTemplate = mongoTemplate;
        this.auditStoreRepository = auditStoreRepository;
    }

    public List<AuditStore> getAllAudit() {
        return auditStoreRepository.findAll();
    }

    public AuditStore createAudit(AuditStore auditStore) {
        List<String> errorList = validateCreate(auditStore);
        if (errorList.size() > 0) {
            log.error("EAudit data is incomplete in Audit - " + auditStore.toString());
            log.error(errorList.toString());
            return null;
        }
        auditStore.setCreatedDate(new Date().getTime());
        return auditStoreRepository.save(auditStore);
    }

    private List<String> validateCreate(AuditStore auditStore) {
        List<String> errorList = new ArrayList<>();
        if (StringUtils.isEmpty(auditStore.getAuditCategory())) {
            errorList.add("AuditCategory is empty");
        }
        /*if (StringUtils.isEmpty(auditStore.getCreatedDate()) || auditStore.getCreatedDate() == 0) {
            errorList.add("CreatedDate is empty");
        }*/
        return errorList;
    }

    public List<AuditStore> getAllAuditFiltered(AuditStoreFilter auditStoreFilter, boolean isLimited) {
        List<AuditStore> auditStoreList = new ArrayList<>();
        if (auditStoreFilter != null) {

            Pageable pageable = null;

            if (isLimited) {
                if (auditStoreFilter.getCreatedDateFrom() != null && auditStoreFilter.getCreatedDateTo() != null) {
                    LocalDate from = Instant.ofEpochMilli(auditStoreFilter.getCreatedDateFrom()).atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate to = Instant.ofEpochMilli(auditStoreFilter.getCreatedDateTo()).atZone(ZoneId.systemDefault()).toLocalDate();
                    from = from.plusMonths(auditPeriod);
                    if (from.isBefore(to)) {
                        throw new BadClientDataException("Audit report can be fetched for a limited period of - " + auditPeriod + " months.");
                    }
                } else {
                    throw new BadClientDataException("Audit report can be fetched for a limited period of - " + auditPeriod + " months.");
                }
            } else {
                if (auditStoreFilter.getSize() > 0 && auditStoreFilter.getOffset() > -1) {
//                    pageable = PageRequest.of(auditStoreFilter.getOffset(), auditStoreFilter.getSize(), new Sort(Sort.Direction.DESC, "createdDate"));
                    pageable = PageRequest.of(auditStoreFilter.getOffset(), auditStoreFilter.getSize());

                } else {
                    throw new BadClientDataException("Pagination data is required for search");
                }
            }

            List<Criteria> criteriaList = new ArrayList<>();
            Map<String, String> metaMap = auditStoreFilter.getMetaMap();
            if (metaMap != null && metaMap.size() > 0) {
                for (String key : metaMap.keySet()) {
                    String queryField = "metaMap." + key;
                    criteriaList.add(Criteria.where(queryField).is(metaMap.get(key)));
                }
            }
            if (!isEmptyString(auditStoreFilter.getAuditCategory())) {
                criteriaList.add(Criteria.where("auditCategory").is(auditStoreFilter.getAuditCategory()));
            }
            if (auditStoreFilter.getCreatedDateFrom() != null && auditStoreFilter.getCreatedDateTo() != null) {
                criteriaList.add(Criteria.where("createdDate").gte(auditStoreFilter.getCreatedDateFrom()));
                criteriaList.add(Criteria.where("createdDate").lte(auditStoreFilter.getCreatedDateTo()));
            } else if (auditStoreFilter.getCreatedDateFrom() != null){
                criteriaList.add(Criteria.where("createdDate").gte(auditStoreFilter.getCreatedDateFrom()));
            } else if (auditStoreFilter.getCreatedDateTo() != null){
                criteriaList.add(Criteria.where("createdDate").lte(auditStoreFilter.getCreatedDateTo()));
            }
            Criteria criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
            Query q = new Query();
            q.addCriteria(criteria);
            if (pageable != null) {
                q.with(pageable);
            }
            auditStoreList = mongoTemplate.find(q, AuditStore.class);
        }
        return auditStoreList;
    }

    public List<DeviceStatusAuditDto> getDeviceStatusUpdateAudit(AuditStoreFilter auditStoreFilter, boolean isLimited) {
        List<DeviceStatusAuditDto> deviceStatusAuditDtos = new ArrayList<>();
        List<AuditStore> auditStoreList = getAllAuditFiltered(auditStoreFilter, isLimited);
        for (AuditStore auditStore : auditStoreList) {
            DeviceStatusAuditDto deviceStatusAuditDto = auditStore.getDeviceStatusAuditDto();
            deviceStatusAuditDtos.add(deviceStatusAuditDto);
        }
        return deviceStatusAuditDtos;
    }

    /*public void exportDeviceStatusUpdateAudit(AuditStoreFilter auditStoreFilter, HttpServletResponse response) throws IOException {
        List<DeviceStatusAuditDto> deviceStatusAuditDtos = getDeviceStatusUpdateAudit(auditStoreFilter, true);
        if (deviceStatusAuditDtos.size() == 0) {
            throw new BadClientDataException("Audit record not found");
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDateTime now = LocalDateTime.now();
        response.setContentType("application/octet-stream");
        String filename = "DeviceStatusAudit" + dtf.format(now) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        String[] excludedColumn = {};
        ByteArrayInputStream stream = ExcelFileWriter.writeToExcel(filename, deviceStatusAuditDtos, excludedColumn);
        IOUtils.copy(stream, response.getOutputStream());
    }*/

    public void exportDeviceStatusUpdateAudit(AuditStoreFilter auditStoreFilter, HttpServletResponse response) throws IOException {
        List<AuditStore> auditStoreList = getAllAuditFiltered(auditStoreFilter, true);
        if (auditStoreList.size() == 0) {
            throw new BadClientDataException("Audit record not found");
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet excelSheet = workbook.createSheet("Status Audit");
        setExcelHeaderForDeviceStatusAuditReport(excelSheet);
        setExcelRowsForDeviceStatusAuditReport(excelSheet, auditStoreList);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDateTime now = LocalDateTime.now();
        response.setContentType("application/octet-stream");
        String filename = "DeviceStatusAudit" + dtf.format(now) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        OutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setExcelHeaderForDeviceStatusAuditReport(XSSFSheet excelSheet) {
        Row excelHeader = excelSheet.createRow(0);
        excelHeader.createCell(0).setCellValue("deviceId");
        excelHeader.createCell(1).setCellValue("serialNumber");
        excelHeader.createCell(2).setCellValue("imeiNumber");
        excelHeader.createCell(3).setCellValue("oldStatus");
        excelHeader.createCell(4).setCellValue("newStatus");
        excelHeader.createCell(5).setCellValue("updatedDate");
        excelHeader.createCell(6).setCellValue("updatedBy");
    }

    public void setExcelRowsForDeviceStatusAuditReport(XSSFSheet excelSheet, List<AuditStore> auditStores) {
        int record = 1;
        for (AuditStore auditStore : auditStores) {
            Row excelRow = excelSheet.createRow(record++);
            excelRow.createCell(0).setCellValue(auditStore.getMetaMap().get("deviceId"));
            excelRow.createCell(1).setCellValue(auditStore.getMetaMap().get("serialNumber"));
            excelRow.createCell(2).setCellValue(auditStore.getMetaMap().get("imeiNumber"));
            excelRow.createCell(3).setCellValue(auditStore.getMetaMap().get("oldStatus"));
            excelRow.createCell(4).setCellValue(auditStore.getMetaMap().get("newStatus"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String dateString = simpleDateFormat.format(auditStore.getCreatedDate());
            excelRow.createCell(5).setCellValue(dateString);
            excelRow.createCell(6).setCellValue(auditStore.getMetaMap().get("user"));
        }
    }

    public AuditStoreFilter getAuditStoreDeviceStatusFilter(AuditStoreDeviceStatusFilter auditStoreDeviceStatusFilter) {
        AuditStoreFilter auditStoreFilter = new AuditStoreFilter();
        auditStoreFilter.setCreatedDateFrom(auditStoreDeviceStatusFilter.getCreatedDateFrom());
        auditStoreFilter.setCreatedDateTo(auditStoreDeviceStatusFilter.getCreatedDateTo());
        auditStoreFilter.setAuditCategory("Device_Status_Update");
        auditStoreFilter.setOffset(auditStoreDeviceStatusFilter.getOffset());
        auditStoreFilter.setSize(auditStoreDeviceStatusFilter.getSize());
        if (!isEmptyString(auditStoreDeviceStatusFilter.getDeviceId())) {
            auditStoreFilter.getMetaMap().put("deviceId", auditStoreDeviceStatusFilter.getDeviceId());
        }
        if (!isEmptyString(auditStoreDeviceStatusFilter.getOldStatus())) {
            auditStoreFilter.getMetaMap().put("oldStatus", auditStoreDeviceStatusFilter.getOldStatus());
        }
        if (!isEmptyString(auditStoreDeviceStatusFilter.getNewStatus())) {
            auditStoreFilter.getMetaMap().put("newStatus", auditStoreDeviceStatusFilter.getNewStatus());
        }
        if (!isEmptyString(auditStoreDeviceStatusFilter.getSerialNumber())) {
            auditStoreFilter.getMetaMap().put("serialNumber", auditStoreDeviceStatusFilter.getSerialNumber());
        }
        if (!isEmptyString(auditStoreDeviceStatusFilter.getImeiNumber())) {
            auditStoreFilter.getMetaMap().put("imeiNumber", auditStoreDeviceStatusFilter.getImeiNumber());
        }
        if (!isEmptyString(auditStoreDeviceStatusFilter.getUser())) {
            auditStoreFilter.getMetaMap().put("user", auditStoreDeviceStatusFilter.getUser());
        }
        return auditStoreFilter;
    }


    public void exportVehicleTrackingAudit(AuditStoreFilter auditStoreFilter, HttpServletResponse response) throws IOException {
        List<AuditStore> auditStoreList = getAllAuditFiltered(auditStoreFilter, true);
        if (auditStoreList.size() == 0) {
            throw new BadClientDataException("Audit record not found");
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet excelSheet = workbook.createSheet("Tracking Audit");
        setExcelHeaderForVehicleTrackingAuditReport(excelSheet);
        setExcelRowsForVehicleTrackingAuditReport(excelSheet, auditStoreList);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDateTime now = LocalDateTime.now();
        response.setContentType("application/octet-stream");
        String filename = "VehicleTrackingAudit" + dtf.format(now) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        OutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setExcelHeaderForVehicleTrackingAuditReport(XSSFSheet excelSheet) {
        Row excelHeader = excelSheet.createRow(0);
        excelHeader.createCell(0).setCellValue("S.No");
        excelHeader.createCell(1).setCellValue("vehicle Chassis No");
        excelHeader.createCell(2).setCellValue("Request Date");
        excelHeader.createCell(3).setCellValue("Requester Email");
        excelHeader.createCell(4).setCellValue("Tracking Ends On");
    }

    public void setExcelRowsForVehicleTrackingAuditReport(XSSFSheet excelSheet, List<AuditStore> auditStores) {
        int record = 1;
        for (AuditStore auditStore : auditStores) {
            int sNo = record;
            Row excelRow = excelSheet.createRow(record++);
            excelRow.createCell(0).setCellValue(sNo);
            excelRow.createCell(1).setCellValue(auditStore.getMetaMap().get("vehicleChassisNumber"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            String dateString = simpleDateFormat.format(auditStore.getCreatedDate());
            excelRow.createCell(2).setCellValue(dateString);
            excelRow.createCell(3).setCellValue(auditStore.getMetaMap().get("user"));
            if (auditStore.getMetaMap() != null &&
                    auditStore.getMetaMap().containsKey("trackingEndsOn") &&
                    StringUtils.isNotEmpty(auditStore.getMetaMap().get("trackingEndsOn"))) {
                String trackingEndsOn = simpleDateFormat.format(Long.valueOf(auditStore.getMetaMap().get("trackingEndsOn")));
                excelRow.createCell(4).setCellValue(trackingEndsOn);
            } else {
                excelRow.createCell(4).setCellValue("");
            }
        }
    }


    private boolean isEmptyString(String value) {
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(value.trim())) {
            return true;
        }
        return false;
    }
}

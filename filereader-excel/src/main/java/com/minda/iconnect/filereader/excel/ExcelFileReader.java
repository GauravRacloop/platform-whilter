package com.minda.iconnect.filereader.excel;

import com.minda.iconnect.platform.core.internal.ResponseCodeImpl;
import com.whilter.exception.ApplicationException;
import com.whilter.filereader.ExcelFileReaderEndpoint;
import com.whilter.filereader.FileReaderService;
import com.minda.iconnect.platform.filereader.FileReaderEndpoint;
import com.minda.iconnect.platform.filereader.ResultSet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by mayank on 30/07/19 11:01 AM.
 */
public class ExcelFileReader implements FileReaderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelFileReader.class);

    private final ExcelFileReaderEndpoint excelFileReaderEndpoint;

    public ExcelFileReader(FileReaderEndpoint excelFileReaderEndpoint) {
        this.excelFileReaderEndpoint = (ExcelFileReaderEndpoint) excelFileReaderEndpoint;
    }

    @Override
    public ResultSet read(InputStream input) {

        String sheetName = excelFileReaderEndpoint.getSheetName();
        Workbook workbook = null;

        try {
            workbook = new XSSFWorkbook(input);
        } catch (IOException ex) {
            LOGGER.error("Workbook not of XLSX format. Trying for XLS format");
        }

        if (workbook == null) {
            try {
                workbook = new HSSFWorkbook(input);
            } catch (IOException ex) {
                throw new ApplicationException(new ResponseCodeImpl("400", "Bad Request"), "Excel File should be of XLSX or XLS format");
            }
        }

        Sheet inputSheet;
        if (sheetName == null || !sheetName.trim().isEmpty()) {
            inputSheet = workbook.getSheetAt(0);
        } else {
            inputSheet = workbook.getSheet(sheetName);
        }

        if (inputSheet == null) {
            throw new ApplicationException(new ResponseCodeImpl("400", "Bad Request"), sheetName + " Sheet Missing");
        }

        HashMap<String, Integer> columnIndexMap = new HashMap<>();
        Iterator<Cell> excelHeaders = inputSheet.getRow(0).iterator();
        Collection<String> headers = new ArrayList<>();
        List<com.minda.iconnect.platform.filereader.Row> rows = new ArrayList<>();


        int counter = 0;
        while (excelHeaders.hasNext()) {
            Cell cell = excelHeaders.next();
            headers.add(cell.getStringCellValue().toLowerCase());
            columnIndexMap.put(cell.getStringCellValue().toLowerCase(), counter++);
        }

        counter = 0;
        Iterator<org.apache.poi.ss.usermodel.Row> sheetRowIterator = inputSheet.rowIterator();
        while (sheetRowIterator.hasNext()) {
            if (counter == 0) {
                counter++;
                sheetRowIterator.next(); // ignore header line
                continue;
            }

            org.apache.poi.ss.usermodel.Row row = sheetRowIterator.next();

            if (isRowEmpty(row, columnIndexMap)) {
                continue;
            }

            Map<String, String> columnNameValueMap = new HashMap<>();
            Map<Integer, String> columnIndexValueMap = new HashMap<>();

            for (String header : headers) {
                Cell cell = row.getCell(columnIndexMap.get(header));
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    Integer integer = columnIndexMap.get(header);
                    String value = cell.getStringCellValue();
                    columnNameValueMap.putIfAbsent(header, value);
                    columnIndexValueMap.putIfAbsent(integer, value);
                }
            }
            rows.add(new ExcelRow(columnNameValueMap, columnIndexValueMap));
        }

        return new ExcelResultSet(headers, rows);
    }

    boolean isRowEmpty(org.apache.poi.ss.usermodel.Row row, HashMap<String, Integer> columnIndexMap) {
        for (Integer index : columnIndexMap.values()) {
            if (index != null) {
                Cell cell = row.getCell(index);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    String value = cell.getStringCellValue();
                    if (value != null && !value.trim().replaceAll(" ", "").isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

package com.moblie.management.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
public class ExcelSheetUtil {

    public static Workbook getSheets(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        Workbook workbook;

        if (extension == null || file.getSize() == 0) {
            throw new IllegalArgumentException("파일 확장자가 없거나 파일이 비어 있습니다.");
        }

        try {
            if ("xlsx".equalsIgnoreCase(extension)) {
                // XLSX 파일 처리
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if ("xls".equalsIgnoreCase(extension)) {
                // XLS 파일 처리
                workbook = new HSSFWorkbook(file.getInputStream());
            } else {
                throw new IllegalArgumentException("지원하지 않는 파일 형식입니다: " + extension);
            }
        } catch (IOException e) {
            workbook = convertHtmlToWorkbook(file);
        }
        return workbook;
    }

    // Html 데이터 테이블 읽어오기
    private static Workbook convertHtmlToWorkbook(MultipartFile htmlFile) throws IOException {
        Document htmlDoc = Jsoup.parse(htmlFile.getInputStream(), "UTF-8", "");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Element table = htmlDoc.select("table").first();
        if (table != null) {
            Elements rows = table.select("tr");

            int rowIndex = 0;
            for (Element row : rows) {
                Row excelRow = sheet.createRow(rowIndex++);
                Elements cells = row.select("td, th");
                int cellIndex = 0;
                for (Element cell : cells) {
                    Cell excelCell = excelRow.createCell(cellIndex++);
                    excelCell.setCellValue(cell.text());
                }
            }
        }

        return workbook;
    }

}

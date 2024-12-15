package com.moblie.management.product.excel;

import com.moblie.management.product.dto.ProductDto;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelSheetUtils {

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

    public static void formatProductExcelData(Sheet workSheet, ProductDto.productsInfo productsInfo, List<Integer> targetColum) {

        productsInfo.products = new ArrayList<>();

        for (int i = 1; i < workSheet.getPhysicalNumberOfRows(); i++) {
            Row row = workSheet.getRow(i);

            if (row == null) continue;

            List<String> columnValues = targetColum.stream()
                    .map(index -> extractedExcelCellData(row, index))
                    .toList();

            ProductDto.createProduct product = new ProductDto.createProduct(
                    columnValues.get(0), // modelNumber
                    columnValues.get(1), // factory
                    columnValues.get(2), // modelClassification
                    columnValues.get(3), // goldType
                    columnValues.get(4), // goldColor
                    columnValues.get(5), // modelWeight
                    columnValues.get(6)  // modelComment
            );

            log.info("formatProductExcelData product {}", product);

            productsInfo.products.add(product);

        }

        log.info("formatProductExcelData productInfo data {}", productsInfo.products.size());
    }

    private static String extractedExcelCellData(Row row, int target) {
        Cell cell = row.getCell(target);
        return cell != null ? cell.getStringCellValue() : null; 
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

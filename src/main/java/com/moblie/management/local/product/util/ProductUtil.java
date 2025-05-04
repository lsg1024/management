package com.moblie.management.local.product.util;

import com.moblie.management.local.product.dto.ProductDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.util.*;

public class ProductUtil {

    public static void formatProductExcelData(Sheet workSheet, ProductDto.productsInfo productsInfo, List<Integer> targetColum) {

        productsInfo.products = new ArrayList<>();

        for (int i = 1; i < workSheet.getPhysicalNumberOfRows(); i++) {
            Row row = workSheet.getRow(i);

            if (row == null) continue;

            List<String> columValues = targetColum.stream()
                    .map(index -> extractedExcelCellData(row, index))
                    .toList();

            ProductDto.productInfo product = new ProductDto.productInfo(
                    columValues.get(0), // modelNumber
                    columValues.get(1).toUpperCase(), // factory
                    columValues.get(2), // modelClassification
                    columValues.get(3), // goldType
                    columValues.get(4), // goldColor
                    columValues.get(5), // modelWeight
                    columValues.get(6)  // modelComment
            );

            productsInfo.products.add(product);

        }
    }

    private static String extractedExcelCellData(Row row, int target) {
        Cell cell = row.getCell(target);
        return cell != null ? cell.getStringCellValue() : null;
    }

}

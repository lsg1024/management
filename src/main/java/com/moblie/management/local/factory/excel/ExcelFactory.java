package com.moblie.management.local.factory.excel;

import com.moblie.management.local.factory.dto.FactoryDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;

public class ExcelFactory {

    public static void formatFactoryExcelData(Sheet workSheet, FactoryDto.factoryInfo factoryInfo) {

        factoryInfo.factories = new ArrayList<>();

        for (int i = 1; i < workSheet.getPhysicalNumberOfRows(); i ++) {
            Row row = workSheet.getRow(i);

            if (row == null) continue;

            FactoryDto.createFactory createFactory = new FactoryDto.createFactory(row.getCell(5).getStringCellValue().toUpperCase());

            factoryInfo.factories.add(createFactory);
        }
    }
}

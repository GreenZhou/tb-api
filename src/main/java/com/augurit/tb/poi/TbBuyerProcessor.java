package com.augurit.tb.poi;

import com.augurit.common.utils.DefaultIdGenerator;
import com.augurit.tb.entity.TbBuyer;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class TbBuyerProcessor {
    public List<TbBuyer> process(File xlsFile) throws Exception {
        try {
            new XSSFWorkbook(xlsFile);
        } catch (OLE2NotOfficeXmlFileException e) {
            return processExcel(xlsFile);
        }

        return processExcel2007(xlsFile);
    }

    private List<TbBuyer> processExcel(File xlsFile) throws Exception {
        List<TbBuyer> buyers = Lists.newArrayList();

        HSSFWorkbook workbook = null;
        InputStream is = null;

        try {
            is = new FileInputStream(xlsFile);
            workbook = new HSSFWorkbook(is);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                if (sheet == null) {
                    continue;
                }

                // 对于每个sheet，读取其中的每一行
                for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                    HSSFRow row = sheet.getRow(j);
                    if(row == null || row.getCell(1) == null || Strings.isNullOrEmpty(row.getCell(1).getStringCellValue())) {
                        break;
                    } else {
                        TbBuyer buyer = new TbBuyer();
                        buyer.setId(DefaultIdGenerator.getIdForStr());
                        buyer.setBuyerWWName(row.getCell(1).getStringCellValue());
                        buyer.setByj(row.getCell(2).getNumericCellValue());

                        buyers.add(buyer);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
            if(workbook != null) {
                workbook.close();
            }
        }



        return buyers;
    }

    private List<TbBuyer> processExcel2007(File xlsFile) throws Exception {
        List<TbBuyer> buyers = Lists.newArrayList();

        XSSFWorkbook workbook = null;
        InputStream is = null;

        try {
            is = new FileInputStream(xlsFile);
            workbook = new XSSFWorkbook(is);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                if (sheet == null) {
                    continue;
                }

                // 对于每个sheet，读取其中的每一行
                for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                    XSSFRow row = sheet.getRow(j);
                    if(row == null || row.getCell(1) == null || Strings.isNullOrEmpty(row.getCell(1).getStringCellValue())) {
                        break;
                    } else {
                        TbBuyer buyer = new TbBuyer();
                        buyer.setId(DefaultIdGenerator.getIdForStr());
                        buyer.setBuyerWWName(row.getCell(1).getStringCellValue());
                        buyer.setByj(row.getCell(2).getNumericCellValue());

                        buyers.add(buyer);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
            if(workbook != null) {
                workbook.close();
            }
        }



        return buyers;
    }

}

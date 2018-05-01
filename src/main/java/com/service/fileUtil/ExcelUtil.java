package com.service.fileUtil;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class ExcelUtil {

    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static XSSFCell cell;


    public void setExcelFile(String path, String sheetName) {
        try {
            FileInputStream ExcelFile = new FileInputStream(path);
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRowCount(String sheetName) {
        ExcelWSheet = ExcelWBook.getSheet(sheetName);
        return ExcelWSheet.getPhysicalNumberOfRows();
    }

    public String getCellData(int rowNum, int colNum) {
        try {
            cell = ExcelWSheet.getRow(rowNum).getCell(colNum);
            String cellValue = cell.getStringCellValue();
            return cellValue;
        } catch (Exception e) {
            return "";
        }
    }

}




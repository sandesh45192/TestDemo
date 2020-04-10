/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.datadriven;

import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cs.csautomation.cs.utility.CSLogger;

public class ReadExcelFile {

    public String          path;
    public FileInputStream fis      = null;
    private XSSFWorkbook   workbook = null;
    private XSSFSheet      sheet    = null;
    private XSSFRow        row      = null;

    /*
     * Creates object of the workbook and the sheet
     * 
     * @param path String file path of the excel file
     * 
     * @param SheetName String sheetName of the excel file
     */
    public ReadExcelFile(String path, String SheetName) {
        this.path = path;
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            int index = workbook.getSheetIndex(SheetName);
            if (isSheetExist(SheetName)) {
                sheet = workbook.getSheetAt(index);
                CSLogger.info("Sheet is present in the Xlxs file.");
            }
            fis.close();
        } catch (Exception e) {
            CSLogger.info(" xlxs File does not found : ", e);
        }
    }

    /*
     * returns the row count in a sheet
     * 
     * @return Int value of row count
     */
    public int getRowCount() {
        int number = sheet.getLastRowNum() + 1;
        return number;
    }

    /*
     * returns the cell count in a sheet
     * 
     * @return int value of cell count
     */
    public int getCellCount() {
        int number = sheet.getRow(0).getLastCellNum();

        return number;
    }

    /*
     * returns the cell data based on the column and row index
     * 
     * @param String colNum column number of the sheet
     * 
     * @param Int rowNum row number of the sheet
     * 
     * @return return the String cell value
     */
    public String getColumsData(int colNum, int rowNum) {
        String cellText = "";
        try {
            if (rowNum <= 0)
                return "";
            if (colNum == -1)
                return "";
            row = sheet.getRow(rowNum - 1);
            if (row.getCell(colNum) == null) {
                return cellText;
            }
            if (row.getCell(colNum).getCellTypeEnum() == CellType.NUMERIC) {
                cellText = String.valueOf(
                        (int) row.getCell(colNum).getNumericCellValue());
            } else {
                cellText = String
                        .valueOf(row.getCell(colNum).getStringCellValue());
            }
        } catch (Exception e) {
            CSLogger.info("row does not fund for the index for cell data : ",
                    e);
        }
        return cellText;
    }

    /*
     * returns the boolean value where finds sheets exists
     * 
     * @param String sheetName name of the sheet
     * 
     * @return return boolean value for sheet is exist or not
     */
    public boolean isSheetExist(String sheetName) {
        try {
            int index = workbook.getSheetIndex(sheetName);
            if (index == -1) {
                index = workbook.getSheetIndex(sheetName.toUpperCase());
                if (index == -1) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            CSLogger.info("Sheet Does not exist ", e);
            return false;
        }

    }

}

/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.datadriven;

import org.cs.csautomation.cs.utility.CSLogger;

public class DataDrivenScript {

    public ReadExcelFile currentTestSuiteXLS;
    public static int    currentTestDataSetID = 2;

    /*
     * returns the data array as per the sheet data to the data provider.
     * @param String FilePath path of the xlsx file
     * @param String SheetName name of the sheet.
     * @return return the string array for the data provider as per the array
     * structure
     */
    public Object[][] readSheetData(String FilePath, String SheetName) {

        currentTestSuiteXLS = new ReadExcelFile(FilePath, SheetName);
        CSLogger.info("created the Sheet object based on the sheet index.");
        int totalNoOfCols = currentTestSuiteXLS.getCellCount();
        int totalNnOfRows = currentTestSuiteXLS.getRowCount();
        Object arrayExcelData[][] = new Object[totalNnOfRows - 1][totalNoOfCols
                - 1];
        int rowsIndex = 0;
        for (currentTestDataSetID = 2; 
                currentTestDataSetID <= currentTestSuiteXLS.getRowCount();
                currentTestDataSetID++) {
            int colIndex = 0;
            for (int colNum = 0; colNum < totalNoOfCols - 1; colNum++) {
                arrayExcelData[rowsIndex][colIndex] = currentTestSuiteXLS
                        .getColumsData(colNum, currentTestDataSetID);
                colIndex++;
            }
            rowsIndex++;
        }

        return arrayExcelData;
    }

}

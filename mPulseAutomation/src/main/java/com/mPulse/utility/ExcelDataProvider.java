package com.mPulse.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelDataProvider {
	private ExcelDataProvider() {}
	public static synchronized Object[][] getCellData(String path, String sheetName) throws InvalidFormatException, IOException {
		FileInputStream file = new FileInputStream(new File(path));
		Workbook workbook = WorkbookFactory.create(file);
		Sheet s = workbook.getSheet(sheetName);
		int rowcount = s.getLastRowNum();
		int cellcount = s.getRow(0).getLastCellNum();
		Object data[][] = new Object[rowcount][cellcount];
		for (int i = 1; i <= rowcount; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < cellcount; j++) {
				Cell c = r.getCell(j);
				try {
					data[i - 1][j] = c.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		file.close();
		return data;
	}
}
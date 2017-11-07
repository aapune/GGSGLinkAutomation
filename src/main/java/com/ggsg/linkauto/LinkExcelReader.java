package com.ggsg.linkauto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class LinkExcelReader {

	private static final int LINK_TEXT_CI = 1;
	private static final int EMAIL_CI = 2;
	private static final int DISRESPECT_TEXT_CI = 3;
	private static final int CORRECTION_TEXT_CI = 4;
	private static final int ACTION_STATUS = 5;

	public Set<LinkVO> returnLinksData() {

		Set<LinkVO> setLinkData = null;
		try {
			Main.LOGGER.info("LINK EXCEL LOCATION : " + Main.configProps.getProperty("EXCEL_FILE_LOCATION"));
			FileInputStream excelFile = new FileInputStream(
					new File(Main.configProps.getProperty("EXCEL_FILE_LOCATION")));

			Workbook workbook = new HSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			setLinkData = new LinkedHashSet<LinkVO>();
			
			while (iterator.hasNext()) {
				
				Row currentRow = iterator.next();
				LinkVO linkVO = null;
				if(currentRow.getRowNum() != 0) 
				{
					Main.LOGGER.info("Row Number: " + currentRow.getRowNum());
					linkVO = new LinkVO();
					Iterator<Cell> cellIterator = currentRow.iterator();
					
					while (cellIterator.hasNext()) 
					{
						Cell currentCell = cellIterator.next();
						Cell tempCell = currentCell;
						tempCell.setCellType(Cell.CELL_TYPE_STRING);

						if (currentCell != null && currentCell.getCellType() != Cell.CELL_TYPE_BLANK
								&& tempCell.getStringCellValue() != null
								&& !"".equals(tempCell.getStringCellValue().trim())) 
						{
							
							if (currentCell.getColumnIndex() == LINK_TEXT_CI) {
								if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
									linkVO.setUrl(currentCell.getStringCellValue());
									Main.LOGGER.info("Reading column no:" + LINK_TEXT_CI + " , URL: " + linkVO.getUrl());
								}
							} else if (currentCell.getColumnIndex() == EMAIL_CI) {
								if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {

									String[] emailIds = null;
									if (currentCell.getStringCellValue().contains(",")) {
										emailIds = currentCell.getStringCellValue().split(",");
									} else {
										emailIds = new String[] { currentCell.getStringCellValue() };
									}
									linkVO.setEmailIds(emailIds);
									Main.LOGGER.info("Reading column no:" + EMAIL_CI + " , Emails: "
											+ Arrays.asList(linkVO.getEmailIds()));
								}
							} else if (currentCell.getColumnIndex() == DISRESPECT_TEXT_CI) {
								if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
									linkVO.setDisrepectText(currentCell.getStringCellValue());
									Main.LOGGER.info("Reading column no:" + DISRESPECT_TEXT_CI + " , Disrespect Text: "
											+ linkVO.getDisrepectText());
								}
							} else if (currentCell.getColumnIndex() == CORRECTION_TEXT_CI) {
								if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
									linkVO.setCorrectedText(currentCell.getStringCellValue());
									Main.LOGGER.info("Reading column no:" + CORRECTION_TEXT_CI + " , Correction Text: "
											+ linkVO.getCorrectedText());
								}
							} else if (currentCell.getColumnIndex() == ACTION_STATUS) {
								if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
									Main.LOGGER.info("Reading column no:" + ACTION_STATUS + ", Status:"
											+ currentCell.getStringCellValue());
									if (currentCell.getStringCellValue() != null) {
										linkVO.setLinkStatus(currentCell.getStringCellValue().trim());
									}
								}
							}
						} else {
							break;
						}

						if (linkVO.getUrl() != null && !"".equals(linkVO.getUrl().trim())) {
							setLinkData.add(linkVO);
							Main.LOGGER.info("URL ADDED IN ACTION LIST: " + linkVO.getUrl());
						}

					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return setLinkData;
	}

}

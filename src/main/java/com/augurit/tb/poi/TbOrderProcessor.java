package com.augurit.tb.poi;

import com.augurit.common.utils.DefaultIdGenerator;
import com.augurit.tb.entity.TbBuyer;
import com.augurit.tb.entity.TbOrder;
import com.augurit.tb.entity.TbOrderBuyer;
import com.augurit.tb.entity.TbSubTask;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TbOrderProcessor {
	public List<TbOrder> process(File xlsFile, List<TbSubTask> subTasks) throws Exception {
		try {
			new XSSFWorkbook(xlsFile);
		} catch (OLE2NotOfficeXmlFileException e) {
			return processExcel(xlsFile, subTasks);
		}

		return processExcel2007(xlsFile, subTasks);
	}

	private List<TbOrder> processExcel(File xlsFile, List<TbSubTask> subTasks) throws Exception {
		List<TbOrder> orders = Lists.newArrayList();

		InputStream is = null;
		HSSFWorkbook workbook = null;

		try {
			is = new FileInputStream(xlsFile);

			workbook = new HSSFWorkbook(is);
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				if (sheet == null) {
					continue;
				}

				TbOrder commonOrder = new TbOrder();
				// 对于每个sheet，读取其中的每一行
				for (int j = 0; j <= sheet.getLastRowNum(); j++) {
					HSSFRow row = sheet.getRow(j);
					if(row == null || row.getCell(0) == null || Strings.isNullOrEmpty(row.getCell(0).getStringCellValue())) {
						break;
					} else if ("店铺名称".equals(row.getCell(0).getStringCellValue())) {
						String salerName = row.getCell(1).getStringCellValue();
						commonOrder.setSalerName(salerName);
					} else if ("店铺旺旺".equals(row.getCell(0).getStringCellValue())) {
						String salerWWName = row.getCell(1).getStringCellValue();
						commonOrder.setSalerWWName(salerWWName);
					} else if ("图片名称".equals(row.getCell(0).getStringCellValue())) {
						String purl = row.getCell(1).getStringCellValue();
						commonOrder.setPurl(xlsFile.getParentFile().getCanonicalPath() + "\\" + purl);
					} else if ("单笔佣金".equals(row.getCell(0).getStringCellValue())) {
						Double yj = row.getCell(1).getNumericCellValue();
						commonOrder.setYj(yj);
					} else if ("关键词".equals(row.getCell(0).getStringCellValue())) {
						orders.addAll(processTbOrders(j + 1, sheet, commonOrder, subTasks));
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(is);
			if(workbook != null) {
				workbook.close();
			}
		}



		return orders;
	}

	private List<TbOrder> processTbOrders(int rownum, HSSFSheet sheet, TbOrder commonOrder, List<TbSubTask> subTasks) throws Exception {
		List<TbOrder> orders = new ArrayList<TbOrder>();

		for(int j = rownum; j <= sheet.getLastRowNum(); j++) {
			HSSFRow row = sheet.getRow(j);
			if (row == null || row.getCell(0) == null || Strings.isNullOrEmpty(row.getCell(0).getStringCellValue())) {
				break;
			}

			String keywd = row.getCell(0).getStringCellValue();
			String demand = row.getCell(1).getStringCellValue();
			HSSFCell cell2 = row.getCell(2);
			cell2.setCellType(CellType.NUMERIC);
			double price = cell2.getNumericCellValue();
			HSSFCell cell3 = row.getCell(3);
			cell3.setCellType(CellType.STRING);
			int num = Integer.parseInt(cell3.getStringCellValue());
			HSSFCell cell4 = row.getCell(4);
			cell4.setCellType(CellType.NUMERIC);
			double tprice = cell4.getNumericCellValue();

			TbOrder order = (TbOrder) ObjectUtils.clone(commonOrder);
			order.setId(DefaultIdGenerator.getIdForStr());
			TbSubTask subTask = subTasks.get((j - rownum) % subTasks.size());
			subTask.setOrderNum(subTask.getOrderNum() + 1);
			order.setTsid("" + subTask.getId());
			order.setKeywd(keywd);
			order.setDemand(demand);
			order.setPrice(price);
			order.setNum(num);
			order.setTprice(tprice);

			orders.add(order);
		}

		return orders;
	}

	private List<TbOrder> processExcel2007(File xlsFile, List<TbSubTask> subTasks) throws Exception {
		List<TbOrder> orders = Lists.newArrayList();
		XSSFWorkbook workbook = new XSSFWorkbook(xlsFile);
		for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			if (sheet == null) {
				continue;
			}

			TbOrder commonOrder = new TbOrder();
			// 对于每个sheet，读取其中的每一行
			for (int j = 0; j <= sheet.getLastRowNum(); j++) {
				XSSFRow row = sheet.getRow(j);
				if(row == null || row.getCell(0) == null || Strings.isNullOrEmpty(row.getCell(0).getStringCellValue())) {
					break;
				} else if ("店铺名称".equals(row.getCell(0).getStringCellValue())) {
					String salerName = row.getCell(1).getStringCellValue();
					commonOrder.setSalerName(salerName);
				} else if ("店铺旺旺".equals(row.getCell(0).getStringCellValue())) {
					String salerWWName = row.getCell(1).getStringCellValue();
					commonOrder.setSalerWWName(salerWWName);
				} else if ("图片名称".equals(row.getCell(0).getStringCellValue())) {
					String purl = row.getCell(1).getStringCellValue();
					commonOrder.setPurl(xlsFile.getParentFile().getCanonicalPath() + "\\" + purl);
				} else if ("单笔佣金".equals(row.getCell(0).getStringCellValue())) {
					Double yj = row.getCell(1).getNumericCellValue();
					commonOrder.setYj(yj);
				} else if ("关键词".equals(row.getCell(0).getStringCellValue())) {
					orders.addAll(processTbOrders2007(j + 1, sheet, commonOrder, subTasks));
				}
			}
		}

		workbook.close();

		return orders;
	}

	private List<TbOrder> processTbOrders2007(int rownum, XSSFSheet sheet, TbOrder commonOrder, List<TbSubTask> subTasks) throws Exception {
		List<TbOrder> orders = new ArrayList<TbOrder>();

		for(int j = rownum; j <= sheet.getLastRowNum(); j++) {
			XSSFRow row = sheet.getRow(j);
			if (row == null || row.getCell(0) == null || Strings.isNullOrEmpty(row.getCell(0).getStringCellValue())) {
				break;
			}
			String keywd = row.getCell(0).getStringCellValue();
			String demand = row.getCell(1).getStringCellValue();
			XSSFCell cell2 = row.getCell(2);
			cell2.setCellType(CellType.NUMERIC);
			double price = cell2.getNumericCellValue();
			XSSFCell cell3 = row.getCell(3);
			cell3.setCellType(CellType.STRING);
			int num = Integer.parseInt(cell3.getStringCellValue());
			XSSFCell cell4 = row.getCell(4);
			cell4.setCellType(CellType.NUMERIC);
			double tprice = cell4.getNumericCellValue();

			TbOrder order = (TbOrder) ObjectUtils.clone(commonOrder);
			order.setId(DefaultIdGenerator.getIdForStr());
			TbSubTask subTask = subTasks.get((j - rownum) % subTasks.size());
			subTask.setOrderNum(subTask.getOrderNum() + 1);
			order.setTsid("" + subTask.getId());
			order.setKeywd(keywd);
			order.setDemand(demand);
			order.setPrice(price);
			order.setNum(num);
			order.setTprice(tprice);

			orders.add(order);
		}

		return orders;
	}

	// 生成初始的订单核查Excel文件
	public static void generateOrderBuyerExcel(List<TbOrderBuyer> orderBuyers, List<TbBuyer> buyers, HttpServletResponse resp) throws Exception {

		int xrows = buyers.size();
		int sXRowNum = 1;// 开始
		int sYRowNum = 2;

		Workbook workbook = new XSSFWorkbook(TbOrderProcessor.class.getClassLoader().getResourceAsStream("tb/orderBuyerCheck.xlsx"));
		Sheet sheet = workbook.getSheetAt(0);

		Row firstRow = sheet.getRow(1);
		CellStyle thicker = firstRow.getCell(0).getCellStyle();
		CellStyle normal = firstRow.getCell(1).getCellStyle();
		// 1. 生成横向标题头
		for(int i = 1; i < buyers.size() + 1; i++) {
			Cell cell = firstRow.createCell(i);
			cell.setCellStyle(normal);
			cell.setCellValue(buyers.get(i - 1).getBuyerWWName());
		}

		// 2. 生成每一行数据和汇总
		String salerName = null;
		int startRowNum = 2;
		Row cententRow = null;
		for(int i = 0; i < orderBuyers.size(); i++) {
			if(!orderBuyers.get(i).getSalerName().equals(salerName)) {
				salerName = orderBuyers.get(i).getSalerName();
				// TODO： 求出一行的汇总

				cententRow = sheet.createRow(startRowNum++);
				Cell cell = cententRow.createCell(0);
				cell.setCellStyle(thicker);
				cell.setCellValue(salerName);
			} else {
				Cell cell = cententRow.createCell(i % buyers.size() + 1);
				cell.setCellStyle(normal);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(orderBuyers.get(i).getTprice());
			}
		}


		String fileName = URLEncoder.encode("原始订单核查表.xlsx", "UTF-8");
		resp.setContentType("application/octet-stream");
		resp.setHeader("content-disposition", "attachment;filename=" + new
				String(fileName.getBytes("ISO8859-1")));
		resp.setHeader("filename", fileName);
		workbook.write(resp.getOutputStream());
	}
}

package com.metasoft.empire.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtils {
	public static final String DATA_FILE_SUFFIX_XLS = ".xls";
	public static final String DATA_FILE_SUFFIX_CSV = ".csv";
	public static final String ID_FIELD_NAME = "id";
	private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

	/**
	 * 栏位在哪一行
	 */
	public static int fieldDefineRow = 1;
	/**
	 * 从哪一行开始读
	 */
	public static int dataStartRow = 3;

	/**
	 * @param clazz !!!clazz must be public!!!
	 * @param file
	 * @return
	 */
	public static <T> Map<Integer, T> load(Class<T> clazz, File file) {
		Map<Integer, T> rows = Collections.emptyMap();
		if ((clazz == null) || (file == null) || (!file.exists()))
			return rows;
		String lowerCaseName = file.getName().toLowerCase();
		if (lowerCaseName.lastIndexOf(".xls") > 0)
			return loadXls(clazz, file);
		if (lowerCaseName.lastIndexOf(".csv") > 0) {
			return loadCsv(clazz, file);
		}
		return rows;
	}

	private static <T> Map<Integer, T> loadXls(Class<T> clazz, File file) {
		Map<Integer, T> rows = Collections.emptyMap();
		HSSFWorkbook wb = null;
		try {
			InputStream fis = new FileInputStream(file);
			POIFSFileSystem poifs = new POIFSFileSystem(fis);
			wb = new HSSFWorkbook(poifs);
		} catch (Exception e) {
			e.printStackTrace();
			return rows;
		}

		//取得每一样表
		HSSFSheet sheet = wb.getSheetAt(0);
		//取得总行数
		int rowcount = sheet.getLastRowNum();
		if (rowcount < dataStartRow)
			return rows;
		//取得栏位行
		HSSFRow fieldRow = sheet.getRow(fieldDefineRow);
		Iterator<Cell> iterator = fieldRow.cellIterator();
		ArrayList<Object> fieldList = new ArrayList<>();
		while (iterator.hasNext()) {
			Cell cell = (Cell) iterator.next();
			fieldList.add(cell.getStringCellValue());
		}
		String[] fields = new String[fieldList.size()];
		fields = (String[]) fieldList.toArray(fields);
		//检查栏位与对象属性匹配
		checkDiff(clazz, file, fields);
		rows = new LinkedHashMap<Integer, T>(rowcount);
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		//取得数据
		for (int i = dataStartRow; i <= rowcount; i++) {
			try {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					Map<String,Object> propertyMap = new HashMap<String,Object>();
					for (int j = 0; j < fields.length; j++) {
						HSSFCell cell = row.getCell(j);
						if (cell != null) {
							String key = fields[j];
							Object value = null;
							switch (evaluator.evaluateInCell(cell).getCellType()) {
							case 4:
								break;
							case 0:
								value = Double.valueOf(cell.getNumericCellValue());
								break;
							case 1:
								value = cell.getStringCellValue();
								break;
							case 3:
								break;
							case 5:
								break;
							case 2:
							}

							propertyMap.put(key, value);
						}
					}
					Double id = (Double) propertyMap.get(ID_FIELD_NAME);
					if ((id == null) || (id.intValue() == 0)) {
						log.warn("### " + file.getAbsolutePath() + " miss id field or id=0");
					} else {
						T t = clazz.newInstance();
						BeanUtils.populate(t, propertyMap);
						rows.put(Integer.valueOf(id.intValue()), t);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("### " + file.getAbsolutePath() + " data init fail");
			}
		}
		log.info("### " + clazz.getName() + " data init from " + file.getAbsolutePath() + ",size: " + rows.size() + ",rowCount: " + rowcount);
		return rows;
	}

	/**
	 * 一定要以","为分割符的CSV文件,编码为UTF-8
	 * @param clazz
	 * @param file
	 * @return
	 */
	private static <T> Map<Integer, T> loadCsv(Class<T> clazz, File file) {
		//所有行
		Map<Integer, T> rows = Collections.emptyMap();
		//所有字串行
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(file, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return rows;
		}
		//第一行空,第二行为栏位,第三行为数据
		if (lines.size() < 3)
			return rows;
		//获得栏位
		String[] fields = ((String) lines.get(1)).split(",");

		//对比 类成员 与 栏位
		checkDiff(clazz, file, fields);

		rows = new LinkedHashMap<Integer, T>(lines.size());
		for (int i = dataStartRow; i < lines.size(); i++) {
			try {
				String row = (String) lines.get(i);
				if (!StringUtils.isEmpty(row)) {
					String[] data = row.split(",", fields.length);
					HashMap<String, String> propertyMap = new HashMap<String,String>();
					for (int j = 0; j < fields.length; j++) {
						String key = fields[j];
						String value = null;
						if (j <= data.length - 1)
							value = data[j];
						propertyMap.put(key, value);
					}
					int id = 0;
					try {
						id = Integer.parseInt((String) propertyMap.get(ID_FIELD_NAME));
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (id == 0) {
						log.warn("### " + file.getAbsolutePath() + " miss id field or id=0");
					} else {
						T t = clazz.newInstance();
						BeanUtils.populate(t, propertyMap);
						rows.put(Integer.valueOf(id), t);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("### " + file.getAbsolutePath() + " data init fail");
			}
		}
		log.info("### " + clazz.getName() + " data init from " + file.getAbsolutePath() + ",size: " + rows.size());
		return rows;
	}

	public static boolean isSuitableFile(File file) {
		String lowerCaseName = file.getName().toLowerCase();
		return (lowerCaseName.endsWith(".xls")) || (lowerCaseName.endsWith(".csv"));
	}

	public static Class<?> getFullClassNameByFile(String fileName) {
		Class<?> clazz = null;
		try {
			int index = fileName.toLowerCase().lastIndexOf(".xls");
			if (index < 0)
				index = fileName.toLowerCase().lastIndexOf(".csv");
			if (index < 0)
				return null;
			String className = fileName.substring(0, index);
			index = className.indexOf("_");
			if (index > 0)
				className = className.substring(0, index);
			clazz = Class.forName(className);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return clazz;
	}

	private static void checkDiff(Class<?> clazz, File file, String[] fields) {
		try {
			Object sample = clazz.newInstance();
			Map<?, ?> map = BeanUtils.describe(sample);
			for (Iterator<?> i$ = map.keySet().iterator(); i$.hasNext();) {
				Object key = i$.next();
				if (!"class".equals(key.toString())) {
					if (!ArrayUtils.contains(fields, key))
						log.warn("### " + file.getAbsolutePath() + " miss field " + key + " in " + clazz.getName());
				}
			}
			for (String fieldName : fields)
				if (!map.containsKey(fieldName))
					log.warn("### " + clazz.getName() + " miss field " + fieldName + " in " + file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
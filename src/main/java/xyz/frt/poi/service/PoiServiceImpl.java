package xyz.frt.poi.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.util.StringUtils;

public class PoiServiceImpl implements PoiService {

	private Integer sheetHeader;
	private Integer startRow;
	private Integer startCol;

	public PoiServiceImpl(Integer sheetHeader, Integer startRow, Integer startCol) {
		this.sheetHeader = sheetHeader;
		this.startRow = startRow;
		this.startCol = startCol;
	}

	@Override
	public <T> List<T> readExcel(InputStream is, ExcelMapRule[] rules, Class<T> clazz) {

		List<T> resList = new ArrayList<>();
		Workbook workbook = null;

		try {
			workbook = WorkbookFactory.create(is);
			// 暂只支持单工作表
			// 遍历工作表
			Sheet sheet = workbook.getSheetAt(0);
			// 获取类属性与列的对应关系
			Map<Integer, ExcelMapRule> map = fieldMapping(sheet, rules, clazz);
			// 判断此sheet是否为空
			if (sheet.getLastRowNum() == 0 && sheet.getPhysicalNumberOfRows() == 0) {
				return null;
			}
			Row row;
			Cell cell;
			// 开始读取 行
			for (int i = sheet.getFirstRowNum() + startRow; i < sheet.getPhysicalNumberOfRows(); i++) {

				// 一个行代表一个对象实例
				T item = clazz.newInstance();
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				// 开始读取 单元格
				for (int j = row.getFirstCellNum() + startCol; j < row.getLastCellNum(); j++) {

					cell = row.getCell(j);
					String cellValue = cell.getStringCellValue();
					
					if (map.containsKey(j)) {
						ExcelMapRule rule = map.get(j);
						Field field = rule.getField();
						field.setAccessible(true);
						//基础校验
						try {
							validateRule(rule, cellValue);
						} catch (PoiException e) {
							String errMsg = "第[" + (i + 1) + "]行, 第[" + (j + 1) + "]列, " + e.getMessage();
							System.err.println(errMsg);
							throw new PoiException(errMsg);
						}

						Class<?> valueType = field.getType();
						String className = valueType.getName();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Object value = null;
						
						// 转换CellValue
						if (className.equals(String.class.getName())) {
							value = cellValue;
						} else if (className.equals(Date.class.getName())) {
							value = sdf.parse(cellValue);
						} else if (className.equals(Integer.class.getName())) {
							value = Integer.valueOf(cellValue);
						} else if (className.equals(Boolean.class.getName())) {
							value = Boolean.valueOf(cellValue);
						} else if (className.equals(Long.class.getName())) {
							value = Long.valueOf(cellValue);
						} else if (className.equals(Short.class.getName())) {
							value = Short.valueOf(cellValue);
						} else if (className.equals(Float.class.getName())) {
							value = Float.valueOf(cellValue);
						} else if (className.equals(Double.class.getName())) {
							value = Double.valueOf(cellValue);
						} else if (className.equals(Character.class.getName())) {
							value = String.valueOf(cellValue).charAt(0);
						} else if (className.equals(Byte.class.getName())) {
							value = Byte.valueOf(cellValue);
						} else {
							throw new PoiException("不支持的数据类型，仅支持String, Date, 及基本数据类型！");
						}

						field.set(item, value);
					}
				}
				resList.add(item);
			}

		} catch (EncryptedDocumentException | InvalidFormatException | IOException | InstantiationException
				| IllegalAccessException | NoSuchFieldException | SecurityException | ParseException e) {
			if (e instanceof IOException) {
				throw new PoiException("文件不存在，请检查文件路径！");
			} else if (e instanceof NoSuchFieldException) {
				throw new PoiException("指定的属性不存在！");
			} else if (e instanceof ParseException) {
				throw new PoiException("日期格式不正确，请检查后重试！");
			} else {
				throw new PoiException(e.getMessage(), e.getCause());
			}
		}

		return resList;
	}

	public <T> List<T> readExcel(File file, ExcelMapRule[] rules, Class<T> clazz) {
		try {
			return readExcel(new FileInputStream(file), rules, clazz);
		} catch (FileNotFoundException e) {
			throw new PoiException("文件不存在，请检查文件路径！");
		}
	}

	/**
	 * 获取属性与列对应关系
	 * 
	 * @param rules
	 * @param clazz
	 * @return map
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	private Map<Integer, ExcelMapRule> fieldMapping(Sheet sheet, ExcelMapRule[] rules, Class<?> clazz)
			throws NoSuchFieldException, SecurityException {
		Map<Integer, ExcelMapRule> map = new HashMap<>();
		Row row = sheet.getRow(sheetHeader);
		for (int i = row.getFirstCellNum() + startCol; i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			String headerName = cell.getStringCellValue();
			if (StringUtils.isEmpty(headerName)) {
				continue;
			}
			for (ExcelMapRule rule : rules) {
				if (rule.getExcelColumnName().equals(headerName)) {
					String fieldName = rule.getFieldName();
					Field field = clazz.getDeclaredField(fieldName);
					rule.setField(field);
					map.put(i, rule);
				}
			}
		}

		return map;
	}

	private boolean validateRule(ExcelMapRule rule, Object val) {
		boolean flag = true;
		if (val == null) {
			if (!rule.getIsNull()) {
				return false;
			}
		}
		ExcelDataType dataType = rule.getExcelDataType();
		switch (dataType) {
		case TXT:
			String txt = (String) val;
			if (StringUtils.isEmpty(txt) || txt.length() > rule.getLength()) {
				flag = false;
				throw new PoiException("字段[" + rule.getExcelColumnName() + "]:不能为空或长度超出限制;");
			}
			break;

		case EMAIL:
			String email = (String) val;
			if (email.indexOf("@") == -1 || email.indexOf(".") == -1 || email.length() > rule.getLength()) {
				flag = false;
				throw new PoiException("字段[" + rule.getExcelColumnName() + "]:格式不正确或长度超出限制;");
			}
			break;

		case PHONE:
			String phone = (String) val;
			if (phone.length() != 11) {
				flag = false;
				throw new PoiException("字段[" + rule.getExcelColumnName() + "]:格式不正确或长度超出限制;");
			}
			break;

		case NUMBER:
			String number = (String) val;
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
			if (!pattern.matcher(number).matches() || number.length() > rule.getLength()) {
				flag = false;
				throw new PoiException("字段[" + rule.getExcelColumnName() + "]:数字格式不正确;");
			}
			break;

		case DATE:
			String date = (String) val;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				sdf.parse(date);
			} catch (ParseException e) {
				flag = false;
				throw new PoiException("字段[" + rule.getExcelColumnName() + "]:日期格式不正确;");
			}
			break;

		case BOOLEAN:
			String bool = (String) val;
			if (!bool.equals("true") || !bool.equals("false")) {
				flag = false;
				throw new PoiException("字段[" + rule.getExcelColumnName() + "]:格式不正确;");
			}
			break;

		case IDCARD:
			String idcard = (String) val;
			if (idcard.length() != 18) {
				flag = false;
				throw new PoiException("字段[" + rule.getExcelColumnName() + "]:身份证号格式不正确;");
			}
			break;

		case POSTCODE:
			String post = (String) val;
			if (post.length() != 6) {
				flag = false;
				throw new PoiException("字段[" + rule.getExcelColumnName() + "]:邮编号格式不正确;");
			}
			break;

		case NATRUE:
			String natrue = (String) val;
			flag = false;
			for (String str : ExcelDataConst.NATRUE) {
				if (str.equals(natrue)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				throw new PoiException("字段[" + rule.getExcelColumnName() + "]:民族格式不正确;");
			}
			break;

		case GENDER:
			String gender = (String) val;
			flag = false;
			for (String str : ExcelDataConst.GENDER) {
				if (str.equals(gender)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				throw new PoiException("字段[" + rule.getExcelColumnName() + "]:性别格式不正确;");
			}
			break;

		default:

			break;
		}

		return flag;
	}

}

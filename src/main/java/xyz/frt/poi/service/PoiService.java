package xyz.frt.poi.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface PoiService {

	/**
	 * 读取工作薄，并转化成List集合
	 * 
	 * @param is
	 * @param rules
	 * @param clazz
	 * @return list
	 */
	public <T> List<T> readExcel(InputStream is, ExcelMapRule[] rules, Class<T> clazz);

	/**
	 * 读取工作薄，并转化成List
	 * 
	 * @param file
	 * @param rules
	 * @param clazz
	 * @return list
	 */
	public <T> List<T> readExcel(File file, ExcelMapRule[] rules, Class<T> clazz);

}

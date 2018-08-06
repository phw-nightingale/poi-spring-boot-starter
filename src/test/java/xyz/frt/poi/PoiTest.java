package xyz.frt.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import com.sun.swing.internal.plaf.basic.resources.basic;

import xyz.frt.poi.service.ExcelDataType;
import xyz.frt.poi.service.ExcelMapRule;
import xyz.frt.poi.service.PoiService;
import xyz.frt.poi.service.PoiServiceImpl;

public class PoiTest {
	
	@Test
	public void test() throws EncryptedDocumentException, InvalidFormatException, IOException {
		
		PoiService service = new PoiServiceImpl(2, 5, 1);
		
		File file = new File("/home/phw/Downloads/test.xlsx");
		ExcelMapRule[] rules = {
				new ExcelMapRule("证件号码", "cid", ExcelDataType.IDCARD, 18, true),
				new ExcelMapRule("姓名", "name", ExcelDataType.TXT, 10, true),
				new ExcelMapRule("卡号", "cardNo", ExcelDataType.TXT, 20, true),
				new ExcelMapRule("单位", "unit", ExcelDataType.TXT, 100, true),
		};
		
		List<UserCard> items = service.readExcel(new FileInputStream(file), rules, UserCard.class);
		if (items != null) {
			for (UserCard item: items) {
				System.out.println(item.toString());
			}
		}
		
	}

}

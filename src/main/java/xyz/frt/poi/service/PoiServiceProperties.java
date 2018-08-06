package xyz.frt.poi.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("xyz.frt.poi")
public class PoiServiceProperties {

	private Integer sheetHeader;
	private Integer startRow;
	private Integer startCol;
	
	public Integer getStartRow() {
		return startRow;
	}
	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}
	public Integer getStartCol() {
		return startCol;
	}
	public void setStartCol(Integer startCol) {
		this.startCol = startCol;
	}
	public Integer getSheetHeader() {
		return sheetHeader;
	}
	public void setSheetHeader(Integer sheetHeader) {
		this.sheetHeader = sheetHeader;
	}
}

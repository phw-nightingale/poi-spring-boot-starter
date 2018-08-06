package xyz.frt.poi.service;

import java.lang.reflect.Field;

public class ExcelMapRule {

	private String excelColumnName;
	private String fieldName;
	private ExcelDataType excelDataType;
	private Integer length;
	private boolean isNull;
	private Object[] examples;
	private String regEx;	//正则表达式高级校验
	private Field field;
	
	public ExcelMapRule(String excelColumnName, String fieldName, ExcelDataType excelDataType, Integer length,
			boolean isNull, String regEx) {
		super();
		this.excelColumnName = excelColumnName;
		this.fieldName = fieldName;
		this.excelDataType = excelDataType;
		this.length = length;
		this.isNull = isNull;
		this.regEx = regEx;
	}

	public ExcelMapRule(String excelColumnName, String fieldName, ExcelDataType excelDataType, Integer length,
			boolean isNull) {
		super();
		this.excelColumnName = excelColumnName;
		this.fieldName = fieldName;
		this.excelDataType = excelDataType;
		this.length = length;
		this.isNull = isNull;
	}

	public ExcelMapRule(String excelColumnName, String fieldName, ExcelDataType excelDataType, Integer length,
			boolean isNull, Object[] examples, String regEx) {
		super();
		this.excelColumnName = excelColumnName;
		this.fieldName = fieldName;
		this.excelDataType = excelDataType;
		this.length = length;
		this.isNull = isNull;
		this.examples = examples;
		this.regEx = regEx;
	}

	public ExcelMapRule(Builder builder) {
		this.excelColumnName = builder.excelColumnName;
		this.fieldName = builder.fieldName;
		this.excelDataType = builder.excelDataType;
		this.length = builder.length;
		this.isNull = builder.isNull;
		this.examples = builder.examples;
		this.regEx = builder.regEx;
	}
	
	public String getExcelColumnName() {
		return excelColumnName;
	}
	public void setExcelColumnName(String excelColumnName) {
		this.excelColumnName = excelColumnName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public ExcelDataType getExcelDataType() {
		return excelDataType;
	}
	public void setExcelDataType(ExcelDataType excelDataType) {
		this.excelDataType = excelDataType;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public boolean getIsNull() {
		return isNull;
	}
	public void setIsNull(boolean isNull) {
		this.isNull = isNull;
	}
	public Object[] getExamples() {
		return examples;
	}
	public void setExamples(Object[] examples) {
		this.examples = examples;
	}
	public String getRegEx() {
		return regEx;
	}
	public void setRegEx(String regEx) {
		this.regEx = regEx;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public Field getField() {
		return field;
	}
	
	public static class Builder {
		
		private String excelColumnName;
		private String fieldName;
		private ExcelDataType excelDataType;
		private Integer length;
		private boolean isNull;
		private Object[] examples;
		private String regEx;	//正则表达式高级校验
		
		public Builder excelColumnName(String val) {
			this.excelColumnName = val;
			return this;
		}
		
		public Builder fieldName(String val) {
			this.fieldName = val;
			return this;
		}
		
		public Builder excelDataType(ExcelDataType val) {
			this.excelDataType = val;
			return this;
		}
		
		public Builder length(Integer val) {
			this.length = val;
			return this;
		}
		
		public Builder isNull(Boolean val) {
			this.isNull = val;
			return this;
		}
		
		public Builder examples(Object[] val) {
			this.examples = val;
			return this;
		}
		
		public Builder regEx(String val) {
			this.regEx = val;
			return this;
		}
		
		public ExcelMapRule build() {
			return new ExcelMapRule(this);
		}
	}
}

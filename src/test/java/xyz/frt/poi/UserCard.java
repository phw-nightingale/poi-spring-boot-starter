package xyz.frt.poi;

public class UserCard {
	
	private String cid;
	private String name;
	private String cardNo;
	private String unit;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	@Override
	public String toString() {
		return "UserCard [cid=" + cid + ", name=" + name + ", cardNo=" + cardNo + ", unit=" + unit + "]";
	}
	
}

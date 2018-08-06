package xyz.frt.poi.service;

public class PoiException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4336058854798815466L;
	
	public PoiException(String errMsg) {
		super(errMsg);
	}
	
	public PoiException(String errMsg, Throwable caze) {
		super(errMsg, caze);
	}

}

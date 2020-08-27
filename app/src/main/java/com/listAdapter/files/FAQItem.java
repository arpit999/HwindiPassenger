package com.listAdapter.files;

public class FAQItem {

	private String iFaqcategoryId = "";
	private String vTitle_EN = "";
	private String Questions = "";
	private String ICON_url = "";

	/*********** Set Methods ******************/
	public void setiFaqcategoryId(String iFaqcategoryId) {
		this.iFaqcategoryId = iFaqcategoryId;
	}

	public void setvTitle_EN(String vTitle_EN) {
		this.vTitle_EN = vTitle_EN;
	}

	public void setQuestions(String Questions) {
		this.Questions = Questions;
	}

	/*********** Get Methods ****************/
	public String getiFaqcategoryId() {
		return this.iFaqcategoryId;
	}

	public String getvTitle_EN() {
		return this.vTitle_EN;
	}

	public String getQuestions() {
		return this.Questions;
	}
	
	public String getICON_url() {
		return this.ICON_url;
	}

	public void setICON_url(String ICON_url) {
		this.ICON_url = ICON_url;
	}
}

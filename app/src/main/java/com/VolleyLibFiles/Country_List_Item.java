package com.VolleyLibFiles;


import java.util.ArrayList;

public class Country_List_Item {

	private String img_flag, country_name, country_name_phone,country_Code;

	public Country_List_Item() {
	}

	public Country_List_Item(String img_flag, String country_name, String country_name_phone) {

		this.img_flag = img_flag;
		this.country_name = country_name;
		this.country_name_phone = country_name_phone;
	}

	public String getimg_flag() {
		return img_flag;
	}

	public void setimg_flag(String img_flag) {
		this.img_flag = img_flag;
	}

	public String getcountry_name() {
		return country_name;
	}

	public void setcountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getcountry_name_phone() {
		return country_name_phone;
	}

	public void setcountry_name_phone(String country_name_phone) {
		this.country_name_phone = country_name_phone;
	}

	public String getCountry_Code() {
		return this.country_Code;
	}

	public void setCountry_Code(String country_Code) {
		this.country_Code = country_Code;
	}

}


/*
import java.util.ArrayList;

public class Country_List_Item {

	private String img_flag, country_name, country_name_phone;

	public Country_List_Item() {
	}

	public Country_List_Item(String img_flag, String country_name, String country_name_phone) {

		this.img_flag = img_flag;
		this.country_name = country_name;
		this.country_name_phone = country_name_phone;
	}

	public String getimg_flag() {
		return img_flag;
	}

	public void setimg_flag(String img_flag) {
		this.img_flag = img_flag;
	}

	public String getcountry_name() {
		return country_name;
	}

	public void setcountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getcountry_name_phone() {
		return country_name_phone;
	}

	public void setcountry_name_phone(String country_name_phone) {
		this.country_name_phone = country_name_phone;
	}

}
*/
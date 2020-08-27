package com.VolleyLibFiles;

import java.util.ArrayList;

public class BookingHistroy_item {

	private String driver_id, DriverName, MerchatID, StartDate, latitude,
	longitude, driver_img,total_fare,status,rating,car_type,trip_json,CurrencySymbol;

	public BookingHistroy_item() {
	}

	public BookingHistroy_item(String driver_id, String DriverName, String StartDate,
			String latitude, String longitude, String driver_img,
			String total_fare, String status, String rating,String Cartype,String trip_json) {

		this.driver_id = driver_id;
		this.DriverName = DriverName;
		
		this.StartDate = StartDate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.driver_img = driver_img;
		this.total_fare = total_fare;
		this.status = status;
		this.rating=rating;
		this.car_type=Cartype;
		this.trip_json=trip_json;
	}

	public String getdriver_id() {
		return driver_id;
	}

	public void setdriver_id(String driver_id) {
		this.driver_id = driver_id;
	}
	
	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String DriverName) {
		this.DriverName = DriverName;
	}
	
	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String StartDate) {
		this.StartDate = StartDate;
	}
	public String getlatitude() {
		return latitude;
	}

	public void setlatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getlongitude() {
		return longitude;
	}

	public void setlongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getdriver_img() {
		return driver_img;
	}

	public void setdriver_img(String driver_img) {
		this.driver_img = driver_img;
	}
	
	
	public String gettotal_fare() {
		return total_fare;
	}

	public void settotal_fare(String total_fare) {
		this.total_fare = total_fare;
	}
	
	
	public String getstatus() {
		return status;
	}

	public void setstatus(String status) {
		this.status = status;
	}
	
	
	public String getrating() {
		return rating;
	}

	public void setrating(String rating) {
		this.rating = rating;
	}
	
	public String getcarType() {
		return car_type;
	}

	public void setCarType(String cartype) {
		this.car_type = cartype;
	}

	public String getTrip_json() {
		return trip_json;
	}

	public void setTrip_json(String trip_json) {
		this.trip_json = trip_json;
	}

	public String getCurrencySymbol() {
		return CurrencySymbol;
	}

	public void setCurrencySymbol(String CurrencySymbol) {
		this.CurrencySymbol = CurrencySymbol;
	}

}

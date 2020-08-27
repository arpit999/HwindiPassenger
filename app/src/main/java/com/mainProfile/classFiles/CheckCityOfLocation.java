package com.mainProfile.classFiles;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import com.hwindiapp.passenger.Mainprofile_Activity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

public class CheckCityOfLocation extends AsyncTask<String, String, String> {

	Mainprofile_Activity mainprofileActivity;
	Context mContext;
	double sourceLocation_latitude, sourceLocation_longitude;

	public CheckCityOfLocation(Mainprofile_Activity mainprofileActivity, Context context,
			double sourceLocation_latitude, double sourceLocation_longitude) {
		// TODO Auto-generated constructor stub
		this.mainprofileActivity = mainprofileActivity;
		this.mContext = context;
		this.sourceLocation_latitude = sourceLocation_latitude;
		this.sourceLocation_longitude = sourceLocation_longitude;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		String cityOflocation_str = "";

		GetCityOfLocation getCity_Location = new GetCityOfLocation(mainprofileActivity.getApplicationContext());

		try {
			cityOflocation_str = getCity_Location.getCity(sourceLocation_latitude, sourceLocation_longitude);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cityOflocation_str = getCity_GeoCoder(sourceLocation_latitude, sourceLocation_longitude);
			// cityOflocation_str="";
		}

		// String CityName = getCity(sourceLocation_latitude,
		// sourceLocation_longitude);

//		Log.d("Check In city CityName", "CityName:" + cityOflocation_str);
		return cityOflocation_str;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		Log.d("Check In city RESULTS CityName", "CityName:" + result);

		if (result != null) {
			mainprofileActivity.updateDriverMarkersFrmMap(result, sourceLocation_latitude, sourceLocation_longitude);
		}

	}

	public String getCity_GeoCoder(double lat, double lon) {
		Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
		List<Address> addresses;
		String cityName = "";
		try {
			addresses = geocoder.getFromLocation(lat, lon, 1);
			if (addresses.size() > 0) {
				cityName = addresses.get(0).getLocality();
//				Log.d("CITY:", "");
//				Log.d("CITY:", "" + cityName);
			} else {
				cityName = "";
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cityName = "";
		}
		return cityName;
	}

}

package com.mainProfile.classFiles;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hwindiapp.passenger.R;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

public class GetCityOfLocation {

	String url_geoCoder = "https://maps.google.com/maps/api/geocode/json?address=%s&sensor=true";
	String locality_str = "locality";

	Context mContext;

	public GetCityOfLocation(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public String getCity(double lat, double lon) throws JSONException {
		String cityName = "";
		boolean error = false;

		String ReverseGeoCode_response = "";
		// google_api_get_address_from_location_serverApi
		String final_url_reverseGeoCode = String.format(url_geoCoder, "" + lat + "," + lon);
		final_url_reverseGeoCode = final_url_reverseGeoCode + "&key="
				+ mContext.getResources().getString(R.string.google_api_get_address_from_location_serverApi);
//		HttpClient client = new DefaultHttpClient();
//		// Log.e("city Url", ":" + final_url_reverseGeoCode);
//
//		HttpGet httpGet = new HttpGet(final_url_reverseGeoCode);
//		try {
//			HttpResponse response = client.execute(httpGet);
//
//			HttpEntity httpEntity = response.getEntity();
//			ReverseGeoCode_response = EntityUtils.toString(httpEntity);
//			// Log.e("ReverseGeoCode_response:", "ReverseGeoCode_response : " +
//			// ReverseGeoCode_response);
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			error = true;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			error = true;
//		}
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(final_url_reverseGeoCode);
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			ReverseGeoCode_response = OutputStreamReader.readStream(in);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = true;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}

		}

		if (error == false) {

			JSONObject obj_results = new JSONObject(ReverseGeoCode_response);
			JSONArray obj_routes_arr = obj_results.getJSONArray("results");

			if (obj_routes_arr.length() > 0) {
				JSONObject obj_temp = obj_routes_arr.getJSONObject(0);
				JSONArray arr_address_components = obj_temp.getJSONArray("address_components");

				boolean contentReached = false;
				for (int i = 0; i < arr_address_components.length(); i++) {
					JSONObject obj_temp_address = arr_address_components.getJSONObject(i);
					JSONArray arr_obj_types = obj_temp_address.getJSONArray("types");

					for (int j = 0; j < arr_obj_types.length(); j++) {

						String isLocalityOrNot = arr_obj_types.getString(j);

						if (isLocalityOrNot.equals(locality_str)) {

							String locality_longName = obj_temp_address.getString("long_name");
							cityName = locality_longName;
							contentReached = true;
							break;
						}
					}

					if (contentReached == true) {
						break;
					}
				}
			}
		}

		// Log.d("City of Location", "City::" + cityName);

		return cityName;
	}

	// public String getCity(double lat, double lon) {
	// Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
	// List<Address> addresses = null;
	// String cityName = "";
	//
	// try {
	// addresses = geocoder.getFromLocation(lat, lon, 1);
	//
	// if (addresses.size() > 0) {
	// cityName = addresses.get(0).getLocality();
	//
	// }
	// Log.d("CITY:", "");
	// Log.d("CITY:", "" + cityName);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return cityName;
	// }

}

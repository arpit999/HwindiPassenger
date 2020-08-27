package com.mainProfile.classFiles;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/

import com.hwindiapp.passenger.Mainprofile_Activity;
import com.utils.CommonUtilities;

import android.os.AsyncTask;

public class Update_List_driver_nearest extends AsyncTask<String, String, String> {

	String CityName = "";
	boolean errorConnection = false;

	Mainprofile_Activity mainprofileActivity;

	public Update_List_driver_nearest(Mainprofile_Activity mainprofileActivity, String CityName) {
		// TODO Auto-generated constructor stub
		this.CityName = CityName;
		this.mainprofileActivity = mainprofileActivity;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		String responseString = "";
		if (CityName != null) {

			String baseUrl = CommonUtilities.SERVER_URL;
			String registerParam = "function=%s&location_user=%s";

			String registerUrl = "";
			try {
				registerUrl = baseUrl
						+ String.format(registerParam, "getAvailableTaxi", URLEncoder.encode(CityName, "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			HttpClient client = new DefaultHttpClient();
////			Log.e("Nearest Taxi URL", "Nearset : " + registerUrl);
//
//			HttpGet httpGet = new HttpGet(registerUrl);
//			try {
//				HttpResponse response1 = client.execute(httpGet);
//
//				HttpEntity httpEntity = response1.getEntity();
//				responseString = EntityUtils.toString(httpEntity);
//				// responseString = "{" + "taxi_drivers" + ":" +
//				// responseString + "}";
//
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				errorConnection = true;
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				errorConnection = true;
//			}
			
			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL(registerUrl);
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				responseString = OutputStreamReader.readStream(in);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorConnection = true;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}

			}

		}
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if(result!=null && !result.equals("") && result.trim().length()>4){
			mainprofileActivity.updateNearestTaxiList(result, errorConnection);
		}
		
	}

}

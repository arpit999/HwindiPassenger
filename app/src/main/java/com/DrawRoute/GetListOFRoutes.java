package com.DrawRoute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.android.gms.maps.model.PolylineOptions;

import android.os.AsyncTask;
import android.util.Log;

public class GetListOFRoutes {

	ArrayList<PolylineOptions> ListOfOptions_routes;

	// Fetches data from url passed
	public void Download_subLocations_Task(String url) {

		// For storing data from web service
		String data = "";

		try {
			// Fetching the data from web service
			data = downloadJson_route_locations(url);
		} catch (Exception e) {
			Log.d("Background Task", e.toString());
		}
		ParserTask parserTask = new ParserTask();
		// Invokes the thread for parsing the JSON data
		ListOfOptions_routes = parserTask.parse_jsonData(data);
	}

	public ArrayList<PolylineOptions> getListOfOptions_routes() {
		return ListOfOptions_routes;
	}

	public String downloadJson_route_locations(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}
}

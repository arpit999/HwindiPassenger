package com.hwindiapp.passenger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.DrawRoute.DirectionsJSONParser;
import com.adapter.PlaceAutocompleteAdapter;
import com.fonts.Text.ClearableEditText;
import com.fonts.Text.ClearableEditText.Listener;
import com.fonts.Text.MyTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.plus.Plus;
import com.mainProfile.classFiles.OutputStreamReader;
import com.utils.CommonUtilities;
import com.utils.SphericalUtil;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FareCalculationActivity extends AppCompatActivity
		implements GoogleApiClient.OnConnectionFailedListener, ConnectionCallbacks, OnMapReadyCallback {

//	ActionBar actionBar;
	TextView text_header;

	GoogleMap Gmap;
	SupportMapFragment map_fragment_fare_calc;

	Polyline polyline;

	Marker destination_point_marker;
	Marker source_point_marker;

	ImageView fare_loader_img;

	protected GoogleApiClient mGoogleApiClient;

	private PlaceAutocompleteAdapter mAdapter;

	private ClearableEditText mAutocompleteView;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

	double sourceLocation_latitude = 0.0;
	double sourceLocation_longitude = 0.0;
	String sourceLocation_address = "";

	double destinationLocation_latitude = 0.0;
	double destinationLocation_longitude = 0.0;
	String destinationLocation_address = "";

	String selectedCarType_str = "";

	AnimationDrawable frameAnimation;

	boolean destinationChoose = false;

	MyTextView txt_base_fare_value;
	MyTextView txt_distance_value;
	MyTextView txt_distance_money_value;
	MyTextView txt_minutes_value;
	MyTextView txt_minutes_money_value;

	// MyTextView required_time_txt;
	// MyTextView distance_txt;
	MyTextView start_location;
	MyTextView end_location;

	MyTextView money_value_txt;
	RelativeLayout detail_container;

	MyTextView txt_base_fare_header;

	String fare_config[];

	DBConnect dbConnect;

	String language_labels_get_frm_sqLite = "";
	String LBL_FARE_CALC_TXT_str = "";
	String LBL_NOT_ABEL_PERFORM_TXT_str = "";
	String LBL_NO_ROUTE_FOUND_TXT_str = "";
	String LBL_ENTER_DESTINATION_HINT_TXT_str = "";
	String LBL_BASE_FARE_SMALL_TXT_str = "";
	String LBL_MIN_FARE_TXT_str = "";
	String LBL_MINUTES_TXT_str = "";
	String LBL_KM_DISTANCE_TXT_str = "";
	
	String defaultCurrencySign = "";
	String user_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fare_calculation);

		ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
		back_navigation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FareCalculationActivity.super.onBackPressed();
			}
		});

		dbConnect = new DBConnect(this, "UC_labels.db");

		text_header = (TextView) findViewById(R.id.text_header);
		
//		defaultCurrencySign=Mainprofile_Activity.defaultCurrencySign_str;

		map_fragment_fare_calc = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapV2_fare_calculation);

		map_fragment_fare_calc.getMapAsync(FareCalculationActivity.this);

		fare_loader_img = (ImageView) findViewById(R.id.fare_loader_img);
		fare_loader_img.setBackgroundResource(R.drawable.fare_calculation_loader);

		mAutocompleteView = (ClearableEditText) findViewById(R.id.selectDestination_editBox);
		// required_time_txt = (MyTextView)
		// findViewById(R.id.required_time_txt);
		// distance_txt = (MyTextView) findViewById(R.id.distance_txt);
		txt_base_fare_value = (MyTextView) findViewById(R.id.txt_base_fare_value);
		txt_distance_value = (MyTextView) findViewById(R.id.txt_distance_value);
		txt_distance_money_value = (MyTextView) findViewById(R.id.txt_distance_money_value);
		txt_minutes_value = (MyTextView) findViewById(R.id.txt_minutes_value);
		txt_minutes_money_value = (MyTextView) findViewById(R.id.txt_minutes_money_value);

		start_location = (MyTextView) findViewById(R.id.start_location);
		end_location = (MyTextView) findViewById(R.id.end_location);
		money_value_txt = (MyTextView) findViewById(R.id.money_value_txt);

		detail_container = (RelativeLayout) findViewById(R.id.detail_container);

		frameAnimation = (AnimationDrawable) fare_loader_img.getBackground();

		txt_base_fare_header = (MyTextView) findViewById(R.id.txt_base_fare_header);
		// Start the animation (looped playback by default).

		/* Set Labels */
		getLanguageLabelsFrmSqLite();
		/* Set Labels Finished */

		Intent getSourceLocation = getIntent();

		sourceLocation_latitude = getSourceLocation.getDoubleExtra("SourceLocation_lattitude", 0.0);
		sourceLocation_longitude = getSourceLocation.getDoubleExtra("SourceLocation_longitude", 0.0);
		destinationLocation_latitude = getSourceLocation.getDoubleExtra("DestinationLocation_lattitude", 0.0);
		destinationLocation_longitude = getSourceLocation.getDoubleExtra("DestinationLocation_longitude", 0.0);
		destinationLocation_address = getSourceLocation.getStringExtra("DestinationLocation_address");
		sourceLocation_address = getSourceLocation.getStringExtra("SourceLocation_address");
		defaultCurrencySign = getSourceLocation.getStringExtra("currencySymbol");
		user_id = getSourceLocation.getStringExtra("UserId");

		fare_config = getSourceLocation.getStringExtra("fare_config").split("@");

		selectedCarType_str = getSourceLocation.getStringExtra("selectedCarType");

		mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

		mAutocompleteView.setListener(new Listener() {

			@Override
			public void didClearText() {
				// TODO Auto-generated method stub
				detail_container.setVisibility(View.INVISIBLE);
			}
		});

		if (checkPlayServices()) {

			// Building the GoogleApi client
			buildGoogleApiClient();

		}

		if (destinationLocation_latitude != 0.0 && destinationLocation_longitude != 0.0
				&& !destinationLocation_address.equals("Not Set")) {

			mAutocompleteView.setText("" + destinationLocation_address);
			mAutocompleteView.dismissDropDown();

			fare_loader_img.setVisibility(View.VISIBLE);

			if (frameAnimation != null) {

				frameAnimation.start();
			}

			new ConfigFareData(sourceLocation_latitude, sourceLocation_longitude, destinationLocation_latitude,
					destinationLocation_longitude).execute();
		}

	}

	@Override
	public void onMapReady(GoogleMap map) {
		// TODO Auto-generated method stub

		this.Gmap = map;

	}

	public void getLanguageLabelsFrmSqLite() {

		Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

		cursor.moveToPosition(0);

		language_labels_get_frm_sqLite = cursor.getString(0);

		JSONObject obj_language_labels = null;

		try {
			obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);

			LBL_FARE_CALC_TXT_str = obj_language_labels.getString("LBL_FARE_CALC_TXT");
			LBL_NOT_ABEL_PERFORM_TXT_str = obj_language_labels.getString("LBL_NOT_ABEL_PERFORM_TXT");
			LBL_NO_ROUTE_FOUND_TXT_str = obj_language_labels.getString("LBL_NO_ROUTE_FOUND_TXT");
			LBL_ENTER_DESTINATION_HINT_TXT_str = obj_language_labels.getString("LBL_ENTER_DESTINATION_HINT_TXT");
			LBL_BASE_FARE_SMALL_TXT_str = obj_language_labels.getString("LBL_BASE_FARE_SMALL_TXT");
			LBL_MIN_FARE_TXT_str = obj_language_labels.getString("LBL_MIN_FARE_TXT");
			LBL_MINUTES_TXT_str = obj_language_labels.getString("LBL_MINUTES_TXT");
			LBL_KM_DISTANCE_TXT_str = obj_language_labels.getString("LBL_KM_DISTANCE_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {

			text_header.setText("" + LBL_FARE_CALC_TXT_str);
			mAutocompleteView.setHint("" + LBL_ENTER_DESTINATION_HINT_TXT_str);
			txt_base_fare_header.setText("" + LBL_BASE_FARE_SMALL_TXT_str);

		}

	}

	/**
	 * Method to verify google play services on the device
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				// Toast.makeText(getApplicationContext(), "" +
				// LBL_DEVICE_NOT_SUPPORTED_TXT_str, Toast.LENGTH_LONG)
				// .show();
				// finish();
			}
			return false;
		}
		return true;
	}

	private void buildGoogleApiClient() {
		// TODO Auto-generated method stub

		mGoogleApiClient = new GoogleApiClient.Builder(this).addOnConnectionFailedListener(this)
				.addConnectionCallbacks(this).addScope(Plus.SCOPE_PLUS_LOGIN)
				/* .enableAutoManage(this, 0 clientId , this) */
				.addApi(Places.GEO_DATA_API).addApi(LocationServices.API).build();
		mGoogleApiClient.connect();

		setLocation(sourceLocation_latitude, sourceLocation_longitude);

	}

	private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			/*
			 * Retrieve the place ID of the selected item from the Adapter. The
			 * adapter stores each Place suggestion in a PlaceAutocomplete
			 * object from which we read the place ID.
			 */
			hideKeyboard();

			final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
			final String placeId = String.valueOf(item.placeId);
			// Log.i(TAG, "Autocomplete item selected: " + item.description);

			/*
			 * Issue a request to the Places Geo Data API to retrieve a Place
			 * object with additional details about the place.
			 */

			fare_loader_img.setVisibility(View.VISIBLE);

			if (frameAnimation != null) {

				frameAnimation.start();
			}

			PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
			placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

			// Toast.makeText(getApplicationContext(), "Clicked: " +
			// item.description, Toast.LENGTH_SHORT).show();
			// Log.i(TAG, "Called getPlaceById to get Place details for " +
			// item.placeId);
		}
	};

	private void hideKeyboard() {
		// Check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * Callback for results from a Places Geo Data API query that shows the
	 * first place result in the details view on screen.
	 */
	private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
		@Override
		public void onResult(PlaceBuffer places) {
			if (!places.getStatus().isSuccess()) {
				// Request did not complete successfully
				// Log.e(TAG, "Place query did not complete. Error: " +
				// places.getStatus().toString());
				places.release();
				return;
			}

			// Get the Place object from the buffer.
			final Place place = places.get(0);

			LatLng selected_destination_place_location = place.getLatLng();

			destinationLocation_address = "" + place.getAddress();
			new ConfigFareData(sourceLocation_latitude, sourceLocation_longitude,
					selected_destination_place_location.latitude, selected_destination_place_location.longitude)
							.execute();

			places.release();
		}
	};

	public void setLocation(double latitude, double longitude) {
		LatLng user_location = new LatLng(latitude, longitude);

		// mAdapter = new PlaceAutocompleteAdapter(this,
		// android.R.layout.simple_list_item_1,
		// mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);

		mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1, mGoogleApiClient,
				convertCenterAndRadiusToBounds(user_location, 500000), null);

		mAutocompleteView.setAdapter(mAdapter);

	}

	public LatLngBounds convertCenterAndRadiusToBounds(LatLng center, double radius) {
		LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
		LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
		return new LatLngBounds(southwest, northeast);
	}

	public class ConfigFareData extends AsyncTask<String, String, String> {

		double source_point_latitude;
		double source_point_longitude;
		double destination_point_latitude;
		double destination_point_longitude;

		String distance_json_frm_google = "";
		String city_str = "";

		String time_to_destination = "";
		String km_to_destination = "";

		PolylineOptions lineOptions = new PolylineOptions();
		boolean status = true;

		public ConfigFareData(double source_point_latitude, double source_point_longitude,
				double destination_point_latitude, double destination_point_longitude) {
			// TODO Auto-generated constructor stub

			this.source_point_latitude = source_point_latitude;
			this.source_point_longitude = source_point_longitude;
			this.destination_point_latitude = destination_point_latitude;
			this.destination_point_longitude = destination_point_longitude;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		public String getFareJson() {
			String responseString = "";

			return responseString;
		}

		public String getDistanceFrmGoogleDrirection() {
			String responseString = "";

			String baseUrl = CommonUtilities.GOOGLE_SERVER_URL_ROUTE;
			String registerParam = "origin=%s&destination=%s&sensor=%s";

			String registerUrl = baseUrl
					+ String.format(registerParam, "" + source_point_latitude + "," + source_point_longitude,
							"" + destination_point_latitude + "," + destination_point_longitude, "true");

			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL(registerUrl);
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				responseString = OutputStreamReader.readStream(in);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				errorTOGetData = true;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}

			}

			return responseString;
		}

		public void parseGoogleDirectionJson(String json_direction) {

			JSONObject json = null;
			JSONArray routeArray;
			JSONObject routes;
			JSONArray newTempARr = null;
			JSONObject newDisTimeOb;
			JSONObject distOb;
			JSONObject timeOb;
			String distance_str = "";
			String time_str = "";
			try {
				json = new JSONObject(json_direction);

				String status_str = json.getString("status");

				if (status_str.equals("OK")) {
					routeArray = json.getJSONArray("routes");
					routes = routeArray.getJSONObject(0);
					newTempARr = routes.getJSONArray("legs");
					newDisTimeOb = newTempARr.getJSONObject(0);
					distOb = newDisTimeOb.getJSONObject("distance");
					timeOb = newDisTimeOb.getJSONObject("duration");
					distance_str = distOb.getString("value");
					time_str = timeOb.getString("value");
				} else {
					status = false;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (json != null && status == true) {

				List<List<HashMap<String, String>>> routes_list = null;
				DirectionsJSONParser parser = new DirectionsJSONParser();
				routes_list = parser.parse(json);

				ArrayList<LatLng> points = new ArrayList<LatLng>();

				if (routes_list.size() > 0) {
					// Fetching i-th route
					List<HashMap<String, String>> path = routes_list.get(0);

					// Fetching all the points in i-th route
					for (int j = 0; j < path.size(); j++) {
						HashMap<String, String> point = path.get(j);

						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);

						points.add(position);

					}

					// Adding all the points in the route to LineOptions
					lineOptions.addAll(points);
					lineOptions.width(18);
					lineOptions.color(Color.BLUE);
					// Polyline polyline=map.addPolyline(lineOptions);
					// polyline_all.add(polyline);
				}

				Long time_value = (Long.valueOf(time_str).longValue()) / 60;
				String time_str_numberOnly = "" + time_value;
				// String distance_str_numberOnly =
				// distance_str.replaceAll("[^0-9]", "");
				Long distance_value = (Long.valueOf(distance_str).longValue()) / 1000;
				String distance_str_numberOnly = "" + distance_value;

				time_to_destination = time_str_numberOnly;
				km_to_destination = distance_str_numberOnly;

			} else {
				time_to_destination = "Error";
				km_to_destination = "Error";
			}

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			distance_json_frm_google = getDistanceFrmGoogleDrirection();

			city_str = getCity(source_point_latitude, source_point_longitude);

			parseGoogleDirectionJson(distance_json_frm_google);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (status == true) {
				if (city_str.equals("No Results") || km_to_destination.equals("Error")
						|| time_to_destination.equals("Error")) {

					fare_loader_img.setVisibility(View.GONE);

					if (frameAnimation != null) {

						frameAnimation.stop();
					}

					Toast.makeText(FareCalculationActivity.this, "" + LBL_NOT_ABEL_PERFORM_TXT_str, Toast.LENGTH_LONG)
							.show();

				} else {

					new CalcFare(city_str, time_to_destination, km_to_destination, selectedCarType_str, lineOptions,
							sourceLocation_latitude, sourceLocation_longitude, destination_point_latitude,
							destination_point_longitude).execute();
				}
			} else {
				Toast.makeText(FareCalculationActivity.this, "" + LBL_NO_ROUTE_FOUND_TXT_str, Toast.LENGTH_LONG).show();
			}

		}

	}

	public class CalcFare extends AsyncTask<String, String, String> {

		String city = "";
		String time = "";
		String distance = "";
		String selectedCarType_str = "";

		String fare_final = "";

		PolylineOptions lineOptions;

		double source_point_latitude;
		double source_point_longitude;
		double destination_point_latitude;
		double destination_point_longitude;

		public CalcFare(String city, String time, String distance, String selectedCarType_str,
				PolylineOptions lineOptions, double source_point_latitude, double source_point_longitude,
				double destination_point_latitude, double destination_point_longitude) {
			// TODO Auto-generated constructor stub
			this.city = city;
			this.time = time;
			this.distance = distance;
			this.selectedCarType_str = selectedCarType_str;
			this.lineOptions = lineOptions;

			this.source_point_latitude = source_point_latitude;
			this.source_point_longitude = source_point_longitude;
			this.destination_point_latitude = destination_point_latitude;
			this.destination_point_longitude = destination_point_longitude;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		public String getFareDeatils() {
			boolean error = false;
			String responseString = "";
			String baseUrl = CommonUtilities.SERVER_URL_EASTIMATE_CALC_FARE;

			String calcEstimateFareURL = String.format(baseUrl, "" + city, "" + distance, "" + time,
					"" + selectedCarType_str,"" + user_id);

			Log.d("calcEstimateFareURL","::"+calcEstimateFareURL);
			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL(calcEstimateFareURL);
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				responseString = OutputStreamReader.readStream(in);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = true;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}

			}

			if (error == true) {
				responseString = "error";
			}

			return responseString;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			fare_final = getFareDeatils();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			fare_loader_img.setVisibility(View.GONE);

			detail_container.setVisibility(View.VISIBLE);

			if (frameAnimation != null) {

				frameAnimation.stop();
			}

			if (fare_final != null && !fare_final.equals("error") && !fare_final.equals("")
					&& fare_final.trim().length() > 0) {

				MarkerOptions markerOptions_sourceLocation = new MarkerOptions();
				markerOptions_sourceLocation.position(new LatLng(sourceLocation_latitude, sourceLocation_longitude));
				markerOptions_sourceLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.source_marker))
						.anchor(0.5f, 0.5f);

				MarkerOptions markerOptions_destinationLocation = new MarkerOptions();
				markerOptions_destinationLocation
						.position(new LatLng(destination_point_latitude, destination_point_longitude));
				markerOptions_destinationLocation
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker)).anchor(0.5f, 0.5f);

				if (polyline != null) {
					polyline.remove();
				}
				if (source_point_marker != null) {
					source_point_marker.remove();
				}

				if (destination_point_marker != null) {
					destination_point_marker.remove();
				}
				if (Gmap != null) {
					Gmap.clear();
					source_point_marker = Gmap.addMarker(markerOptions_sourceLocation);
					destination_point_marker = Gmap.addMarker(markerOptions_destinationLocation);

					polyline = Gmap.addPolyline(lineOptions);

					LatLngBounds.Builder builder = new LatLngBounds.Builder();
					builder.include(new LatLng(sourceLocation_latitude, sourceLocation_longitude));
					builder.include(new LatLng(destination_point_latitude, destination_point_longitude));

					LatLngBounds bounds = builder.build();
					int padding = 100; // offset from edges of the map in pixels

					CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
					Gmap.moveCamera(cu);

					// moveToBounds(polyline);
				}

				// required_time_txt.setText("" + time + " minutes");
				// distance_txt.setText("" + distance + " km");

//				float distance_fare = Float.parseFloat(distance) * Float.parseFloat(fare_config[2]);
//				float time_fare = Float.parseFloat(time) * Float.parseFloat(fare_config[1]);
//
//				txt_base_fare_value.setText(fare_config[3] + " " + fare_config[0]);
//				txt_distance_value.setText("" + distance + " " + LBL_KM_DISTANCE_TXT_str);
//				txt_distance_money_value.setText("" + distance_fare);
//				txt_minutes_value.setText("" + time + " " + LBL_MINUTES_TXT_str);
//				txt_minutes_money_value.setText("" + time_fare);
//
//				start_location.setText("" + sourceLocation_address);
//				end_location.setText("" + destinationLocation_address);
////				Log.d("fare_final", "fare_final:" + fare_final);
//
//				money_value_txt.setText(LBL_MIN_FARE_TXT_str + ": " + fare_final + " "+defaultCurrencySign);
				try {
					JSONObject obj_temp = new JSONObject(fare_final);
					String fare_distance = obj_temp.getString("fPricePerKM");
					String fare_time = obj_temp.getString("fPricePerMin");
					String iBaseFare = obj_temp.getString("iBaseFare");
					String total_fare = obj_temp.getString("total_fare");


					txt_base_fare_value.setText(fare_config[3] + " " + iBaseFare);
					txt_distance_value.setText("" + distance + " " + LBL_KM_DISTANCE_TXT_str);
					txt_distance_money_value.setText("" + fare_distance);
					txt_minutes_value.setText("" + time + " " + LBL_MINUTES_TXT_str);
					txt_minutes_money_value.setText("" + fare_time);

					start_location.setText("" + sourceLocation_address);
					end_location.setText("" + destinationLocation_address);
//				Log.d("fare_final", "fare_final:" + fare_final);

					money_value_txt.setText(LBL_MIN_FARE_TXT_str + ": " + total_fare + " " + defaultCurrencySign);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(FareCalculationActivity.this, "" + LBL_NOT_ABEL_PERFORM_TXT_str, Toast.LENGTH_LONG)
						.show();
			}
		}

	}

	private void moveToBounds(Polyline p) {

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		// for (int i = 0; i < p.getPoints().size(); i++) {
		// builder.include(p.getPoints().get(i));
		// }

		builder.include(p.getPoints().get(0));

		// builder.include(p.getPoints().get((int)p.getPoints().size()/4));
		//// builder.include(p.getPoints().get((int)p.getPoints().size()/2));
		// builder.include(p.getPoints().get((int)(p.getPoints().size()/1.322)));
		builder.include(p.getPoints().get((int) p.getPoints().size() - 1));

		LatLngBounds bounds = builder.build();
		int padding = 100; // offset from edges of the map in pixels

		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		Gmap.moveCamera(cu);
	}

	public String getCity(double lat, double lon) {
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		List<Address> addresses;
		String cityName = "";

		boolean error = false;
		try {
			addresses = geocoder.getFromLocation(lat, lon, 1);
			cityName = addresses.get(0).getLocality();
//			Log.d("CITY:", "");
//			Log.d("CITY:", "" + cityName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = true;
		}
		if (error == true) {
			cityName = "No Results";
		}
		return cityName;
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

		// Location mLastLocation =
		// LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

		// if (mLastLocation != null) {
		//// Log.d("mLastLocation", "mLastLocation:" +
		// mLastLocation.getLatitude() + ":" + mLastLocation.getLongitude());
		//
		// setLocation(mLastLocation.getLatitude(),
		// mLastLocation.getLongitude());
		// } else {
		// setLocation(0.0, 0.0);
		// }

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}
}

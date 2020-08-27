package com.hwindiapp.passenger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

import com.adapter.PlaceAutocompleteAdapter;
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
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.Plus;
import com.mainProfile.classFiles.OutputStreamReader;
import com.utils.CommonUtilities;
import com.utils.SphericalUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddHomePlaceActivity extends AppCompatActivity
		implements GoogleApiClient.OnConnectionFailedListener, ConnectionCallbacks, OnMapReadyCallback {

//	ActionBar actionBar;
	TextView text_header;

	GoogleMap map;

	protected GoogleApiClient mGoogleApiClient;

	private PlaceAutocompleteAdapter mAdapter;

	private AutoCompleteTextView mAutocompleteView;

	private TextView mPlaceDetailsText;

	private TextView mPlaceDetailsAttribution;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

	Marker initial_home_location_marker;
	LatLng user_selected_latLng;

	ProgressDialog pDialog;

	RippleStyleButton add_home_place_btn;

	SharedPreferences mPrefs;

	String userSelectedLocationAddress = "";

	boolean locationAlreadySet = false;

	MyTextView hint_txt;

	DBConnect dbConnect;
	String language_labels_get_frm_sqLite = "";
	String LBL_ADD_HOME_BIG_TXT_str = "";
	String LBL_SETTING_LOCATION_TXT_str = "";
	String LBL_INFO_SET_TXT_str = "";
	String LBL_SOME_ERROR_TXT_str = "";
	String LBL_LONG_TOUCH_CHANGE_LOC_TXT_str = "";
	String LBL_ENTER_HOME_LOC_HINT_TXT_str="";
	String LBL_ADD_PLACE_TXT_str="";
	
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_home_place);
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		
		setSupportActionBar(mToolbar);

		ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
		back_navigation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddHomePlaceActivity.super.onBackPressed();
			}
		});
		dbConnect = new DBConnect(AddHomePlaceActivity.this, "UC_labels.db");

		text_header = (TextView) findViewById(R.id.text_header);
		mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.AddHomePlace_editBox);
		hint_txt = (MyTextView) findViewById(R.id.hint_txt);
		add_home_place_btn = (RippleStyleButton) findViewById(R.id.add_home_place_btn);

		/* set Language labels */
		getLanguageLabelsFrmSqLite();
		/* setting language label finished */

		mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

		SharedPreferences mpref_place = PreferenceManager.getDefaultSharedPreferences(AddHomePlaceActivity.this);

		String home_address_str = mpref_place.getString("userHomeLocationAddress", null);
		String userHomeLocationLatitude_str = mpref_place.getString("userHomeLocationLatitude", null);
		String userHomeLocationLongitude_str = mpref_place.getString("userHomeLocationLongitude", null);

		if (home_address_str != null && userHomeLocationLatitude_str != null && userHomeLocationLongitude_str != null) {

			double home_location_latitude = Double.parseDouble(userHomeLocationLatitude_str);
			double home_location_longitude = Double.parseDouble(userHomeLocationLongitude_str);

			user_selected_latLng = new LatLng(home_location_latitude, home_location_longitude);
			userSelectedLocationAddress = home_address_str;

			mAutocompleteView.setText("" + home_address_str);
			mAutocompleteView.dismissDropDown();

			locationAlreadySet = true;
		}

		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapV2_add_home_place);

		fm.getMapAsync(AddHomePlaceActivity.this);

		if (checkPlayServices()) {

			// Building the GoogleApi client
			buildGoogleApiClient();

		}

		mPrefs = PreferenceManager.getDefaultSharedPreferences(AddHomePlaceActivity.this);

		add_home_place_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("userHomeLocationLatitude", "" + user_selected_latLng.latitude);
				editor.putString("userHomeLocationLongitude", "" + user_selected_latLng.longitude);
				editor.putString("userHomeLocationAddress", "" + userSelectedLocationAddress);

				editor.commit();

				finish();
			}
		});

	}

	@Override
	public void onMapReady(GoogleMap gMap) {
		// TODO Auto-generated method stub

		this.map = gMap;

		this.map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				// marker.get
				marker.hideInfoWindow();

				return true;
			}
		});

		this.map.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng location_latLng) {
				// TODO Auto-generated method stub

				if (initial_home_location_marker != null) {
					initial_home_location_marker.remove();
					initial_home_location_marker = null;
				}

				user_selected_latLng = location_latLng;
				initial_home_location_marker = map
						.addMarker(new MarkerOptions().position(location_latLng).title("Hello"));

				GetAddressFromLocation process_address = new GetAddressFromLocation(location_latLng.latitude,
						location_latLng.longitude);
				process_address.execute();

			}
		});

	}

	public void getLanguageLabelsFrmSqLite() {

		Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

		cursor.moveToPosition(0);

		language_labels_get_frm_sqLite = cursor.getString(0);

		JSONObject obj_language_labels = null;
		try {
			obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
			LBL_ADD_HOME_BIG_TXT_str = obj_language_labels.getString("LBL_ADD_HOME_BIG_TXT");
			LBL_SETTING_LOCATION_TXT_str = obj_language_labels.getString("LBL_SETTING_LOCATION_TXT");
			LBL_INFO_SET_TXT_str = obj_language_labels.getString("LBL_INFO_SET_TXT");
			LBL_SOME_ERROR_TXT_str = obj_language_labels.getString("LBL_SOME_ERROR_TXT");
			LBL_LONG_TOUCH_CHANGE_LOC_TXT_str = obj_language_labels.getString("LBL_LONG_TOUCH_CHANGE_LOC_TXT");
			LBL_ENTER_HOME_LOC_HINT_TXT_str= obj_language_labels.getString("LBL_ENTER_HOME_LOC_HINT_TXT");
			LBL_ADD_PLACE_TXT_str= obj_language_labels.getString("LBL_ADD_PLACE_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {
			text_header.setText("" + LBL_ADD_HOME_BIG_TXT_str);
			hint_txt.setText(""+LBL_LONG_TOUCH_CHANGE_LOC_TXT_str);
			mAutocompleteView.setHint(""+LBL_ENTER_HOME_LOC_HINT_TXT_str);
			add_home_place_btn.setText(""+LBL_ADD_PLACE_TXT_str);
		}

	}

	private void buildGoogleApiClient() {
		// TODO Auto-generated method stub

		mGoogleApiClient = new GoogleApiClient.Builder(this).addOnConnectionFailedListener(this)
				.addConnectionCallbacks(this).addScope(Plus.SCOPE_PLUS_LOGIN)
				/* .enableAutoManage(this, 0 clientId , this) */
				.addApi(Places.GEO_DATA_API).addApi(LocationServices.API).build();
		mGoogleApiClient.connect();

	}

	public LatLngBounds convertCenterAndRadiusToBounds(LatLng center, double radius) {
		LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
		LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
		return new LatLngBounds(southwest, northeast);
	}

	/**
	 * Listener that handles selections from suggestions from the
	 * AutoCompleteTextView that displays Place suggestions. Gets the place id
	 * of the selected item and issues a request to the Places Geo Data API to
	 * retrieve more details about the place.
	 *
	 * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
	 *      String...)
	 */
	private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			/*
			 * Retrieve the place ID of the selected item from the Adapter. The
			 * adapter stores each Place suggestion in a PlaceAutocomplete
			 * object from which we read the place ID.
			 */
			final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
			final String placeId = String.valueOf(item.placeId);
			// Log.i(TAG, "Autocomplete item selected: " + item.description);

			/*
			 * Issue a request to the Places Geo Data API to retrieve a Place
			 * object with additional details about the place.
			 */

			hideKeyboard();
			pDialog = new ProgressDialog(AddHomePlaceActivity.this, R.style.DialogTheme_custom);
			pDialog.setMessage("" + LBL_SETTING_LOCATION_TXT_str);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.setCancelable(false);
			pDialog.show();

			PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
			placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

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

			if (pDialog != null) {
				pDialog.dismiss();
			}
			// Get the Place object from the buffer.
			final Place place = places.get(0);

			LatLng place_location = place.getLatLng();

			user_selected_latLng = place_location;

			userSelectedLocationAddress = "" + place.getAddress();

			if (initial_home_location_marker != null) {
				initial_home_location_marker.remove();
				initial_home_location_marker = null;
			}

			// Log.d("userSelectedLocationAddress:",
			// "userSelectedLocationAddress:" + userSelectedLocationAddress);

			initial_home_location_marker = map.addMarker(new MarkerOptions().position(place_location).title("Hello"));

			CameraUpdate cu = CameraUpdateFactory.newLatLng(place_location);

			map.moveCamera(cu);

			// Log.d("place_location:", "place_location:" + place_location);
			// Format details of the place for display and show it in a
			// TextView.
			// mPlaceDetailsText.setText(formatPlaceDetails(getResources(),
			// place.getName(),
			// place.getId(), place.getAddress(), place.getPhoneNumber(),
			// place.getWebsiteUri()));
			//
			// // Display the third party attributions if set.
			// final CharSequence thirdPartyAttribution =
			// places.getAttributions();
			// if (thirdPartyAttribution == null) {
			// mPlaceDetailsAttribution.setVisibility(View.GONE);
			// } else {
			// mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
			// mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
			// }

			// Log.i(TAG, "Place details received: " + place.getName());

			places.release();
		}
	};

	// private static Spanned formatPlaceDetails(Resources res, CharSequence
	// name, String id,
	// CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
	//// Log.e(TAG, res.getString(R.string.place_details, name, id, address,
	// phoneNumber,
	//// websiteUri));
	// return Html.fromHtml(res.getString(R.string.place_details, name, id,
	// address, phoneNumber,
	// websiteUri));
	//
	// }

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

	public void setLocation(double latitude, double longitude) {

		LatLng user_location = new LatLng(latitude, longitude);

		// mAdapter = new PlaceAutocompleteAdapter(this,
		// android.R.layout.simple_list_item_1,
		// mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);

		mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1, mGoogleApiClient,
				convertCenterAndRadiusToBounds(user_location, 500000), null);

		mAutocompleteView.setAdapter(mAdapter);

		if (locationAlreadySet == false) {

			if (map != null) {
				initial_home_location_marker = map
						.addMarker(new MarkerOptions().position(user_location).title("Hello"));

				user_selected_latLng = user_location;
				int padding = 0; // offset from edges of the map in pixels
				CameraUpdate cu = CameraUpdateFactory
						.newLatLngBounds(convertCenterAndRadiusToBounds(user_location, 10000), padding);

				map.moveCamera(cu);

				GetAddressFromLocation process_address = new GetAddressFromLocation(user_location.latitude,
						user_location.longitude);
				process_address.execute();
			}

		} else if (map != null) {
			initial_home_location_marker = map
					.addMarker(new MarkerOptions().position(user_selected_latLng).title("Hello"));

			int padding = 0; // offset from edges of the map in pixels
			CameraUpdate cu = CameraUpdateFactory
					.newLatLngBounds(convertCenterAndRadiusToBounds(user_selected_latLng, 10000), padding);

			map.moveCamera(cu);
		}
	}

	public class GetAddressFromLocation extends AsyncTask<String, String, String> {

		double lat_add;
		double lon_add;

		boolean error_connection = false;

		String json_addresses = "";

		String Address_loc = "";

		public GetAddressFromLocation(double lat_add, double lon_add) {
			// TODO Auto-generated constructor stub
			this.lat_add = lat_add;
			this.lon_add = lon_add;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(AddHomePlaceActivity.this, R.style.DialogTheme_custom);
			pDialog.setMessage("" + LBL_INFO_SET_TXT_str);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		public String getAddress_json() {
			String responseString = "";

			String address_frm_location_url = CommonUtilities.GOOGLE_SERVER_URL_GET_ADDRESS;

			String url_final_get_address = String.format(address_frm_location_url, "" + lat_add + "," + lon_add);

//			HttpClient client = new DefaultHttpClient();
//			// Log.e("Address by Location", "URL : " + url_final_get_address);
//
//			HttpGet httpGet = new HttpGet(url_final_get_address);
//			try {
//				HttpResponse response = client.execute(httpGet);
//
//				HttpEntity httpEntity = response.getEntity();
//				responseString = EntityUtils.toString(httpEntity);
//				// Log.d("Address", "response : " + responseString);
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				error_connection = true;
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				error_connection = true;
//			}

			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL(url_final_get_address);
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				responseString = OutputStreamReader.readStream(in);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error_connection = true;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}

			}
			
			return responseString;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			json_addresses = getAddress_json();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pDialog != null) {
				pDialog.dismiss();
			}

			if (error_connection == false) {
				JSONObject obj_address_result;
				try {
					obj_address_result = new JSONObject(json_addresses);

					if (obj_address_result != null) {
						JSONArray obj_arr_results = obj_address_result.getJSONArray("results");
						JSONObject getAddress_frm_arr = obj_arr_results.getJSONObject(0);
						String[] finalAddress = getAddress_frm_arr.getString("formatted_address").split(",");

						boolean first_input = true;
						for (int i = 0; i < finalAddress.length; i++) {
							if (!finalAddress[i].contains("Unnamed") && !finalAddress[i].contains("null")) {

								if (i == 0 && !finalAddress[0].matches("[a-zA-Z]+")) {
									continue;
								}
								if (first_input == true) {
									Address_loc = finalAddress[i];
									first_input = false;
								} else {
									Address_loc = Address_loc + ", " + finalAddress[i];
								}

							}
						}

						// Log.d("Address:", ":add:" + Address_loc);
						userSelectedLocationAddress = "" + Address_loc;

						mAutocompleteView.setText("" + Address_loc);
						mAutocompleteView.dismissDropDown();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					error_connection = true;
				}
			} else {
				Toast.makeText(AddHomePlaceActivity.this, "" + LBL_SOME_ERROR_TXT_str, Toast.LENGTH_LONG).show();
			}

		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

		Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

		if (mLastLocation != null) {
			// Log.d("mLastLocation", "mLastLocation:" +
			// mLastLocation.getLatitude() + ":" +
			// mLastLocation.getLongitude());

			setLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
		} else {
			setLocation(0.0, 0.0);
		}

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

}

package com.hwindiapp.passenger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class SelectDestinationActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, ConnectionCallbacks, OnMapReadyCallback {

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

    RelativeLayout layout_dest_content;

    SupportMapFragment support_map_fragment_fm;

    RelativeLayout area_selected_home_work_place;
    MyTextView strip_separation;

    MyTextView workPlace;
    MyTextView homePlace;

    String dest_latitude = "";
    String dest_longitude = "";
    String dest_address = "";

    boolean dataFrmPrevious = false;

    MyTextView hint_txt;

    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";
    String LBL_SETTING_LOCATION_TXT_str = "";
    String LBL_INFO_SET_TXT_str = "";
    String LBL_SOME_ERROR_TXT_str = "";
    String LBL_ADD_DESTINATION_BTN_TXT_str = "";
    String LBL_LONG_TOUCH_CHANGE_LOC_TXT_str = "";
    String LBL_SEARCH_PLACE_HINT_TXT_str = "";
    String LBL_SELECT_DESTINATION_HEADER_TXT_str = "";

    private Toolbar mToolbar;

    String isSourceLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_destination);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);


        ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
        back_navigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SelectDestinationActivity.super.onBackPressed();
            }
        });

        Intent getExistingData = getIntent();

        dest_latitude = getExistingData.getStringExtra("Selected_latitude");
        dest_longitude = getExistingData.getStringExtra("Selected_longitude");
        dest_address = getExistingData.getStringExtra("Selected_address");
        isSourceLocation = getExistingData.getStringExtra("isSourceLocation");

        text_header = (TextView) findViewById(R.id.text_header);

        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.select_destination_location);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        layout_dest_content = (RelativeLayout) findViewById(R.id.layout_dest_content);

        dbConnect = new DBConnect(this, "UC_labels.db");

        hint_txt = (MyTextView) findViewById(R.id.hint_txt);

        area_selected_home_work_place = (RelativeLayout) findViewById(R.id.area_selected_home_work_place);
        strip_separation = (MyTextView) findViewById(R.id.strip_separation);
        workPlace = (MyTextView) findViewById(R.id.workPlace);
        homePlace = (MyTextView) findViewById(R.id.homePlace);

        add_home_place_btn = (RippleStyleButton) findViewById(R.id.add_destination_place_btn);

        support_map_fragment_fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapV2_add_home_place);

        support_map_fragment_fm.getMapAsync(SelectDestinationActivity.this);

        support_map_fragment_fm.getView().setVisibility(View.INVISIBLE);

        getLanguageLabelsFrmSqLite();

        if (checkPlayServices()) {

            buildGoogleApiClient();

        }

        if (!dest_latitude.equals("Not Set") && !dest_longitude.equals("Not Set") && !dest_address.equals("Not Set")) {
            mAutocompleteView.setText("" + dest_address);
            mAutocompleteView.dismissDropDown();

            userSelectedLocationAddress = dest_address;

            double dest_point_latitude = Double.parseDouble(dest_latitude);
            double dest_point_longitude = Double.parseDouble(dest_longitude);

            user_selected_latLng = new LatLng(dest_point_latitude, dest_point_longitude);

            add_home_place_btn.setVisibility(View.VISIBLE);

            dataFrmPrevious = true;

            if (support_map_fragment_fm.getView().getVisibility() != View.VISIBLE) {
                support_map_fragment_fm.getView().setVisibility(View.VISIBLE);
            }

            if (initial_home_location_marker != null) {
                initial_home_location_marker = null;
            }

            layout_dest_content.setVisibility(View.GONE);
        }

        add_home_place_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // ft.show(fm).commit();

                Intent intent = new Intent();
                intent.putExtra("Selected_latitude", "" + user_selected_latLng.latitude);
                intent.putExtra("Selected_longitude", "" + user_selected_latLng.longitude);
                intent.putExtra("Selected_address", "" + userSelectedLocationAddress);
                setResult(RESULT_OK, intent);

                finish();

            }
        });

        SharedPreferences mpref_place = PreferenceManager.getDefaultSharedPreferences(SelectDestinationActivity.this);

        final String home_address_str = mpref_place.getString("userHomeLocationAddress", null);
        final String userHomeLocationLatitude_str = mpref_place.getString("userHomeLocationLatitude", null);
        final String userHomeLocationLongitude_str = mpref_place.getString("userHomeLocationLongitude", null);

        final String work_address_str = mpref_place.getString("userWorkLocationAddress", null);
        final String userWorkLocationLatitude_str = mpref_place.getString("userWorkLocationLatitude", null);
        final String userWorkLocationLongitude_str = mpref_place.getString("userWorkLocationLongitude", null);

        if (home_address_str != null && !home_address_str.equals("")) {
            area_selected_home_work_place.setVisibility(View.VISIBLE);
            homePlace.setVisibility(View.VISIBLE);
            layout_dest_content.setVisibility(View.GONE);
        }

        if (work_address_str != null && !work_address_str.equals("")) {
            if (area_selected_home_work_place.getVisibility() != View.VISIBLE) {
                area_selected_home_work_place.setVisibility(View.VISIBLE);
            }

            if (homePlace.getVisibility() == View.VISIBLE) {
                strip_separation.setVisibility(View.VISIBLE);
            }

            workPlace.setVisibility(View.VISIBLE);
        }

        homePlace.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mAutocompleteView.setText("" + home_address_str);
                mAutocompleteView.dismissDropDown();

                area_selected_home_work_place.setVisibility(View.GONE);

                double home_point_latitude = Double.parseDouble(userHomeLocationLatitude_str);
                double home_point_longitude = Double.parseDouble(userHomeLocationLongitude_str);

                userSelectedLocationAddress = home_address_str;

                user_selected_latLng = new LatLng(home_point_latitude, home_point_longitude);

                add_home_place_btn.setVisibility(View.VISIBLE);

                if (support_map_fragment_fm.getView().getVisibility() != View.VISIBLE) {
                    support_map_fragment_fm.getView().setVisibility(View.VISIBLE);
                }

                if (initial_home_location_marker != null) {
                    initial_home_location_marker = null;
                }
                initial_home_location_marker = map
                        .addMarker(new MarkerOptions().position(user_selected_latLng).title("Hello"));

                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(user_selected_latLng, 14.0f);

                map.moveCamera(cu);

                layout_dest_content.setVisibility(View.GONE);
            }
        });

        workPlace.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mAutocompleteView.setText("" + work_address_str);
                mAutocompleteView.dismissDropDown();

                area_selected_home_work_place.setVisibility(View.GONE);

                double work_point_latitude = Double.parseDouble(userWorkLocationLatitude_str);
                double work_point_longitude = Double.parseDouble(userWorkLocationLongitude_str);

                user_selected_latLng = new LatLng(work_point_latitude, work_point_longitude);

                userSelectedLocationAddress = work_address_str;

                add_home_place_btn.setVisibility(View.VISIBLE);

                if (support_map_fragment_fm.getView().getVisibility() != View.VISIBLE) {
                    support_map_fragment_fm.getView().setVisibility(View.VISIBLE);
                }

                if (initial_home_location_marker != null) {
                    initial_home_location_marker = null;
                }
                initial_home_location_marker = map
                        .addMarker(new MarkerOptions().position(user_selected_latLng).title("Hello"));

                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(user_selected_latLng, 14.0f);

                map.moveCamera(cu);

                layout_dest_content.setVisibility(View.GONE);
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
            LBL_SETTING_LOCATION_TXT_str = obj_language_labels.getString("LBL_SETTING_LOCATION_TXT");
            LBL_INFO_SET_TXT_str = obj_language_labels.getString("LBL_INFO_SET_TXT");
            LBL_SOME_ERROR_TXT_str = obj_language_labels.getString("LBL_SOME_ERROR_TXT");
            LBL_ADD_DESTINATION_BTN_TXT_str = obj_language_labels.getString("LBL_ADD_DESTINATION_BTN_TXT");
            LBL_LONG_TOUCH_CHANGE_LOC_TXT_str = obj_language_labels.getString("LBL_LONG_TOUCH_CHANGE_LOC_TXT");
            LBL_SEARCH_PLACE_HINT_TXT_str = obj_language_labels.getString("LBL_SEARCH_PLACE_HINT_TXT");
            LBL_SELECT_DESTINATION_HEADER_TXT_str = obj_language_labels.getString("LBL_SELECT_DESTINATION_HEADER_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj_language_labels != null) {

            hint_txt.setText("" + LBL_LONG_TOUCH_CHANGE_LOC_TXT_str);
            mAutocompleteView.setHint("" + LBL_SEARCH_PLACE_HINT_TXT_str);

            if (isSourceLocation != null && isSourceLocation.equals("true")) {
                text_header.setText("Add PickUp Location");
                add_home_place_btn.setText("Add Location");
            } else {
                text_header.setText("" + LBL_SELECT_DESTINATION_HEADER_TXT_str);
                add_home_place_btn.setText("" + LBL_ADD_DESTINATION_BTN_TXT_str);
            }

        }

    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        // TODO Auto-generated method stub

        this.map = gMap;

        if (dataFrmPrevious == true) {
            initial_home_location_marker = map
                    .addMarker(new MarkerOptions().position(user_selected_latLng).title("Hello"));

            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(user_selected_latLng, 14.0f);

            map.moveCamera(cu);

            area_selected_home_work_place.setVisibility(View.GONE);
        }
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

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            add_home_place_btn.setVisibility(View.VISIBLE);
            if (support_map_fragment_fm.getView().getVisibility() != View.VISIBLE) {
                support_map_fragment_fm.getView().setVisibility(View.VISIBLE);
            }

            layout_dest_content.setVisibility(View.GONE);

            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            hideKeyboard();
            pDialog = new ProgressDialog(SelectDestinationActivity.this, R.style.DialogTheme_custom);
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

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {

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

            initial_home_location_marker = map.addMarker(new MarkerOptions().position(place_location).title("Hello"));

            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(place_location, 14.0f);

            map.moveCamera(cu);

            places.release();
        }
    };

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

            }
            return false;
        }
        return true;
    }

    public void setLocation(double latitude, double longitude) {

        LatLng user_location = new LatLng(latitude, longitude);

        mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1, mGoogleApiClient,
                convertCenterAndRadiusToBounds(user_location, 500000), null);

        mAutocompleteView.setAdapter(mAdapter);

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
            pDialog = new ProgressDialog(SelectDestinationActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage("" + LBL_INFO_SET_TXT_str);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        public String getAddress_json() {
            String responseString = "";

            String address_frm_location_url = CommonUtilities.GOOGLE_SERVER_URL_GET_ADDRESS;

            String url_final_get_address = String.format(address_frm_location_url, "" + lat_add + "," + lon_add);

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
                Toast.makeText(SelectDestinationActivity.this, "" + LBL_SOME_ERROR_TXT_str, Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

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

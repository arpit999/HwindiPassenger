package com.hwindiapp.passenger;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.DrawRoute.Create_directionUrl;
import com.DrawRoute.GetListOFRoutes;
import com.fonts.Text.MyTextView;
import com.fonts.Text.MyTextViewCapital;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mainProfile.classFiles.SendEmailReceipt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookingDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    // ActionBar actionBar;

    GoogleMap map;

    String iTripId_str = "";

    String destination_address_str = "";
    String fare_str = "";
    String vRideNo_str = "";
    String TripStratTime = "";

    String trip_status_str = "";

    String trip_json_str = "";

    TextView text_header;
    MyTextViewCapital header_name_txt;

    MyTextView destination_address;
    MyTextView trip_start_date;
    MyTextView ride_no_txt;

    MyTextView name_driver_txt;

    byte[] byteArray_bit_driver_pic;

    String tEndDate = "";
    String tStartDate = "";

    String iDistance = "";

    String fServiceTax = "";

    String iFareParKm = "";
    // String fDistance="";
    String iFare = "";

    MyTextView thanks_header;
    MyTextView ride_no_header;
    MyTextView name_passenger_header;
    MyTextView name_driver_header;
    MyTextView trip_start_date_header;
    MyTextView destination_address_header;
    MyTextView name_passenger_txt;
    MyTextView txt_base_fare_header;
    MyTextView txt_base_fare_value;

    MyTextView txt_distance_value;
    MyTextView txt_distance_money_value;
    MyTextView txt_minutes_value;
    MyTextView txt_minutes_money_value;
    MyTextView txt_total_fare_header;
    MyTextView txt_total_fare_value;
    MyTextView fair_charges_txt;

    MyTextView tripStatus_txt;

    String tStartLat_str = "";
    String tStartLong_str = "";
    String tEndLat_str = "";
    String tEndLong_str = "";
    String startDate_str = "";
//	String startTripTime = "";

    String DriverName_str = "";

    String vVehicleType_str = "";
    String iBaseFare_str = "";
    String fDistance_str = "";
    String TripFareOfDistance_str = "";
    String TripFareOfMinutes_str = "";
    String iFare_str = "";
    String TripTimeInMinutes_str = "";
    String vTripPaymentMode_str = "";
    String CurrencySymbol_str = "";
    String discount_str = "";
    String eCancelled_str = "";
    String vCancelReason_str = "";
    String vCancelComment_str = "";
    String fCommision_str = "";

    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";
    String LBL_RECEIPT_HEADER_TXT_str = "";
    String LBL_DOLLAR_SIGN_TXT_str = "";
    String LBL_THANKS_RIDING_TXT_str = "";
    String LBL_RIDE_NO_TXT_str = "";
    String LBL_NUMBER_TXT_str = "";
    String LBL_PASSENGER_TXT_str = "";
    String LBL_DRIVER_TXT_str = "";
    String LBL_TRIP_REQUEST_DATE_TXT_str = "";
    String LBL_TRIP_DATE_TXT_str = "";
    String LBL_DROP_OFF_LOCATION_TXT_str = "";
    String LBL_CANCELED_TRIP_TXT_str = "";
    String LBL_FINISHED_TRIP_TXT_str = "";
    String LBL_TRIP_GOING_TXT_str = "";
    String LBL_AT_TXT_str = "";
    String LBL_KM_DISTANCE_TXT_str = "";
    String LBL_MINUTES_TXT_str = "";
    String LBL_PAYPAL_PAYMENT_TXT_str = "";
    String LBL_CASH_PAYMENT_TXT_str = "";
    String LBL_SUBTOTAL_TXT_str = "";
    String LBL_BASE_FARE_SMALL_TXT_str = "";
    String LBL_CHARGES_TXT_str = "";
    String LBL_GET_RECEIPT_TXT_str = "";
    String LBL_CHECK_INBOX_TXT_str = "";
    String LBL_FAILED_SEND_RECEIPT_EMAIL_TXT_str = "";
    String LBL_SEND_EMAIL_LOADING_TXT_str = "";
    String LBL_TIME_TXT_str = "";
    String LBL_PLATFORM_FEE_TXT_str = "";
    String LBL_DISCOUNT_APPLIED_TXT_str = "";

    RelativeLayout payment_cash_area;
    RelativeLayout payment_paypal_area;

    RelativeLayout trip_detail_area;

    MyTextView txt_paypal_payment;
    MyTextView txt_cash_payment;
    MyTextView txt_platform_fee_headerTxt;
    MyTextView txt_platform_fee_value;
    MyTextView discount_header;
    MyTextView discount_value;

    TableRow discountRow;
//	String defaultCurrencyCodeSign_str = "";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
        back_navigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                BookingDetailActivity.super.onBackPressed();
            }
        });

        dbConnect = new DBConnect(this, "UC_labels.db");

        text_header = (TextView) findViewById(R.id.text_header);

        header_name_txt = (MyTextViewCapital) findViewById(R.id.sub_text_header);
        header_name_txt.setVisibility(View.VISIBLE);
        header_name_txt.setTextColor(getResources().getColor(R.color.Receipt_BTN_Booking_detail_Label_color));

        thanks_header = (MyTextView) findViewById(R.id.thanks_header);
        ride_no_header = (MyTextView) findViewById(R.id.ride_no_header);
        name_passenger_header = (MyTextView) findViewById(R.id.name_passenger_header);
        name_driver_header = (MyTextView) findViewById(R.id.name_driver_header);
        trip_start_date_header = (MyTextView) findViewById(R.id.trip_start_date_header);
        destination_address_header = (MyTextView) findViewById(R.id.destination_address_header);
        txt_platform_fee_headerTxt = (MyTextView) findViewById(R.id.txt_platform_fee_headerTxt);
        txt_platform_fee_value = (MyTextView) findViewById(R.id.txt_platform_fee_value);
        discount_header = (MyTextView) findViewById(R.id.discount_header);
        discount_value = (MyTextView) findViewById(R.id.discount_value);

        discountRow = (TableRow) findViewById(R.id.discountRow);

        destination_address = (MyTextView) findViewById(R.id.destination_address);
        trip_start_date = (MyTextView) findViewById(R.id.trip_start_date);

        ride_no_txt = (MyTextView) findViewById(R.id.ride_no_txt);
        name_passenger_txt = (MyTextView) findViewById(R.id.name_passenger_txt);

        name_driver_txt = (MyTextView) findViewById(R.id.name_driver_txt);

        tripStatus_txt = (MyTextView) findViewById(R.id.tripStatus_txt);
        txt_base_fare_header = (MyTextView) findViewById(R.id.txt_base_fare_header);
        txt_base_fare_value = (MyTextView) findViewById(R.id.txt_base_fare_value);

        txt_distance_value = (MyTextView) findViewById(R.id.txt_distance_value);
        txt_distance_money_value = (MyTextView) findViewById(R.id.txt_distance_money_value);
        txt_minutes_value = (MyTextView) findViewById(R.id.txt_minutes_value);
        txt_minutes_money_value = (MyTextView) findViewById(R.id.txt_minutes_money_value);
        txt_total_fare_header = (MyTextView) findViewById(R.id.txt_total_fare_header);
        txt_total_fare_value = (MyTextView) findViewById(R.id.txt_total_fare_value);

        payment_cash_area = (RelativeLayout) findViewById(R.id.payment_cash_area);
        payment_paypal_area = (RelativeLayout) findViewById(R.id.payment_paypal_area);

        trip_detail_area = (RelativeLayout) findViewById(R.id.trip_detail_area);
        txt_paypal_payment = (MyTextView) findViewById(R.id.txt_paypal_payment);
        txt_cash_payment = (MyTextView) findViewById(R.id.txt_cash_payment);
        fair_charges_txt = (MyTextView) findViewById(R.id.fair_charges_txt);

//		defaultCurrencyCodeSign_str = Mainprofile_Activity.defaultCurrencySign_str;

		/* Set Labels */
        getLanguageLabelsFrmSqLite();
        /* Set Labels Finished */

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapV2_receipt_detail);

        fm.getMapAsync(this);
        // Getting Map for the SupportMapFragment

        map.getUiSettings().setZoomControlsEnabled(false);

        Intent getTrip_json = getIntent();

        trip_json_str = getTrip_json.getStringExtra("trip_json_str");

        Bundle extras = getIntent().getExtras();
        byteArray_bit_driver_pic = extras.getByteArray("driver_profile_pic");

        System.gc();

        Bitmap bit_driver_pic = BitmapFactory.decodeByteArray(byteArray_bit_driver_pic, 0,
                byteArray_bit_driver_pic.length);

        RoundedImageView img_driver_profile_pic = (RoundedImageView) findViewById(R.id.img_driver_profile_pic);

        img_driver_profile_pic.setImageBitmap(bit_driver_pic);

        parse_booking_history_detail(trip_json_str);

        header_name_txt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new SendEmailReceipt(BookingDetailActivity.this, iTripId_str, LBL_FAILED_SEND_RECEIPT_EMAIL_TXT_str,
                        LBL_CHECK_INBOX_TXT_str, LBL_SEND_EMAIL_LOADING_TXT_str).execute();
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

            LBL_RECEIPT_HEADER_TXT_str = obj_language_labels.getString("LBL_RECEIPT_HEADER_TXT");
            LBL_DOLLAR_SIGN_TXT_str = obj_language_labels.getString("LBL_DOLLAR_SIGN_TXT");
            LBL_THANKS_RIDING_TXT_str = obj_language_labels.getString("LBL_THANKS_RIDING_TXT");
            LBL_RIDE_NO_TXT_str = obj_language_labels.getString("LBL_RIDE_NO_TXT");
            LBL_NUMBER_TXT_str = obj_language_labels.getString("LBL_NUMBER_TXT");
            LBL_PASSENGER_TXT_str = obj_language_labels.getString("LBL_PASSENGER_TXT");
            LBL_DRIVER_TXT_str = obj_language_labels.getString("LBL_DRIVER_TXT");
            LBL_TRIP_REQUEST_DATE_TXT_str = obj_language_labels.getString("LBL_TRIP_REQUEST_DATE_TXT");
            LBL_TRIP_DATE_TXT_str = obj_language_labels.getString("LBL_TRIP_DATE_TXT");
            LBL_DROP_OFF_LOCATION_TXT_str = obj_language_labels.getString("LBL_DROP_OFF_LOCATION_TXT");
            LBL_CANCELED_TRIP_TXT_str = obj_language_labels.getString("LBL_CANCELED_TRIP_TXT");
            LBL_FINISHED_TRIP_TXT_str = obj_language_labels.getString("LBL_FINISHED_TRIP_TXT");
            LBL_TRIP_GOING_TXT_str = obj_language_labels.getString("LBL_TRIP_GOING_TXT");
            LBL_AT_TXT_str = obj_language_labels.getString("LBL_AT_TXT");
            LBL_KM_DISTANCE_TXT_str = obj_language_labels.getString("LBL_KM_DISTANCE_TXT");
            LBL_MINUTES_TXT_str = obj_language_labels.getString("LBL_MINUTES_TXT");
            LBL_PAYPAL_PAYMENT_TXT_str = obj_language_labels.getString("LBL_PAYPAL_PAYMENT_TXT");
            LBL_CASH_PAYMENT_TXT_str = obj_language_labels.getString("LBL_CASH_PAYMENT_TXT");
            LBL_SUBTOTAL_TXT_str = obj_language_labels.getString("LBL_SUBTOTAL_TXT");
            LBL_BASE_FARE_SMALL_TXT_str = obj_language_labels.getString("LBL_BASE_FARE_SMALL_TXT");
            LBL_CHARGES_TXT_str = obj_language_labels.getString("LBL_CHARGES_TXT");
            LBL_GET_RECEIPT_TXT_str = obj_language_labels.getString("LBL_GET_RECEIPT_TXT");
            LBL_CHECK_INBOX_TXT_str = obj_language_labels.getString("LBL_CHECK_INBOX_TXT");
            LBL_FAILED_SEND_RECEIPT_EMAIL_TXT_str = obj_language_labels.getString("LBL_FAILED_SEND_RECEIPT_EMAIL_TXT");
            LBL_SEND_EMAIL_LOADING_TXT_str = obj_language_labels.getString("LBL_SEND_EMAIL_LOADING_TXT");
            LBL_TIME_TXT_str = obj_language_labels.getString("LBL_TIME_TXT");
            LBL_PLATFORM_FEE_TXT_str = obj_language_labels.getString("LBL_PLATFORM_FEE_TXT");
            LBL_DISCOUNT_APPLIED_TXT_str = obj_language_labels.getString("LBL_DISCOUNT_APPLIED_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj_language_labels != null) {

            text_header.setText("" + LBL_RECEIPT_HEADER_TXT_str);
            thanks_header.setText("" + LBL_THANKS_RIDING_TXT_str);
            ride_no_header.setText("" + LBL_RIDE_NO_TXT_str);
            ride_no_txt.setText("" + LBL_NUMBER_TXT_str);
            name_passenger_header.setText("" + LBL_PASSENGER_TXT_str);
            name_passenger_txt.setText("" + LBL_PASSENGER_TXT_str);
            name_driver_header.setText("" + LBL_DRIVER_TXT_str);
            name_driver_txt.setText("" + LBL_DRIVER_TXT_str);
            trip_start_date_header.setText("" + LBL_TRIP_REQUEST_DATE_TXT_str);
            trip_start_date.setText("" + LBL_TRIP_DATE_TXT_str);
            destination_address_header.setText("" + LBL_DROP_OFF_LOCATION_TXT_str);
            txt_paypal_payment.setText("" + LBL_PAYPAL_PAYMENT_TXT_str);
            txt_cash_payment.setText("" + LBL_CASH_PAYMENT_TXT_str);
            txt_total_fare_header.setText("" + LBL_SUBTOTAL_TXT_str);
            fair_charges_txt.setText("" + LBL_CHARGES_TXT_str);
            header_name_txt.setText("" + LBL_GET_RECEIPT_TXT_str);

            txt_platform_fee_headerTxt.setText("" + LBL_PLATFORM_FEE_TXT_str);

        }

    }



    public void parse_booking_history_detail(String json_booking_histrory_detail_str) {

        LatLng source_lat_lon;
        LatLng destination_lat_lon;
        JSONObject trip_booking_detail = null;

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        try {
            trip_booking_detail = new JSONObject(json_booking_histrory_detail_str);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            iTripId_str = trip_booking_detail.getString("iTripId");
            destination_address_str = trip_booking_detail.getString("tDaddress");
            fare_str = trip_booking_detail.getString("iFare");
            trip_status_str = trip_booking_detail.getString("iActive");
            vRideNo_str = trip_booking_detail.getString("vRideNo");
            tStartLat_str = trip_booking_detail.getString("tStartLat");
            tStartLong_str = trip_booking_detail.getString("tStartLong");
            tEndLat_str = trip_booking_detail.getString("tEndLat");
            tEndLong_str = trip_booking_detail.getString("tEndLong");
            startDate_str = trip_booking_detail.getString("tTripRequestDate");
            DriverName_str = trip_booking_detail.getJSONObject("DriverDetails").getString("vName");

            vVehicleType_str = trip_booking_detail.getString("vVehicleType");
            iBaseFare_str = trip_booking_detail.getString("iBaseFare");
            fDistance_str = trip_booking_detail.getString("fDistance");
            TripFareOfDistance_str = trip_booking_detail.getString("TripFareOfDistance");
            TripFareOfMinutes_str = trip_booking_detail.getString("TripFareOfMinutes");
            iFare_str = trip_booking_detail.getString("iFare");
            TripTimeInMinutes_str = trip_booking_detail.getString("TripTimeInMinutes");
            vTripPaymentMode_str = trip_booking_detail.getString("vTripPaymentMode");
            fCommision_str = trip_booking_detail.getString("fCommision");
            CurrencySymbol_str = trip_booking_detail.getString("CurrencySymbol");
            discount_str = trip_booking_detail.getString("Discount");
            eCancelled_str = trip_booking_detail.getString("eCancelled");
            vCancelReason_str = trip_booking_detail.getString("vCancelReason");
            vCancelComment_str = trip_booking_detail.getString("vCancelComment");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (vTripPaymentMode_str.equals("Cash")) {
            payment_cash_area.setVisibility(View.VISIBLE);
            payment_paypal_area.setVisibility(View.GONE);
        }

        // Log.d("destination_address_str::", "::" + destination_address_str);
        destination_address.setText(destination_address_str);

        ride_no_txt.setText(vRideNo_str);

        txt_base_fare_value.setText(vVehicleType_str + " " + iBaseFare_str);
        txt_distance_value.setText("" + fDistance_str + " " + LBL_KM_DISTANCE_TXT_str);
        txt_distance_money_value.setText("" + TripFareOfDistance_str);
        // txt_minutes_value.setText("" + TripTimeInMinutes_str + " " +
        // LBL_MINUTES_TXT_str);
        txt_minutes_value.setText("" + TripTimeInMinutes_str + " " + LBL_TIME_TXT_str);
        txt_minutes_money_value.setText("" + TripFareOfMinutes_str);


        txt_total_fare_value.setText(CurrencySymbol_str + " " + iFare_str);
        txt_platform_fee_value.setText("" + fCommision_str);

        if (Mainprofile_Activity.Name != null) {

            try {
                String name_passenger = Mainprofile_Activity.Name.substring(0, 1).toUpperCase()
                        + Mainprofile_Activity.Name.substring(1);
                name_passenger_txt.setText(name_passenger);
            } catch (Exception e) {
                // TODO: handle exception
                String name_passenger = Mainprofile_Activity.Name;
                name_passenger_txt.setText(name_passenger);
            }

        }

        try {
            String name_driver = DriverName_str.substring(0, 1).toUpperCase() + DriverName_str.substring(1);
            name_driver_txt.setText(name_driver);
        } catch (Exception e) {
            // TODO: handle exception
            String name_driver = DriverName_str;
            name_driver_txt.setText(name_driver);
        }

        double startLat = Double.parseDouble(tStartLat_str);
        double startLon = Double.parseDouble(tStartLong_str);

        double EndLat;
        double EndtLon;
        if (tEndLat_str != null && tEndLat_str.trim().length() > 2 && !tEndLat_str.equals("")) {
            EndLat = Double.parseDouble(tEndLat_str);
            EndtLon = Double.parseDouble(tEndLong_str);
        } else {
            EndLat = Double.parseDouble(tStartLat_str);
            EndtLon = Double.parseDouble(tStartLong_str);
        }

        source_lat_lon = new LatLng(startLat, startLon);
        destination_lat_lon = new LatLng(EndLat, EndtLon);

        MarkerOptions marker_opt = new MarkerOptions().position(source_lat_lon);

        // Changing marker icon
        marker_opt.icon(BitmapDescriptorFactory.fromResource(R.drawable.source_marker)).anchor(0.5f, 0.5f);
        Marker source_marker = map.addMarker(marker_opt);

        builder.include(source_marker.getPosition());

        if (trip_status_str.contains("Finished")) {
            MarkerOptions marker_opt_destination = new MarkerOptions().position(destination_lat_lon);

            // Changing marker icon
            marker_opt_destination.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker))
                    .anchor(0.5f, 0.5f);

            Marker destination_marker = map.addMarker(marker_opt_destination);

            builder.include(destination_marker.getPosition());
        }

        LatLngBounds bounds = builder.build();

        int padding = 10; // offset from edges of the map in pixels
        // CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
        // padding);
        // map.moveCamera(cu);

        map.setOnCameraChangeListener(new OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // Move camera.
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                // Remove listener to prevent position reset on camera move.
                map.setOnCameraChangeListener(null);
            }
        });
        map.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                // TODO Auto-generated method stub
                marker.hideInfoWindow();
                return true;
            }
        });

        map.getUiSettings().setAllGesturesEnabled(false);

        if (trip_status_str.contains("Canceled")) {
            tripStatus_txt.setText("" + LBL_CANCELED_TRIP_TXT_str);

        } else if (trip_status_str.contains("Finished")) {

            tripStatus_txt.setText("" + LBL_FINISHED_TRIP_TXT_str);

            trip_detail_area.setVisibility(View.VISIBLE);
            header_name_txt.setVisibility(View.VISIBLE);

            new drawRoute(source_lat_lon, destination_lat_lon).execute();

        } else if (trip_status_str.contains("Active")) {
            tripStatus_txt.setText("" + LBL_TRIP_GOING_TXT_str);

        }

        if (!discount_str.equals("0")) {

            discount_header.setText(LBL_DISCOUNT_APPLIED_TXT_str);
            discount_value.setText(discount_str);
            discountRow.setVisibility(View.VISIBLE);
        }

        trip_start_date.setText(startDate_str);

        if(eCancelled_str.equals("Yes")){
            header_name_txt.setVisibility(View.GONE);
            tripStatus_txt.setText("This trip has been canceled by driver. Reason: "+vCancelReason_str);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    public class drawRoute extends AsyncTask<String, String, String> {
        ArrayList<LatLng> custom_point = new ArrayList<LatLng>();
        LatLng s_latLng;
        LatLng d_latLng;
        GetListOFRoutes routeList;

        public drawRoute(LatLng sourceLoc, LatLng destLoc) {
            // TODO Auto-generated constructor stub
            this.s_latLng = sourceLoc;
            this.d_latLng = destLoc;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            Create_directionUrl getJson_url = new Create_directionUrl();
            String url = getJson_url.getDirectionsUrl(s_latLng, d_latLng);
            // System.out.println("URL ROUTE::" + url);

            routeList = new GetListOFRoutes();
            routeList.Download_subLocations_Task(url);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            ArrayList<PolylineOptions> ListOfPolyLineOptions_routes = routeList.getListOfOptions_routes();

            try {
                Polyline route_direction = map.addPolyline(ListOfPolyLineOptions_routes.get(0));
            } catch (Exception e) {
                // TODO: handle exception
            }

        }

    }
}

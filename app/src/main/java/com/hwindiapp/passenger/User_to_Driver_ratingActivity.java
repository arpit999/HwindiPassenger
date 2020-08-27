package com.hwindiapp.passenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.content.IntentCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fonts.Text.MyEditText;
import com.fonts.Text.MyTextView;
import com.mainProfile.classFiles.Internet_warning_dialog_run;
import com.mainProfile.classFiles.Internet_warning_dialog_run.NoInternetListner;
import com.mainProfile.classFiles.OutputStreamReader;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.utils.CommonUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/

public class User_to_Driver_ratingActivity extends AppCompatActivity implements NoInternetListner {

    TextView text_header;

    private static final String CONFIG_ENVIRONMENT = CommonUtilities.CONFIG_ENVIRONMENT;

    private static final String CONFIG_CLIENT_ID = CommonUtilities.CONFIG_CLIENT_ID;

    private static final int REQUEST_PAYPAL_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName(CommonUtilities.DISPLAY_TAG)
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    RippleStyleButton btn_payment;

    RelativeLayout payment_btn_area;

    RelativeLayout ratingBar_area;
    RelativeLayout txt_payment_failed_area;
    RelativeLayout ratings_done_area;
    MyTextView payment_failed_txt;

    MyTextView trip_number_txt;

    RatingBar ratingBar_default_1;
    String driver_img_url = "";
    String yourFare = "";
    String json_responseString_profile = "";

    String user_id = "";
    String driver_id = "";
    String trip_id_final = "";
    String cash_payment = "";
    String fromMainProfile_str = "";

    String trip_number = "";
    RoundedImageView img_map;
    MyTextView link_comment;

    boolean ratingSet = false;
    boolean comment_set = false;
    String comment_str_final = "";

    String payment_status = "";
    //	String defaultCurrencySign_str = "";
    String defaultCurrencyCode_str = "";

    boolean paymentDone = false;
    boolean RatingDone = false;

    String paymentId_str = "";

    ImageView back_navigation;

    // MyTextView header_text;
    MyTextView trip_summary_area;

    RippleStyleButton btn_usr_to_driver_submit;

    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";

    String LBL_RATING_PAGE_HEADER_TXT_str = "";
    String LBL_TRIP_SUMMARY_TXT_str = "";
    String LBL_PAYMENT_FAILED_TXT_str = "";
    String LBL_TRIP_NUMBER_TXT_str = "";
    String LBL_COMMENT_AREA_HEADER_TXT_str = "";
    String LBL_BTN_SUBMIT_TXT_str = "";
    String LBL_BTN_PAYMENT_TXT_str = "";
    String LBL_AT_TXT_str = "";
    String LBL_YOUR_TRIP_NUM_TXT_str = "";
    String LBL_ADD_COMMENT_TXT_str = "";
    String LBL_ERROR_RATING_DIALOG_TXT_str = "";
    String LBL_ERROR_COMMENT_DIALOG_TXT_str = "";
    String LBL_PLEASE_WAIT_TXT_str = "";
    String LBL_ERROR_RATING_SUBMIT_AGAIN_TXT_str = "";
    String LBL_SUCCESS_RATING_SUBMIT_TXT_str = "";
    String LBL_LOADING_PAYMENT_VERIFY_TXT_str = "";
    String LBL_PAYMENT_VERIFICATION_FAILD_TXT_str = "";
    String LBL_PAYMENT_FAILED_PREFIX_TXT_str = "";
    String LBL_COMMENT_TXT_str = "";
    String LBL_ADD_COMMENT_HEADER_TXT_str = "";
    String LBL_WRITE_COMMENT_HINT_TXT_str = "";
    String LBL_BTN_OK_TXT_str = "";
    String LBL_WAIT_CONFIRM_BY_DRIVER_TXT_str = "";
    String LBL_TROUBLE_TXT_str = "";
    String LBL_TRY_AGAIN_TXT_str = "";
    String LBL_LOADING_VERIFICATION_TXT_str = "";
    String LBL_PAYPAL_FAILED_NOTE_ONE_TXT_str = "";
    String LBL_PAYPAL_FAILED_NOTE_TWO_TXT_str = "";
    String LBL_PAYPAL_STATUS_FAILED_TXT_str = "";
    String LBL_DISCOUNT_APPLIED_TXT_str = "";

    ImageView fare_loading_img;
    AnimationDrawable frameAnimation;
    MyTextView fare_area;
    MyTextView time_area;
    MyTextView discountValueTxt;
    MyEditText usr_to_driver;

    MyTextView trip_not_verified_txt;

    InternetConnection intCheck;
    Internet_warning_dialog_run no_internet_dialog;

    AlertDialog alert_paymentStatusError;
    AlertDialog alert_paymentFailedError;

    private Toolbar mToolbar;

    MyTextView trip_cancel_txt;

    androidx.appcompat.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_to_driverratingbar);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        Intent getDriverImageUrl = getIntent();
        driver_img_url = getDriverImageUrl.getStringExtra("driver_img_url");

        user_id = getDriverImageUrl.getStringExtra("user_id");
        driver_id = getDriverImageUrl.getStringExtra("driver_id");
        trip_id_final = getDriverImageUrl.getStringExtra("trip_id_final");
        trip_number = getDriverImageUrl.getStringExtra("trip_number");
        json_responseString_profile = getDriverImageUrl.getStringExtra("profile_json");
        cash_payment = getDriverImageUrl.getStringExtra("cash_payment");
        fromMainProfile_str = getDriverImageUrl.getStringExtra("fromMainProfile");

        ratingBar_default_1 = (RatingBar) findViewById(R.id.ratingbar_default_1);

        payment_btn_area = (RelativeLayout) findViewById(R.id.payment_btn_area);

        ratingBar_area = (RelativeLayout) findViewById(R.id.ratingBar_area);

        payment_status = getDriverImageUrl.getStringExtra("paymentEnabledOrNot");

        text_header = (TextView) findViewById(R.id.text_header);
        trip_summary_area = (MyTextView) findViewById(R.id.trip_summary_area);
        payment_failed_txt = (MyTextView) findViewById(R.id.payment_failed_txt);
        trip_number_txt = (MyTextView) findViewById(R.id.trip_number_txt);
        link_comment = (MyTextView) findViewById(R.id.link_comment);
        btn_usr_to_driver_submit = (RippleStyleButton) findViewById(R.id.btn_usr_to_driver_submit);
        btn_payment = (RippleStyleButton) findViewById(R.id.btn_payment);
        txt_payment_failed_area = (RelativeLayout) findViewById(R.id.txt_payment_failed_area);
        fare_area = (MyTextView) findViewById(R.id.fare_area);
        discountValueTxt = (MyTextView) findViewById(R.id.discountValueTxt);
        trip_not_verified_txt = (MyTextView) findViewById(R.id.trip_not_verified_txt);
        trip_cancel_txt = (MyTextView) findViewById(R.id.trip_cancel_txt);
        usr_to_driver = (MyEditText) findViewById(R.id.comment_usr_to_driver);

        ratings_done_area = (RelativeLayout) findViewById(R.id.ratings_done_area);

        time_area = (MyTextView) findViewById(R.id.time_area);

        img_map = (RoundedImageView) findViewById(R.id.img_map);

        fare_loading_img = (ImageView) findViewById(R.id.fare_loading_img);
        fare_loading_img.setBackgroundResource(R.drawable.fare_calculation_loader);

        frameAnimation = (AnimationDrawable) fare_loading_img.getBackground();

        intCheck = new InternetConnection(this);
        no_internet_dialog = new Internet_warning_dialog_run(false, User_to_Driver_ratingActivity.this);
        no_internet_dialog.setListener(User_to_Driver_ratingActivity.this);

        dbConnect = new DBConnect(this, "UC_labels.db");

        getLanguageLabelsFrmSqLite();

        frameAnimation.start();

        trip_number_txt.setText(LBL_YOUR_TRIP_NUM_TXT_str + " " + trip_number);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        if (fromMainProfile_str.equals("true")) {

            if (cash_payment.equals("true")){

                if (cash_payment.equals("true") || (payment_status != null && !payment_status.equals("Yes"))) {
                    payment_btn_area.setVisibility(View.GONE);
                    ratingBar_area.setVisibility(View.VISIBLE);
                }

            }
            else{
                payment_btn_area.setVisibility(View.VISIBLE);
                ratingBar_area.setVisibility(View.GONE);
            }

            // old code
            /*if (cash_payment.equals("true") || (payment_status != null && !payment_status.equals("Yes"))) {
                payment_btn_area.setVisibility(View.GONE);
                ratingBar_area.setVisibility(View.VISIBLE);
            }*/
            /*else {
                payment_btn_area.setVisibility(View.VISIBLE);
                ratingBar_area.setVisibility(View.GONE);
            }*/

            if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

                no_internet_dialog.run();
                // new
                // Internet_warning_dialog_run(false).run();
            } else {
                new GetFare().execute();
            }

        } else if (fromMainProfile_str.equals("false")) {

            try {
                parseJSONTrips(json_responseString_profile);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        btn_usr_to_driver_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String getDefault_messageString = link_comment.getText()
                        .toString()/* .replace("Leave a Comment", "") */;

                String rating = "" + ratingBar_default_1.getRating();
                if (rating.equals("0.0")) {
//					Toast.makeText(User_to_Driver_ratingActivity.this, "" + LBL_ERROR_RATING_DIALOG_TXT_str, Toast.LENGTH_LONG).show();
                    showMessage("", LBL_ERROR_RATING_DIALOG_TXT_str);
                    return;
                } else {
                    ratingSet = true;
                }

                if(ratingBar_default_1.getRating() < 2 && getDefault_messageString.trim().equals("")){
                    comment_set = false;
                }else{
                    comment_set = true;
                }

                if (comment_set == false) {

                    showMessage("", LBL_ERROR_COMMENT_DIALOG_TXT_str);
                    return;
                }

                if (comment_set == true && ratingSet == true) {
                    new submit_usr_to_driver_rating(rating, getDefault_messageString).execute();
                }

            }
        });

        link_comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new runCommentDialog().run();
            }
        });

        RoundedImageView img_car = (RoundedImageView) findViewById(R.id.img_car);
        img_car.setImageResource(R.drawable.small_car);

        RoundedImageView img_user = (RoundedImageView) findViewById(R.id.img_user);

        if (driver_img_url.length() == 0 || driver_img_url.equals("NONE") || driver_img_url.equals("null")
                || img_user.equals("")) {
            try {
                Bitmap bmp_usr_pic = BitmapFactory.decodeResource(User_to_Driver_ratingActivity.this.getResources(),
                        R.drawable.friends_main);
                img_user.setImageBitmap(bmp_usr_pic);
            } catch (Exception e2) {
                // TODO: handle exception
            }
        } else {
            new DriverImageLoadTask(driver_img_url, img_user).execute();
        }

        back_navigation = (ImageView) findViewById(R.id.back_navigation);
        back_navigation.setVisibility(View.GONE);
        back_navigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                User_to_Driver_ratingActivity.super.onBackPressed();
            }
        });

    }

    public void parseJSONTrips(String json_response_profile) throws JSONException {

        JSONObject obj_profile_json_dats = new JSONObject(json_response_profile);
        JSONObject obj_last_trip = obj_profile_json_dats.getJSONObject("Last_trip_data");
        String eVerified_str = obj_last_trip.getString("eVerified");
        String tRequestDate_str = obj_last_trip.getString("tTripRequestDate");
        String vTripPaymentMode_str = obj_last_trip.getString("vTripPaymentMode");
//        String vTripPaymentMode_str = "Paypal";
        String PaymentStatus_From_Passenger_str = obj_profile_json_dats.getString("PaymentStatus_From_Passenger");
        String Ratings_From_Passenger_str = obj_profile_json_dats.getString("Ratings_From_Passenger");
        String PayPalConfiguration_str = obj_profile_json_dats.getString("PayPalConfiguration");
//        String PayPalConfiguration_str = "Yes";

        if (vTripPaymentMode_str.equals("Paypal") && !PaymentStatus_From_Passenger_str.equals("Approved") && PayPalConfiguration_str.equals("Yes")) {
            payment_btn_area.setVisibility(View.VISIBLE);
            ratingBar_area.setVisibility(View.GONE);
        } else {
            payment_btn_area.setVisibility(View.VISIBLE);
            if (!Ratings_From_Passenger_str.equals("Done")) {
                ratingBar_area.setVisibility(View.VISIBLE);
            }

            if (Ratings_From_Passenger_str.equals("Done") && !eVerified_str.equals("Verified")) {
                ratingBar_area.setVisibility(View.GONE);
                ratings_done_area.setVisibility(View.VISIBLE);
            }

        }

        new GetFare().execute();
    }

    public void getLanguageLabelsFrmSqLite() {

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

        cursor.moveToPosition(0);

        language_labels_get_frm_sqLite = cursor.getString(0);

        JSONObject obj_language_labels = null;
        try {
            obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
            LBL_RATING_PAGE_HEADER_TXT_str = obj_language_labels.getString("LBL_RATING_PAGE_HEADER_TXT");
            LBL_TRIP_SUMMARY_TXT_str = obj_language_labels.getString("LBL_TRIP_SUMMARY_TXT");
            LBL_PAYMENT_FAILED_TXT_str = obj_language_labels.getString("LBL_PAYMENT_FAILED_TXT");
            LBL_TRIP_NUMBER_TXT_str = obj_language_labels.getString("LBL_TRIP_NUMBER_TXT");
            LBL_COMMENT_AREA_HEADER_TXT_str = obj_language_labels.getString("LBL_COMMENT_AREA_HEADER_TXT");
            LBL_BTN_SUBMIT_TXT_str = obj_language_labels.getString("LBL_BTN_SUBMIT_TXT");
            LBL_BTN_PAYMENT_TXT_str = obj_language_labels.getString("LBL_BTN_PAYMENT_TXT");
            LBL_AT_TXT_str = obj_language_labels.getString("LBL_AT_TXT");
            LBL_YOUR_TRIP_NUM_TXT_str = obj_language_labels.getString("LBL_YOUR_TRIP_NUM_TXT");
            LBL_ADD_COMMENT_TXT_str = obj_language_labels.getString("LBL_ADD_COMMENT_TXT");
            LBL_ERROR_RATING_DIALOG_TXT_str = obj_language_labels.getString("LBL_ERROR_RATING_DIALOG_TXT");
            LBL_ERROR_COMMENT_DIALOG_TXT_str = obj_language_labels.getString("LBL_ERROR_COMMENT_DIALOG_TXT");
            LBL_PLEASE_WAIT_TXT_str = obj_language_labels.getString("LBL_PLEASE_WAIT_TXT");
            LBL_ERROR_RATING_SUBMIT_AGAIN_TXT_str = obj_language_labels.getString("LBL_ERROR_RATING_SUBMIT_AGAIN_TXT");
            LBL_SUCCESS_RATING_SUBMIT_TXT_str = obj_language_labels.getString("LBL_SUCCESS_RATING_SUBMIT_TXT");
            LBL_LOADING_PAYMENT_VERIFY_TXT_str = obj_language_labels.getString("LBL_LOADING_PAYMENT_VERIFY_TXT");
            LBL_PAYMENT_VERIFICATION_FAILD_TXT_str = obj_language_labels
                    .getString("LBL_PAYMENT_VERIFICATION_FAILD_TXT");
            LBL_PAYMENT_FAILED_PREFIX_TXT_str = obj_language_labels.getString("LBL_PAYMENT_FAILED_PREFIX_TXT");
            LBL_COMMENT_TXT_str = obj_language_labels.getString("LBL_COMMENT_TXT");
            LBL_BTN_OK_TXT_str = obj_language_labels.getString("LBL_BTN_OK_TXT");
            LBL_WRITE_COMMENT_HINT_TXT_str = obj_language_labels.getString("LBL_WRITE_COMMENT_HINT_TXT");
            LBL_ADD_COMMENT_HEADER_TXT_str = obj_language_labels.getString("LBL_ADD_COMMENT_HEADER_TXT");
            LBL_WAIT_CONFIRM_BY_DRIVER_TXT_str = obj_language_labels.getString("LBL_WAIT_CONFIRM_BY_DRIVER_TXT");
            LBL_TROUBLE_TXT_str = obj_language_labels.getString("LBL_TROUBLE_TXT");
            LBL_TRY_AGAIN_TXT_str = obj_language_labels.getString("LBL_TRY_AGAIN_TXT");
            LBL_LOADING_VERIFICATION_TXT_str = obj_language_labels.getString("LBL_LOADING_VERIFICATION_TXT");
            LBL_PAYPAL_FAILED_NOTE_ONE_TXT_str = obj_language_labels.getString("LBL_PAYPAL_FAILED_NOTE_ONE_TXT");
            LBL_PAYPAL_FAILED_NOTE_TWO_TXT_str = obj_language_labels.getString("LBL_PAYPAL_FAILED_NOTE_TWO_TXT");
            LBL_PAYPAL_STATUS_FAILED_TXT_str = obj_language_labels.getString("LBL_PAYPAL_STATUS_FAILED_TXT");
            LBL_DISCOUNT_APPLIED_TXT_str = obj_language_labels.getString("LBL_DISCOUNT_APPLIED_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj_language_labels != null) {

            text_header.setText("" + LBL_RATING_PAGE_HEADER_TXT_str);
            trip_summary_area.setText(" " + LBL_TRIP_SUMMARY_TXT_str + " ");
            payment_failed_txt.setText(" " + LBL_PAYMENT_FAILED_TXT_str + " ");
            trip_number_txt.setText("" + LBL_TRIP_NUMBER_TXT_str);
            link_comment.setText("" + LBL_COMMENT_AREA_HEADER_TXT_str);
            btn_usr_to_driver_submit.setText("" + LBL_BTN_SUBMIT_TXT_str);
            btn_payment.setText("" + LBL_BTN_PAYMENT_TXT_str);
            trip_not_verified_txt.setText("" + LBL_WAIT_CONFIRM_BY_DRIVER_TXT_str);
        }

    }

    public void CheckVerificationCode(View v) {
        new CheckCode(trip_id_final).execute();
    }

    public class GetFare extends AsyncTask<String, String, String> {
        String responseString = "";
        boolean errorConnection = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&TripId=%s";
            String registerUrl = "";
            try {
                registerUrl = baseUrl + String.format(registerParam, "displayFareToPassenger",
                        URLEncoder.encode(trip_id_final, "UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

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

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            frameAnimation.stop();
            fare_loading_img.setVisibility(View.GONE);
            fare_area.setVisibility(View.VISIBLE);

            if (errorConnection == false && result != null && !result.trim().equals("")) {
                try {
                    InitializeProcedure(result);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                no_internet_dialog = new Internet_warning_dialog_run(false, User_to_Driver_ratingActivity.this);
                no_internet_dialog.setListener(User_to_Driver_ratingActivity.this);
                no_internet_dialog.run();
            }

        }

    }

    public void InitializeProcedure(String json_fare) throws JSONException {

        JSONObject obj_fare = new JSONObject(json_fare);

        String iFare_str = obj_fare.getString("iFare");
        String TotalFare_str = obj_fare.getString("TotalFare");
        String CurrencySymbol_str = obj_fare.getString("CurrencySymbol");
        String tEndLat_str = obj_fare.getString("tEndLat");
        String tEndLong_str = obj_fare.getString("tEndLong");
//		String tStartDate_str = obj_fare.getString("tStartDate");
        String formattedTripDate_str = obj_fare.getString("FormattedTripDate");
        String PayPalConfiguration_str = obj_fare.getString("PayPalConfiguration");
        String DefaultCurrencyCode_str = obj_fare.getString("DefaultCurrencyCode");
        String eCancelled_str = obj_fare.getString("eCancelled");
        String vCancelReason_str = obj_fare.getString("vCancelReason");
        String vCancelComment_str = obj_fare.getString("vCancelComment");

        String discount_str = obj_fare.getString("Discount");


        fare_area.setText(" " + CurrencySymbol_str + " " + TotalFare_str + " ");

        defaultCurrencyCode_str = DefaultCurrencyCode_str;
        yourFare = iFare_str;

        btn_usr_to_driver_submit.setClickable(true);
        btn_payment.setClickable(true);

        new MapImageLoadTask(
                "https://maps.googleapis.com/maps/api/staticmap?zoom=14&size=100x100&maptype=roadmap&markers=color:blue|label:Sorce|"
                        + tEndLat_str + "," + tEndLong_str,
                img_map).execute();

        time_area.setText(" " + formattedTripDate_str + " ");

        if(!discount_str.equals("0")){

            discountValueTxt.setText(LBL_DISCOUNT_APPLIED_TXT_str + ": "+ discount_str+" "+CurrencySymbol_str);

            discountValueTxt.setVisibility(View.VISIBLE);
        }

        if(eCancelled_str.equals("Yes")){
            trip_cancel_txt.setText("Trip is cancelled by driver. Reason: "+vCancelReason_str);
        }else{
            trip_cancel_txt.setVisibility(View.GONE);
        }

    }

    public class CheckCode extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        String responseString = "";

        String tripID = "";

        boolean errorToGetData = false;

        public CheckCode(String tripID) {
            // TODO Auto-generated constructor stub
            this.tripID = tripID;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(User_to_Driver_ratingActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage("" + LBL_LOADING_VERIFICATION_TXT_str);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url_check_verification_code = CommonUtilities.SERVER_URL + "&function=CheckVerificationCode&TripId="
                    + tripID;

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(url_check_verification_code);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = OutputStreamReader.readStream(in);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                errorToGetData = true;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            if (errorToGetData == false) {

                if (responseString.trim().equals("Verified")) {

                    Intent send_data = new Intent(User_to_Driver_ratingActivity.this,
                            Launcher_TaxiServiceActivity.class);
                    send_data.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(send_data);

                } else {
                    Toast.makeText(User_to_Driver_ratingActivity.this, "" + LBL_WAIT_CONFIRM_BY_DRIVER_TXT_str,
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(User_to_Driver_ratingActivity.this, "" + LBL_TROUBLE_TXT_str, Toast.LENGTH_LONG).show();
            }
        }

    }

    public class AddPaymentData extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        String responseString = "";

        String PayPalPaymentId_str = "";
        String PaidAmount_str = "";

        String tripID = "";

        boolean errorToGetData = false;

        public AddPaymentData(String tripID, String PayPalPaymentId_str, String PaidAmount_str) {
            // TODO Auto-generated constructor stub
            this.tripID = tripID;
            this.PayPalPaymentId_str = PayPalPaymentId_str;
            this.PaidAmount_str = PaidAmount_str;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(User_to_Driver_ratingActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage("" + LBL_LOADING_VERIFICATION_TXT_str);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url_add_payment = CommonUtilities.SERVER_URL + "&function=AddPaypalPaymentData&TripId=" + tripID
                    + "&PayPalPaymentId=" + PayPalPaymentId_str + "&PaidAmount=" + PaidAmount_str;

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(url_add_payment);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = OutputStreamReader.readStream(in);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                errorToGetData = true;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            if (errorToGetData == false) {

                if (responseString.trim().equals("PaymentSuccessful")) {
                    payment_btn_area.setVisibility(View.GONE);
                    ratingBar_area.setVisibility(View.VISIBLE);
                } else {

                    buildPaymentFailedDialog(PayPalPaymentId_str);
                    Toast.makeText(User_to_Driver_ratingActivity.this, "" + LBL_TRY_AGAIN_TXT_str, Toast.LENGTH_LONG)
                            .show();
                }

            } else {
                buildPaymentFailedDialog(PayPalPaymentId_str);
                Toast.makeText(User_to_Driver_ratingActivity.this, "" + LBL_TROUBLE_TXT_str, Toast.LENGTH_LONG).show();
            }
        }

    }

    public class MapImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private RoundedImageView imageView;
        Bitmap myBitmap = null;

        public MapImageLoadTask(String url, RoundedImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {

                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                myBitmap = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result == null) {

            } else {
                imageView.setImageBitmap(result);
            }
        }

    }

    public void StartPaymentProcedure(View view) {

        if (paymentDone == false) {

            PayPalPayment payment = new PayPalPayment(new BigDecimal("" + yourFare), "" + defaultCurrencyCode_str,
                    CommonUtilities.DISPLAY_TAG, PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent_start_paymentAct = new Intent(User_to_Driver_ratingActivity.this, PaymentActivity.class);

            // send the same configuration for restart resiliency
            intent_start_paymentAct.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

            intent_start_paymentAct.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

            startActivityForResult(intent_start_paymentAct, REQUEST_PAYPAL_PAYMENT);

        } else {
            new AddPaymentData(trip_id_final, paymentId_str, yourFare).execute();
        }
    }

    public class DriverImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private RoundedImageView imageView;
        Bitmap myBitmap = null;

        public DriverImageLoadTask(String url, RoundedImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result == null) {
                try {
                    Bitmap bmp_usr_pic = BitmapFactory.decodeResource(User_to_Driver_ratingActivity.this.getResources(),
                            R.drawable.friends_main);
                    imageView.setImageBitmap(bmp_usr_pic);
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            } else {
                imageView.setImageBitmap(result);
            }
        }

    }

    class runCommentDialog implements Runnable {

        RatingBar ratingbar_default_add_comment;
        MyEditText writeComment;

        @Override
        public void run() {
            // TODO Auto-generated method stub

            final Dialog dialog_check_internet = new Dialog(User_to_Driver_ratingActivity.this,
                    R.style.DialogAddComment);

            dialog_check_internet.setContentView(R.layout.custom_dialog_comment_add);

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

            ratingbar_default_add_comment = (RatingBar) dialog_check_internet
                    .findViewById(R.id.ratingbar_default_add_comment);

            String rt1 = "" + ratingBar_default_1.getRating();

            MyTextView header_text_dialog = (MyTextView) dialog_check_internet.findViewById(R.id.header_text);
            header_text_dialog.setText("" + LBL_COMMENT_TXT_str);

            MyTextView add_cmt_header_dialog = (MyTextView) dialog_check_internet.findViewById(R.id.add_cmt_header);
            add_cmt_header_dialog.setText("" + LBL_ADD_COMMENT_HEADER_TXT_str);

            writeComment = (MyEditText) dialog_check_internet.findViewById(R.id.writeComment);

            writeComment.setHint("" + LBL_WRITE_COMMENT_HINT_TXT_str);
            writeComment.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    writeComment.setError(null);
                }
            });

            ImageView back_navigation = (ImageView) dialog_check_internet
                    .findViewById(R.id.back_navigation_comment_add);
            back_navigation.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog_check_internet.dismiss();
                }
            });

            float defaultRating = Float.parseFloat(rt1);
            ratingbar_default_add_comment.setRating(defaultRating);
            RippleStyleButton submit_btn = (RippleStyleButton) dialog_check_internet.findViewById(R.id.submit_btn);

            submit_btn.setText("" + LBL_BTN_OK_TXT_str);

            submit_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String rt2 = "" + ratingbar_default_add_comment.getRating();

                    if (writeComment.getText().toString().trim().equals("")
                            && writeComment.getText().toString().length() > 0) {
                        writeComment.setError("" + LBL_ERROR_COMMENT_DIALOG_TXT_str);
                    } else if (rt2.equals("0.0")) {
                        showMessage("", LBL_ERROR_RATING_DIALOG_TXT_str);
//						Toast.makeText(User_to_Driver_ratingActivity.this, "" + LBL_ERROR_RATING_DIALOG_TXT_str,
//								Toast.LENGTH_LONG).show();
                    } else {

                        comment_set = true;

                        String message = writeComment.getText().toString();
                        dialog_check_internet.dismiss();

                        ratingBar_default_1.setRating(Float.parseFloat(rt2));
                        link_comment.setText(message);
                    }

                }
            });

            Window window = dialog_check_internet.getWindow();
            window.setGravity(Gravity.CENTER);

            window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

            dialog_check_internet.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

            dialog_check_internet.show();
            dialog_check_internet.setCanceledOnTouchOutside(false);
            dialog_check_internet.setCancelable(true);

        }

    }

    public class submit_usr_to_driver_rating extends AsyncTask<String, String, String> {

        String rat1 = "";
        String message = "";

        public submit_usr_to_driver_rating(String rt1, String message) {
            // TODO Auto-generated constructor stub
            this.rat1 = rt1;
            this.message = message;
        }

        ProgressDialog pdDialog;
        String responseString;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            // String baseUrl = SERVER_URL + "submit_rating_usr_to_driver.php?";
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&usr_email=%s&driver_id=%s&tripID=%s&rating_1=%s&message=%s&date=%s";

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();

            String date_get = dateFormat.format(cal.getTime());
            // System.out.println(dateFormat.format(cal.getTime()));

            String registerUrl = baseUrl + String.format(registerParam, "submit_rating_driver",
                    URLEncoder.encode(user_id), URLEncoder.encode(driver_id), URLEncoder.encode(trip_id_final),
                    URLEncoder.encode(rat1), URLEncoder.encode(message), URLEncoder.encode(date_get));

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(registerUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = OutputStreamReader.readStream(in);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdDialog = new ProgressDialog(User_to_Driver_ratingActivity.this, R.style.DialogTheme_custom);
            pdDialog.setMessage("" + LBL_PLEASE_WAIT_TXT_str);
            pdDialog.setCancelable(false);
            pdDialog.setCanceledOnTouchOutside(false);
            pdDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pdDialog.dismiss();

            if (responseString.trim().equals("LBL_RATING_EXIST")) {
                Toast.makeText(User_to_Driver_ratingActivity.this, "" + LBL_ERROR_RATING_SUBMIT_AGAIN_TXT_str,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(User_to_Driver_ratingActivity.this, "" + LBL_SUCCESS_RATING_SUBMIT_TXT_str,
                        Toast.LENGTH_SHORT).show();

                if (back_navigation != null) {
                    back_navigation.setVisibility(View.VISIBLE);
                }
                // finish();

                Intent send_data = new Intent(User_to_Driver_ratingActivity.this, Launcher_TaxiServiceActivity.class);
                send_data.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(send_data);

                RatingDone = true;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {

                        JSONObject jsonObj = new JSONObject(confirm.toJSONObject().toString());

                        JSONObject obj_response = jsonObj.getJSONObject("response");

                        String status_str = obj_response.getString("state");
                        String payment_id_str = obj_response.getString("id");

                        if (status_str.trim().equals("approved")) {

                            paymentId_str = payment_id_str;
                            String payment_client = confirm.getPayment().toJSONObject().toString();
                            Toast.makeText(getApplicationContext(), payment_id_str, Toast.LENGTH_LONG).show();

                            paymentDone = true;
                            new AddPaymentData(trip_id_final, payment_id_str, yourFare).execute();

                        } else {
                            buildPaymentErrorDialog();
                        }

                    } catch (JSONException e) {
                        // Log.e("paymentExample", "an extremely unlikely
                        // failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // Log.i("paymentExample", "An invalid Payment was submitted.
                // Please see the docs.");
            }
        }

    }

    public void buildPaymentErrorDialog() {

        if (alert_paymentStatusError != null) {
            alert_paymentStatusError.dismiss();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LightThemeGPSalert);
        builder.setMessage("" + LBL_PAYPAL_STATUS_FAILED_TXT_str).setCancelable(false)
                .setPositiveButton("" + LBL_BTN_OK_TXT_str, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        alert_paymentStatusError.dismiss();
                    }
                });
        alert_paymentStatusError = builder.create();
        alert_paymentStatusError.show();
    }

    public void buildPaymentFailedDialog(String paymetId) {

        if (alert_paymentFailedError != null) {
            alert_paymentFailedError.dismiss();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LightThemeGPSalert);
        builder.setMessage(
                LBL_PAYPAL_FAILED_NOTE_ONE_TXT_str + " " + paymetId + "" + LBL_PAYPAL_FAILED_NOTE_TWO_TXT_str)
                .setCancelable(false).setPositiveButton("" + LBL_BTN_OK_TXT_str, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                alert_paymentStatusError.dismiss();
            }
        });
        alert_paymentFailedError = builder.create();
        alert_paymentFailedError.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { // Back key pressed
            // Things to Do

            if (RatingDone == false) {
                Toast.makeText(User_to_Driver_ratingActivity.this, "" + LBL_ERROR_RATING_DIALOG_TXT_str,
                        Toast.LENGTH_LONG).show();
                return true;
            } else {
                // onBackPressed();
                finish();
                return true;
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void handleNoInternetBTNCallback(int id_btn) {
        // TODO Auto-generated method stub
        if (id_btn == 0) {
            new GetFare().execute();
        } else if (id_btn == 1) {

        }
    }

    public void showMessage(String title_str, String content_str) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                User_to_Driver_ratingActivity.this);

        alertDialogBuilder.setTitle(title_str);
        alertDialogBuilder
                .setMessage(content_str)
                .setCancelable(true)
                .setPositiveButton(LBL_BTN_OK_TXT_str, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}

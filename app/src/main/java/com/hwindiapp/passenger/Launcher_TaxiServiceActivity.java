package com.hwindiapp.passenger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/
import org.json.JSONException;
import org.json.JSONObject;

import com.fonts.Text.MyTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mainProfile.classFiles.GetLastLocation;
import com.mainProfile.classFiles.GetLocationUpdates;
import com.mainProfile.classFiles.Internet_warning_dialog_run;
import com.mainProfile.classFiles.Internet_warning_dialog_run.NoInternetListner;
import com.mainProfile.classFiles.OpenMainProfile;
import com.mainProfile.classFiles.OutputStreamReader;
import com.hwindiapp.passenger.languageLabels.ProcessLanguage_labels;
import com.hwindiapp.passenger.languageLabels.ProcessLanguage_labels.UpdateLanguageListner;
import com.mukesh.permissions.AppPermissions;
import com.utils.CommonUtilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class Launcher_TaxiServiceActivity extends AppCompatActivity implements NoInternetListner,
        com.mainProfile.classFiles.GetLastLocation.LastLocationListner, UpdateLanguageListner {

    private static final String[] ALL_PERMISSIONS = {
            android.Manifest.permission.READ_SMS, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.SEND_SMS, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_WIFI_STATE
    };

    private static final int ALL_REQUEST_CODE = 0;
    private AppPermissions mRuntimePermission;

    public static String resFRM_FB = "";
    public static String resFRM_sign = "";

    String back_btn_is_pressed_fbDialog = "";

    public String backFromFB_dialog = "";

    // AnimationDrawable animation_image_loading;
//    AnimationDrawable loading_anim;
    WebView imageAnim;

    RippleStyleButton btn_signIn;

    RippleStyleButton btn_signUP_user;
    MyTextView OrText;

    // Internet Connection Class
    InternetConnection intCheck = new InternetConnection(this);

    String access_sign_token_email;

    MyTextView wait_text;

    String id;
    String name;

    private SharedPreferences mPrefs;

    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";

    String LBL_LOADING_BAR_TXT_str = "";
    String LBL_FB_LOADING_TXT_str = "";
    String LBL_ERROR_OCCURED_str = "";
    String LBL_PLEASE_WAIT_TXT_str = "";
    String LBL_NO_LOCATION_FOUND_TXT_str = "";
    String LBL_BTN_OK_TXT_str = "";
    String LBL_ERROR_TXT_str = "";
    String LBL_ACC_DELETE_TXT_str = "";
    String LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str = "";

    Internet_warning_dialog_run no_internet_dialog;

    GetLastLocation getLastLocation;
    public static Location mLastLocation;

    int REQUEST_CODE_GPS_ON = 141;
    androidx.appcompat.app.AlertDialog alert_gps;
    androidx.appcompat.app.AlertDialog alert_no_location;
    androidx.appcompat.app.AlertDialog alert_problem;
    androidx.appcompat.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_taxi_service);

        mRuntimePermission = new AppPermissions(this);

        resFRM_FB = "Not Yet FB";
        resFRM_sign = "Not Yet Sign";

        dbConnect = new DBConnect(this, "UC_labels.db");

        no_internet_dialog = new Internet_warning_dialog_run(false, Launcher_TaxiServiceActivity.this);
        no_internet_dialog.setListener(Launcher_TaxiServiceActivity.this);

        wait_text = (MyTextView) findViewById(R.id.waiting_text);
        btn_signIn = (RippleStyleButton) findViewById(R.id.btn_signIn);
        btn_signUP_user = (RippleStyleButton) findViewById(R.id.btn_register);
        btn_signIn.setColor(getResources().getColor(R.color.btn_launcher_hover_color));
        btn_signIn.setClipRadius(2);

        btn_signUP_user.setColor(getResources().getColor(R.color.btn_launcher_hover_color));
        btn_signUP_user.setClipRadius(2);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        imageAnim = (WebView) findViewById(R.id.Loading_image);

        imageAnim.getSettings().setLoadWithOverviewMode(true);
        imageAnim.getSettings().setUseWideViewPort(true);
        imageAnim.getSettings().setJavaScriptEnabled(true);
        imageAnim.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        imageAnim.setBackgroundColor(Color.TRANSPARENT);
        imageAnim.loadUrl("file:///android_asset/loader.gif");

        setDefaultLabels();

        if (mRuntimePermission.hasPermission(ALL_PERMISSIONS)) {
            Log.i("", "onCreate:  all permission granted");
            //            Toast.makeText(this, "All permission already given", Toast.LENGTH_SHORT).show();
            checkGPS(false);
        } else {
            Toast.makeText(Launcher_TaxiServiceActivity.this, "Enable permission Hwindi->Appinfo->Permission", Toast.LENGTH_SHORT).show();
            mRuntimePermission.requestPermission(this, ALL_PERMISSIONS, ALL_REQUEST_CODE);
        }

    }

    public void createLoginRequest() {
        if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

            no_internet_dialog.run();
        } else {
            // new Internet_warning_dialog_run().run();
            login_process();
        }
    }

    public void checkGPS(boolean fromResults) {


        if (isLocationEnabled(this) == true) {
            if (alert_gps != null) {
                alert_gps.dismiss();
            }

            if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

                no_internet_dialog.run();
                // new Internet_warning_dialog_run(false).run();
            } else {
                // new Internet_warning_dialog_run().run();
                getLastLocation = new GetLastLocation(Launcher_TaxiServiceActivity.this);
                getLastLocation.setLastLocationListener(this);
                getLastLocation.buildGoogleApiClient();
            }
        } else {
            // if (fromResults == false) {

            buildAlertMessageNoGps();
            // }
        }

    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (SettingNotFoundException e) {
                e.printStackTrace();
            }

            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (locationMode != Settings.Secure.LOCATION_MODE_OFF && statusOfGPS == true) {
                return true;
            }

            return false;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            return !TextUtils.isEmpty(locationProviders);
        }

    }

    private void buildAlertMessageNoGps() {

        if (alert_gps != null) {
            alert_gps.dismiss();
        }

        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                        startActivityForResult(intent, REQUEST_CODE_GPS_ON);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                // dialog.cancel();

                finish();
            }
        });
        alert_gps = builder.create();
        alert_gps.show();
    }

    /**
     * Modifies the standard behavior to allow results to be delivered to fragments.
     * This imposes a restriction that requestCode be <= 0xffff.
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (requestCode != -1 && (requestCode & 0xffff0000) != 0) {
            throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        }
        super.startActivityForResult(intent, requestCode);
    }


    private void buildAlertMessageNoLocation() {

        if (alert_no_location != null) {
            alert_no_location.dismiss();
        }

        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("" + LBL_NO_LOCATION_FOUND_TXT_str).setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        if (alert_no_location != null) {
                            alert_no_location.dismiss();
                        }
                        checkLocationAvail(Launcher_TaxiServiceActivity.mLastLocation);
                    }
                })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                // dialog.cancel();

                                finish();
                            }
                        });
        alert_no_location = builder.create();
        alert_no_location.show();
    }

    private void buildAlertMessageERROR() {

        if (alert_problem != null) {
            alert_problem.dismiss();
        }

        if (LBL_ERROR_OCCURED_str == null || LBL_ERROR_OCCURED_str.trim().equals("")
                || LBL_ERROR_OCCURED_str.trim().length() == 0 || LBL_ERROR_OCCURED_str.trim().equals("null")) {
            LBL_ERROR_OCCURED_str = "Sorry. Some Error Occurred. Please try again Later.";
        }

        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("" + LBL_ERROR_OCCURED_str).setCancelable(false).setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        // dialog.cancel();

                        finish();
                    }
                });
        alert_problem = builder.create();
        alert_problem.show();
    }

//    class Starter implements Runnable {
//
//        public void run() {
//            loading_anim.start();
//        }
//
//    }

    public void SignInMethod(View v) {
        Intent signIn_intent = new Intent(this, SignInActivity.class);

        startActivity(signIn_intent);
    }

    public void signUpUser(View v) {
        Intent signUp = new Intent(this, UserSignUpActivity.class);
        startActivity(signUp);
    }

    public void login_process() {

        access_sign_token_email = mPrefs.getString("access_sign_token_email", null);

        if (access_sign_token_email == null) {

            downloadLanguageLabels();

        } else {
            wait_text.setVisibility(View.VISIBLE);
            imageAnim.setVisibility(View.VISIBLE);
//            imageAnim.post(new Starter());

            // downloadLanguageLabels();
            getLanguageLabelsFrmSqLite();

            if (access_sign_token_email != null) {

                String access_sign_token_pass = mPrefs.getString("access_sign_token_pass", null);

                String access_sign_token_user_id_auto = mPrefs.getString("access_sign_token_user_id_auto", null);
                new UserDetail(access_sign_token_email, access_sign_token_pass, access_sign_token_user_id_auto)
                        .execute();
            }


        }
    }

    public void setDefaultLabels() {
        if (LBL_NO_LOCATION_FOUND_TXT_str != null && LBL_NO_LOCATION_FOUND_TXT_str.equals("")) {
            LBL_NO_LOCATION_FOUND_TXT_str = "Sorry. We are not able to get your Location. Please try again later.";
        }

        if (LBL_FB_LOADING_TXT_str != null && LBL_FB_LOADING_TXT_str.equals("")) {
            LBL_FB_LOADING_TXT_str = "Please Wait. We are Connecting to Facebook";
        }

        if (LBL_LOADING_BAR_TXT_str != null && LBL_LOADING_BAR_TXT_str.equals("")) {
            LBL_LOADING_BAR_TXT_str = "Signing In...";
        }

        if (LBL_ERROR_OCCURED_str != null && LBL_ERROR_OCCURED_str.equals("")) {
            LBL_ERROR_OCCURED_str = "Sorry. Some Error Occurred. Please try again Later.";
        }

        if (LBL_PLEASE_WAIT_TXT_str != null && LBL_PLEASE_WAIT_TXT_str.equals("")) {
            LBL_PLEASE_WAIT_TXT_str = "Please Wait";
        }

        if (LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str != null
                && LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str.equals("")) {
            LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str = "Sorry problem occurred. Please contact us.";
        }
    }

    public void getLanguageLabelsFrmSqLite() {

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

        try {
            cursor.moveToPosition(0);

            language_labels_get_frm_sqLite = cursor.getString(0);

            JSONObject obj_language_labels = null;
            try {
                obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
                LBL_LOADING_BAR_TXT_str = obj_language_labels.getString("LBL_LOADING_BAR_TXT");
                LBL_FB_LOADING_TXT_str = obj_language_labels.getString("LBL_FB_LOADING_TXT");
                LBL_ERROR_OCCURED_str = obj_language_labels.getString("LBL_ERROR_OCCURED");
                LBL_PLEASE_WAIT_TXT_str = obj_language_labels.getString("LBL_PLEASE_WAIT_TXT");
                LBL_NO_LOCATION_FOUND_TXT_str = obj_language_labels.getString("LBL_NO_LOCATION_FOUND_TXT");
                LBL_BTN_OK_TXT_str = obj_language_labels.getString("LBL_BTN_OK_TXT");
                LBL_ERROR_TXT_str = obj_language_labels.getString("LBL_ERROR_TXT");
                LBL_ACC_DELETE_TXT_str = obj_language_labels.getString("LBL_ACC_DELETE_TXT");
                LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str = obj_language_labels
                        .getString("LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                buildAlertMessageERROR();
            }
        } catch (Exception e) {
            // TODO: handle exception
            buildAlertMessageERROR();
        }


        Cursor cursor_configurations = dbConnect
                .execQuery("select vValue from labels WHERE vLabel=\"Configuration_Links\"");

        try {
            cursor_configurations.moveToPosition(0);

            String link_json_str_configuration = cursor_configurations.getString(0);

            setConfigurations(link_json_str_configuration);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            buildAlertMessageERROR();
        }

    }

    public void downloadLanguageLabels() {

        imageAnim.setVisibility(View.VISIBLE);
//        imageAnim.post(new Starter());

        String language_default_url = CommonUtilities.SERVER_URL_LABELS_DEFAULT;

        ProcessLanguage_labels processStart_language_labels = new ProcessLanguage_labels(true, language_default_url);

        processStart_language_labels.setUpdateLanguageListener(this);

        processStart_language_labels.execute();
    }

    public void finishedDownload_languageLabels(String json_language_labels, String languageList_json_str,
                                                String configurationLinks_json_str) {

        if (json_language_labels != null && json_language_labels.toString().length() > 6) {

            if (CheckIsDataAlreadyInDBorNot("labels", "vLabel", "Language_labels") == false) {
                String sql = "INSERT INTO labels(vLabel,vValue) values('%s','%s')";
                sql = String.format(sql, "Language_labels", json_language_labels.replace("'", "''").toString());
                dbConnect.execNonQuery(sql);
            } else {
                String sql = "UPDATE `labels` SET vValue = '" + json_language_labels.replace("'", "''").toString() + "'"
                        + " WHERE vLabel = \"Language_labels\"";
                dbConnect.execNonQuery(sql);
            }

        }

        if (languageList_json_str != null && languageList_json_str.toString().length() > 6) {

            if (CheckIsDataAlreadyInDBorNot("labels", "vLabel", "Language_List") == false) {
                String sql = "INSERT INTO labels(vLabel,vValue) values('%s','%s')";
                sql = String.format(sql, "Language_List", languageList_json_str.replace("'", "''").toString());
                dbConnect.execNonQuery(sql);
            } else {
                String sql = "UPDATE `labels` SET vValue = '" + languageList_json_str.replace("'", "''").toString()
                        + "'" + " WHERE vLabel = \"Language_List\"";
                dbConnect.execNonQuery(sql);
            }

        }

        if (configurationLinks_json_str != null && configurationLinks_json_str.toString().length() > 6) {

            if (CheckIsDataAlreadyInDBorNot("labels", "vLabel", "Configuration_Links") == false) {
                String sql = "INSERT INTO labels(vLabel,vValue) values('%s','%s')";
                sql = String.format(sql, "Configuration_Links",
                        configurationLinks_json_str.replace("'", "''").toString());
                dbConnect.execNonQuery(sql);
            } else {
                String sql = "UPDATE `labels` SET vValue = '"
                        + configurationLinks_json_str.replace("'", "''").toString() + "'"
                        + " WHERE vLabel = \"Configuration_Links\"";
                dbConnect.execNonQuery(sql);
            }

        }

        if (json_language_labels == null || json_language_labels.toString().length() < 6
                || languageList_json_str == null || languageList_json_str.toString().length() < 6
                || configurationLinks_json_str == null || configurationLinks_json_str.toString().length() < 6) {
            buildAlertMessageERROR();

            return;
        }

        try {
            setConfigurations(configurationLinks_json_str);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            buildAlertMessageERROR();

            return;
        }

        JSONObject labels_obj = null;
        String LBL_SIGN_IN_TXT_str = "";
        String LBL_REGISTER_TXT_str = "";

        try {
            labels_obj = new JSONObject(json_language_labels);
            LBL_SIGN_IN_TXT_str = labels_obj.getString("LBL_SIGN_IN_TXT");
            LBL_REGISTER_TXT_str = labels_obj.getString("LBL_REGISTER_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (labels_obj != null) {
            btn_signIn.setText("" + LBL_SIGN_IN_TXT_str);
            btn_signUP_user.setText("" + LBL_REGISTER_TXT_str);
            // wait_text.setText(""+LBL_LOADING_BAR_TXT_str);
        }

        wait_text.setVisibility(View.GONE);
        imageAnim.setVisibility(View.GONE);
        // imageAnim.post(new Starter());
//        loading_anim.stop();

        btn_signIn.setVisibility(View.VISIBLE);
        btn_signUP_user.setVisibility(View.VISIBLE);
    }

    public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {

        String Query = "SELECT * from `labels` WHERE vLabel= \"" + fieldValue + "\"";
        Cursor cursor = dbConnect.execQuery(Query);

        if (cursor != null) {
            // Log.d("cursor.getCount()::", "cc::" + cursor.getCount());
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        }
        return false;
    }

    public void setConfigurations(String Configuration_Json) throws JSONException {

        JSONObject obj_configuration = new JSONObject(Configuration_Json);

        String FACEBOOK_APP_ID_str = obj_configuration.getString("FACEBOOK_APP_ID");
        String CONFIG_CLIENT_ID_str = obj_configuration.getString("CONFIG_CLIENT_ID");
        String GOOGLE_SENDER_ID_str = obj_configuration.getString("GOOGLE_SENDER_ID");

        CommonUtilities.FB_PROJECT_ID = FACEBOOK_APP_ID_str;
        CommonUtilities.CONFIG_CLIENT_ID = CONFIG_CLIENT_ID_str;
        CommonUtilities.SENDER_ID = GOOGLE_SENDER_ID_str;
    }

    public String getCity(double lat, double lon) {
        Geocoder geocoder = new Geocoder(Launcher_TaxiServiceActivity.this, Locale.getDefault());
        List<Address> addresses;
        String cityName = "";
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            cityName = addresses.get(0).getLocality();
            // Log.d("CITY:", "");
            // Log.d("CITY:", "" + cityName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cityName;
    }

    public class UserDetail extends AsyncTask<String, String, String> {
        String responseString = "";

        String cityName = "";

        String userName_email = "";
        String userPassword = "";
        String access_sign_token_user_id_auto = "";
        String checkLogout = "";
        String string_response_detail = "";

        String GCMregistrationId = "";

        public UserDetail(String user_email, String User_pass, String access_sign_token_user_id_auto) {
            // TODO Auto-generated constructor stub
            this.userName_email = user_email;
            this.userPassword = User_pass;
            this.access_sign_token_user_id_auto = access_sign_token_user_id_auto;
        }

        public String getRegisteredUserDetail() {

            // String baseUrl = SERVER_URL + "getUserDetail.php?";

            if (GCMregistrationId == null || GCMregistrationId.trim().equals("")) {
                GCMregistrationId = "";
            }

            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&Email=%s&access_sign_token_user_id_auto=%s&cityName=%s&GCMID=%s";

            if (URLEncoder.encode(userName_email) != null) {

                String registerUrl = baseUrl
                        + String.format(registerParam, "getDetail", URLEncoder.encode(userName_email), URLEncoder.encode(access_sign_token_user_id_auto),
                        URLEncoder.encode("" + cityName), "" + GCMregistrationId);


//                Log.d("registerUrl","registerUrl:"+registerUrl);
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

                resFRM_sign = responseString;

            }

            if ((responseString != null || !responseString.equals("")) && responseString.length() > 2) {

                if (responseString.trim().equals("LBL_PASSWORD_ERROR_TXT")
                        || responseString.trim().equals("LBL_NO_REG_FOUND")
                        || responseString.trim().equals("LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER")
                        || responseString.trim().equals("ACC_DELETED")) {

                    // buildAlertMessageERROR();
                } else {

                    JSONObject jsonObject_userDetail = null;
                    String img_name = "";
                    String user_id = "";

                    try {
                        jsonObject_userDetail = new JSONObject(responseString);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (jsonObject_userDetail != null) {

                        try {
                            user_id = jsonObject_userDetail.getString(CommonUtilities.iUserId);
                            img_name = jsonObject_userDetail.getString(CommonUtilities.vImgName);
                            checkLogout = jsonObject_userDetail.getString(CommonUtilities.vLogoutDev);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    } else {
                        // new Internet_warning_dialog_run(false).run();
                    }

                }
            } else {
                // new Internet_warning_dialog_run(false).run();
            }
            return responseString;

        }

        public void processGCMID_user() throws IOException {
            InstanceID instanceID = InstanceID.getInstance(Launcher_TaxiServiceActivity.this);
            GCMregistrationId = instanceID.getToken(CommonUtilities.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null);
            // Log.d("Launcher GCMregistrationId::", ":GCMregistrationId:" +
            // GCMregistrationId);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            cityName = getCity(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            try {
//				Log.d("checkPlayServices()", "checkPlayServices()::"+checkPlayServices());
                if (checkPlayServices() ) {
                    processGCMID_user();
                    string_response_detail = getRegisteredUserDetail();
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            wait_text.setText("" + LBL_LOADING_BAR_TXT_str);
        }

        protected void onPostExecute(String file_url) {

            if (string_response_detail != null && !string_response_detail.equals("")
                    && string_response_detail.length() > 2) {
                if (checkLogout.equals("true")) {
                    new updateLogDetail(access_sign_token_user_id_auto).execute();
                } else {
                    GotoProfile(string_response_detail);
                }
            } else {
                no_internet_dialog.changeErrorConnection(true);
                no_internet_dialog.run();
            }

        }
    }

    public boolean checkPlayServices() {
        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        final int result = googleAPI.isGooglePlayServicesAvailable(Launcher_TaxiServiceActivity.this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {

                Launcher_TaxiServiceActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        googleAPI.getErrorDialog(Launcher_TaxiServiceActivity.this, result,
                                CommonUtilities.PLAY_SERVICES_RESOLUTION_REQUEST).show();
                    }
                });

            }

            return false;
        }

        return true;
    }

    public void GotoProfile(String res_json_detail) {

        if (Launcher_TaxiServiceActivity.this.isFinishing() == false) {

            if (res_json_detail.trim().equals("LBL_PASSWORD_ERROR_TXT")
                    || res_json_detail.trim().equals("LBL_NO_REG_FOUND")
                    || res_json_detail.trim().equals("LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER")
                    || res_json_detail.trim().equals("ACC_DELETED")) {


//                Toast.makeText(Launcher_TaxiServiceActivity.this, "" + LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str,
//                        Toast.LENGTH_LONG).show();
//                remove_sign_out();
//
//                Intent signOut_intent_to_Main = new Intent(Launcher_TaxiServiceActivity.this,
//                        Launcher_TaxiServiceActivity.class);
//
//                finish();
//                startActivity(signOut_intent_to_Main);

                if (res_json_detail.trim().equals("ACC_DELETED")) {
                    showMessage(LBL_ERROR_TXT_str, LBL_ACC_DELETE_TXT_str, true);
                } else {
                    showMessage(LBL_ERROR_TXT_str, LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str, true);
                }


            } else {

                Launcher_TaxiServiceActivity.this
                        .sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
                Launcher_TaxiServiceActivity.this
                        .sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));

                JSONObject jsonObject_userDetail = null;
                try {
                    jsonObject_userDetail = new JSONObject(res_json_detail);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (jsonObject_userDetail != null) {

                    new OpenMainProfile(Launcher_TaxiServiceActivity.this, res_json_detail).run();

                    ActivityCompat.finishAffinity(this);

                } else {
                    Toast.makeText(this, "" + LBL_ERROR_OCCURED_str, Toast.LENGTH_SHORT).show();
                    buildAlertMessageERROR();
                }
            }
        }
    }

    public class updateLogDetail extends AsyncTask<String, String, String> {
        String access_sign_token_user_id_auto = "";
        String responseString;
        ProgressDialog pDialog;

        public updateLogDetail(String access_sign_token_user_id_auto) {
            // TODO Auto-generated constructor stub
            this.access_sign_token_user_id_auto = access_sign_token_user_id_auto;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(Launcher_TaxiServiceActivity.this);
            pDialog.setMessage(LBL_PLEASE_WAIT_TXT_str + "....");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

        }

        public String getRegisteredUserDetail() {

            // String baseUrl = SERVER_URL + "getUserDetail.php?";
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&access_sign_token_user_id_auto=%s";

            if (URLEncoder.encode(access_sign_token_user_id_auto) != null) {

                String registerUrl = baseUrl
                        + String.format(registerParam, "updateLog", URLEncoder.encode(access_sign_token_user_id_auto));

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

            }

            return responseString;

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String res = getRegisteredUserDetail();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            remove_sign_out();
            pDialog.dismiss();

            Intent signOut_intent_to_Main = new Intent(Launcher_TaxiServiceActivity.this,
                    Launcher_TaxiServiceActivity.class);

            finish();
            startActivity(signOut_intent_to_Main);
        }

    }


    public void remove_sign_out() {

//		if (access_token_main != null) {
//
//			mPrefs.edit().remove("access_expires").commit();
//			mPrefs.edit().remove("access_token").commit();
//			mPrefs.edit().clear().commit();
//		} else {
        mPrefs.edit().remove("access_sign_token_email").commit();
        mPrefs.edit().remove("access_sign_token_pass").commit();
        mPrefs.edit().remove("access_sign_token_user_id_auto").commit();
        mPrefs.edit().clear().commit();
//		}

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GPS_ON) {
            checkGPS(true);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        System.gc();
    }

    @Override
    public void handleNoInternetBTNCallback(int id_btn) {
        // TODO Auto-generated method stub
        if (id_btn == 0) {
            // imageAnim.setVisibility(View.VISIBLE);
            // wait_text.setVisibility(View.VISIBLE);
            // login_process();
            checkGPS(false);
        } else if (id_btn == 1) {
            finish();
        }
    }

    public void checkLocationAvail(Location mLastLocation) {
        if (mLastLocation != null) {
            Launcher_TaxiServiceActivity.mLastLocation = mLastLocation;

            if (mLastLocation.getLatitude() == 0.0 || mLastLocation.getLongitude() == 0.0) {
                buildAlertMessageNoLocation();
                Toast.makeText(Launcher_TaxiServiceActivity.this, "" + LBL_NO_LOCATION_FOUND_TXT_str, Toast.LENGTH_LONG)
                        .show();
            } else {
                createLoginRequest();
            }

        } else {
            buildAlertMessageNoLocation();
        }
    }

    @Override
    public void handleLastLocationListnerCallback(Location mLastLocation) {
        // TODO Auto-generated method stub
        if (LBL_NO_LOCATION_FOUND_TXT_str == null || LBL_NO_LOCATION_FOUND_TXT_str.equals("")
                || LBL_NO_LOCATION_FOUND_TXT_str.trim().length() < 5) {
            LBL_NO_LOCATION_FOUND_TXT_str = "Sorry. We are not able to get your Location. Please try again later.";
        }

//        Log.d("mLastLocation(this)", "mLastLocation(this)::" + mLastLocation.getLatitude());
        checkLocationAvail(mLastLocation);

    }

    @Override
    public void handleLastLocationListnerNOVALUECallback(int id) {
        // TODO Auto-generated method stub

        if (LBL_NO_LOCATION_FOUND_TXT_str == null || LBL_NO_LOCATION_FOUND_TXT_str.equals("")
                || LBL_NO_LOCATION_FOUND_TXT_str.trim().length() < 5) {
            LBL_NO_LOCATION_FOUND_TXT_str = "Sorry. We are not able to get your Location. Please try again later.";
        }

        if (id == 0) {
            buildAlertMessageNoLocation();
            Toast.makeText(Launcher_TaxiServiceActivity.this, "" + LBL_NO_LOCATION_FOUND_TXT_str, Toast.LENGTH_LONG)
                    .show();
        }

        GetLocationUpdates getLocUpdates = new GetLocationUpdates(Launcher_TaxiServiceActivity.this, 2);
        getLocUpdates.setLocationUpdatesListener(new GetLocationUpdates.TripLocationUpdates() {
            @Override
            public void handleTripLocationUpdatesCallback(Location location) {
                Launcher_TaxiServiceActivity.mLastLocation = location;
            }
        });

    }

    @Override
    public void handleUpdateLangCallback(String Labels_lang) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleDefaultLangCallback(String languageLabels, String languageList, String countryList) {
        // TODO Auto-generated method stub
        finishedDownload_languageLabels(languageLabels, languageList, countryList);
    }

    @Override
    public void handleErrorOnLangCallback(boolean error) {
        // TODO Auto-generated method stub
        no_internet_dialog.changeErrorConnection(error);
        no_internet_dialog.run();
    }

    public void showMessage(String title_str, String content_str, final boolean close) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                Launcher_TaxiServiceActivity.this);

        alertDialogBuilder.setTitle(title_str);
        alertDialogBuilder
                .setMessage(content_str)
                .setCancelable(true)
                .setPositiveButton(LBL_BTN_OK_TXT_str, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.dismiss();

                        if (close == true) {
                            finish();
                        }
                    }
                });
        alertDialog = alertDialogBuilder.create();

        if (close == true) {
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
        }

        alertDialog.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_REQUEST_CODE:
                List<Integer> permissionResults = new ArrayList<>();
                for (int grantResult : grantResults) {
                    permissionResults.add(grantResult);

                }
                if (permissionResults.contains(PackageManager.PERMISSION_DENIED)) {

                    if (!mRuntimePermission.hasPermission(ALL_PERMISSIONS)){
                        mRuntimePermission.requestPermission( Launcher_TaxiServiceActivity.this, ALL_PERMISSIONS, ALL_REQUEST_CODE);
                    }

                    Toast.makeText(this, "Permissiion Enable required for run app goto Hwindi->Appinfo->permission", Toast.LENGTH_LONG).show();

                } else {
                    Log.i("", "onRequestPermissionsResult: All permission granted");
//                    Toast.makeText(this, "All Permissions granted", Toast.LENGTH_SHORT).show();
                    checkGPS(false);
                }
                break;
        }
    }


}

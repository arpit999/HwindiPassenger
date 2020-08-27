package com.hwindiapp.passenger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.WebDialog;
import com.facebook.login.widget.LoginButton;
import com.fonts.Text.MyTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mainProfile.classFiles.OpenMainProfile;
import com.mainProfile.classFiles.OutputStreamReader;
import com.mainProfile.classFiles.RegisterFbLoginResCallBack;
import com.materialedittext.MaterialEditText;
import com.utils.CommonUtilities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    // ActionBar actionbar;
    TextView text_header;

    MaterialEditText userName;
    MaterialEditText password;
    String userName_str;
    String pass_str;

    LoginButton loginButton;
    LinearLayout fbLoginBtnArea;
    CallbackManager callbackManager;

    String driver_login_id;
    String driver_name;
    String driver_id_auto;

    String user_id;
    String user_name;
    String user_gender;
    String user_id_auto;
    String vTripStatus;
    String user_email;

    String GCMregId;

    ProgressDialog dialog_change_LanguageCode;

    String id;
    String name;
    String fname;
    String lname;
    String email;
    String gender;

//	SharedPreferences mPrefs;

//    public static boolean callingFromDriver = false;
//    public static boolean callingFromUserSignIN = false;

//    String json_signIn;
//    String vLang_code_str = "";

    boolean allCorrect_email = false;
    boolean allCorrect_password = false;

    MyTextView forgetPassLink_txt;
    RippleStyleButton SignIn_btn;

    // MyTextView email_header;
    // MyTextView password_header;
    MyTextView or_txt;

    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";
    String LBL_SIGN_IN_TXT_str = "";
    String LBL_USER_NAME_HINT_TXT_str = "";
    String LBL_PASSWORD_HINT_TXT_str = "";
    String LBL_FEILD_REQUIRD_ERROR_TXT_str = "";
    String LBL_PASSWORD_LBL_TXT_str = "";
    String LBL_EMAIL_LBL_TXT_str = "";
    String LBL_OR_TXT_str = "";
    String LBL_FEILD_PASSWORD_ERROR_TXT_str = "";
    String LBL_FEILD_EMAIL_ERROR_TXT_str = "";
    String LBL_FB_LOADING_TXT_str = "";
    String LBL_LOADING_GET_INFORMATION_TXT_str = "";
    String LBL_SERVICE_NOT_AVAIL_TXT_str = "";
    String LBL_SIGN_IN_LOADING_TXT_str = "";
    String LBL_ERROR_TXT_str = "";
    String LBL_NO_REG_FOUND_str = "";
    String LBL_SIGN_UP_TXT_str = "";
    String LBL_PASSWORD_ERROR_TXT_str = "";
    String LBL_ERROR_OCCURED_str = "";
    String Default_Language_CODE = "";
    String LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str = "";
    String LBL_FORGET_PASS_TXT_str = "";
    String LBL_INFO_SET_TXT_str = "";
    String LBL_BTN_OK_TXT_str = "";
    String LBL_ACC_DELETE_TXT_str = "";

    // MyTextView header_text;

    Location mLastLocation;

    String GCMRegID = "";

    String forget_pass_link_str = "";

    private Toolbar mToolbar;

    androidx.appcompat.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        WebDialog.setWebDialogTheme(R.style.FBDialogtheme);
        setContentView(R.layout.activity_sign_in);

        FacebookSdk.setApplicationId(CommonUtilities.FB_PROJECT_ID);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(null);

        text_header = (TextView) findViewById(R.id.text_header);
        userName = (MaterialEditText) findViewById(R.id.UserName);
        password = (MaterialEditText) findViewById(R.id.user_Password);
        loginButton = (LoginButton) findViewById(R.id.FBlogin_button);
        fbLoginBtnArea = (LinearLayout) findViewById(R.id.fbLoginBtnArea);

        forgetPassLink_txt = (MyTextView) findViewById(R.id.forgetPassLink_txt);

        SignIn_btn = (RippleStyleButton) findViewById(R.id.SignIn_button);
        or_txt = (MyTextView) findViewById(R.id.or_txt);

        dbConnect = new DBConnect(this, "UC_labels.db");

        getLanguageLabelsFrmSqLite();

        mLastLocation = Launcher_TaxiServiceActivity.mLastLocation;

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends", "user_about_me"));

        loginButton.registerCallback(callbackManager, new RegisterFbLoginResCallBack(SignInActivity.this));

        userName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                // error_userName.setText("");
                userName.setError(null);
            }
        });

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                // error_password.setText("");
                password.setError(null);
            }
        });

        SignIn_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                pass_str = password.getText().toString();
                userName_str = userName.getText().toString();

                if (userName_str.contains("@") && isEmailValid(userName_str) == true) {
                    allCorrect_email = true;

                } else {

                    if (userName_str.trim().length() > 0) {

                        userName.setError(LBL_FEILD_EMAIL_ERROR_TXT_str);
                    } else {

                        userName.setError(LBL_FEILD_REQUIRD_ERROR_TXT_str);
                    }

                    allCorrect_email = false;
                }

                if (pass_str.length() < 1) {

                    if (pass_str.trim().length() > 0) {
                        password.setError("" + LBL_FEILD_PASSWORD_ERROR_TXT_str);
                    } else {
                        password.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
                    }

                    allCorrect_password = false;
                } else {
                    allCorrect_password = true;
                }

                if (allCorrect_email == true && allCorrect_password == true) {

                    new UserDetail().execute();

                }

            }
        });

        ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);

        fbLoginBtnArea.setOnClickListener(new setOnClickList());
        forgetPassLink_txt.setOnClickListener(new setOnClickList());
        back_navigation.setOnClickListener(new setOnClickList());

    }

    public void getLanguageLabelsFrmSqLite() {

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

        cursor.moveToPosition(0);

        language_labels_get_frm_sqLite = cursor.getString(0);

        JSONObject obj_language_labels = null;
        try {
            obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
            LBL_SIGN_IN_TXT_str = obj_language_labels.getString("LBL_SIGN_IN_TXT");
            LBL_USER_NAME_HINT_TXT_str = obj_language_labels.getString("LBL_USER_NAME_HINT_TXT");
            LBL_PASSWORD_HINT_TXT_str = obj_language_labels.getString("LBL_PASSWORD_HINT_TXT");
            LBL_FEILD_REQUIRD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_REQUIRD_ERROR_TXT");
            LBL_PASSWORD_LBL_TXT_str = obj_language_labels.getString("LBL_PASSWORD_LBL_TXT");
            LBL_EMAIL_LBL_TXT_str = obj_language_labels.getString("LBL_EMAIL_LBL_TXT");
            LBL_OR_TXT_str = obj_language_labels.getString("LBL_OR_TXT");
            LBL_FEILD_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_PASSWORD_ERROR_TXT");
            LBL_FEILD_EMAIL_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_EMAIL_ERROR_TXT");
            LBL_FB_LOADING_TXT_str = obj_language_labels.getString("LBL_FB_LOADING_TXT");
            LBL_LOADING_GET_INFORMATION_TXT_str = obj_language_labels.getString("LBL_LOADING_GET_INFORMATION_TXT");
            LBL_SERVICE_NOT_AVAIL_TXT_str = obj_language_labels.getString("LBL_SERVICE_NOT_AVAIL_TXT");
            LBL_SIGN_IN_LOADING_TXT_str = obj_language_labels.getString("LBL_SIGN_IN_LOADING_TXT");
            LBL_ERROR_TXT_str = obj_language_labels.getString("LBL_ERROR_TXT");
            LBL_NO_REG_FOUND_str = obj_language_labels.getString("LBL_NO_REG_FOUND");
            LBL_SIGN_UP_TXT_str = obj_language_labels.getString("LBL_SIGN_UP_TXT");
            LBL_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_PASSWORD_ERROR_TXT");
            LBL_ERROR_OCCURED_str = obj_language_labels.getString("LBL_ERROR_OCCURED");
            Default_Language_CODE = obj_language_labels.getString("vCode");
            LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str = obj_language_labels
                    .getString("LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER");
            LBL_FORGET_PASS_TXT_str = obj_language_labels.getString("LBL_FORGET_PASS_TXT");
            LBL_INFO_SET_TXT_str = obj_language_labels.getString("LBL_INFO_SET_TXT");
            LBL_BTN_OK_TXT_str = obj_language_labels.getString("LBL_BTN_OK_TXT");
            LBL_ACC_DELETE_TXT_str = obj_language_labels.getString("LBL_ACC_DELETE_TXT");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj_language_labels != null) {
            text_header.setText("" + LBL_SIGN_IN_TXT_str);
            userName.setHint("" + LBL_USER_NAME_HINT_TXT_str);

            password.setHint("" + LBL_PASSWORD_HINT_TXT_str);

            SignIn_btn.setText("" + LBL_SIGN_IN_TXT_str);

            userName.setFloatingLabelText("" + LBL_EMAIL_LBL_TXT_str);
            password.setFloatingLabelText("" + LBL_PASSWORD_LBL_TXT_str);

            or_txt.setText("" + LBL_OR_TXT_str);
            forgetPassLink_txt.setText("" + LBL_FORGET_PASS_TXT_str);
        }

        Cursor cursor_forgetPassLink = dbConnect
                .execQuery("select vValue from labels WHERE vLabel=\"Configuration_Links\"");

        try {
            cursor_forgetPassLink.moveToPosition(0);

            String link_json_str_forget_pass = cursor_forgetPassLink.getString(0);

            JSONObject forgetPassLink_obj = new JSONObject(link_json_str_forget_pass);

            forget_pass_link_str = forgetPassLink_obj.getString("LINK_FORGET_PASS_PAGE_PASSENGER");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.back_navigation:
                    SignInActivity.super.onBackPressed();
                    break;

                case R.id.forgetPassLink_txt:
                    Intent forgetPassLink = new Intent(Intent.ACTION_VIEW, Uri.parse("" + forget_pass_link_str));
                    startActivity(forgetPassLink);
                    break;

                case R.id.fbLoginBtnArea:
                    loginButton.performClick();
                    break;
            }
        }
    }

    public boolean checkPlayServices() {
        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        final int result = googleAPI.isGooglePlayServicesAvailable(SignInActivity.this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {

                SignInActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        googleAPI.getErrorDialog(SignInActivity.this, result,
                                CommonUtilities.PLAY_SERVICES_RESOLUTION_REQUEST).show();
                    }
                });

            }

            return false;
        }

        return true;
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    public class UserDetail extends AsyncTask<String, String, String> {
        String responseString = "";
        ProgressDialog pDialog;
        String GCMregistrationId = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(SignInActivity.this, R.style.ProgressDialogTheme);
            pDialog.setMessage(Html.fromHtml("<b>" + LBL_SIGN_IN_LOADING_TXT_str + "</b>"));
            // pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public String getRegisteredUserDetail() {
            String responseString = "";
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&Email=%s&Pass=%s&GCMID=%s";

            if (GCMregistrationId == null || GCMregistrationId.trim().equals("")) {
                GCMregistrationId = "";
            }

            if (URLEncoder.encode(userName_str) != null) {

                String registerUrl = baseUrl
                        + String.format(registerParam, "getDetail_signIN_passenger", URLEncoder.encode(userName_str),
                        URLEncoder.encode(pass_str), "" + GCMregistrationId);

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

        public void processGCMID_user() throws IOException {
            InstanceID instanceID = InstanceID.getInstance(SignInActivity.this);
            GCMregistrationId = instanceID.getToken(CommonUtilities.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                if (checkPlayServices() == true) {
                    processGCMID_user();
                    responseString = getRegisteredUserDetail();
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            pDialog.dismiss();

            if (GCMregistrationId != null && !GCMregistrationId.equals("") && !responseString.equals("")) {

                checkSinInResponseString(responseString);

            } else {
                Toast.makeText(SignInActivity.this, "" + LBL_SERVICE_NOT_AVAIL_TXT_str, Toast.LENGTH_LONG).show();
            }

        }
    }


    public void checkSinInResponseString(String responseString) {


        if (!responseString.trim().equals("LBL_PASSWORD_ERROR_TXT")
                && !responseString.trim().equals("LBL_NO_REG_FOUND")
                && !responseString.trim().equals("LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER")
                && !responseString.trim().equals("ACC_DELETED")) {

            JSONObject jsonObject_userDetail = null;
            try {
                jsonObject_userDetail = new JSONObject(responseString);
                String changeLangCode_str = jsonObject_userDetail.getString("changeLangCode");

                if (changeLangCode_str.equals("Yes")) {
                    String UpdatedLanguageLabels_str = jsonObject_userDetail.getString("UpdatedLanguageLabels");
                    updateLangLabels(UpdatedLanguageLabels_str);
                }

                String ProfileData_str = jsonObject_userDetail.getString("ProfileData");

                GotoProfile(ProfileData_str);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {

            if (responseString.trim().equals("LBL_NO_REG_FOUND")) {

                showMessage(LBL_ERROR_TXT_str, LBL_NO_REG_FOUND_str, false);

            }
            if (responseString.trim().equals("LBL_PASSWORD_ERROR_TXT")) {
                password.setText("");
                password.setError("" + LBL_PASSWORD_ERROR_TXT_str);
            }

            if (responseString.trim().equals("LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER")) {

                showMessage(LBL_ERROR_TXT_str, LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str, false);
            }

            if (responseString.trim().equals("ACC_DELETED")) {
                showMessage(LBL_ERROR_TXT_str, LBL_ACC_DELETE_TXT_str, false);
            }

        }

    }

    public void updateLangLabels(String json_language_labels) {
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


    public void GotoProfile(String res_json_detail) {

        if (res_json_detail.trim().equals("LBL_PASSWORD_ERROR_TXT") || res_json_detail.trim().equals("LBL_NO_REG_FOUND")
                || res_json_detail.trim().equals("LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER")
                || res_json_detail.trim().equals("ACC_DELETED")) {

            Toast.makeText(this, "" + LBL_ERROR_OCCURED_str, Toast.LENGTH_SHORT).show();
        } else {

            SignInActivity.this.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
            SignInActivity.this.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));

            JSONObject jsonObject_userDetail = null;
            try {
                jsonObject_userDetail = new JSONObject(res_json_detail);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (jsonObject_userDetail != null) {

                new OpenMainProfile(SignInActivity.this, res_json_detail).run();

                ActivityCompat.finishAffinity(this);

            } else {
                Toast.makeText(this, "" + LBL_ERROR_OCCURED_str, Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void showMessage(String title_str, String content_str, final boolean close) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                SignInActivity.this);

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
}

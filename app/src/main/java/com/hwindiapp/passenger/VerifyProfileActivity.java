package com.hwindiapp.passenger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
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

import com.fonts.Text.MyTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mainProfile.classFiles.CountrySelection_dialog;
import com.mainProfile.classFiles.OpenMainProfile;
import com.mainProfile.classFiles.OutputStreamReader;
import com.materialedittext.MaterialEditText;
import com.mainProfile.classFiles.CountrySelection_dialog.CountrySelectionListner;
import com.utils.CommonUtilities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.IntentCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyProfileActivity extends AppCompatActivity {

    MaterialEditText First_Name;
    MaterialEditText Last_Name;
    MaterialEditText email_address;
    MaterialEditText mobile_num;
    MaterialEditText inviteCodeEditBox;

    RippleStyleButton verify_button;

    RelativeLayout first_name_area;

    RelativeLayout last_name_area;

    TextView logo_text_img;

    MyTextView genderHTxt;
    MyTextView countryCode_txt;
    String countryCode_str = "";

    boolean firstName_correct = false;
    boolean LastName_correct = false;
    boolean mobile_correct = false;
    boolean email_correct = false;
    boolean country_code_correct = false;

    String id;
    String first_name_str;
    String lastName_str;
    String mobile_str;
    String country_code_str;
    String email_str;
    String GCMregistrationId = "";
//	String selected_language_code = "";

    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";
    String LBL_VERIFICATION_PAGE_HEADER_str = "";
    String LBL_FIRST_NAME_HEADER_TXT_str = "";
    String LBL_EMAIL_LBL_TXT_str = "";
    String LBL_MOBILE_NUMBER_HINT_TXT_str = "";
    String LBL_MOBILE_NUMBER_HEADER_TXT_str = "";
    String LBL_PASSWORD_LBL_TXT_str = "";
    String LBL_PASSWORD_HINT_TXT_str = "";
    String LBL_LAST_NAME_HEADER_TXT_str = "";
    String LBL_FEILD_REQUIRD_ERROR_TXT_str = "";
    String LBL_FEILD_EMAIL_ERROR_TXT_str = "";
    String LBL_VERIFY_PASSWORD_ERROR_TXT_str = "";
    String LBL_LOADING_VERIFICATION_TXT_str = "";
    String LBL_VERIFICATION_MOBILE_FAILED_TXT_str = "";
    String LBL_BTN_VERIFY_TXT_str = "";
    String LBL_COUNTRY_TXT_str = "";
    String LBL_MOBILE_VERIFICATION_FAILED_TXT_str = "";
    String LBL_REGISTER_LOADING_TXT_str = "";
    String LBL_REGISTERATION_FAILED_TXT_str = "";
    String LBL_ALREADY_REGISTERED_TXT_str = "";
    String LBL_MOBILE_EXIST_str = "";
    String LBL_FEILD_PASSWORD_ERROR_TXT_str = "";
    String LBL_MALE_TXT_str = "";
    String LBL_FEMALE_TXT_str = "";
    String LBL_SERVICE_NOT_AVAIL_TXT_str = "";
    String LBL_OPTIONAL_TXT_str = "";
    String LBL_ERROR_OCCURED_TXT_str = "";
    String LBL_INVALID_INVITE_CODE_TXT_str = "";
    String LBL_INVITE_CODE_TXT_str = "";
    String LBL_INVITE_CODE_HINT_TXT_str = "";
    String LBL_GENDER_TXT_str = "";

    String final_Fname = "";
    String final_id = "";
    String final_Lname = "";
    String final_email = "";
    String final_Mobile = "";
    //	String final_Password = "";
    String final_GCMID = "";
    //	String final_LanguageCode = "";
    String final_phoneCode = "";

    SharedPreferences mPrefs;
    Location mLastLocation;

    CountrySelection_dialog selectCountryDialog;
    boolean firstClickOnCountryTXT = true;
    String countryListJSON = "";

    private Toolbar mToolbar;

    String MOBILE_VERIFICATION_ENABLE_str = "";

    RadioButton maleRadioVBtn;
    RadioButton femaleRadioVBtn;
    RadioGroup genderSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_profile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);


        ImageView img_back = (ImageView) findViewById(R.id.back_navigation);
        img_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                VerifyProfileActivity.super.onBackPressed();

            }
        });

        mLastLocation = Launcher_TaxiServiceActivity.mLastLocation;

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        dbConnect = new DBConnect(this, "UC_labels.db");

        logo_text_img = (TextView) findViewById(R.id.text_header);

        Bundle bundle = getIntent().getExtras();

        id = bundle.getString("ID");
        first_name_str = bundle.getString("Fname");
        lastName_str = bundle.getString("Lname");
        email_str = bundle.getString("Email");
        GCMregistrationId = bundle.getString("GCMregistrationId");
//		selected_language_code = bundle.getString("selected_language_code");

        genderHTxt = (MyTextView) findViewById(R.id.genderHTxt);
        First_Name = (MaterialEditText) findViewById(R.id.First_Name);
        Last_Name = (MaterialEditText) findViewById(R.id.Last_Name);

        mobile_num = (MaterialEditText) findViewById(R.id.mobile_num);
        email_address = (MaterialEditText) findViewById(R.id.email_address);
        inviteCodeEditBox = (MaterialEditText) findViewById(R.id.inviteCodeEditBox);

        maleRadioVBtn = (RadioButton) findViewById(R.id.maleRadioVBtn);
        femaleRadioVBtn = (RadioButton) findViewById(R.id.femaleRadioVBtn);
        genderSelection = (RadioGroup) findViewById(R.id.genderSelection);

        First_Name.setText(first_name_str);
        Last_Name.setText(lastName_str);

//		if (!mobile_str.contains("NO")) {
//			mobile_num.setText(mobile_str);
//		}

//		if (password_str.equals("NO")) {
//			password.setText(password_str);
//		}

        email_address.setText(email_str);

        countryCode_txt = (MyTextView) findViewById(R.id.countryCode_txt);


        verify_button = (RippleStyleButton) findViewById(R.id.verify_button);

		/* Set Language Labels */
        getLanguageLabelsFrmSqLite();
        /* Set Language Labels Finished */

        first_name_area = (RelativeLayout) findViewById(R.id.first_name_area);

        last_name_area = (RelativeLayout) findViewById(R.id.last_name_area);

        selectCountryDialog = new CountrySelection_dialog(this, firstClickOnCountryTXT, countryListJSON);
        selectCountryDialog.setCountryListener(new CountrySelectionListner() {

            @Override
            public void handleSelectedCountryCallback(String phoneCode, String countryCode) {
                // TODO Auto-generated method stub
                countryCode_txt.setText("" + phoneCode);

                mobile_num.setError(null);
                countryCode_str = countryCode;
            }

            @Override
            public void handleCountryJSONCallback(String json_countryList) {
                // TODO Auto-generated method stub

                if (json_countryList != null && !json_countryList.equals("") && json_countryList.trim().length() > 10) {
                    countryListJSON = json_countryList;
                    firstClickOnCountryTXT = false;

                    selectCountryDialog.changeFirstClickValue(countryListJSON);
                }

            }
        });

        verify_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String first_name_str = First_Name.getText().toString();
                String lastName_str = Last_Name.getText().toString();
                String mobile_str_get = mobile_num.getText().toString();
                String email_str = email_address.getText().toString();
                String inviteCode_str = inviteCodeEditBox.getText().toString().trim();
                String country_code_str = countryCode_txt.getText().toString();

                if (first_name_str.trim().length() > 0) {
                    firstName_correct = true;
                } else {
                    firstName_correct = false;
                    First_Name.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
                }

                if (lastName_str.trim().length() > 0) {
                    LastName_correct = true;
                } else {
                    LastName_correct = false;
                    Last_Name.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
                }

                if (mobile_str_get.trim().length() > 4) {

                    if (!mobile_str_get.contains(" ")) {
                        mobile_correct = true;
                    } else {
                        mobile_correct = false;
                        mobile_num.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
                    }
                } else {
                    mobile_num.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
                }

                if (isEmailValid(email_str)) {
                    email_correct = true;
                } else {
                    email_correct = false;
                    email_address.setError("" + LBL_FEILD_EMAIL_ERROR_TXT_str);
                }

                if (country_code_str.matches("[0-9]+")) {
                    country_code_correct = true;
                } else if (country_code_correct == false) {
                    country_code_correct = false;
                    mobile_num.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
                }

                if (firstName_correct == true && LastName_correct == true && mobile_correct == true
                        && email_correct == true && country_code_correct == true) {

                    new check_registerationInfo_user(email_str, mobile_str_get, country_code_str, inviteCode_str).execute();

                }

            }
        });

        email_address.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                email_address.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        inviteCodeEditBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                inviteCodeEditBox.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        First_Name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                First_Name.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        Last_Name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                Last_Name.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        mobile_num.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                mobile_num.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        countryCode_txt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                selectCountryDialog.run();
            }
        });

        // initializePopUpWindow();
    }


    public void finalRegisterNewUser() {
        String mobile_str_get = mobile_num.getText().toString();
        String country_code_str = countryCode_txt.getText().toString();
        String email_str = email_address.getText().toString();

        String first_name_str = First_Name.getText().toString();
        String lastName_str = Last_Name.getText().toString();

        final_Fname = "" + first_name_str;
        final_id = "" + id;
        final_Lname = "" + lastName_str;
        final_email = "" + email_str;
        final_Mobile = "" + mobile_str_get;
//		final_Password = "" + password_str;
        final_GCMID = "" + GCMregistrationId;
//		final_LanguageCode = "" + selected_language_code;
        final_phoneCode = "" + country_code_str;

        new Register_NewUser().execute();

    }

    class check_registerationInfo_user extends AsyncTask<String, String, String> {

        String email_str = "";
        String mobile_str = "";
        String country_code_str = "";
        boolean registered = false;
        ProgressDialog pDialog;
        String inviteCode_str = "";
        String responseString = "";


        public check_registerationInfo_user(String email_str, String mobile_str, String country_code_str, String inviteCode_str) {
            this.email_str = email_str;
            this.mobile_str = mobile_str;
            this.country_code_str = country_code_str;
            this.inviteCode_str = inviteCode_str;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(VerifyProfileActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage("" + LBL_LOADING_VERIFICATION_TXT_str);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        public void check_registration() {

            // checkRegisteredUser(email_str);

            // String baseUrl = CommonUtilities.SERVER_URL_GET_DETAIL;
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&Email=%s&Phone=%s&InviteCode=%s";

            if (URLEncoder.encode(email_str) != null) {

                String registerUrl = baseUrl + String.format(registerParam, "checkUser_passenger",
                        URLEncoder.encode(email_str), URLEncoder.encode(mobile_str), URLEncoder.encode(inviteCode_str));

//				Log.d("registerUrl","registerUrl:"+registerUrl);
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

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            check_registration();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();


            if (responseString != null && !responseString.trim().equals("")) {

                try {
                    String message_str = (new JSONObject(responseString)).getString("MSG");

                    if (message_str.equals("NO_REG_FOUND")) {
                        String inviteCode_str = (new JSONObject(responseString)).getString("vInviteCode");

                        if (this.inviteCode_str.trim().equals("") || inviteCode_str.trim().equals("1")) {

                            if (MOBILE_VERIFICATION_ENABLE_str.equals("Yes")) {

                                Intent in = new Intent(VerifyProfileActivity.this, VerifyMobileActivity.class);

//                                in.putExtra("mobile", country_code_str + mobile_str);
                                in.putExtra("mobile", mobile_str);
                                in.putExtra("country", country_code_str);
                                startActivityForResult(in, VerifyMobileActivity.REQUEST_CODE);
                            } else {
                                finalRegisterNewUser();
                            }

                        } else {
                            inviteCodeEditBox.setError(LBL_INVALID_INVITE_CODE_TXT_str);
                        }


                    } else {
                        if (message_str.trim().equals("EMAIL_EXIST")) {
                            Toast.makeText(VerifyProfileActivity.this, "" + LBL_ALREADY_REGISTERED_TXT_str, Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Toast.makeText(VerifyProfileActivity.this, "" + LBL_ERROR_OCCURED_TXT_str, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(VerifyProfileActivity.this, "" + LBL_ERROR_OCCURED_TXT_str, Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(VerifyProfileActivity.this, "" + LBL_ERROR_OCCURED_TXT_str, Toast.LENGTH_LONG).show();
            }

//			if (responseString != null && responseString.trim().equals("NO_REG_FOUND")) {
//
//				if(MOBILE_VERIFICATION_ENABLE_str.equals("Yes")){
//
//					Intent in = new Intent(VerifyProfileActivity.this, VerifyMobileActivity.class);
//
//					in.putExtra("mobile", country_code_str + mobile_str);
//					startActivityForResult(in, VerifyMobileActivity.REQUEST_CODE);
//				}else{
//					finalRegisterNewUser();
//				}
//
//			} else {
//
//				if (responseString.trim().equals("EMAIL_EXIST")) {
//					Toast.makeText(VerifyProfileActivity.this, "" + LBL_ALREADY_REGISTERED_TXT_str, Toast.LENGTH_LONG)
//							.show();
//				} else {
//					Toast.makeText(VerifyProfileActivity.this, "" + LBL_MOBILE_EXIST_str, Toast.LENGTH_LONG).show();
//				}
//
//			}

        }
    }

    public boolean checkPlayServices() {
        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        final int result = googleAPI.isGooglePlayServicesAvailable(VerifyProfileActivity.this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {

                VerifyProfileActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        googleAPI.getErrorDialog(VerifyProfileActivity.this, result,
                                CommonUtilities.PLAY_SERVICES_RESOLUTION_REQUEST).show();
                    }
                });

            }

            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);

        if (arg0 == VerifyMobileActivity.REQUEST_CODE && arg1 == RESULT_OK) {
            String message = arg2.getStringExtra("message");
            int result = arg2.getIntExtra("result", 0);

            if (message.contains("SUCCESS")) {

                finalRegisterNewUser();

            } else {

                Toast.makeText(getApplicationContext(), "" + LBL_MOBILE_VERIFICATION_FAILED_TXT_str, Toast.LENGTH_SHORT)
                        .show();

            }

        }
    }

    public class Register_NewUser extends AsyncTask<String, String, String> {

        String responseString = "";
//		String cityName = "";

        ProgressDialog pDialog;

        String reg_gcmId = "";

        String configuration_fare_json = "";
        boolean errorInConnection = false;
        String gender_str = "";
        String inviteCode_str = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            if (genderSelection.getCheckedRadioButtonId() == R.id.maleRadioVBtn) {
                gender_str = "Male";
            } else {
                gender_str = "Female";
            }
            inviteCode_str = inviteCodeEditBox.getText().toString().trim();

            pDialog = new ProgressDialog(VerifyProfileActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage("" + LBL_REGISTER_LOADING_TXT_str);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

//			cityName = getCity(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            // String baseUrl = SERVER_URL + "register_user_new.php?";
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&fbid=%s&Fname=%s&Lname=%s&email=%s&phone=%s"
                    + "&GCMID=%s&PhoneCode=%s&CountryCode=%s&Gender=%s&InviteCode=%s";

            String registerUrl = baseUrl + String.format(registerParam, "registerFbUser", final_id,
                    "" + URLEncoder.encode(final_Fname), "" + URLEncoder.encode(final_Lname), "" + final_email,
                    final_Mobile, "" + final_GCMID, URLEncoder.encode("" + final_phoneCode), URLEncoder.encode("" + countryCode_str), "" + gender_str, "" + inviteCode_str);

//            Log.d("registerUrl","::"+registerUrl);
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(registerUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = OutputStreamReader.readStream(in);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                errorInConnection = true;
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

            if (pDialog != null) {
                pDialog.dismiss();
            }

            processTOprofile(responseString, errorInConnection);
        }

    }

    public void processTOprofile(String json_server_registrationStatus, boolean errorInConnection) {

        if (errorInConnection == false && !json_server_registrationStatus.trim().equals("")
                && !json_server_registrationStatus.trim().equals("EMAIL_EXIST")
                && !json_server_registrationStatus.trim().equals("MOBILE_EXIST")) {

            new OpenMainProfile(VerifyProfileActivity.this, json_server_registrationStatus).run();
            ActivityCompat.finishAffinity(this);

        } else {

            if (errorInConnection == false && json_server_registrationStatus.trim().equals("EMAIL_EXIST")) {
                Toast.makeText(VerifyProfileActivity.this, "" + LBL_ALREADY_REGISTERED_TXT_str, Toast.LENGTH_LONG)
                        .show();
            } else if (errorInConnection == false && json_server_registrationStatus.trim().equals("MOBILE_EXIST")) {
                Toast.makeText(VerifyProfileActivity.this, "" + LBL_MOBILE_EXIST_str, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(VerifyProfileActivity.this, "" + LBL_REGISTERATION_FAILED_TXT_str, Toast.LENGTH_SHORT)
                        .show();
            }

            if (!id.equals("0")) {

                String access_token_main = mPrefs.getString("access_token", null);

                if (access_token_main != null) {

                    mPrefs.edit().remove("access_expires").commit();
                    mPrefs.edit().remove("access_token").commit();
                    mPrefs.edit().clear().commit();

                    Intent start = new Intent(VerifyProfileActivity.this, Launcher_TaxiServiceActivity.class);
                    start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    // Going_to_launchActivity=true;
                    startActivity(start);
                }

            }

        }
    }


    public static String randInt(int min, int max) {

        String code = "";
        for (int i = 0; i < 4; i++) {
            Random rand = new Random();
            int randomNum = rand.nextInt((max - min) + 1) + min;
            // Log.d("NUMBER::", "" + randomNum);
            code = code + "" + randomNum;
        }

        return code;
    }

    public boolean isPasswordCorrect(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{5,10}";
        // System.out.println(password.matches(pattern));

        boolean pass_matchOrNot = password.matches(pattern);

        return pass_matchOrNot;
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

    public void getLanguageLabelsFrmSqLite() {

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

        cursor.moveToPosition(0);

        language_labels_get_frm_sqLite = cursor.getString(0);

        JSONObject obj_language_labels = null;
        try {
            obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
            LBL_VERIFICATION_PAGE_HEADER_str = obj_language_labels.getString("LBL_VERIFICATION_PAGE_HEADER");
            LBL_FIRST_NAME_HEADER_TXT_str = obj_language_labels.getString("LBL_FIRST_NAME_HEADER_TXT");
            LBL_LAST_NAME_HEADER_TXT_str = obj_language_labels.getString("LBL_LAST_NAME_HEADER_TXT");
            LBL_MOBILE_NUMBER_HINT_TXT_str = obj_language_labels.getString("LBL_MOBILE_NUMBER_HINT_TXT");
            LBL_MOBILE_NUMBER_HEADER_TXT_str = obj_language_labels.getString("LBL_MOBILE_NUMBER_HEADER_TXT");
            LBL_PASSWORD_LBL_TXT_str = obj_language_labels.getString("LBL_PASSWORD_LBL_TXT");
            LBL_PASSWORD_HINT_TXT_str = obj_language_labels.getString("LBL_PASSWORD_HINT_TXT");
            LBL_EMAIL_LBL_TXT_str = obj_language_labels.getString("LBL_EMAIL_LBL_TXT");
            LBL_FEILD_REQUIRD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_REQUIRD_ERROR_TXT");
            LBL_FEILD_EMAIL_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_EMAIL_ERROR_TXT");
            LBL_VERIFY_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_VERIFY_PASSWORD_ERROR_TXT");
            LBL_LOADING_VERIFICATION_TXT_str = obj_language_labels.getString("LBL_LOADING_VERIFICATION_TXT");
            LBL_VERIFICATION_MOBILE_FAILED_TXT_str = obj_language_labels
                    .getString("LBL_VERIFICATION_MOBILE_FAILED_TXT");
            LBL_BTN_VERIFY_TXT_str = obj_language_labels.getString("LBL_BTN_VERIFY_TXT");
            LBL_COUNTRY_TXT_str = obj_language_labels.getString("LBL_COUNTRY_TXT");
            LBL_MOBILE_VERIFICATION_FAILED_TXT_str = obj_language_labels
                    .getString("LBL_MOBILE_VERIFICATION_FAILED_TXT");
            LBL_REGISTER_LOADING_TXT_str = obj_language_labels.getString("LBL_REGISTER_LOADING_TXT");
            LBL_REGISTERATION_FAILED_TXT_str = obj_language_labels.getString("LBL_REGISTERATION_FAILED_TXT");
            LBL_ALREADY_REGISTERED_TXT_str = obj_language_labels.getString("LBL_ALREADY_REGISTERED_TXT");
            LBL_MOBILE_EXIST_str = obj_language_labels.getString("LBL_MOBILE_EXIST");
            LBL_FEILD_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_PASSWORD_ERROR_TXT");
            LBL_MALE_TXT_str = obj_language_labels.getString("LBL_MALE_TXT");
            LBL_FEMALE_TXT_str = obj_language_labels.getString("LBL_FEMALE_TXT");
            LBL_SERVICE_NOT_AVAIL_TXT_str = obj_language_labels.getString("LBL_SERVICE_NOT_AVAIL_TXT");
            LBL_OPTIONAL_TXT_str = obj_language_labels.getString("LBL_OPTIONAL_TXT");
            LBL_ERROR_OCCURED_TXT_str = obj_language_labels.getString("LBL_ERROR_OCCURED_TXT");
            LBL_INVALID_INVITE_CODE_TXT_str = obj_language_labels.getString("LBL_INVALID_INVITE_CODE_TXT");
            LBL_INVITE_CODE_TXT_str = obj_language_labels.getString("LBL_INVITE_CODE_TXT");
            LBL_INVITE_CODE_HINT_TXT_str = obj_language_labels.getString("LBL_INVITE_CODE_HINT_TXT");
            LBL_GENDER_TXT_str = obj_language_labels.getString("LBL_GENDER_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj_language_labels != null) {
            logo_text_img.setText("" + LBL_VERIFICATION_PAGE_HEADER_str);

            First_Name.setHint("" + LBL_FIRST_NAME_HEADER_TXT_str);
            Last_Name.setHint("" + LBL_LAST_NAME_HEADER_TXT_str);

            mobile_num.setHint("" + LBL_MOBILE_NUMBER_HINT_TXT_str);
            email_address.setHint("" + LBL_EMAIL_LBL_TXT_str);

            First_Name.setFloatingLabelText("" + LBL_FIRST_NAME_HEADER_TXT_str);
            Last_Name.setFloatingLabelText("" + LBL_LAST_NAME_HEADER_TXT_str);
            email_address.setFloatingLabelText("" + LBL_EMAIL_LBL_TXT_str);
            mobile_num.setFloatingLabelText("" + LBL_MOBILE_NUMBER_HEADER_TXT_str);
            verify_button.setText("" + LBL_BTN_VERIFY_TXT_str);
            countryCode_txt.setText("" + LBL_COUNTRY_TXT_str);

            maleRadioVBtn.setText(LBL_MALE_TXT_str);
            femaleRadioVBtn.setText(LBL_FEMALE_TXT_str);

            inviteCodeEditBox.setHint(LBL_INVITE_CODE_HINT_TXT_str);
            inviteCodeEditBox.setHelperText(LBL_OPTIONAL_TXT_str);
            inviteCodeEditBox.setFloatingLabelText(LBL_INVITE_CODE_TXT_str);
            inviteCodeEditBox.setHelperTextAlwaysShown(true);

            genderHTxt.setText(LBL_GENDER_TXT_str);


        }

        Cursor cursor_configData = dbConnect
                .execQuery("select vValue from labels WHERE vLabel=\"Configuration_Links\"");

        try {
            cursor_configData.moveToPosition(0);

            String json_configData = cursor_configData.getString(0);

            JSONObject configData_obj = new JSONObject(json_configData);

            MOBILE_VERIFICATION_ENABLE_str = configData_obj.getString("MOBILE_VERIFICATION_ENABLE");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

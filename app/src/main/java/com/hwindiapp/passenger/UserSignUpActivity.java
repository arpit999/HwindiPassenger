package com.hwindiapp.passenger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.WebDialog;
import com.facebook.login.widget.LoginButton;
import com.fonts.Text.MyTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.hwindiapp.passenger.languageLabels.ProcessLanguage_labels;
import com.hwindiapp.passenger.languageLabels.ProcessLanguage_labels.UpdateLanguageListner;
import com.mainProfile.classFiles.CountrySelection_dialog;
import com.mainProfile.classFiles.CountrySelection_dialog.CountrySelectionListner;
import com.mainProfile.classFiles.OpenMainProfile;
import com.mainProfile.classFiles.OutputStreamReader;
import com.mainProfile.classFiles.RegisterFbLoginResCallBack;
import com.materialedittext.MaterialEditText;
import com.utils.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/

public class UserSignUpActivity extends AppCompatActivity implements UpdateLanguageListner {

    MaterialEditText First_Name;
    MaterialEditText Last_Name;
    MaterialEditText email_address;
    MaterialEditText mobile_num;
    MaterialEditText password;
    MaterialEditText Repassword;
    MaterialEditText inviteCodeEditBox;

    PopupWindow popup_country = null;

    String id;
    String name;
    String fname;
    String lname;
    String email;
    String gender;


    SharedPreferences mPrefs;

    RippleStyleButton Next_button;
    MyTextView select_language_box;
    TextView logo_text_img;
    MyTextView or_txt;
    MyTextView genderHTxt;

    MaterialEditText countryCode_txt;
    String countryCode_str = "";


    boolean normal_signUP = true;

    public static String access_token = "";
    public static long access_expires;

    ProgressDialog dialog_change_LanguageCode;

    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";

    String LBL_REGISTRATION_PAGE_HEADER_TXT_str = "";
    String LBL_FIRST_NAME_HEADER_TXT_str = "";
    String LBL_FEILD_REQUIRD_ERROR_TXT_str = "";
    String LBL_LAST_NAME_HEADER_TXT_str = "";
    String LBL_FEILD_PASSWORD_ERROR_TXT_str = "";
    String LBL_EMAIL_LBL_TXT_str = "";
    String LBL_MOBILE_NUMBER_HINT_TXT_str = "";
    String LBL_MOBILE_NUMBER_HEADER_TXT_str = "";
    String LBL_PASSWORD_LBL_TXT_str = "";
    String LBL_PASSWORD_HINT_TXT_str = "";
    String LBL_BTN_NEXT_TXT_str = "";
    String LBL_FEILD_EMAIL_ERROR_TXT_str = "";
    String LBL_ALREADY_REGISTERED_TXT_str = "";
    String LBL_LOADING_VERIFICATION_TXT_str = "";
    String LBL_SERVICE_NOT_AVAIL_TXT_str = "";
    String LBL_FB_LOADING_TXT_str = "";
    String LBL_LOADING_GET_INFORMATION_TXT_str = "";
    String LBL_OR_TXT_str = "";
    String LBL_SELECT_LANGUAGE_str = "";
    String LBL_COUNTRY_TXT_str = "";
    String LBL_SELECT_LANGUAGE_HINT_TXT_str = "";
    String LBL_SELECT_TXT_str = "";
    String LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str = "";
    String LBL_VERIFY_PASSWORD_HINT_TXT_str = "";
    String LBL_VERIFY_PASSWORD_ERROR_TXT_str = "";
    String LBL_MOBILE_VERIFICATION_FAILED_TXT_str = "";
    String LBL_REGISTERATION_FAILED_TXT_str = "";
    String LBL_REGISTER_LOADING_TXT_str = "";
    String LBL_INFO_SET_TXT_str = "";
    String LBL_MOBILE_EXIST_str = "";
    String LBL_MALE_TXT_str = "";
    String LBL_FEMALE_TXT_str = "";
    String LBL_OPTIONAL_TXT_str = "";
    String LBL_ERROR_OCCURED_TXT_str = "";
    String LBL_INVALID_INVITE_CODE_TXT_str = "";
    String LBL_INVITE_CODE_TXT_str = "";
    String LBL_INVITE_CODE_HINT_TXT_str = "";
    String LBL_GENDER_TXT_str = "";

    AlertDialog alert_show_language_list;

    ArrayList<String> items_txt_language = new ArrayList<String>();
    ArrayList<String> items_language_code = new ArrayList<String>();
    ArrayList<String> items_language_id = new ArrayList<String>();

    String selected_language = "";
    String selected_language_code = "";

    boolean chooseLanguage = false;

    String Default_language_code = "";
    String GCMregistrationId_str = "";

    boolean registered_retrive;
    String first_name_str_retrive;
    String mobile_str_retrive;
    String lastName_str_retrive;
    String email_str_retrive;
    String password_str_retrive;
    String GCMregistrationId_retrive;
    String selected_language_code_retrive;
    String country_code_str_retrive = "";

    String final_Fname = "";
    String final_id = "";
    String final_Lname = "";
    String final_email = "";
    String final_Mobile = "";
    String final_Password = "";
    String final_GCMID = "";
    String final_LanguageCode = "";
    String final_phoneCode = "";

    Location mLastLocation;

    CountrySelection_dialog selectCountryDialog;
    boolean firstClickOnCountryTXT = true;
    String countryListJSON = "";

    private Toolbar mToolbar;

    String MOBILE_VERIFICATION_ENABLE_str = "";

    LoginButton loginButton;
    LinearLayout fbLoginBtnArea;
    CallbackManager callbackManager;

    RadioButton maleRadioBtn;
    RadioButton femaleRadioBtn;
    RadioGroup genderSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        WebDialog.setWebDialogTheme(R.style.FBDialogtheme);

        setContentView(R.layout.activity_user_sign_up);

        FacebookSdk.setApplicationId(CommonUtilities.FB_PROJECT_ID);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);


        ImageView img_back = (ImageView) findViewById(R.id.back_navigation);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        mLastLocation = Launcher_TaxiServiceActivity.mLastLocation;

        dbConnect = new DBConnect(this, "UC_labels.db");

        logo_text_img = (TextView) findViewById(R.id.text_header);

        or_txt = (MyTextView) findViewById(R.id.or_txt);
        genderHTxt = (MyTextView) findViewById(R.id.genderHTxt);

        First_Name = (MaterialEditText) findViewById(R.id.First_Name);
        Last_Name = (MaterialEditText) findViewById(R.id.Last_Name);
        email_address = (MaterialEditText) findViewById(R.id.email_address);
        mobile_num = (MaterialEditText) findViewById(R.id.mobile_num);
        password = (MaterialEditText) findViewById(R.id.password);
        Repassword = (MaterialEditText) findViewById(R.id.Repassword);
        inviteCodeEditBox = (MaterialEditText) findViewById(R.id.inviteCodeEditBox);
        loginButton = (LoginButton) findViewById(R.id.FBlogin_button);
        fbLoginBtnArea = (LinearLayout) findViewById(R.id.fbLoginBtnArea);

        maleRadioBtn = (RadioButton) findViewById(R.id.maleRadioBtn);
        femaleRadioBtn = (RadioButton) findViewById(R.id.femaleRadioBtn);
        genderSelection = (RadioGroup) findViewById(R.id.genderSelection);


        select_language_box = (MyTextView) findViewById(R.id.select_language_box);


        countryCode_txt = (MaterialEditText) findViewById(R.id.countryCode_txt);

        Next_button = (RippleStyleButton) findViewById(R.id.Next_button);


        countryCode_txt.setCursorVisible(false);
        countryCode_txt.setLongClickable(true);
        countryCode_txt.setFocusable(false);

        countryCode_txt.setClickable(true);
        countryCode_txt.setSelected(false);
        countryCode_txt.setKeyListener(null);


		/* Set Labels */
        getLanguageLabelsFrmSqLite();
        /* Set Labels Finished */


        email_address.addTextChangedListener(new GenericTextWatcher(email_address));
        First_Name.addTextChangedListener(new GenericTextWatcher(First_Name));
        Last_Name.addTextChangedListener(new GenericTextWatcher(Last_Name));
        mobile_num.addTextChangedListener(new GenericTextWatcher(mobile_num));
        password.addTextChangedListener(new GenericTextWatcher(password));
        Repassword.addTextChangedListener(new GenericTextWatcher(Repassword));
        inviteCodeEditBox.addTextChangedListener(new GenericTextWatcher(inviteCodeEditBox));

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends", "user_about_me"));

        loginButton.registerCallback(callbackManager, new RegisterFbLoginResCallBack(UserSignUpActivity.this));

//        select_language_box.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (alert_show_language_list != null) {
//                    alert_show_language_list.show();
//                }
//            }
//        });

        selectCountryDialog = new CountrySelection_dialog(this, firstClickOnCountryTXT, countryListJSON);
        selectCountryDialog.setCountryListener(new CountrySelectionListner() {

            @Override
            public void handleSelectedCountryCallback(String phoneCode, String countryCode) {
                // TODO Auto-generated method stub
                countryCode_txt.setText("" + phoneCode);

                countryCode_txt.setError(null);

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

//        Next_button.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                checkSignUpValues();
//            }
//        });
        fbLoginBtnArea.setOnClickListener(new setOnClickList());
        img_back.setOnClickListener(new setOnClickList());
        countryCode_txt.setOnClickListener(new setOnClickList());
        select_language_box.setOnClickListener(new setOnClickList());
        Next_button.setOnClickListener(new setOnClickList());
//        countryCode_txt.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//
//                selectCountryDialog.run();
//            }
//        });

    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.back_navigation:
                    UserSignUpActivity.super.onBackPressed();
                    break;

                case R.id.countryCode_txt:
                    selectCountryDialog.run();
                    break;

                case R.id.Next_button:
                    checkSignUpValues();
                    break;

                case R.id.select_language_box:
                    if (alert_show_language_list != null) {
                        alert_show_language_list.show();
                    }
                    break;

                case R.id.fbLoginBtnArea:
                    loginButton.performClick();
                    break;
            }
        }
    }

    public void checkSignUpValues() {

        boolean firstName_correct = false;
        boolean LastName_correct = false;
        boolean mobile_correct = false;
        boolean password_correct = false;
        boolean Repassword_correct = false;
        boolean email_correct = false;
        boolean country_code_correct = false;

        normal_signUP = true;
        String first_name_str = First_Name.getText().toString();
        String lastName_str = Last_Name.getText().toString();
        String mobile_str = mobile_num.getText().toString();
        String password_str = password.getText().toString();
        String Repassword_str = Repassword.getText().toString();
        String email_str = email_address.getText().toString();
        String inviteCode_str = inviteCodeEditBox.getText().toString().trim();

        String country_code_str = countryCode_txt.getText().toString();

        if (mobile_str.trim().length() > 4) {

            if (!mobile_str.contains(" ")) {
                mobile_correct = true;
                mobile_num.setError(null);
            } else {
                // mobile_correct=false;
                mobile_num.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);

                mobile_correct = false;
            }
        } else {
            mobile_num.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
        }

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
//			error_Last_Name.setVisibility(View.VISIBLE);
            Last_Name.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
        }

        if (password_str.trim().length() > 0) {

            password_correct = true;
        } else {
            password_correct = false;
            password.setError("" + LBL_FEILD_PASSWORD_ERROR_TXT_str);

            if (password_str.trim().length() > 0) {
                password.setError(LBL_FEILD_PASSWORD_ERROR_TXT_str);
            } else {
                password.setError(LBL_FEILD_REQUIRD_ERROR_TXT_str);
            }

        }


        if (isEmailValid(email_str)) {
            email_correct = true;
        } else {
            email_address.setError("" + LBL_FEILD_EMAIL_ERROR_TXT_str);
            email_correct = false;

            if (email_str.trim().length() > 0) {
                email_address.setError(LBL_FEILD_REQUIRD_ERROR_TXT_str);
            } else {
                email_address.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
            }
        }

        if (country_code_str.matches("[0-9]+")) {
            country_code_correct = true;
        } else if (country_code_correct == false) {
            country_code_correct = false;

            countryCode_txt.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);

        }

        if (Repassword_str.equals(password_str)) {
            Repassword_correct = true;

        } else {
            Repassword_correct = false;
            Repassword.setError("" + LBL_VERIFY_PASSWORD_ERROR_TXT_str);
        }

        if (firstName_correct == true && LastName_correct == true && mobile_correct == true && password_correct == true
                && email_correct == true && country_code_correct == true && Repassword_correct == true) {

            if (!selected_language_code.equals("") && selected_language_code.trim().length() > 1
                    && chooseLanguage == true) {
                new check_registerationInfo_user(first_name_str, country_code_str,
                        /* country_code_str + */mobile_str, lastName_str, email_str, password_str, inviteCode_str).execute();
            } else {
                Toast.makeText(UserSignUpActivity.this, "" + LBL_SELECT_LANGUAGE_str, Toast.LENGTH_LONG).show();
            }

        }

    }


    public class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            switch (view.getId()) {

                case R.id.email_address:
                    email_address.setError(null);
                    break;

                case R.id.First_Name:
                    First_Name.setError(null);
                    break;

                case R.id.Last_Name:
                    Last_Name.setError(null);
                    break;

                case R.id.mobile_num:
                    mobile_num.setError(null);
                    break;

                case R.id.password:
                    password.setError(null);
                    break;

                case R.id.Repassword:
                    Repassword.setError(null);
                    break;

                case R.id.inviteCodeEditBox:
                    inviteCodeEditBox.setError(null);
                    break;

            }

        }

    }

    public void verify_info(boolean registered, String first_name_str, String country_str, String mobile_str, String lastName_str,
                            String email_str, String password_str, String GCMregistrationId, String selected_language_code) {

        if (registered == false) {

            GCMregistrationId_str = GCMregistrationId;
            Intent in = new Intent(UserSignUpActivity.this, VerifyMobileActivity.class);

//            in.putExtra("mobile", mobile_str);
            in.putExtra("mobile", mobile_str);
            in.putExtra("country", country_str);
            startActivityForResult(in, VerifyMobileActivity.REQUEST_CODE);
        } else {
            Toast.makeText(UserSignUpActivity.this, "" + LBL_ALREADY_REGISTERED_TXT_str, Toast.LENGTH_LONG).show();
        }

    }

    public void getLanguageLabelsFrmSqLite() {

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

        cursor.moveToPosition(0);

        language_labels_get_frm_sqLite = cursor.getString(0);

        JSONObject obj_language_labels = null;
        try {
            obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
            LBL_REGISTRATION_PAGE_HEADER_TXT_str = obj_language_labels.getString("LBL_REGISTRATION_PAGE_HEADER_TXT");
            LBL_FIRST_NAME_HEADER_TXT_str = obj_language_labels.getString("LBL_FIRST_NAME_HEADER_TXT");
            LBL_FEILD_REQUIRD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_REQUIRD_ERROR_TXT");
            LBL_LAST_NAME_HEADER_TXT_str = obj_language_labels.getString("LBL_LAST_NAME_HEADER_TXT");
            LBL_FEILD_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_PASSWORD_ERROR_TXT");
            LBL_EMAIL_LBL_TXT_str = obj_language_labels.getString("LBL_EMAIL_LBL_TXT");
            LBL_MOBILE_NUMBER_HINT_TXT_str = obj_language_labels.getString("LBL_MOBILE_NUMBER_HINT_TXT");
            LBL_MOBILE_NUMBER_HEADER_TXT_str = obj_language_labels.getString("LBL_MOBILE_NUMBER_HEADER_TXT");
            LBL_PASSWORD_LBL_TXT_str = obj_language_labels.getString("LBL_PASSWORD_LBL_TXT");
            LBL_PASSWORD_HINT_TXT_str = obj_language_labels.getString("LBL_PASSWORD_HINT_TXT");
            LBL_BTN_NEXT_TXT_str = obj_language_labels.getString("LBL_BTN_NEXT_TXT");
            LBL_FEILD_EMAIL_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_EMAIL_ERROR_TXT");
            LBL_ALREADY_REGISTERED_TXT_str = obj_language_labels.getString("LBL_ALREADY_REGISTERED_TXT");
            LBL_LOADING_VERIFICATION_TXT_str = obj_language_labels.getString("LBL_LOADING_VERIFICATION_TXT");
            LBL_SERVICE_NOT_AVAIL_TXT_str = obj_language_labels.getString("LBL_SERVICE_NOT_AVAIL_TXT");
            LBL_FB_LOADING_TXT_str = obj_language_labels.getString("LBL_FB_LOADING_TXT");
            LBL_LOADING_GET_INFORMATION_TXT_str = obj_language_labels.getString("LBL_FB_LOADING_TXT");
            LBL_OR_TXT_str = obj_language_labels.getString("LBL_OR_TXT");
            LBL_SELECT_LANGUAGE_str = obj_language_labels.getString("LBL_SELECT_LANGUAGE");
            LBL_COUNTRY_TXT_str = obj_language_labels.getString("LBL_COUNTRY_TXT");
            LBL_SELECT_LANGUAGE_HINT_TXT_str = obj_language_labels.getString("LBL_SELECT_LANGUAGE_HINT_TXT");
            LBL_SELECT_TXT_str = obj_language_labels.getString("LBL_SELECT_TXT");
            LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str = obj_language_labels
                    .getString("LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT");
            LBL_VERIFY_PASSWORD_HINT_TXT_str = obj_language_labels.getString("LBL_VERIFY_PASSWORD_HINT_TXT");
            LBL_VERIFY_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_VERIFY_PASSWORD_ERROR_TXT");
            LBL_MOBILE_VERIFICATION_FAILED_TXT_str = obj_language_labels
                    .getString("LBL_MOBILE_VERIFICATION_FAILED_TXT");
            LBL_REGISTERATION_FAILED_TXT_str = obj_language_labels.getString("LBL_REGISTERATION_FAILED_TXT");

            LBL_REGISTER_LOADING_TXT_str = obj_language_labels.getString("LBL_REGISTER_LOADING_TXT");
            LBL_INFO_SET_TXT_str = obj_language_labels.getString("LBL_REGISTER_LOADING_TXT");
            LBL_MOBILE_EXIST_str = obj_language_labels.getString("LBL_MOBILE_EXIST");
            LBL_MALE_TXT_str = obj_language_labels.getString("LBL_MALE_TXT");
            LBL_FEMALE_TXT_str = obj_language_labels.getString("LBL_FEMALE_TXT");
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

            logo_text_img.setText("" + LBL_REGISTRATION_PAGE_HEADER_TXT_str);

            First_Name.setHint(LBL_FIRST_NAME_HEADER_TXT_str);
            First_Name.setFloatingLabelText("" + LBL_FIRST_NAME_HEADER_TXT_str);

            Last_Name.setHint(LBL_LAST_NAME_HEADER_TXT_str);
            Last_Name.setFloatingLabelText("" + LBL_LAST_NAME_HEADER_TXT_str);

            email_address.setHint(LBL_EMAIL_LBL_TXT_str);
            email_address.setFloatingLabelText("" + LBL_EMAIL_LBL_TXT_str);

            mobile_num.setHint(LBL_MOBILE_NUMBER_HINT_TXT_str);
            mobile_num.setFloatingLabelText("" + LBL_MOBILE_NUMBER_HEADER_TXT_str);

            password.setHint(LBL_PASSWORD_HINT_TXT_str);
            password.setFloatingLabelText(LBL_PASSWORD_LBL_TXT_str);

            Repassword.setHint("" + LBL_VERIFY_PASSWORD_HINT_TXT_str);
            Repassword.setFloatingLabelText("" + LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str);

//			password.setHint(LBL_PASSWORD_HINT_TXT_str);

            Next_button.setText("" + LBL_BTN_NEXT_TXT_str);
            or_txt.setText(LBL_OR_TXT_str);

            countryCode_txt.setHint("" + LBL_COUNTRY_TXT_str);
            countryCode_txt.setFloatingLabelText("" + LBL_COUNTRY_TXT_str);

            select_language_box.setHint("" + LBL_SELECT_LANGUAGE_HINT_TXT_str);
            maleRadioBtn.setText(LBL_MALE_TXT_str);
            femaleRadioBtn.setText(LBL_FEMALE_TXT_str);

            inviteCodeEditBox.setHint(LBL_INVITE_CODE_HINT_TXT_str);
            inviteCodeEditBox.setHelperText(LBL_OPTIONAL_TXT_str);
            inviteCodeEditBox.setHelperTextAlwaysShown(true);
            inviteCodeEditBox.setFloatingLabelText(LBL_INVITE_CODE_TXT_str);
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

        setLanguage_list();

    }

    public void setLanguage_list() {
        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_List\"");

        String txt_language_list;

        cursor.moveToPosition(0);

        txt_language_list = cursor.getString(0).trim();

        cursor.close();

        JSONObject obj_languages = null;
        JSONArray list_obj_languages = null;
        try {
            obj_languages = new JSONObject(txt_language_list);
            list_obj_languages = obj_languages.getJSONArray("list_languages");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int i = 0; i < list_obj_languages.length(); i++) {

            JSONObject obj_temp = null;
            try {
                obj_temp = list_obj_languages.getJSONObject(i);
                items_txt_language.add(obj_temp.getString("vTitle"));
                items_language_code.add(obj_temp.getString("vCode"));
                items_language_id.add(obj_temp.getString("iLanguageMasId"));

                String default_languageCode = "" + obj_temp.getString("eDefault");
                if (default_languageCode.equals("Yes")) {
                    Default_language_code = "" + obj_temp.getString("vCode");

                    selected_language = obj_temp.getString("vTitle");
                    selected_language_code = obj_temp.getString("vCode");

                    chooseLanguage = true;
                }

                // selected_language_code

//                if (obj_temp.getString("vTitle").equals("English")) {
//                    selected_language = obj_temp.getString("vTitle");
//                    selected_language_code = obj_temp.getString("vCode");
//                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        CharSequence[] cs_languages_txt = items_txt_language.toArray(new CharSequence[items_txt_language.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("" + LBL_SELECT_TXT_str);

        builder.setItems(cs_languages_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (alert_show_language_list != null) {
                    alert_show_language_list.dismiss();
                }
                selected_language = items_txt_language.get(item);
                selected_language_code = items_language_code.get(item);
                select_language_box.setText("" + selected_language);
                chooseLanguage = true;
            }
        });

        alert_show_language_list = builder.create();
    }

    class check_registerationInfo_user extends AsyncTask<String, String, String> {

        String email_str = "";
        boolean registered = false;
        ProgressDialog pDialog;
        String first_name_str = "";
        String mobile_str = "";
        String lastName_str = "";
        String password_str = "";
        String GCMregistrationId = "";
        String country_code_str = "";
        String inviteCode_str = "";

        String responseString = "";

        public check_registerationInfo_user(String first_name_str, String country_code_str, String mobile_str, String lastName_str, String email_str, String password_str, String inviteCode_str) {
            // TODO Auto-generated constructor stub
            this.first_name_str = first_name_str;
            this.country_code_str = country_code_str;
            this.mobile_str = mobile_str;
            this.lastName_str = lastName_str;
            this.email_str = email_str;
            this.password_str = password_str;
            this.inviteCode_str = inviteCode_str;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(UserSignUpActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage("" + LBL_LOADING_VERIFICATION_TXT_str);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        public void processGCMID_user() throws IOException {
            InstanceID instanceID = InstanceID.getInstance(UserSignUpActivity.this);
            GCMregistrationId = instanceID.getToken(CommonUtilities.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null);

        }

        public void check_registration() {

            // checkRegisteredUser(email_str);

            // String baseUrl = CommonUtilities.SERVER_URL_GET_DETAIL;
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&Email=%s&Phone=%s&InviteCode=%s";

            if (URLEncoder.encode(email_str) != null) {

                String registerUrl = baseUrl + String.format(registerParam, "checkUser_passenger",
                        URLEncoder.encode(email_str), URLEncoder.encode(mobile_str), URLEncoder.encode(inviteCode_str));

                Log.d("", "check_registration: registerUrl = " + registerUrl);

                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(registerUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    responseString = OutputStreamReader.readStream(in);

                    Log.d("Responce ", "check_registration: " + responseString);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }

                }

            }
//            if (responseString != null && responseString.trim().equals("NO_REG_FOUND")) {
//                registered = false;
//                try {
//
//                    if (checkPlayServices() == true) {
//                        processGCMID_user();
//                    }
//
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            } else {
//                registered = true;
//            }

            try {

                if (checkPlayServices() == true) {
                    processGCMID_user();
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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

                    Log.d("", "onPostExecute: message_str = " + message_str);

                    if (message_str.equals("NO_REG_FOUND")) {
                        String inviteCode_str = (new JSONObject(responseString)).getString("vInviteCode");

                        if (this.inviteCode_str.trim().equals("") || inviteCode_str.trim().equals("1")) {
                            if (GCMregistrationId != null && !GCMregistrationId.equals("")) {
                                checkLanguageCode(registered, first_name_str, country_code_str, mobile_str, lastName_str, email_str,
                                        password_str, GCMregistrationId, selected_language_code);

                            } else {
                                Toast.makeText(UserSignUpActivity.this, "" + LBL_SERVICE_NOT_AVAIL_TXT_str, Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            inviteCodeEditBox.setError(LBL_INVALID_INVITE_CODE_TXT_str);
                        }


                    } else if (message_str.trim().equals("EMAIL_EXIST")) {

                        Toast.makeText(UserSignUpActivity.this, "" + LBL_ALREADY_REGISTERED_TXT_str, Toast.LENGTH_LONG)
                                .show();
                    } else if (message_str.trim().equals("MOBILE_EXIST")) {
                        Toast.makeText(UserSignUpActivity.this, "Mobile Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserSignUpActivity.this, "" + LBL_ERROR_OCCURED_TXT_str, Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UserSignUpActivity.this, "" + LBL_ERROR_OCCURED_TXT_str, Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(UserSignUpActivity.this, "" + LBL_ERROR_OCCURED_TXT_str, Toast.LENGTH_LONG).show();
            }

//            if (responseString != null && responseString.trim().equals("NO_REG_FOUND") && registered == false) {
//                if (GCMregistrationId != null && !GCMregistrationId.equals("")) {
//                    checkLanguageCode(registered, first_name_str, country_code_str, mobile_str, lastName_str, email_str,
//                            password_str, GCMregistrationId, selected_language_code);
//
//                } else {
//                    Toast.makeText(UserSignUpActivity.this, "" + LBL_SERVICE_NOT_AVAIL_TXT_str, Toast.LENGTH_LONG)
//                            .show();
//                }
//            } else {
//
//                if (responseString.trim().equals("EMAIL_EXIST")) {
//                    Toast.makeText(UserSignUpActivity.this, "" + LBL_ALREADY_REGISTERED_TXT_str, Toast.LENGTH_LONG)
//                            .show();
//                } else {
//                    Toast.makeText(UserSignUpActivity.this, "" + LBL_MOBILE_EXIST_str, Toast.LENGTH_LONG).show();
//                }
//
//            }

        }
    }

    public void checkLanguageCode(boolean registered, String first_name_str, String country_code_str, String mobile_str,
                                  String lastName_str, String email_str, String password_str, String GCMregistrationId,
                                  String selected_language_code) {

        if (Default_language_code.equals("" + selected_language_code)) {

            if (MOBILE_VERIFICATION_ENABLE_str.equals("Yes")) {
                verify_info(registered, first_name_str, country_code_str, mobile_str, lastName_str, email_str,
                        password_str, GCMregistrationId, selected_language_code);
            } else {
                finalRegisterNewUser();
            }

        } else {
            String language_default_url = CommonUtilities.SERVER_URL_LABELS_DEFAULT + "&vCode="
                    + selected_language_code;

            setData(registered, first_name_str, country_code_str, mobile_str, lastName_str, email_str, password_str,
                    GCMregistrationId, selected_language_code);

            dialog_change_LanguageCode = new ProgressDialog(UserSignUpActivity.this, R.style.DialogTheme_custom);
            dialog_change_LanguageCode.setMessage("" + LBL_INFO_SET_TXT_str);
            dialog_change_LanguageCode.setCancelable(false);
            dialog_change_LanguageCode.setCanceledOnTouchOutside(false);
            dialog_change_LanguageCode.show();

            ProcessLanguage_labels processStart_language_labels = new ProcessLanguage_labels(false,
                    language_default_url);

            processStart_language_labels.setUpdateLanguageListener(this);

            processStart_language_labels.execute();
        }

    }

    public void setData(boolean registered, String first_name_str, String country_code_str, String mobile_str,
                        String lastName_str, String email_str, String password_str, String GCMregistrationId,
                        String selected_language_code) {

        this.registered_retrive = registered;
        this.first_name_str_retrive = first_name_str;
        this.mobile_str_retrive = mobile_str;
        this.lastName_str_retrive = lastName_str;
        this.email_str_retrive = email_str;
        this.password_str_retrive = password_str;
        this.GCMregistrationId_retrive = GCMregistrationId;
        this.selected_language_code_retrive = selected_language_code;
        this.country_code_str_retrive = country_code_str;
    }

    public boolean isPasswordCorrect(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&_])(?=\\S+$).{5,10}";
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

    public void finalRegisterNewUser() {
        String mobile_str_get = mobile_num.getText().toString();
        String country_code_str = countryCode_txt.getText().toString();

        String first_name_str = First_Name.getText().toString();
        String lastName_str = Last_Name.getText().toString();
        String password_str = password.getText().toString();
        String email_str = email_address.getText().toString();

        final_Fname = "" + first_name_str;
        final_id = "0";
        final_Lname = "" + lastName_str;
        final_email = "" + email_str;
        final_Mobile = "" + mobile_str_get;
        final_Password = "" + password_str;
        final_GCMID = "" + GCMregistrationId_str;
        final_LanguageCode = "" + selected_language_code;
        final_phoneCode = "" + country_code_str;

        new Register_NewUser().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VerifyMobileActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            String message = data.getStringExtra("message");
            int result = data.getIntExtra("result", 0);

            if (message.contains("SUCCESS")) {

                finalRegisterNewUser();

            } else if (message.contains("FAILED")) {

                Toast.makeText(getApplicationContext(), "" + LBL_MOBILE_VERIFICATION_FAILED_TXT_str,
                        Toast.LENGTH_SHORT).show();

            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {

        String Query = "SELECT * from `labels` WHERE vLabel= \"" + fieldValue + "\"";
        Cursor cursor = dbConnect.execQuery(Query);

        if (cursor != null) {

            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        }
        return false;
    }

    public class Register_NewUser extends AsyncTask<String, String, String> {

        String responseString = "";
        String cityName = "";

        ProgressDialog pDialog;

        String reg_gcmId = "";

        String configuration_fare_json = "";

        String gender_str = "";
        boolean errorInConnection = false;
        String inviteCode_str = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            if (genderSelection.getCheckedRadioButtonId() == R.id.maleRadioBtn) {
                gender_str = "Male";
            } else {
                gender_str = "Female";
            }

            inviteCode_str = inviteCodeEditBox.getText().toString().trim();

            pDialog = new ProgressDialog(UserSignUpActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage("" + LBL_REGISTER_LOADING_TXT_str);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

        }

        public String getCity(double lat, double lon) {
            Geocoder geocoder = new Geocoder(UserSignUpActivity.this, Locale.getDefault());
            List<Address> addresses;
            String cityName = "";
            try {
                addresses = geocoder.getFromLocation(lat, lon, 1);

                if (addresses.size() > 0) {
                    cityName = addresses.get(0).getLocality();
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                cityName = "";
            }
            return cityName;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            cityName = getCity(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&fbid=%s&Fname=%s&Lname=%s&email=%s&phone=%s"
                    + "&password=%s&Gcm_id=%s&Language_Code=%s&cityName=%s&PhoneCode=%s&CountryCode=%s&Gender=%s&InviteCode=%s";

            String registerUrl = baseUrl + String.format(registerParam, "signup", final_id,
                    "" + URLEncoder.encode(final_Fname), "" + URLEncoder.encode(final_Lname), "" + final_email,
                    final_Mobile, "" + final_Password, "" + final_GCMID, URLEncoder.encode(final_LanguageCode),
                    URLEncoder.encode("" + cityName), URLEncoder.encode("" + final_phoneCode), URLEncoder.encode("" + countryCode_str), "" + gender_str, "" + inviteCode_str);

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
                && !json_server_registrationStatus.trim().equals("Failed")) {

            JSONObject json_first_id_user_auto = null;
            String user_auto_id = "";
            String vImgName_str = "";
            try {

                json_first_id_user_auto = new JSONObject(json_server_registrationStatus);
                user_auto_id = json_first_id_user_auto.getString(CommonUtilities.iUserId);
                vImgName_str = json_first_id_user_auto.getString("vImgName");

            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            // Bitmap profile_image =
            // BitmapFactory.decodeResource(UserSignUpActivity.this.getResources(),
            // R.drawable.friends_main);

            // ByteArrayOutputStream stream = new ByteArrayOutputStream();

            // Bitmap profile_image;

            // profile_image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            // byte[] byteArray = stream.toByteArray();

//            Intent send_data = new Intent(UserSignUpActivity.this, Mainprofile_Activity.class);
//
//            Bundle bn_send_data = new Bundle();
//            bn_send_data.putString("ID", "0");
//            bn_send_data.putString("Name", "" + final_Fname + " " + final_Lname);
//            bn_send_data.putString("Email", final_email);
//            bn_send_data.putString("Gender", "");
//            bn_send_data.putString("user_id", user_auto_id);
//            // bn_send_data.putByteArray("fProfile_image_bitmap", byteArray);
//            bn_send_data.putString("fProfile_image_bitmap", vImgName_str);
//
//            SharedPreferences.Editor editor = mPrefs.edit();
//            editor.putString("access_sign_token_email", final_email);
//            editor.putString("access_sign_token_pass", final_Password);
//
//            editor.putString("selected_language_code", final_LanguageCode);
//            editor.putString("access_sign_token_user_id_auto", user_auto_id);
//            editor.commit();
//
//            bn_send_data.putString("json_responseString", json_server_registrationStatus);
//            bn_send_data.putString("vTripStatus", "NO");
//
//            send_data.putExtras(bn_send_data);
//
//            // send_data.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//            // IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(send_data);
//            ActivityCompat.finishAffinity(this);

            new OpenMainProfile(UserSignUpActivity.this, json_server_registrationStatus).run();

        } else {

            if (errorInConnection == false && json_server_registrationStatus.trim().equals("Failed")) {
                Toast.makeText(UserSignUpActivity.this, "" + LBL_ALREADY_REGISTERED_TXT_str, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(UserSignUpActivity.this, "" + LBL_REGISTERATION_FAILED_TXT_str, Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    public boolean checkPlayServices() {
        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        final int result = googleAPI.isGooglePlayServicesAvailable(UserSignUpActivity.this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {

                UserSignUpActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        googleAPI.getErrorDialog(UserSignUpActivity.this, result,
                                CommonUtilities.PLAY_SERVICES_RESOLUTION_REQUEST).show();
                    }
                });

            }

            return false;
        }

        return true;
    }

    @Override
    public void handleUpdateLangCallback(String Labels_lang) {
        // TODO Auto-generated method stub

        if (dialog_change_LanguageCode != null) {
            dialog_change_LanguageCode.dismiss();
        }
        if (Labels_lang != null && Labels_lang.toString().length() > 6) {

            if (CheckIsDataAlreadyInDBorNot("labels", "vLabel", "Language_labels") == false) {
                String sql = "INSERT INTO labels(vLabel,vValue) values('%s','%s')";
                sql = String.format(sql, "Language_labels", Labels_lang.replace("'", "''").toString());
                dbConnect.execNonQuery(sql);
            } else {
                String sql = "UPDATE `labels` SET vValue = '" + Labels_lang.replace("'", "''").toString() + "'"
                        + " WHERE vLabel = \"Language_labels\"";
                dbConnect.execNonQuery(sql);
            }

            if (MOBILE_VERIFICATION_ENABLE_str.equals("Yes")) {
                verify_info(registered_retrive, first_name_str_retrive, country_code_str_retrive, mobile_str_retrive,
                        lastName_str_retrive, email_str_retrive, password_str_retrive, GCMregistrationId_retrive,
                        selected_language_code_retrive);
            } else {
                finalRegisterNewUser();
            }


        }

    }

    @Override
    public void handleErrorOnLangCallback(boolean error) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleDefaultLangCallback(String languageLabels, String languageList, String countryList) {
        // TODO Auto-generated method stub

    }
}

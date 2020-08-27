package com.hwindiapp.passenger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fonts.Text.MyTextView;
import com.mainProfile.classFiles.CountrySelection_dialog;
import com.mainProfile.classFiles.CountrySelection_dialog.CountrySelectionListner;
import com.mainProfile.classFiles.DownloadProfileImg;
import com.mainProfile.classFiles.OutputStreamReader;
import com.materialedittext.MaterialEditText;
import com.utils.CommonUtilities;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
import java.util.Calendar;

public class UpdateUsr_ProfileActivity extends AppCompatActivity {

    TextView text_header;

    String string_response_detail;

    String id = "";
    String user_id_auto = "";
    String Fname = "";
    String Lname = "";
    String email = "";
    String gender = "";
    String mobile = "";
    String phoneCode_str = "";
    String BirthDate = "";

    String vImgName_str = "";
    String id_facebook = "";
    String currencyCode_str = "";
    String profileJson_str = "";

    MyTextView birthDateHTxt;
    MyTextView birthDateTxt;
    MyTextView birthDateErrorTxt;
    MyTextView currencyHTxt;
    MyTextView genderHTxt;

    MyTextView update_currency_box;

    MaterialEditText edit_userPhone;
    MaterialEditText first_name_text;
    MaterialEditText last_name_text;

    private SharedPreferences mPrefs;

    MaterialEditText countryCode_txt;

    LinearLayout Name_area;
    RippleStyleButton update_btn;

    RoundedImageView user_image_area;

    RadioButton maleRadioBtn;
    RadioButton femaleRadioBtn;
    RadioGroup genderSelection;

    ArrayList<String> items_currency_code = new ArrayList<String>();
    String selected_Currency_code = "";

    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";

    String LBL_CHANGE_PASSWORD_TXT_str = "";
    String LBL_PROFILE_UPDATE_TITLE_TXT_str = "";
    String LBL_USER_NAME_HEADER_TXT_str = "";
    String LBL_FEILD_REQUIRD_ERROR_TXT_str = "";
    String LBL_UPDATE_PASSWORD_HEADER_TXT_str = "";
    String LBL_UPDATE_PASSWORD_HINT_TXT_str = "";
    String LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str = "";
    String LBL_UPDATE_CONFIRM_PASSWORD_HINT_TXT_str = "";
    String LBL_MOBILE_NUMBER_HEADER_TXT_str = "";
    String LBL_UPDATE_USER_PHONE_HINT_TXT_str = "";
    String LBL_CARD_NUMBER_HEADER_TXT_str = "";
    String LBL_CARD_NUMBER_HINT_TXT_str = "";
    String LBL_EXP_DATE_HEADER_TXT_str = "";
    String LBL_EXP_MONTH_HINT_TXT_str = "";
    String LBL_EXP_YEAR_HINT_TXT_str = "";
    String LBL_CVV_HEADER_TXT_str = "";
    String LBL_CVV_HINT_TXT_str = "";
    String LBL_BTN_PROFILE_UPDATE_PAGE_TXT_str = "";
    String LBL_BTN_UPDATE_PASSWORD_TXT_str = "";
    String LBL_BTN_UPDATE_PAYMENT_INFO_TXT_str = "";
    String LBL_ERROR_CARD_NUMBER_TXT_str = "";
    String LBL_NO_CHANGE_TXT_str = "";
    String LBL_FEILD_PASSWORD_ERROR_TXT_str = "";
    String LBL_PAYMENT_CARD_ENTER_TXT_str = "";
    String LBL_VERIFY_PASSWORD_ERROR_TXT_str = "";
    String LBL_PASS_UPDATE_LOADING_TXT_str = "";
    String LBL_UPDATE_PROFILE_INFO_LOADING_TXT_str = "";
    String LBL_UPDATE_PAYMENT_INFO_LOADING_TXT_str = "";
    String LBL_FIRST_NAME_HEADER_TXT_str = "";
    String LBL_LAST_NAME_HEADER_TXT_str = "";
    String LBL_FIRST_NAME_HINT_TXT_str = "";
    String LBL_LAST_NAME_HINT_TXT_str = "";
    String LBL_UPDATE_INFO_FAILED_TXT_str = "";
    String LBL_COUNTRY_TXT_str = "";
    String LBL_MOBILE_VERIFICATION_FAILED_TXT_str = "";
    String LBL_MOBILE_NUMBER_HINT_TXT_str = "";
    String LBL_BIRTH_DATE_TXT_str = "";
    String LBL_SELECT_TXT_str = "";
    String LBL_CURRENCY_TXT_str = "";
    String LBL_BTN_OK_TXT_str = "";
    String LBL_INFO_UPDATED_TXT_str = "";
    String LBL_MALE_TXT_str = "";
    String LBL_FEMALE_TXT_str = "";
    String LBL_GENDER_TXT_str = "";

    CountrySelection_dialog selectCountryDialog;
    boolean firstClickOnCountryTXT = true;
    String countryListJSON = "";

    private Toolbar mToolbar;

    String MOBILE_VERIFICATION_ENABLE_str = "";

    boolean dateSelect = false;

    AlertDialog alert_show_currency_list;
    androidx.appcompat.app.AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        MOBILE_VERIFICATION_ENABLE_str = CommonUtilities.MOBILENUM_VERIFICATION_ENABLE;

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        dbConnect = new DBConnect(this, "UC_labels.db");

        string_response_detail = "no update";
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("ID");
        user_id_auto = bundle.getString("user_id");
        Fname = bundle.getString("FName");
        Lname = bundle.getString("LName");
        email = bundle.getString("Email");
        gender = bundle.getString("Gender");
        mobile = bundle.getString("phone");
        phoneCode_str = bundle.getString("phoneCode");
        currencyCode_str = bundle.getString("CurrencyCode");
        profileJson_str = bundle.getString("ProfileJSON");
        BirthDate = bundle.getString("BirthDate");

        vImgName_str = bundle.getString("fProfile_image_bitmap");
        id_facebook = bundle.getString("FBID");

        text_header = (TextView) findViewById(R.id.text_header);

        Name_area = (LinearLayout) findViewById(R.id.Name_area);

        first_name_text = (MaterialEditText) findViewById(R.id.firstNameEditBox);
        last_name_text = (MaterialEditText) findViewById(R.id.lastNameEditBox);

        countryCode_txt = (MaterialEditText) findViewById(R.id.countryCode_txt);

        edit_userPhone = (MaterialEditText) findViewById(R.id.edit_userPhone);
        birthDateHTxt = (MyTextView) findViewById(R.id.birthDateHTxt);
        birthDateTxt = (MyTextView) findViewById(R.id.birthDateTxt);
        birthDateErrorTxt = (MyTextView) findViewById(R.id.birthDateErrorTxt);
        currencyHTxt = (MyTextView) findViewById(R.id.currencyHTxt);
        update_currency_box = (MyTextView) findViewById(R.id.update_currency_box);
        genderHTxt = (MyTextView) findViewById(R.id.genderHTxt);

        maleRadioBtn = (RadioButton) findViewById(R.id.maleRadioBtn);
        femaleRadioBtn = (RadioButton) findViewById(R.id.femaleRadioBtn);
        genderSelection = (RadioGroup) findViewById(R.id.genderSelection);

        update_btn = (RippleStyleButton) findViewById(R.id.update_btn);
        user_image_area = (RoundedImageView) findViewById(R.id.img_user);

		/* Set Labels */
        getLanguageLabelsFrmSqLite();
        /* Set Labels Finished */

        checkProfileImage();

        first_name_text.setText("" + Fname);
        last_name_text.setText("" + Lname);
        edit_userPhone.setText(mobile);
        countryCode_txt.setText(phoneCode_str);

        selected_Currency_code = currencyCode_str;
        update_currency_box.setText(currencyCode_str);

        if (BirthDate != null && !BirthDate.equals("") && !BirthDate.equals("0000-00-00")) {
            birthDateTxt.setText(BirthDate);
            dateSelect = true;
            birthDateTxt.setClickable(false);
        }

        if (gender.equals("Male")) {
            maleRadioBtn.setChecked(true);
        } else {
            femaleRadioBtn.setChecked(true);
        }

        countryCode_txt.setCursorVisible(false);
        countryCode_txt.setLongClickable(true);
        countryCode_txt.setFocusable(false);

        countryCode_txt.setClickable(true);
        countryCode_txt.setSelected(false);
        countryCode_txt.setKeyListener(null);

        first_name_text.addTextChangedListener(new setOnTxtChangeList(first_name_text));
        last_name_text.addTextChangedListener(new setOnTxtChangeList(first_name_text));
        edit_userPhone.addTextChangedListener(new setOnTxtChangeList(first_name_text));

        ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
        back_navigation.setOnClickListener(new setOnClickAct());

        update_btn.setOnClickListener(new setOnClickAct());

        countryCode_txt.setOnClickListener(new setOnClickAct());

        back_navigation.setOnClickListener(new setOnClickAct());
        update_btn.setOnClickListener(new setOnClickAct());
        birthDateTxt.setOnClickListener(new setOnClickAct());
        update_currency_box.setOnClickListener(new setOnClickAct());
        countryCode_txt.setOnClickListener(new setOnClickAct());

        selectCountryDialog = new CountrySelection_dialog(this, firstClickOnCountryTXT, countryListJSON);
        selectCountryDialog.setCountryListener(new CountrySelectionListner() {

            @Override
            public void handleSelectedCountryCallback(String phoneCode, String countryCode) {
                // TODO Auto-generated method stub
                countryCode_txt.setText("" + phoneCode);

                countryCode_txt.setError(null);
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

        try {
            JSONArray arr_country = (new JSONObject(profileJson_str)).getJSONArray("CurrencyList");
            setCurrency_list(arr_country);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void checkProfileImage() {
        String access_token_main = mPrefs.getString("access_token", null);
        if (access_token_main != null && id_facebook != null && !id_facebook.trim().equals("")
                && !id_facebook.trim().equals("0")) {

            String imageUrl = "http://graph.facebook.com/" + id_facebook + "/picture?type=large&width=200&height=200";

            new DownloadProfileImg(UpdateUsr_ProfileActivity.this, user_image_area, imageUrl, "" + id_facebook + ".jpg")
                    .execute();

        } else {
            if (vImgName_str == null || vImgName_str.equals("") || vImgName_str.equals("NONE")) {
                user_image_area.setImageResource(R.drawable.friends_main);
            } else {
                new DownloadProfileImg(UpdateUsr_ProfileActivity.this, user_image_area,
                        CommonUtilities.SERVER_URL_PHOTOS + "upload/Passenger/" + user_id_auto + "/" + vImgName_str,
                        vImgName_str).execute();
            }
        }

    }

    public void getLanguageLabelsFrmSqLite() {

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

        cursor.moveToPosition(0);

        language_labels_get_frm_sqLite = cursor.getString(0);

        JSONObject obj_language_labels = null;

        try {
            obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
            LBL_CHANGE_PASSWORD_TXT_str = obj_language_labels.getString("LBL_CHANGE_PASSWORD_TXT");
            LBL_PROFILE_UPDATE_TITLE_TXT_str = obj_language_labels.getString("LBL_PROFILE_UPDATE_TITLE_TXT");
            LBL_USER_NAME_HEADER_TXT_str = obj_language_labels.getString("LBL_USER_NAME_HEADER_TXT");
            LBL_FEILD_REQUIRD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_REQUIRD_ERROR_TXT");
            LBL_UPDATE_PASSWORD_HEADER_TXT_str = obj_language_labels.getString("LBL_UPDATE_PASSWORD_HEADER_TXT");
            LBL_UPDATE_PASSWORD_HINT_TXT_str = obj_language_labels.getString("LBL_UPDATE_PASSWORD_HINT_TXT");
            LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str = obj_language_labels.getString("LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT");
            LBL_UPDATE_CONFIRM_PASSWORD_HINT_TXT_str = obj_language_labels.getString("LBL_UPDATE_CONFIRM_PASSWORD_HINT_TXT");
            LBL_MOBILE_NUMBER_HEADER_TXT_str = obj_language_labels.getString("LBL_MOBILE_NUMBER_HEADER_TXT");
            LBL_UPDATE_USER_PHONE_HINT_TXT_str = obj_language_labels.getString("LBL_UPDATE_USER_PHONE_HINT_TXT");
            LBL_CARD_NUMBER_HEADER_TXT_str = obj_language_labels.getString("LBL_CARD_NUMBER_HEADER_TXT");
            LBL_CARD_NUMBER_HINT_TXT_str = obj_language_labels.getString("LBL_CARD_NUMBER_HINT_TXT");
            LBL_EXP_DATE_HEADER_TXT_str = obj_language_labels.getString("LBL_EXP_DATE_HEADER_TXT");
            LBL_EXP_MONTH_HINT_TXT_str = obj_language_labels.getString("LBL_EXP_MONTH_HINT_TXT");
            LBL_EXP_YEAR_HINT_TXT_str = obj_language_labels.getString("LBL_EXP_YEAR_HINT_TXT");
            LBL_CVV_HEADER_TXT_str = obj_language_labels.getString("LBL_CVV_HEADER_TXT");
            LBL_CVV_HINT_TXT_str = obj_language_labels.getString("LBL_CVV_HINT_TXT");
            LBL_BTN_PROFILE_UPDATE_PAGE_TXT_str = obj_language_labels.getString("LBL_BTN_PROFILE_UPDATE_PAGE_TXT");
            LBL_BTN_UPDATE_PASSWORD_TXT_str = obj_language_labels.getString("LBL_BTN_UPDATE_PASSWORD_TXT");
            LBL_BTN_UPDATE_PAYMENT_INFO_TXT_str = obj_language_labels.getString("LBL_BTN_UPDATE_PAYMENT_INFO_TXT");
            LBL_ERROR_CARD_NUMBER_TXT_str = obj_language_labels.getString("LBL_ERROR_CARD_NUMBER_TXT");
            LBL_NO_CHANGE_TXT_str = obj_language_labels.getString("LBL_NO_CHANGE_TXT");
            LBL_FEILD_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_PASSWORD_ERROR_TXT");
            LBL_PAYMENT_CARD_ENTER_TXT_str = obj_language_labels.getString("LBL_PAYMENT_CARD_ENTER_TXT");
            LBL_VERIFY_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_VERIFY_PASSWORD_ERROR_TXT");
            LBL_PASS_UPDATE_LOADING_TXT_str = obj_language_labels.getString("LBL_PASS_UPDATE_LOADING_TXT");
            LBL_UPDATE_PROFILE_INFO_LOADING_TXT_str = obj_language_labels.getString("LBL_UPDATE_PROFILE_INFO_LOADING_TXT");
            LBL_UPDATE_PAYMENT_INFO_LOADING_TXT_str = obj_language_labels.getString("LBL_UPDATE_PAYMENT_INFO_LOADING_TXT");
            LBL_FIRST_NAME_HEADER_TXT_str = obj_language_labels.getString("LBL_FIRST_NAME_HEADER_TXT");
            LBL_LAST_NAME_HEADER_TXT_str = obj_language_labels.getString("LBL_LAST_NAME_HEADER_TXT");
            LBL_FIRST_NAME_HINT_TXT_str = obj_language_labels.getString("LBL_FIRST_NAME_HINT_TXT");
            LBL_LAST_NAME_HINT_TXT_str = obj_language_labels.getString("LBL_LAST_NAME_HINT_TXT");
            LBL_UPDATE_INFO_FAILED_TXT_str = obj_language_labels.getString("LBL_UPDATE_INFO_FAILED_TXT");
            LBL_COUNTRY_TXT_str = obj_language_labels.getString("LBL_COUNTRY_TXT");
            LBL_MOBILE_VERIFICATION_FAILED_TXT_str = obj_language_labels.getString("LBL_MOBILE_VERIFICATION_FAILED_TXT");
            LBL_MOBILE_NUMBER_HINT_TXT_str = obj_language_labels.getString("LBL_MOBILE_NUMBER_HINT_TXT");
            LBL_BIRTH_DATE_TXT_str = obj_language_labels.getString("LBL_BIRTH_DATE_TXT");
            LBL_SELECT_TXT_str = obj_language_labels.getString("LBL_SELECT_TXT");
            LBL_CURRENCY_TXT_str = obj_language_labels.getString("LBL_CURRENCY_TXT");
            LBL_BTN_OK_TXT_str = obj_language_labels.getString("LBL_BTN_OK_TXT");
            LBL_INFO_UPDATED_TXT_str = obj_language_labels.getString("LBL_INFO_UPDATED_TXT");
            LBL_MALE_TXT_str = obj_language_labels.getString("LBL_MALE_TXT");
            LBL_FEMALE_TXT_str = obj_language_labels.getString("LBL_FEMALE_TXT");
            LBL_GENDER_TXT_str = obj_language_labels.getString("LBL_GENDER_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj_language_labels != null) {
            text_header.setText("" + LBL_PROFILE_UPDATE_TITLE_TXT_str);
            update_btn.setText("" + LBL_BTN_PROFILE_UPDATE_PAGE_TXT_str);

            first_name_text.setHint("" + LBL_FIRST_NAME_HINT_TXT_str);
            last_name_text.setHint("" + LBL_LAST_NAME_HINT_TXT_str);

            first_name_text.setFloatingLabelText(LBL_FIRST_NAME_HEADER_TXT_str);
            last_name_text.setFloatingLabelText(LBL_LAST_NAME_HEADER_TXT_str);

            edit_userPhone.setHint(LBL_MOBILE_NUMBER_HINT_TXT_str);
            edit_userPhone.setFloatingLabelText("" + LBL_MOBILE_NUMBER_HEADER_TXT_str);

            countryCode_txt.setHint("" + LBL_COUNTRY_TXT_str);
            countryCode_txt.setFloatingLabelText("" + LBL_COUNTRY_TXT_str);

            countryCode_txt.setHint("" + LBL_COUNTRY_TXT_str);

            birthDateHTxt.setText(LBL_BIRTH_DATE_TXT_str);
            birthDateTxt.setText(" -- " + LBL_SELECT_TXT_str + " -- ");
            birthDateErrorTxt.setText(LBL_FEILD_REQUIRD_ERROR_TXT_str);

            currencyHTxt.setText("" + LBL_CURRENCY_TXT_str);

            maleRadioBtn.setText(LBL_MALE_TXT_str);
            femaleRadioBtn.setText(LBL_FEMALE_TXT_str);
            genderHTxt.setText(LBL_GENDER_TXT_str);
        }

//		Cursor cursor_configData = dbConnect
//				.execQuery("select vValue from labels WHERE vLabel=\"Configuration_Links\"");
//
//		try {
//			cursor_configData.moveToPosition(0);
//
//			String json_configData = cursor_configData.getString(0);
//
//			JSONObject configData_obj = new JSONObject(json_configData);
//
//			MOBILE_VERIFICATION_ENABLE_str = configData_obj.getString("MOBILE_VERIFICATION_ENABLE");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

    }

    public void setCurrency_list(JSONArray currencyListarr) {

        for (int i = 0; i < currencyListarr.length(); i++) {

            JSONObject obj_temp = null;
            try {
                obj_temp = currencyListarr.getJSONObject(i);
                items_currency_code.add(obj_temp.getString("vName"));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        CharSequence[] cs_currency_txt = items_currency_code.toArray(new CharSequence[items_currency_code.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LanguagePopUpTheme);
        builder.setTitle("" + LBL_SELECT_TXT_str);

        builder.setItems(cs_currency_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

                if (alert_show_currency_list != null) {
                    alert_show_currency_list.dismiss();
                }
                selected_Currency_code = items_currency_code.get(item);
                update_currency_box.setText(selected_Currency_code);
//                new UpdateCurrency().execute();
            }
        });
        // builder.setItems(cs_languages_txt, null);

        alert_show_currency_list = builder.create();

    }

    public class setOnTxtChangeList implements TextWatcher {

        View view;

        public setOnTxtChangeList(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            switch (view.getId()) {
                case R.id.first_name_text:
                    first_name_text.setError(null);
                    break;
                case R.id.last_name_text:
                    last_name_text.setError(null);
                    break;

                case R.id.edit_userPhone:
                    edit_userPhone.setError(null);
                    break;

            }
        }
    }

    public class setOnClickAct implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back_navigation:
                    UpdateUsr_ProfileActivity.super.onBackPressed();
                    break;

                case R.id.update_btn:
                    update_info_usr();
                    break;

                case R.id.birthDateTxt:
                    if (dateSelect)
                    {
                        Log.d("dateSelect "+dateSelect, "onClick: date already exist");
                    }else
                    setDate(birthDateTxt);
                    break;

                case R.id.countryCode_txt:
                    selectCountryDialog.run();
                    break;

                case R.id.update_currency_box:
                    if (alert_show_currency_list != null) {
                        alert_show_currency_list.show();
                    }
                    break;
            }
        }
    }

    public void setDate(final MyTextView dateTxt) {

        final Calendar now = Calendar.getInstance();

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                        String month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);
                        String day = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
//                        dateTxt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//                        dateTxt.setText(dayOfMonth + "-" + month + "-" + year);
                        dateTxt.setText(year + "-" + month + "-" + day);
                        dateSelect = true;

                        birthDateErrorTxt.setVisibility(View.GONE);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
//        now.set(Calendar.YEAR, 1970);
//        now.set(Calendar.DATE, 1);
//        now.set(Calendar.MONTH, Calendar.JANUARY);
        Calendar setDate = Calendar.getInstance();
        setDate.set(1970, Calendar.JANUARY, 1);
        dpd.setMinDate(setDate);

//        setDate.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
//        dpd.setMaxDate(setDate);
        dpd.setMaxDate(now);
        dpd.show(getFragmentManager(), "Datepickerdialog");
        birthDateTxt.setClickable(false);
    }

    public void update_info_usr() {

        String first_name_update = first_name_text.getText().toString();
        String last_name_update = last_name_text.getText().toString();
        String mobile_update = edit_userPhone.getText().toString();
        String countryCode_txt_update = countryCode_txt.getText().toString();

        boolean errorFName = false;
        boolean errorLName = false;
        boolean errorMobile = false;
        boolean errorCountry = false;

        if (first_name_update.trim().length() < 2) {
            errorFName = true;
            first_name_text.setError(LBL_FEILD_REQUIRD_ERROR_TXT_str);
        }

        if (last_name_update.trim().length() < 2) {
            errorLName = true;
            last_name_text.setError(LBL_FEILD_REQUIRD_ERROR_TXT_str);
        }

        if (mobile_update.trim().length() < 4) {
            errorMobile = true;
            edit_userPhone.setError(LBL_FEILD_REQUIRD_ERROR_TXT_str);
        }

        if (countryCode_txt_update.trim().length() == 0) {
            errorCountry = true;
            countryCode_txt.setError("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
        }

        if (dateSelect == false) {
            birthDateErrorTxt.setVisibility(View.VISIBLE);
        }

        if (errorFName == true || errorLName == true || errorMobile == true || errorCountry == true || dateSelect == false) {
            return;
        }

        if (!countryCode_txt_update.trim().equals(phoneCode_str.trim())
                || !mobile_update.trim().equals(mobile.trim())) {
            requestVerifyMobile(first_name_update, last_name_update, mobile_update, countryCode_txt_update);
        } else {
            new UserDetail(first_name_update, last_name_update, mobile_update, countryCode_txt_update).execute();
        }

    }

    public void requestVerifyMobile(String first_name_update, String last_name_update, String mobile_update,
                                    String phoneCode_update) {

        if (MOBILE_VERIFICATION_ENABLE_str.equals("Yes")) {
//			Intent in = new Intent(UpdateUsr_ProfileActivity.this, VerifyMobile.class);
//
//			in.putExtra("app_id", "" + CommonUtilities.Mobile_num_verification_APP_ID);
//			in.putExtra("access_token", "" + CommonUtilities.Mobile_num_verification_Access_Token);
//			in.putExtra("mobile", phoneCode_update + mobile_update);
//			startActivityForResult(in, VerifyMobile.REQUEST_CODE);

            Intent in = new Intent(UpdateUsr_ProfileActivity.this, VerifyMobileActivity.class);

            in.putExtra("mobile", phoneCode_update + mobile_update);
            startActivityForResult(in, VerifyMobileActivity.REQUEST_CODE);
        } else {
            verifiedMobileFinished();
        }


    }


    public class UserDetail extends AsyncTask<String, String, String> {
        String responseString = "";
        ProgressDialog pDialog;

        String firstName_update_str = "";
        String lastName_update_str = "";
        String mobile_update_str = "";
        String countryCode_txt_update = "";
        String gender_str = "";
        String birthDate_str = "";
        String currencyCode_str = "";

        public UserDetail(String firstName_update_str, String lastName_update_str, String mobile_update_str,
                          String countryCode_txt_update) {
            // TODO Auto-generated constructor stub
            this.firstName_update_str = firstName_update_str;
            this.lastName_update_str = lastName_update_str;
            this.mobile_update_str = mobile_update_str;
            this.countryCode_txt_update = countryCode_txt_update;
        }

        public String getRegisteredUserDetail() {

            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&Fname=%s&Lname=%s&mobile=%s&user_id=%s&phoneCode=%s&Gender=%s&BirthDate=%s&CurrencyCode=%s";

            String registerUrl = baseUrl + String.format(registerParam, "updateProfileDetailPassenger",
                    URLEncoder.encode(firstName_update_str), URLEncoder.encode(lastName_update_str), URLEncoder.encode(mobile_update_str),
                    URLEncoder.encode(user_id_auto), "" + URLEncoder.encode(countryCode_txt_update), "" + URLEncoder.encode(gender_str),
                    "" + URLEncoder.encode(birthDate_str), "" + URLEncoder.encode(currencyCode_str));

//            Log.d("registerUrl",":"+registerUrl);
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

            return responseString;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            if (genderSelection.getCheckedRadioButtonId() == R.id.maleRadioBtn) {
                gender_str = "Male";
            } else {
                gender_str = "Female";
            }
            birthDate_str = birthDateTxt.getText().toString();
            currencyCode_str = selected_Currency_code;

            pDialog = new ProgressDialog(UpdateUsr_ProfileActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage(Html.fromHtml("" + LBL_UPDATE_PROFILE_INFO_LOADING_TXT_str));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            string_response_detail = getRegisteredUserDetail();

            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

            if (string_response_detail != null && !string_response_detail.equals("")
                    && !string_response_detail.equals("Failed.")) {

                UserProfileActivity.updated_json_responseString_profile = string_response_detail;

                Mainprofile_Activity.updated_json_responseString_profile = string_response_detail;

                Mainprofile_Activity.updateProfileTrue = true;

                UserProfileActivity.updateTrue = true;

                showMessage("", LBL_INFO_UPDATED_TXT_str);
//                UpdateUsr_ProfileActivity.super.onBackPressed();
            } else {

                Toast.makeText(UpdateUsr_ProfileActivity.this, "" + LBL_UPDATE_INFO_FAILED_TXT_str, Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    public void verifiedMobileFinished() {
        String first_name_update = first_name_text.getText().toString();
        String last_name_update = last_name_text.getText().toString();
        String mobile_update = edit_userPhone.getText().toString();
        String countryCode_txt_update = countryCode_txt.getText().toString();

        new UserDetail(first_name_update, last_name_update, mobile_update, countryCode_txt_update).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VerifyMobileActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            String message = data.getStringExtra("message");
            int result = data.getIntExtra("result", 0);

            if (message.contains("SUCCESS")) {

                verifiedMobileFinished();

            } else if (message.contains("FAILED")) {

                Toast.makeText(getApplicationContext(), "" + LBL_MOBILE_VERIFICATION_FAILED_TXT_str, Toast.LENGTH_SHORT)
                        .show();

            }

        }
    }

    public void showMessage(String title_str, String content_str) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                UpdateUsr_ProfileActivity.this);

        alertDialogBuilder.setTitle(title_str);
        alertDialogBuilder
                .setMessage(content_str)
                .setCancelable(true)
                .setPositiveButton(LBL_BTN_OK_TXT_str, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.dismiss();

                        UpdateUsr_ProfileActivity.super.onBackPressed();
                    }
                });
        alertDialog = alertDialogBuilder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

}
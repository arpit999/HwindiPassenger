package com.hwindiapp.passenger;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fonts.Text.MyTextView;
import com.mainProfile.classFiles.Ask_password_dialog;
import com.mainProfile.classFiles.DownloadProfileImg;
import com.mainProfile.classFiles.OutputStreamReader;
import com.utils.CommonUtilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfileActivity extends AppCompatActivity {

    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    private Uri fileUri;
    RoundedImageView user_image_area;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final int SELECT_PICTURE = 2;

    String json_responseString_profile;
    // boolean skip_payment = false;

    public static String updated_json_responseString_profile = "";

    String vImgName_str = "";
    String id_facebook = "";
    String name_user;
    String email_user;
    String user_id_auto;
    String id;
    String first_name;
    String last_name;
    String email;
    String gender;
    String phone;
    String vPhoneCode_str = "";

    String currentPass_User_passenger = "";
    String password;
    Bitmap bmp;

    RippleStyleButton btn_rippel_update_info_btn;
    String final_cred_num_shown;

    public static boolean updateTrue = false;

    // MyTextView user_name_text;

    // ActionBar actionbar;
    TextView text_header;

    // MyTextView userName_header;
    MyTextView currencyHTxt;
    MyTextView languageHTxt;
    MyTextView Email_header;
    MyTextView Mobile_header;
    MyTextView places_header;
    MyTextView referralCode_header;
    MyTextView homePlace;
    MyTextView workPlace;

    MyTextView firstName_header;
    MyTextView lastName_header;

    MyTextView first_name_text;
    MyTextView last_name_text;

    MyTextView update_language_box;
    MyTextView update_currency_box;
    MyTextView referralPoints_header;
    MyTextView referralCodeTXT;

    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";

    String LBL_PROFILE_TITLE_TXT_str = "";
    String LBL_USER_NAME_LBL_TXT_str = "";
    String LBL_EMAIL_LBL_TXT_str = "";
    String LBL_MOBILE_NUMBER_HEADER_TXT_str = "";
    String LBL_PAYMENT_INFO_HEADER_TXT_str = "";
    String LBL_PLACES_HEADER_TXT_str = "";
    String LBL_ADD_HOME_PLACE_TXT_str = "";
    String LBL_ADD_WORK_PLACE_TXT_str = "";
    String LBL_BTN_UPDATE_PROFILE_TXT_str = "";
    String LBL_BTN_UPDATE_PAYMENT_TXT_str = "";
    String LBL_NOT_SUPPORT_CAMERA_TXT_str = "";
    String LBL_FAILED_CAPTURE_IMAGE_TXT_str = "";
    String LBL_SELECT_LANGUAGE_HINT_TXT_str = "";
    String LBL_SELECTED_LANGUAGE_TXT_str = "";
    String LBL_FAILED_UPDATE_LANGUAGE_TXT_str = "";
    String LBL_UPDATING_LANGUAGE_LOADING_TXT_str = "";
    String LBL_SELECT_TXT_str = "";
    String LBL_ADD_PAYMENT_DETAIL_TXT_str = "";
    String LBL_WAIT_UPLOAD_IMAGE_LOADING_TXT_str = "";
    String LBL_SOME_ERROR_TXT_str = "";
    String LBL_FIRST_NAME_HEADER_TXT_str = "";
    String LBL_LAST_NAME_HEADER_TXT_str = "";
    String LBL_COUNTRY_TXT_str = "";
    String LBL_PASS_UPDATE_LOADING_TXT_str = "";
    String LBL_UPDATE_INFO_FAILED_TXT_str = "";
    String LBL_PASS_UPDATE_SUCCESS_str = "";
    String LBL_CURRENCY_TXT_str = "";
    String LBL_FAILED_TXT_str = "";
    String LBL_LOADING_TXT_str = "";
    String LBL_LANGUAGE_TXT_str = "";
    String LBL_REFERRAL_CODE_TXT_str = "";
    String LBL_REFERRAL_POINTS_H_TXT_str = "";
    String LBL_REFER_TO_EARN_POINTS_TXT_str = "";

    AlertDialog alert_show_language_list;
    AlertDialog alert_show_currency_list;

    ArrayList<String> items_txt_language = new ArrayList<String>();
    ArrayList<String> items_language_code = new ArrayList<String>();
    ArrayList<String> items_language_id = new ArrayList<String>();

    ArrayList<String> items_currency_code = new ArrayList<String>();

    String selected_language = "";
    String selected_language_code = "";

    String selected_Currency_code = "";
    String birthDate;

    SharedPreferences mPrefs;

    String str_home_address = "";
    String str_work_address = "";

    String home_location_latitude = "0.0";
    String home_location_longitude = "0.0";

    String work_location_latitude = "0.0";
    String work_location_longitude = "0.0";

    MyTextView countryCode_txt;

    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);


        text_header = (TextView) findViewById(R.id.text_header);

        Bundle bundle = getIntent().getExtras();
        name_user = bundle.getString("Name");
        email_user = bundle.getString("Email");
        json_responseString_profile = bundle.getString("json_responseString_profile");
        vImgName_str = bundle.getString("fProfile_image_bitmap");
        id_facebook = bundle.getString("FBID");

        dbConnect = new DBConnect(this, "UC_labels.db");

        updated_json_responseString_profile = "";
        updateTrue = false;

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // userName_header = (MyTextView) findViewById(R.id.userName_header);

        currencyHTxt = (MyTextView) findViewById(R.id.currencyHTxt);
        languageHTxt = (MyTextView) findViewById(R.id.languageHTxt);
        Email_header = (MyTextView) findViewById(R.id.Email_header);
        Mobile_header = (MyTextView) findViewById(R.id.Mobile_header);
        places_header = (MyTextView) findViewById(R.id.places_header);
        referralCode_header = (MyTextView) findViewById(R.id.referralCode_header);
        homePlace = (MyTextView) findViewById(R.id.homePlace);
        workPlace = (MyTextView) findViewById(R.id.workPlace);

        countryCode_txt = (MyTextView) findViewById(R.id.countryCode_txt);
        referralPoints_header = (MyTextView) findViewById(R.id.referralPoints_header);
        referralCodeTXT = (MyTextView) findViewById(R.id.referralCodeTXT);

        first_name_text = (MyTextView) findViewById(R.id.first_name_text);
        last_name_text = (MyTextView) findViewById(R.id.last_name_text);

        firstName_header = (MyTextView) findViewById(R.id.firstName_header);
        lastName_header = (MyTextView) findViewById(R.id.lastName_header);

        update_language_box = (MyTextView) findViewById(R.id.update_language_box);
        update_currency_box = (MyTextView) findViewById(R.id.update_currency_box);

        btn_rippel_update_info_btn = (RippleStyleButton) findViewById(R.id.update_info_btn);

        user_image_area = (RoundedImageView) findViewById(R.id.user_image_area);

		/* Set Labels */
        getLanguageLabelsFrmSqLite();
        /* Set Labels Finished */

        setLanguage_list();

        MyTextView user_email = (MyTextView) findViewById(R.id.user_email);
        user_email.setText(email_user);

        parse_json_string(json_responseString_profile);

        checkProfileImage();

        user_image_area.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new usr_img_dialog_run().run();
            }
        });

        ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
        back_navigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UserProfileActivity.super.onBackPressed();
            }
        });

        ImageView ic_overflow = (ImageView) findViewById(R.id.ic_overflow);
        ic_overflow.setVisibility(View.VISIBLE);
        ic_overflow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showPopup(v);
            }
        });

        btn_rippel_update_info_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                update_info_user();
            }
        });

        update_language_box.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (alert_show_language_list != null) {
                    alert_show_language_list.show();
                }
            }
        });

        update_currency_box.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (alert_show_currency_list != null) {
                    alert_show_currency_list.show();
                }
            }
        });

        homePlace.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent addHomeActivity = new Intent(UserProfileActivity.this, AddHomePlaceActivity.class);
                startActivity(addHomeActivity);

            }
        });

        workPlace.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent addWorkActivity = new Intent(UserProfileActivity.this, AddWorkPlaceActivity.class);
                startActivity(addWorkActivity);
            }
        });
    }


    public void checkProfileImage() {
        String access_token_main = mPrefs.getString("access_token", null);
        if (access_token_main != null && id_facebook != null && !id_facebook.trim().equals("")
                && !id_facebook.trim().equals("0")) {

            String imageUrl = "http://graph.facebook.com/" + id_facebook + "/picture?type=large&width=200&height=200";

            new DownloadProfileImg(UserProfileActivity.this, user_image_area, imageUrl, "" + id_facebook + ".jpg")
                    .execute();

        } else {
            if (vImgName_str == null || vImgName_str.equals("") || vImgName_str.equals("NONE")) {
                user_image_area.setImageResource(R.drawable.friends_main);
            } else {
                new DownloadProfileImg(UserProfileActivity.this, user_image_area,
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
            LBL_PROFILE_TITLE_TXT_str = obj_language_labels.getString("LBL_PROFILE_TITLE_TXT");
            LBL_USER_NAME_LBL_TXT_str = obj_language_labels.getString("LBL_USER_NAME_LBL_TXT");
            LBL_EMAIL_LBL_TXT_str = obj_language_labels.getString("LBL_EMAIL_LBL_TXT");
            LBL_MOBILE_NUMBER_HEADER_TXT_str = obj_language_labels.getString("LBL_MOBILE_NUMBER_HEADER_TXT");
            LBL_PAYMENT_INFO_HEADER_TXT_str = obj_language_labels.getString("LBL_PAYMENT_INFO_HEADER_TXT");
            LBL_PLACES_HEADER_TXT_str = obj_language_labels.getString("LBL_PLACES_HEADER_TXT");
            LBL_ADD_HOME_PLACE_TXT_str = obj_language_labels.getString("LBL_ADD_HOME_PLACE_TXT");
            LBL_ADD_WORK_PLACE_TXT_str = obj_language_labels.getString("LBL_ADD_WORK_PLACE_TXT");
            LBL_BTN_UPDATE_PROFILE_TXT_str = obj_language_labels.getString("LBL_BTN_UPDATE_PROFILE_TXT");
            LBL_BTN_UPDATE_PAYMENT_TXT_str = obj_language_labels.getString("LBL_BTN_UPDATE_PAYMENT_TXT");
            LBL_NOT_SUPPORT_CAMERA_TXT_str = obj_language_labels.getString("LBL_NOT_SUPPORT_CAMERA_TXT");
            LBL_FAILED_CAPTURE_IMAGE_TXT_str = obj_language_labels.getString("LBL_FAILED_CAPTURE_IMAGE_TXT");
            LBL_SELECT_LANGUAGE_HINT_TXT_str = obj_language_labels.getString("LBL_SELECT_LANGUAGE_HINT_TXT");
            LBL_SELECTED_LANGUAGE_TXT_str = obj_language_labels.getString("LBL_SELECT_LANGUAGE_HINT_TXT");
            LBL_FAILED_UPDATE_LANGUAGE_TXT_str = obj_language_labels.getString("LBL_FAILED_UPDATE_LANGUAGE_TXT");
            LBL_UPDATING_LANGUAGE_LOADING_TXT_str = obj_language_labels.getString("LBL_UPDATING_LANGUAGE_LOADING_TXT");
            LBL_SELECT_TXT_str = obj_language_labels.getString("LBL_SELECT_TXT");
            LBL_ADD_PAYMENT_DETAIL_TXT_str = obj_language_labels.getString("LBL_ADD_PAYMENT_DETAIL_TXT");
            LBL_WAIT_UPLOAD_IMAGE_LOADING_TXT_str = obj_language_labels.getString("LBL_WAIT_UPLOAD_IMAGE_LOADING_TXT");
            LBL_SOME_ERROR_TXT_str = obj_language_labels.getString("LBL_SOME_ERROR_TXT");
            LBL_FIRST_NAME_HEADER_TXT_str = obj_language_labels.getString("LBL_FIRST_NAME_HEADER_TXT");
            LBL_LAST_NAME_HEADER_TXT_str = obj_language_labels.getString("LBL_LAST_NAME_HEADER_TXT");
            LBL_COUNTRY_TXT_str = obj_language_labels.getString("LBL_COUNTRY_TXT");
            LBL_PASS_UPDATE_LOADING_TXT_str = obj_language_labels.getString("LBL_PASS_UPDATE_LOADING_TXT");
            LBL_UPDATE_INFO_FAILED_TXT_str = obj_language_labels.getString("LBL_UPDATE_INFO_FAILED_TXT");
            LBL_PASS_UPDATE_SUCCESS_str = obj_language_labels.getString("LBL_PASS_UPDATE_SUCCESS");
            LBL_CURRENCY_TXT_str = obj_language_labels.getString("LBL_CURRENCY_TXT");
            LBL_FAILED_TXT_str = obj_language_labels.getString("LBL_FAILED_TXT");
            LBL_LOADING_TXT_str = obj_language_labels.getString("LBL_LOADING_TXT");
            LBL_LANGUAGE_TXT_str = obj_language_labels.getString("LBL_LANGUAGE_TXT");
            LBL_REFERRAL_CODE_TXT_str = obj_language_labels.getString("LBL_REFERRAL_CODE_TXT");
            LBL_REFERRAL_POINTS_H_TXT_str = obj_language_labels.getString("LBL_REFERRAL_POINTS_H_TXT");
            LBL_REFER_TO_EARN_POINTS_TXT_str = obj_language_labels.getString("LBL_REFER_TO_EARN_POINTS_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj_language_labels != null) {
            text_header.setText("" + LBL_PROFILE_TITLE_TXT_str);
            Email_header.setText("" + LBL_EMAIL_LBL_TXT_str);
            Mobile_header.setText("" + LBL_MOBILE_NUMBER_HEADER_TXT_str);
            places_header.setText("" + LBL_PLACES_HEADER_TXT_str);
            btn_rippel_update_info_btn.setText("" + LBL_BTN_UPDATE_PROFILE_TXT_str);
            update_language_box.setHint("" + LBL_SELECT_LANGUAGE_HINT_TXT_str);
            firstName_header.setText("" + LBL_FIRST_NAME_HEADER_TXT_str);
            lastName_header.setText("" + LBL_LAST_NAME_HEADER_TXT_str);
//			countryCode_txt.setHint("" + LBL_COUNTRY_TXT_str);
            currencyHTxt.setText("" + LBL_CURRENCY_TXT_str);
            languageHTxt.setText("" + LBL_LANGUAGE_TXT_str);
            referralCode_header.setText("" + LBL_REFERRAL_CODE_TXT_str);
            referralPoints_header.setText("" + LBL_REFERRAL_POINTS_H_TXT_str);
        }

        checkPlaces();

    }

    public void showPopup(View v) {

        PopupMenu popup = new PopupMenu(UserProfileActivity.this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.user_profile, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO Auto-generated method stub
                int order = item.getOrder();
                if (order == 1) {
                    new Ask_password_dialog(UserProfileActivity.this, UserProfileActivity.this,
                            currentPass_User_passenger).run();
                } else if (order == 2) {
                    update_info_user();
                }
                return false;
            }
        });
    }

    public void setLanguage_list() {

        String userSelectedLanguageCode = mPrefs.getString("selected_language_code", null);

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_List\"");

        String txt_language_list;

        cursor.moveToPosition(0);
        // while (cursor.moveToNext()) {
        txt_language_list = cursor.getString(0).trim();

        cursor.close();

        JSONObject obj_languages = null;
        JSONArray list_obj_languages = null;
        try {
            obj_languages = new JSONObject(txt_language_list);
            list_obj_languages = obj_languages.getJSONArray("list_languages");
            // Log.d("list_obj_languages", "" + list_obj_languages.toString());

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

                String language_code = "" + obj_temp.getString("vCode");
                if (userSelectedLanguageCode != null && !userSelectedLanguageCode.equals("")) {

                    if (userSelectedLanguageCode.contains("" + language_code)) {
                        update_language_box.setText("" + obj_temp.getString("vTitle"));
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        CharSequence[] cs_languages_txt = items_txt_language.toArray(new CharSequence[items_txt_language.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LanguagePopUpTheme);
        builder.setTitle("" + LBL_SELECT_TXT_str);

        builder.setItems(cs_languages_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

                if (alert_show_language_list != null) {
                    alert_show_language_list.dismiss();
                }
                selected_language = items_txt_language.get(item);
                selected_language_code = items_language_code.get(item);

                new UpdateLanguageCode().execute();
            }
        });
        // builder.setItems(cs_languages_txt, null);

        alert_show_language_list = builder.create();
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

        CharSequence[] cs_languages_txt = items_currency_code.toArray(new CharSequence[items_currency_code.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LanguagePopUpTheme);
        builder.setTitle("" + LBL_SELECT_TXT_str);

        builder.setItems(cs_languages_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

                if (alert_show_currency_list != null) {
                    alert_show_currency_list.dismiss();
                }
                selected_Currency_code = items_currency_code.get(item);

                new UpdateCurrency().execute();
            }
        });
        // builder.setItems(cs_languages_txt, null);

        alert_show_currency_list = builder.create();

    }

    public void changePassword_Update(String new_pass_str) {

        new UserPassUpdate(new_pass_str).execute();

    }

    public class UserPassUpdate extends AsyncTask<String, String, String> {
        String responseString = "";
        ProgressDialog pDialog;
        String user_password;
        String string_response_passUpdate_detail = "";

        public UserPassUpdate(String userPass) {
            // TODO Auto-generated constructor stub
            this.user_password = userPass;
        }

        public String getRegisteredUserDetail() {

            // String baseUrl = CommonUtilities.SERVER_URL_UPDATE_DETAIL;
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&user_id=%s&pass=%s";

            String registerUrl = baseUrl + String.format(registerParam, "update_pass_passenger_Detail",
                    URLEncoder.encode(user_id_auto), URLEncoder.encode(user_password));

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
            pDialog = new ProgressDialog(UserProfileActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage(Html.fromHtml("" + LBL_PASS_UPDATE_LOADING_TXT_str));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            string_response_passUpdate_detail = getRegisteredUserDetail();

            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

            if (string_response_passUpdate_detail != null && !string_response_passUpdate_detail.equals("")
                    && !string_response_passUpdate_detail.equals("Failed.")) {

                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString("access_sign_token_pass", user_password);
                editor.commit();

                updated_json_responseString_profile = string_response_passUpdate_detail;

                Mainprofile_Activity.updated_json_responseString_profile = string_response_passUpdate_detail;

                Mainprofile_Activity.updateProfileTrue = true;

                parse_json_string(string_response_passUpdate_detail);

                Toast.makeText(UserProfileActivity.this, "" + LBL_PASS_UPDATE_SUCCESS_str, Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(UserProfileActivity.this, "" + LBL_UPDATE_INFO_FAILED_TXT_str, Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UpdateLanguageCode extends AsyncTask<String, String, String> {
        String json_language_labels_str = "";

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(UserProfileActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage(Html.fromHtml("" + LBL_UPDATING_LANGUAGE_LOADING_TXT_str));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public String language_labels_get() {
            String responseString = "";

            String language_default_url = CommonUtilities.SERVER_URL
                    /* _LABELS_DEFAULT */ + "function=UpdateLanguageCode&vCode=" + selected_language_code + "&UserID="
                    + user_id_auto + "&UserType=Passenger";


            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(language_default_url);
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
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            json_language_labels_str = language_labels_get();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            pDialog.dismiss();
            if (json_language_labels_str != null && json_language_labels_str.toString().length() > 6
                    && !json_language_labels_str.trim().equals("UpdateFailed")) {

                if (CheckIsDataAlreadyInDBorNot("labels", "vLabel", "Language_labels") == false) {
                    String sql = "INSERT INTO labels(vLabel,vValue) values('%s','%s')";
                    sql = String.format(sql, "Language_labels", json_language_labels_str.replace("'", "''").toString());
                    dbConnect.execNonQuery(sql);
                } else {
                    String sql = "UPDATE `labels` SET vValue = '"
                            + json_language_labels_str.replace("'", "''").toString() + "'"
                            + " WHERE vLabel = \"Language_labels\"";
                    dbConnect.execNonQuery(sql);
                }

                update_language_box.setText("" + selected_language);

                SharedPreferences.Editor editor = mPrefs.edit();

                editor.putString("selected_language_code", selected_language_code);
                // Log.d("user_auto_id :", "" + user_auto_id);
                editor.commit();

                Mainprofile_Activity.language_changed = true;

                getLanguageLabelsFrmSqLite();

            } else {
                Toast.makeText(UserProfileActivity.this, "" + LBL_FAILED_UPDATE_LANGUAGE_TXT_str, Toast.LENGTH_LONG)
                        .show();
            }
        }

    }


    public class UpdateCurrency extends AsyncTask<String, String, String> {
        String result_str = "";

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(UserProfileActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage(Html.fromHtml("" + LBL_LOADING_TXT_str));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public String language_labels_get() {
            String responseString = "";

            String language_default_url = CommonUtilities.SERVER_URL + "function=updateCurrencyValue&vCurrencyCode=" + selected_Currency_code + "&UserID="
                    + user_id_auto + "&UserType=Passenger";


            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(language_default_url);
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
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            result_str = language_labels_get();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            pDialog.dismiss();
            if (result_str != null && result_str.toString().length() > 6
                    && !result_str.trim().equals("UpdateFailed")) {

                update_currency_box.setText(selected_Currency_code);

                try {
                    JSONObject obj_temp = new JSONObject(json_responseString_profile);
                    obj_temp.remove("vCurrencyPassenger");
                    obj_temp.put("vCurrencyPassenger", selected_Currency_code);

                    Mainprofile_Activity.updated_json_responseString_profile = obj_temp.toString();

                    Mainprofile_Activity.updateProfileTrue = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(UserProfileActivity.this, "" + LBL_FAILED_TXT_str, Toast.LENGTH_LONG)
                        .show();
            }
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

    class usr_img_dialog_run implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            final Dialog dialog_img_update = new Dialog(UserProfileActivity.this, R.style.DialogSlideAnim_markerDetail);

            dialog_img_update.setContentView(R.layout.custom_dialog_update_image);

            ImageView img_update_close_dialog = (ImageView) dialog_img_update
                    .findViewById(R.id.img_update_close_dialog);
            img_update_close_dialog.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (dialog_img_update != null) {
                        dialog_img_update.cancel();
                    }
                }
            });

            ImageView cameraIcon = (ImageView) dialog_img_update.findViewById(R.id.cameraIcon);
            cameraIcon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    // Checking camera availability
                    if (!isDeviceSupportCamera()) {
                        Toast.makeText(getApplicationContext(), "" + LBL_NOT_SUPPORT_CAMERA_TXT_str, Toast.LENGTH_LONG)
                                .show();
                    } else {
                        // will close the app if the device does't have camera

                        if (dialog_img_update != null) {
                            dialog_img_update.cancel();
                        }
                        chooseFromCamera();
                    }
                }
            });

            ImageView GalleryIcon = (ImageView) dialog_img_update.findViewById(R.id.GalleryIcon);
            GalleryIcon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (dialog_img_update != null) {
                        dialog_img_update.cancel();
                    }
                    chooseFromGallery();
                }
            });

            dialog_img_update.setCanceledOnTouchOutside(true);

            Window window = dialog_img_update.getWindow();
            window.setGravity(Gravity.BOTTOM);

            window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            dialog_img_update.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            dialog_img_update.show();

        }

    }

    public void chooseFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                // Toast.makeText(getApplicationContext(), "User cancelled image
                // capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "" + LBL_FAILED_CAPTURE_IMAGE_TXT_str, Toast.LENGTH_SHORT)
                        .show();
            }
        }
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {


                Uri selectedImageUri = data.getData();

                String selectedImagePath = getPath(selectedImageUri);
//                Log.d("BEFORE", "selectedImagePath:" + selectedImagePath);
                if (getPath(selectedImageUri) == null) {
                    selectedImagePath = selectedImageUri.getPath();
                }


//				Uri selectedImageUri = data.getData();
//				String selectedImagePath = getPath(selectedImageUri);
                // System.out.println("Image Path : " + selectedImagePath);

                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, options);

                if (bitmap != null) {
                    new ImageGalleryTask(bitmap).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "" + LBL_FAILED_TXT_str, Toast.LENGTH_SHORT)
                            .show();
                }

            }
        }
    }

    class ImageGalleryTask extends AsyncTask<Void, Void, String> {
        Bitmap bitmap_profilePic;

        String responseString = "";
        ProgressDialog pDialog_upload_image;

        public ImageGalleryTask(Bitmap bitmap) {
            // TODO Auto-generated constructor stub
            this.bitmap_profilePic = bitmap;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog_upload_image = new ProgressDialog(UserProfileActivity.this, R.style.DialogTheme_custom);
            pDialog_upload_image.setMessage("" + LBL_WAIT_UPLOAD_IMAGE_LOADING_TXT_str);
            pDialog_upload_image.setCancelable(false);
            pDialog_upload_image.setCanceledOnTouchOutside(false);
            pDialog_upload_image.show();

        }

        @Override
        protected String doInBackground(Void... unsued) {
            InputStream is;
            BitmapFactory.Options bfo;
            Bitmap bitmapOrg;
            ByteArrayOutputStream bao;

            bfo = new BitmapFactory.Options();
            bfo.inSampleSize = 4;

            DateFormat df = new SimpleDateFormat("dd-MM-yy-HH-mm-ss");
            Calendar calobj = Calendar.getInstance();
            String date_get = df.format(calobj.getTime());
            // System.out.println("date_get:" + date_get);

            bao = new ByteArrayOutputStream();
            bitmap_profilePic.compress(Bitmap.CompressFormat.JPEG, 80, bao);
            byte[] ba = bao.toByteArray();
            String ba1 = Base64.encodeBytes(ba);


            HashMap<String, String> uploadImgData = new HashMap<String, String>();
            uploadImgData.put("function", "uploadImage_passenger");
            uploadImgData.put("user_id", user_id_auto);
            uploadImgData.put("image", ba1);
            uploadImgData.put("cmd", date_get + ".jpeg");

            responseString = OutputStreamReader.performPostCall(CommonUtilities.SERVER_URL, uploadImgData);


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... unsued) {

        }

        @Override
        protected void onPostExecute(String sResponse) {

            if (pDialog_upload_image != null) {
                pDialog_upload_image.dismiss();
            }

//            Log.d("Img Response","::"+responseString);
            if (responseString != null && !responseString.trim().equals("")
                    && !responseString.trim().equals("Failed")) {

                JSONObject obj_img;
                try {
                    obj_img = new JSONObject(responseString);
                    String action_msg = obj_img.getString("Action");
                    String ImgName_str = obj_img.getString("ImgName");

                    if (action_msg.trim().equals("SUCCESS")) {
                        user_image_area.setImageBitmap(bitmap_profilePic);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap_profilePic.compress(CompressFormat.JPEG, 80, stream);
                        InputStream inputStream_profilePic = new ByteArrayInputStream(stream.toByteArray());

                        SaveIamge(ImgName_str, inputStream_profilePic);

                        Drawable dr_user_img = new BitmapDrawable(getResources(), bitmap_profilePic);
                        Mainprofile_Activity.facebook_profile_image_main = dr_user_img;
                        Mainprofile_Activity.vImgName_str = ImgName_str;
                        Mainprofile_Activity.updateHeaderImage();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {

                Toast.makeText(UserProfileActivity.this, "" + LBL_SOME_ERROR_TXT_str, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private File SaveIamge(String name, InputStream in_originalFile) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + CommonUtilities.storedImageFolderName);
        myDir.mkdirs();

        if (vImgName_str != null) {
            File old_pic_file = new File(myDir, vImgName_str);
            if (old_pic_file.exists()) {
                old_pic_file.delete();
            }
        }

        File file = new File(myDir, name);

        try {

            FileOutputStream out = new FileOutputStream(file);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in_originalFile.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in_originalFile.close();
            out.close();

            vImgName_str = name;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }


    public String getPath(Uri uri) {
//		String[] projection = { MediaStore.Images.Media.DATA };
//		Cursor cursor = managedQuery(uri, projection, null, null, null);
//		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//		cursor.moveToFirst();
//		return cursor.getString(column_index);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            return filePath;
        } else {
            return null;
        }
    }

    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

            new ImageGalleryTask(bitmap).execute();
            // user_image_area.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void chooseFromGallery() {
        // System.out.println("Gallery pressed");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void parse_json_string(String json_str) {

        // String parse_build = json_str.replace("[", "").replace("]", "");

        JSONObject jsonObject_userDetail = null;
        try {
            jsonObject_userDetail = new JSONObject(json_str);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            id = jsonObject_userDetail.getString(CommonUtilities.vFbId);
            user_id_auto = jsonObject_userDetail.getString(CommonUtilities.iUserId);
            // name = jsonObject_userDetail.getString(CommonUtilities.vName);
            first_name = jsonObject_userDetail.getString("vName");
            last_name = jsonObject_userDetail.getString("vLastName");

            email = jsonObject_userDetail.getString(CommonUtilities.vEmail);
            gender = jsonObject_userDetail.getString(CommonUtilities.eGender);
            phone = jsonObject_userDetail.getString(CommonUtilities.vPhone);
            vPhoneCode_str = jsonObject_userDetail.getString("vPhoneCode");
            currentPass_User_passenger = jsonObject_userDetail.getString("Passenger_Password_decrypt");
            selected_Currency_code = jsonObject_userDetail.getString("vCurrencyPassenger");
            birthDate = jsonObject_userDetail.getString("dBirthDate");


            password = jsonObject_userDetail.getString(CommonUtilities.vPasswordUser);

            bmp = BitmapFactory.decodeResource(UserProfileActivity.this.getResources(), R.drawable.friends_main);

            update_currency_box.setText(selected_Currency_code);

            JSONArray arr_country = jsonObject_userDetail.getJSONArray("CurrencyList");
            setCurrency_list(arr_country);

            MyTextView user_mobile = (MyTextView) findViewById(R.id.user_mobile);
            MyTextView referralCodeTXT = (MyTextView) findViewById(R.id.referralCodeTXT);
            MyTextView referralPointsTXT = (MyTextView) findViewById(R.id.referralPointsTXT);

            user_mobile.setText(phone);
            referralCodeTXT.setText(jsonObject_userDetail.getString("vReferralCode"));

            countryCode_txt.setText("+" + vPhoneCode_str);


            first_name_text.setText("" + first_name);
            last_name_text.setText("" + last_name);

            this.json_responseString_profile = json_str;

            String totalReferralPoints = jsonObject_userDetail.getString("TotalReferralPoints");

            if (!totalReferralPoints.trim().equals("") && totalReferralPoints.trim().length() > 0) {
                referralPointsTXT.setText(totalReferralPoints);
            }else{
                referralPointsTXT.setText(LBL_REFER_TO_EARN_POINTS_TXT_str);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void update_info_user() {

        Intent send_data = new Intent(UserProfileActivity.this, UpdateUsr_ProfileActivity.class);

        Bundle bn_send_data = new Bundle();
        bn_send_data.putString("ID", id);
        bn_send_data.putString("user_id", user_id_auto);
        bn_send_data.putString("FName", first_name);
        bn_send_data.putString("LName", last_name);
        bn_send_data.putString("Email", email);
        bn_send_data.putString("Gender", gender);
        // bn_send_data.putString("PayMentInfo", "NO");
        bn_send_data.putString("phone", phone);
        bn_send_data.putString("phoneCode", "" + vPhoneCode_str);
        bn_send_data.putString("password", password);
        bn_send_data.putString("fProfile_image_bitmap", vImgName_str);
        bn_send_data.putString("FBID", id_facebook);
        bn_send_data.putString("BirthDate", birthDate);
        bn_send_data.putString("CurrencyCode", selected_Currency_code);
        bn_send_data.putString("ProfileJSON", json_responseString_profile);

        send_data.putExtras(bn_send_data);

        startActivity(send_data);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (updateTrue == true) {
            if (!updated_json_responseString_profile.equals("") && !updated_json_responseString_profile.equals("[]")) {
                parse_json_string(updated_json_responseString_profile);

            }
        }

        checkPlaces();
    }

    public void checkPlaces() {
        final SharedPreferences mpref_place = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);

        String home_address_str = mpref_place.getString("userHomeLocationAddress", null);
        String work_address_str = mpref_place.getString("userWorkLocationAddress", null);

        String userHomeLocationLatitude_str = mpref_place.getString("userHomeLocationLatitude", null);
        String userHomeLocationLongitude_str = mpref_place.getString("userHomeLocationLongitude", null);
        String userWorkLocationLatitude_str = mpref_place.getString("userWorkLocationLatitude", null);
        String userWorkLocationLongitude_str = mpref_place.getString("userWorkLocationLongitude", null);

        Drawable img_delete = getResources().getDrawable(R.drawable.ic_delete);
        Drawable img_home_place = getResources().getDrawable(R.drawable.home_place);
        Drawable img_work_place = getResources().getDrawable(R.drawable.work_place);


        if (home_address_str != null) {
            str_home_address = home_address_str;
            homePlace.setText("" + str_home_address);


            homePlace.setCompoundDrawablesWithIntrinsicBounds(img_home_place, null, img_delete, null);

            homePlace.setOnTouchListener(new OnTouchListener() {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int leftEdgeOfRightDrawable = homePlace.getRight()
                                - homePlace.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();

                        if (event.getRawX() >= leftEdgeOfRightDrawable) {


                            mpref_place.edit().remove("userHomeLocationAddress").commit();
                            mpref_place.edit().remove("userHomeLocationLatitude").commit();
                            mpref_place.edit().remove("userHomeLocationLongitude").commit();

                            homePlace.setText("" + LBL_ADD_HOME_PLACE_TXT_str);

                            return true;
                        }
                    }
                    return false;
                }
            });
        } else {
            homePlace.setText("" + LBL_ADD_HOME_PLACE_TXT_str);
        }

        if (work_address_str != null) {
            str_work_address = work_address_str;
            workPlace.setText("" + str_work_address);

            workPlace.setCompoundDrawablesWithIntrinsicBounds(img_work_place, null, img_delete, null);

            workPlace.setOnTouchListener(new OnTouchListener() {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int leftEdgeOfRightDrawable = workPlace.getRight()
                                - workPlace.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();

                        if (event.getRawX() >= leftEdgeOfRightDrawable) {
                            mpref_place.edit().remove("userWorkLocationAddress").commit();
                            mpref_place.edit().remove("userWorkLocationLatitude").commit();
                            mpref_place.edit().remove("userWorkLocationLongitude").commit();

                            workPlace.setText("" + LBL_ADD_WORK_PLACE_TXT_str);

                            return true;
                        }
                    }
                    return false;
                }
            });
        } else {
            workPlace.setText("" + LBL_ADD_WORK_PLACE_TXT_str);
        }

        if (userHomeLocationLatitude_str != null) {
            home_location_latitude = userHomeLocationLatitude_str;
        }

        if (userHomeLocationLongitude_str != null) {
            home_location_longitude = userHomeLocationLongitude_str;
        }

        if (userWorkLocationLatitude_str != null) {
            work_location_latitude = userWorkLocationLatitude_str;
        }

        if (userWorkLocationLongitude_str != null) {
            work_location_longitude = userWorkLocationLongitude_str;
        }
    }
}

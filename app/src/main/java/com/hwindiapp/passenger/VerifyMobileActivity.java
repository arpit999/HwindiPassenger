package com.hwindiapp.passenger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fonts.Text.MyEditText;
import com.fonts.Text.MyTextView;
import com.mainProfile.classFiles.OutputStreamReader;
import com.utils.CommonUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VerifyMobileActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    TextView text_header;
    DBConnect dbConnect;

    MyTextView msgWaitTxtView;
    MyTextView mobileTxtView;
    MyEditText verificationCodeEditBox;
    MyTextView didNotReceiveTxt;
    MyTextView countDownTxt;

    RippleStyleButton btn_verify;
    RippleStyleButton btn_re_sendCode;

    String language_labels_get_frm_sqLite = "";
    String LBL_MOBILE_VERIFy_TXT_str = "";
    String LBL_CALL_ME_TXT_str = "";
    String LBL_DID_NOT_RECEIVE_CALL_TXT_str = "";
    String LBL_BTN_VERIFY_TXT_str = "";
    String LBL_MISS_CALL_GEN_INFO_IPHONE_str = "";
    String LBL_LOADING_TXT_str = "";
    String LBL_ERROR_OCCURED_str = "";
    String LBL_VERIFICATION_MOBILE_FAILED_TXT_str = "";
    String LBL_BTN_OK_TXT_str = "";
    String LBL_MOBILE_VERIFICATION_FAILED_TXT_str = "";

    public static int REQUEST_CODE = 126;

    String mobileNum_str = "";
    String country_str = "";

    String verificationCode_str = "";

    AlertDialog alert_problem;
    AlertDialog alert_verificationCode;

    LinearLayout countDownArea;

    CountDownTimer countDnTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        text_header = (TextView) findViewById(R.id.text_header);
        msgWaitTxtView = (MyTextView) findViewById(R.id.msgWaitTxtView);
        mobileTxtView = (MyTextView) findViewById(R.id.mobileTxtView);
        verificationCodeEditBox = (MyEditText) findViewById(R.id.verificationCodeEditBox);
        btn_verify = (RippleStyleButton) findViewById(R.id.btn_verify);
        btn_re_sendCode = (RippleStyleButton) findViewById(R.id.btn_re_sendCode);
        didNotReceiveTxt = (MyTextView) findViewById(R.id.didNotReceiveTxt);
        countDownTxt = (MyTextView) findViewById(R.id.countDownTxt);
        countDownArea = (LinearLayout) findViewById(R.id.countDownArea);

        dbConnect = new DBConnect(this, "UC_labels.db");

        /* Set Labels */
        getLanguageLabelsFrmSqLite();
        /* Set Labels Finished */

        ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
        back_navigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                VerifyMobileActivity.super.onBackPressed();
            }
        });

//        mobileNum_str = getIntent().getStringExtra("mobile");
        mobileNum_str = getIntent().getStringExtra("mobile");
        country_str = getIntent().getStringExtra("country");

        mobileTxtView.setText("+" + country_str + mobileNum_str);

        new requestVerification().execute();

        btn_re_sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new requestVerification().execute();
            }
        });

        didNotReceiveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCountDownArea();
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificationCode = verificationCodeEditBox.getText().toString();
                if (!verificationCode.trim().equals("") && !verificationCode_str.trim().equals("") && verificationCode.equals(verificationCode_str)) {

                    Intent in = new Intent();
                    in.putExtra("message", "SUCCESS");
                    setResult(RESULT_OK, in);
                    finish();
                } else {
                    buildAlertMessageERROR(LBL_VERIFICATION_MOBILE_FAILED_TXT_str);
                }
            }
        });
        btn_re_sendCode.setEnabled(false);
    }

    public void checkCountDownArea() {
        if (countDownArea.getVisibility() != View.VISIBLE) {
            countDownArea.setVisibility(View.VISIBLE);
            startCountDownTimer();
        } else {

            cancelTimer();
        }
    }

    public void cancelTimer() {
        if (countDnTimer != null) {
            countDnTimer.cancel();
        }
        countDownArea.setVisibility(View.GONE);
    }

    public void editMobileNum(View v) {
        VerifyMobileActivity.super.onBackPressed();
    }

    public void getLanguageLabelsFrmSqLite() {

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

        cursor.moveToPosition(0);

        language_labels_get_frm_sqLite = cursor.getString(0);

        JSONObject obj_language_labels = null;

        try {
            obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
            LBL_MOBILE_VERIFy_TXT_str = obj_language_labels.getString("LBL_MOBILE_VERIFy_TXT");
            LBL_CALL_ME_TXT_str = obj_language_labels.getString("LBL_CALL_ME_TXT");
            LBL_DID_NOT_RECEIVE_CALL_TXT_str = obj_language_labels.getString("LBL_DID_NOT_RECEIVE_CALL_TXT");
            LBL_BTN_VERIFY_TXT_str = obj_language_labels.getString("LBL_BTN_VERIFY_TXT");
            LBL_MISS_CALL_GEN_INFO_IPHONE_str = obj_language_labels.getString("LBL_MISS_CALL_GEN_INFO_IPHONE");
            LBL_LOADING_TXT_str = obj_language_labels.getString("LBL_LOADING_TXT");
            LBL_ERROR_OCCURED_str = obj_language_labels.getString("LBL_ERROR_OCCURED");
            LBL_VERIFICATION_MOBILE_FAILED_TXT_str = obj_language_labels.getString("LBL_VERIFICATION_MOBILE_FAILED_TXT");
            LBL_BTN_OK_TXT_str = obj_language_labels.getString("LBL_BTN_OK_TXT");
            LBL_MOBILE_VERIFICATION_FAILED_TXT_str = obj_language_labels.getString("LBL_MOBILE_VERIFICATION_FAILED_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj_language_labels != null) {

            text_header.setText("" + LBL_MOBILE_VERIFy_TXT_str);
            btn_re_sendCode.setText("" + LBL_CALL_ME_TXT_str);
            didNotReceiveTxt.setText(LBL_DID_NOT_RECEIVE_CALL_TXT_str);
            btn_verify.setText(LBL_BTN_VERIFY_TXT_str);
            msgWaitTxtView.setText(LBL_MISS_CALL_GEN_INFO_IPHONE_str);
        }

    }

    public void startCountDownTimer() {
        countDnTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long milliseconds) {

                int seconds = (int) (milliseconds / 1000) % 60;
                int minutes = (int) ((milliseconds / (1000 * 60)) % 60);

                countDownTxt.setText(twoDigitString(minutes) + ":" + twoDigitString(seconds) + " / " + "01:00");
            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                countDownTxt.setText("01:00");
                btn_re_sendCode.setEnabled(true);
            }

        }.start();
    }

    private String twoDigitString(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    public class requestVerification extends AsyncTask<String, String, String> {

        String responseString = "";

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            cancelTimer();

            pDialog = new ProgressDialog(VerifyMobileActivity.this, R.style.DialogTheme_custom);
            pDialog.setMessage("" + LBL_LOADING_TXT_str);
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        public String getMobileResponse() {
            String responseString = "";
            HttpURLConnection urlConnection = null;
            try {

//				Log.d("verification url", "::" + CommonUtilities.SERVER_URL + "type=sendVerificationSMS&MobileNo=" + mobileNum_str);
//                URL url = new URL(CommonUtilities.SERVER_URL + "type=sendVerificationSMS&MobileNo=" + mobileNum_str);
                URL url = new URL(CommonUtilities.SERVER_URL + "type=sendVerificationSMS&MobileNo=" + mobileNum_str + "&CountryCode=" + country_str);
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
        protected String doInBackground(String... strings) {

            responseString = getMobileResponse();
            Log.d("responseString", "responseString::" + responseString);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pDialog.dismiss();

            if (responseString != null && !responseString.equals("")) {

                JSONObject tempObj = null;
                try {
                    tempObj = new JSONObject(responseString);

                    String action_str = tempObj.getString("action");
                    if (action_str.equals("1")) {
                        verificationCode_str = tempObj.getString("verificationCode");
                    } else {
                        buildAlertMessageERROR(LBL_MOBILE_VERIFICATION_FAILED_TXT_str);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    buildAlertMessageERROR(LBL_ERROR_OCCURED_str);
                }


            } else {
                buildAlertMessageERROR(LBL_ERROR_OCCURED_str);
            }
        }
    }

    private void buildAlertMessageERROR(String content) {

        if (alert_problem != null) {
            alert_problem.dismiss();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LightThemeGPSalert);
        builder.setMessage("" + content).setCancelable(false).setNegativeButton("" + LBL_BTN_OK_TXT_str,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        alert_problem.cancel();

//                        finish();
                    }
                });
        alert_problem = builder.create();
        alert_problem.show();
    }

//    private void buildVerificationCodeERROR() {
//
//        if (alert_verificationCode != null) {
//            alert_verificationCode.dismiss();
//        }
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LightThemeGPSalert);
//        builder.setMessage("" + LBL_VERIFICATION_MOBILE_FAILED_TXT_str).setCancelable(false).setNegativeButton("" + LBL_BTN_OK_TXT_str,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        alert_problem.cancel();
//
//                    }
//                });
//        alert_verificationCode = builder.create();
//        alert_verificationCode.show();
//    }
}

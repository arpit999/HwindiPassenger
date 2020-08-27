package com.mainProfile.classFiles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.hwindiapp.passenger.DBConnect;
import com.hwindiapp.passenger.R;
import com.hwindiapp.passenger.VerifyProfileActivity;
import com.utils.CommonUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Admin on 18-02-2016.
 */
public class RegisterFbLoginResCallBack implements FacebookCallback<LoginResult> {
    Context mContext;
    DBConnect dbConnect;

    String language_labels_get_frm_sqLite = "";
    String LBL_LOADING_TXT_str = "";
    String LBL_ERROR_OCCURED_str = "";
    String LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str = "";
    String LBL_SERVICE_NOT_AVAIL_TXT_str = "";
    String LBL_ACC_DELETE_TXT_str = "";
    String LBL_ERROR_TXT_str = "";
    String LBL_BTN_OK_TXT_str = "";

    ProgressDialog pDialog;

    androidx.appcompat.app.AlertDialog alertDialog;

    public RegisterFbLoginResCallBack(Context mContext) {
        this.mContext = mContext;
        dbConnect = new DBConnect(this.mContext, "UC_labels.db");

        getLanguageLabelsFrmSqLite();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {

        pDialog = new ProgressDialog(mContext, R.style.ProgressDialogTheme);
        pDialog.setMessage("" + LBL_LOADING_TXT_str);
        // pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject me,
                            GraphResponse response) {
                        // Application code
                        if (response.getError() != null) {
                            // handle error
//                                Log.d("onError","onError:" + response.getError());
                            pDialog.dismiss();
                            Toast.makeText(mContext, "" + LBL_ERROR_OCCURED_str, Toast.LENGTH_SHORT).show();
                        } else {

                            try {
                                String email_str = "";
                                if (me.has("email")) {
                                    email_str = me.getString("email");
                                }
//                                String name_str = me.getString("name");
                                String first_name_str = me.getString("first_name");
                                String last_name_str = me.getString("last_name");
                                String fb_id_str = me.getString("id");

                                new registerFBUser(email_str, first_name_str, last_name_str, fb_id_str).execute();
                            } catch (Exception e) {

                            }
                        }

                        LoginManager.getInstance().logOut();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {
        Log.d("onCancel", "onCancel");
    }

    @Override
    public void onError(FacebookException e) {
        Log.d("onError", "onError:" + e);
    }

    public void getLanguageLabelsFrmSqLite() {

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

        cursor.moveToPosition(0);

        language_labels_get_frm_sqLite = cursor.getString(0);

        JSONObject obj_language_labels = null;
        try {
            obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
            LBL_LOADING_TXT_str = obj_language_labels.getString("LBL_LOADING_TXT");
            LBL_ERROR_OCCURED_str = obj_language_labels.getString("LBL_ERROR_OCCURED");
            LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str = obj_language_labels.getString("LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER");
            LBL_SERVICE_NOT_AVAIL_TXT_str = obj_language_labels.getString("LBL_SERVICE_NOT_AVAIL_TXT");
            LBL_ACC_DELETE_TXT_str = obj_language_labels.getString("LBL_ACC_DELETE_TXT");
            LBL_ERROR_TXT_str = obj_language_labels.getString("LBL_ERROR_TXT");
            LBL_BTN_OK_TXT_str = obj_language_labels.getString("LBL_BTN_OK_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public class registerFBUser extends AsyncTask<String, String, String> {

        String email_str = "", fName_str = "", lName_str = "", fbId_str = "", responseString = "", GCMregistrationId = "";


        public registerFBUser(String email_str, String fName_str, String lName_str, String fbId_str) {
            this.email_str = email_str;
            this.fName_str = fName_str;
            this.lName_str = lName_str;
            this.fbId_str = fbId_str;
        }

        public void processGCMID_user() throws IOException {
            InstanceID instanceID = InstanceID.getInstance(mContext);
            GCMregistrationId = instanceID.getToken(CommonUtilities.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null);
            // Log.d("GCMregistrationId::", ":GCMregistrationId:" +
            // GCMregistrationId);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        public String loginWithFb() {
            String result = "";
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&fbid=%s&Fname=%s&Lname=%s&email=%s&GCMID=%s";

            if (GCMregistrationId == null || GCMregistrationId.trim().equals("")) {
                GCMregistrationId = "";
            }

            String registerUrl = baseUrl
                    + String.format(registerParam, "LoginWithFB", "" + fbId_str,
                    URLEncoder.encode(fName_str), URLEncoder.encode("" + lName_str), "" + URLEncoder.encode("" + email_str), "" + GCMregistrationId);

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(registerUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = OutputStreamReader.readStream(in);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected String doInBackground(String... strings) {
//            LoginWithFB

            try {
                if (checkPlayServices() == true) {
                    processGCMID_user();
                    responseString = loginWithFb();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pDialog.dismiss();

            if (GCMregistrationId != null && !GCMregistrationId.equals("")) {

                if (responseString == null || responseString.trim().equals("")) {
//                    Toast.makeText(mContext, "" + LBL_ERROR_OCCURED_str, Toast.LENGTH_SHORT).show();

                    showMessage(LBL_ERROR_TXT_str, LBL_ERROR_OCCURED_str, false);
                } else if (responseString.trim().equals("LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER")) {

//                    Toast.makeText(mContext, "" + LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str, Toast.LENGTH_SHORT).show();
                    showMessage(LBL_ERROR_TXT_str, LBL_CONTACT_US_STATUS_NOTACTIVE_PASSENGER_str, false);
                } else if (responseString.trim().equals("ACC_DELETED")) {

                    showMessage(LBL_ERROR_TXT_str, LBL_ACC_DELETE_TXT_str, false);
                } else if (responseString.trim().equals("DO_REGISTER")) {

                    //Register

                    Intent send_data = new Intent(mContext, VerifyProfileActivity.class);

                    Bundle bn_send_data = new Bundle();
                    bn_send_data.putString("ID", fbId_str);
                    bn_send_data.putString("Fname", fName_str);
                    bn_send_data.putString("Lname", lName_str);
                    bn_send_data.putString("Email", email_str);
                    bn_send_data.putString("GCMregistrationId", "" + GCMregistrationId);

                    send_data.putExtras(bn_send_data);

                    mContext.startActivity(send_data);

                } else {

                    try {

                        JSONObject jsonObject_userDetail = new JSONObject(responseString);
                        String changeLangCode_str = jsonObject_userDetail.getString("changeLangCode");

                        if (changeLangCode_str.equals("Yes")) {
                            String UpdatedLanguageLabels_str = jsonObject_userDetail.getString("UpdatedLanguageLabels");
                            updateLangLabels(UpdatedLanguageLabels_str);
                        }

                        String ProfileData_str = jsonObject_userDetail.getString("ProfileData");

                        new OpenMainProfile(mContext, ProfileData_str).run();
                        // login
                        ActivityCompat.finishAffinity((Activity) mContext);

                    } catch (Exception e) {

                    }

                }

            } else {
                Toast.makeText(mContext, "" + LBL_SERVICE_NOT_AVAIL_TXT_str, Toast.LENGTH_LONG).show();
            }

        }
    }

    public boolean checkPlayServices() {
        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        final int result = googleAPI.isGooglePlayServicesAvailable(mContext);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {

                ((Activity) mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        googleAPI.getErrorDialog((Activity) mContext, result,
                                CommonUtilities.PLAY_SERVICES_RESOLUTION_REQUEST).show();
                    }
                });

            }

            return false;
        }

        return true;
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

    public void showMessage(String title_str, String content_str, final boolean close) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                mContext);

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

        if (close == true) {
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
        }

        alertDialog.show();

    }
}

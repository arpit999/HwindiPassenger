package com.mainProfile.classFiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;

import com.hwindiapp.passenger.Mainprofile_Activity;
import com.hwindiapp.passenger.User_to_Driver_ratingActivity;
import com.utils.CommonUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by Admin on 18-02-2016.
 */
public class OpenMainProfile implements Runnable{

    Context mContext;
    String responseString="";
    public OpenMainProfile(Context mContext,String responseString) {
        this.mContext=mContext;
        this.responseString = responseString;
    }

    @Override
    public void run() {

        mContext.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
        mContext.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));

        JSONObject jsonObject_userDetail = null;
        JSONObject obj_Last_trip_data = null;
        String PaymentStatus_From_Passenger_str = "";
        String Ratings_From_Passenger_str = "";
        String vTripPaymentMode_str = "";
        String eVerified_str = "";
        try {
            jsonObject_userDetail = new JSONObject(responseString);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // Bitmap bmp = null;
        try {
            String user_id = jsonObject_userDetail.getString(CommonUtilities.iUserId);
            String id = jsonObject_userDetail.getString(CommonUtilities.vFbId);
            String name = jsonObject_userDetail.getString(CommonUtilities.vName);
            String vLastName_str = jsonObject_userDetail.getString("vLastName");
            String vImgName_str = jsonObject_userDetail.getString("vImgName");
            String vLang_str = jsonObject_userDetail.getString("vLang");

            String email = jsonObject_userDetail.getString(CommonUtilities.vEmail);
            String gender = jsonObject_userDetail.getString(CommonUtilities.eGender);
            String vTripStatus = jsonObject_userDetail.getString(CommonUtilities.vTripStatus);


            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("access_sign_token_email", email);

            editor.putString("selected_language_code", "" + vLang_str);

            editor.putString("access_sign_token_user_id_auto", user_id);
            editor.commit();

            if (vTripStatus.equals("Not Active")) {
                obj_Last_trip_data = jsonObject_userDetail.getJSONObject("Last_trip_data");

                PaymentStatus_From_Passenger_str = jsonObject_userDetail
                        .getString("PaymentStatus_From_Passenger");
                Ratings_From_Passenger_str = jsonObject_userDetail.getString("Ratings_From_Passenger");
                eVerified_str = obj_Last_trip_data.getString("eVerified");
                vTripPaymentMode_str = obj_Last_trip_data.getString("vTripPaymentMode");
            }

            // Not Requesting
            if (!vTripStatus.equals("Not Active") || ((PaymentStatus_From_Passenger_str.equals("Approved")
                    || vTripPaymentMode_str.equals("Cash")) && Ratings_From_Passenger_str.equals("Done")
                    && eVerified_str.equals("Verified"))) {
                // byte[] byteArray = stream.toByteArray();
                Intent send_data = new Intent(mContext,Mainprofile_Activity.class);

                Bundle bn_send_data = new Bundle();
                bn_send_data.putString("user_id", user_id);
                bn_send_data.putString("ID", id);
                bn_send_data.putString("Name", name + " " + vLastName_str);
                bn_send_data.putString("Email", email);
                bn_send_data.putString("Gender", gender);
                bn_send_data.putString("json_responseString", responseString);
                bn_send_data.putString("vTripStatus", "" + vTripStatus);
                // bn_send_data.putByteArray("fProfile_image_bitmap",
                // byteArray);
                bn_send_data.putString("fProfile_image_bitmap", vImgName_str);
                send_data.putExtras(bn_send_data);

                mContext.startActivity(send_data);
            } else {

                String driverId = obj_Last_trip_data.getString("iDriverId");

                String driver_img_last_trip_name_str = obj_Last_trip_data.getString("driver_img_lastTrip");

                String driverImgURL = CommonUtilities.SERVER_URL_PHOTOS + "upload/Driver/"
                        + URLEncoder.encode(driverId) + "/" + driver_img_last_trip_name_str;

                if (driver_img_last_trip_name_str.length() == 0
                        || driver_img_last_trip_name_str.equals("NONE")
                        || driver_img_last_trip_name_str.equals("null")
                        || driver_img_last_trip_name_str.equals("")) {
                    // Log.d("img_name:img_name:", ""+img_name);
                    driverImgURL = "NONE";

                }

                String trip_id_final = obj_Last_trip_data.getString("iTripId");
                String cashPayment = obj_Last_trip_data.getString("vTripPaymentMode");
                String trip_number = obj_Last_trip_data.getString("iVerificationCode");

                String payPalEnabledORNOT = jsonObject_userDetail.getString("PayPalConfiguration");

                Intent startRating_page = new Intent(mContext,User_to_Driver_ratingActivity.class);
                startRating_page.putExtra("driver_img_url", driverImgURL);
                startRating_page.putExtra("user_id", user_id);
                startRating_page.putExtra("driver_id", driverId);
                startRating_page.putExtra("trip_id_final", trip_id_final);
                startRating_page.putExtra("cash_payment", "" + cashPayment);
                // startRating_page.putExtra("yourFare", "" +
                // totalFare_int);
                startRating_page.putExtra("trip_number", trip_number);
                startRating_page.putExtra("fromMainProfile", "false");
                startRating_page.putExtra("profile_json", "" + responseString);
                startRating_page.putExtra("paymentEnabledOrNot", payPalEnabledORNOT);

                mContext.startActivity(startRating_page);

            }

            ActivityCompat.finishAffinity((Activity)mContext);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

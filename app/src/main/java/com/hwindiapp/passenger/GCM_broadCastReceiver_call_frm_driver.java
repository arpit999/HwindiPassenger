package com.hwindiapp.passenger;

import java.util.List;

import org.json.JSONObject;

import com.listAdapter.files.Message_user_driver;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.PowerManager;
import android.util.Log;
import android.widget.ArrayAdapter;

public class GCM_broadCastReceiver_call_frm_driver extends BroadcastReceiver {

	Mainprofile_Activity main_profile_activity = null;
	UserTODriverMessagingActivity user_driver_messaging_activity = null;

	void setMainProfileActivityHandler(Mainprofile_Activity main_profile_activity) {
		this.main_profile_activity = main_profile_activity;
	}

	void setUserToDriverMessagingActivityHandler(UserTODriverMessagingActivity user_driver_messaging_activity) {
		this.user_driver_messaging_activity = user_driver_messaging_activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals("com.hwindiapp.passenger.DriverCalling")) {

			String message = intent.getExtras().getString("message_frm_driver");
//			Log.d("BroadCastmessagedriver:", "" + message);
			
			try {
				JSONObject jsonObj_temp = new JSONObject(message);
				
//				Log.d("Uber clone jsonObj_temp", "" + jsonObj_temp.getString("Message"));
				
				if (jsonObj_temp.getString("Message").equals("CabRequestAccepted")) {
					main_profile_activity.callbackFromDriver(context, message);


				} else if (jsonObj_temp.getString("Message").equals("NavigationStarted")) {
					// generateNotification_TripEnd(context,
					// "Your Trip is End Now.");
					main_profile_activity.changeMessage();
				} else if (jsonObj_temp.getString("Message").equals("TripEnd")/*message_frm_driver.contains("Trip end")*/) {
					// generateNotification_TripEnd(context,
					// "Your Trip is End Now.");
					main_profile_activity.tripEnd(context);
				}  else if (jsonObj_temp.getString("Message").equals("TripStarted")/*message_frm_driver.contains("Start Trip")*/) {
					// generateNotification_TripEnd(context,
					// "Your Trip is End Now.");

					main_profile_activity.tripStart(context);
				} else if (jsonObj_temp.getString("Message").equals("DestinationAdded")/*message_frm_driver.contains("Start Trip")*/) {
					// generateNotification_TripEnd(context,
					// "Your Trip is End Now.");

					main_profile_activity.destAddedByDriver(message);
				} else if (jsonObj_temp.getString("Message").equals("TripCancelledByDriver")/*message_frm_driver.contains("Start Trip")*/) {
					// generateNotification_TripEnd(context,
					// "Your Trip is End Now.");

					main_profile_activity.tripCanceledByDriver(message);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}

			

		}
		
	}

}

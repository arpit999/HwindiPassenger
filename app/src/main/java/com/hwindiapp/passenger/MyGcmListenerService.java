package com.hwindiapp.passenger;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

	boolean isActivityFound = false;

	private static final String TAG = "com.hwindiapp.passenger.GCM.MESSAGE";

	NotificationManager notificationmanager;
	int NOTIFICATION_ID = 45;

	/**
	 * Called when message is received.
	 *
	 * @param from
	 *            SenderID of the sender.
	 * @param data
	 *            Data bundle containing message data as key/value pairs. For
	 *            Set of keys use data.keySet().
	 */
	// [START receive_message]
	@Override
	public void onMessageReceived(String from, Bundle data) {
		String message = data.getString("message");
//		Log.d(TAG, "From: " + from);
//		Log.d(TAG, "Message: " + message);

		/**
		 * Production applications would usually process the message here. Eg: -
		 * Syncing with server. - Store message in local database. - Update UI.
		 */

		/**
		 * In some cases it may be useful to show a notification indicating to
		 * the user that a message was received.
		 */

		if (message != null && message.contains("callMessageToUser")) {
			PowerManager powerManager = (PowerManager) this.getSystemService("power");
			boolean isScreenOn = powerManager.isScreenOn();

			if (checkCurrentScreen() == true && isScreenOn == true) {
				Intent intent_broad = new Intent("com.hwindiapp.passenger.MessageFrmDriver");
				intent_broad.putExtra("message_frm_driver", message);
				this.sendBroadcast(intent_broad);
			} else {

				String message_frm_driver = message;

				String dr_id_frm_message = StringUtils.substringBefore(message_frm_driver,
						":Driver::-callMessageToUser:");
//				Log.d("DriverID", "" + dr_id_frm_message);
				String orig_message = StringUtils.substringAfter(message_frm_driver, "callMessageToUser:");

				processMessage(this, dr_id_frm_message, orig_message);

				sendNotification();

			}

		} else {
			Intent intent_broad = new Intent("com.hwindiapp.passenger.DriverCalling");
			intent_broad.putExtra("message_frm_driver", message);
			this.sendBroadcast(intent_broad);
		}

	}
	// [END receive_message]

	public boolean checkCurrentScreen() {
		ActivityManager am = (ActivityManager) this.getSystemService(this.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

		String current_screen = taskInfo.get(0).topActivity.getClassName();

		if (current_screen.equals("com.hwindiapp.passenger.UserTODriverMessagingActivity")) {

			return true;
		} else {
			return false;
		}
	}

	public void processMessage(Context context, String dr_id_frm_message, String txt_messages) {

		/*SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		final String access_sign_token_user_id_auto = mPrefs.getString("access_sign_token_user_id_auto", null);
//		Log.d("UserID", "" + access_sign_token_user_id_auto);

		String tripId = Mainprofile_Activity.trip_id_final;

		if ((tripId != null && !tripId.trim().equals("null") && !tripId.equals(""))) {
			DBConnect dbConnect = new DBConnect(context, "UberCloneMessageDB.db");

			String sql = "insert into Messages(iTripId,iDriver_id,iUser_id,tMessagesDriverUser) values('%s','%s','%s','%s')";

			sql = String.format(sql, tripId, dr_id_frm_message, access_sign_token_user_id_auto, txt_messages);

			dbConnect.execNonQuery(sql);
			dbConnect.close();
		}*/

	}

	/**
	 * Create and show a simple notification containing the received GCM
	 * message.
	 *
	 * @param message
	 *            GCM message received.
	 */
	// private void sendNotification(String message) {
	// PendingIntent pendingIntent = PendingIntent.getActivity(this,
	// 0 /* Request code */, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
	//
	// Uri defaultSoundUri =
	// RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	// NotificationCompat.Builder notificationBuilder = new
	// NotificationCompat.Builder(getApplicationContext())
	// .setSmallIcon(R.drawable.logo_img_y).setContentTitle("HARRISON
	// COUPONS").setContentText(message)
	// .setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
	//
	// NotificationManager notificationManager = (NotificationManager)
	// getSystemService(Context.NOTIFICATION_SERVICE);
	//
	// notificationManager.notify(0 /* ID of notification */,
	// notificationBuilder.build());
	// }

	public void sendNotification() {

		Notification foregroundNote = null;

		// Using RemoteViews to bind custom layouts into Notification
		Intent intent = new Intent(this, CancelNotificationReceiver.class);
		intent.putExtra("notificationId", NOTIFICATION_ID);

		// Create the PendingIntent
		PendingIntent btPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

		// Locate and set the Image into customnotificationtext.xml ImageViews

		foregroundNote = builder.setSmallIcon(R.mipmap.ic_launcher)
				// Set Ticker Message
				// .setTicker("Hello")
				// Set Title
				.setContentTitle("Driver sent you message.")
				// Set Text
				.setContentText("Open app to see message.").setPriority(2)
				// Dismiss Notification
				.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true)
				// Set PendingIntent into Notification
				.setContentIntent(btPendingIntent).build();

		foregroundNote.priority = NotificationCompat.FLAG_HIGH_PRIORITY;

		// Create Notification Manager

		if (notificationmanager != null) {
			notificationmanager = null;
		}

		notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Build Notification with Notification Manager

		notificationmanager.notify(NOTIFICATION_ID, foregroundNote);

	}

}

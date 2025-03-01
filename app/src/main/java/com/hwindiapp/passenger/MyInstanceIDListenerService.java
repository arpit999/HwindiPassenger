package com.hwindiapp.passenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.core.content.IntentCompat;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {

	private static final String TAG = "com.bbcs.RegistrationID";

	/**
	 * Called if InstanceID token is updated. This may occur if the security of
	 * the previous token had been compromised. This call is initiated by the
	 * InstanceID provider.
	 */
	// [START refresh_token]
	@Override
	public void onTokenRefresh() {
		// Fetch updated Instance ID token and notify our app's server of any
		// changes (if applicable).
		// Intent intent = new Intent(this, RegistrationIntentService.class);
		// startService(intent);

		Intent intent = new Intent();
		intent.setAction("com.GCM.RegistrationID.bbcs.main.clone");
		sendBroadcast(intent);

		logOutFromDevice();

	}
	// [END refresh_token]

	public void logOutFromDevice() {
		SharedPreferences mPrefs;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		mPrefs.edit().clear().commit();

		Intent signOut_intent_to_Main = new Intent(getApplicationContext(), Launcher_TaxiServiceActivity.class);
		signOut_intent_to_Main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		signOut_intent_to_Main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// Taxi_Service_launch_MainActivity.signOut = true;

		startActivity(signOut_intent_to_Main);

		System.gc();
	}
}

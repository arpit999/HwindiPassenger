package com.hwindiapp.passenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		// get and send location information

//		Log.d("Alrm Called", "Yes called");
		
//		mainprofile_activity.callBackFrmAlarm();
		Intent intent_broad = new Intent("com.hwindiapp.passenger.ALARMCALLED");
        intent_broad.putExtra("AlarmCalled", "called");
        context.sendBroadcast(intent_broad);
	}
}

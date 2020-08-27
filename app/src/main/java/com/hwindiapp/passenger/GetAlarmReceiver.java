package com.hwindiapp.passenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GetAlarmReceiver extends BroadcastReceiver{
	
	Mainprofile_Activity mainprofile_activity=null;
	void setMainprofileActivityHandler(
			Mainprofile_Activity mainprofile_activity) {
		this.mainprofile_activity = mainprofile_activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("com.hwindiapp.passenger.ALARMCALLED")){
//			Log.d("Alarm Receiver BroadCast Called", "In BroadCast called");
			
			mainprofile_activity.callBackFrmAlarmBroadCastReceiver();
		}
	}

}

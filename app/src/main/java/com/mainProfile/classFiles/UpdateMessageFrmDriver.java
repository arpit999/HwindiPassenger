package com.mainProfile.classFiles;

import org.apache.commons.lang.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UpdateMessageFrmDriver extends BroadcastReceiver {

	private NewMessageListner updateMessageListner;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent != null && intent.getAction().equals("com.hwindiapp.passenger.MessageFrmDriver")) {
			String message_frm_driver = intent.getStringExtra("message_frm_driver");
			
			String dr_id_frm_message = StringUtils.substringBefore(message_frm_driver, ":Driver::-callMessageToUser:");
//			Log.d("DriverID", ""+dr_id_frm_message);
			String orig_message = StringUtils.substringAfter(message_frm_driver, "callMessageToUser:");
			
			updateMessageListner.handleDriverMessageCallback(orig_message);
		}

	}

	public interface NewMessageListner {
		void handleDriverMessageCallback(String message_str);
	}

	public void setNewMessageListener(NewMessageListner NewMessageListner) {
		this.updateMessageListner = NewMessageListner;
	}

}

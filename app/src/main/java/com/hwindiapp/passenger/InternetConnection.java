package com.hwindiapp.passenger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {

	private Context context;

	public InternetConnection(Context context) {
		this.context = context;
	}

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo Wifi_Data = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		boolean isWifiConnected = Wifi_Data.isConnected();

		NetworkInfo Mobile_Data = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		boolean isMobileConnected = Mobile_Data.isConnected();

		return isWifiConnected || isMobileConnected;

	}

	public boolean check_int() {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = cm.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}

}

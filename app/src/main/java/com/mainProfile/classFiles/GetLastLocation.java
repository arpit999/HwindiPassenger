package com.mainProfile.classFiles;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

public class GetLastLocation implements ConnectionCallbacks, OnConnectionFailedListener {
	Context mContext;
	GoogleApiClient mGoogleApiClient;

	private LastLocationListner LastLocationListner;

	public GetLastLocation(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

		Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

		if (mLastLocation != null) {
			LastLocationListner.handleLastLocationListnerCallback(mLastLocation);
		} else if (mLastLocation == null) {
			LastLocationListner.handleLastLocationListnerNOVALUECallback(0);
		}

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	public interface LastLocationListner {
		void handleLastLocationListnerCallback(Location mLastLocation);

		void handleLastLocationListnerNOVALUECallback(int id);
	}

	public void setLastLocationListener(LastLocationListner LastLocationListner) {
		this.LastLocationListner = LastLocationListner;
	}
}

package com.mainProfile.classFiles;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GetLocationUpdates implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	Context mContext;

	private  int UPDATE_INTERVAL = 4000; // 10 sec
	private  int FATEST_INTERVAL = 2000; // 5 sec
	private  int DISPLACEMENT = 8; // 10 meters
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;

	private TripLocationUpdates tripLocations;

	public GetLocationUpdates(Context context, int displacement) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.DISPLACEMENT = displacement;
		buildGoogleApiClient();
		createLocationRequest();
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
		mGoogleApiClient.connect();
	}

	/**
	 * Creating location request object
	 */
	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FATEST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
	}

	/**
	 * Starting the location updates
	 */
	protected void startLocationUpdates() {

		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

	}

	/**
	 * Stopping location updates
	 */
	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
//		Log.d("Location changed", "changed");
		if (tripLocations != null) {
			tripLocations.handleTripLocationUpdatesCallback(location);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}

	}

	public void StopUpdates() {
		stopLocationUpdates();
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
//		Log.d("Get Location Updates On connected", "Called");
		if (tripLocations != null) {
			Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			tripLocations.handleTripLocationUpdatesCallback(mLastLocation);
		}
		startLocationUpdates();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	public interface TripLocationUpdates {
		void handleTripLocationUpdatesCallback(Location location);
	}

	public void setLocationUpdatesListener(TripLocationUpdates tripLocations) {
		this.tripLocations = tripLocations;
	}

}

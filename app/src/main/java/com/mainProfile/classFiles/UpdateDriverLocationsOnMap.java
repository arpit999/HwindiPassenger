package com.mainProfile.classFiles;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hwindiapp.passenger.Mainprofile_Activity;
import com.hwindiapp.passenger.R;
import com.utils.CommonUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Admin on 01-06-2016.
 */
public class UpdateDriverLocationsOnMap implements Runnable {
    GoogleMap gMap;
    Context mContext;
    String driverId = "";
    Marker marker;
    int mInterval;

    private Handler mHandler_UpdateTask;

    UpdateAssignedDriverLocation updateDriverLoc;

    Mainprofile_Activity mainProfileAct;

    public UpdateDriverLocationsOnMap(Marker marker, GoogleMap gMap, Context mContext, String driverId, int mInterval) {
        this.gMap = gMap;
        this.mContext = mContext;
        this.driverId = driverId;
        this.marker = marker;
        this.mInterval = mInterval;
        mHandler_UpdateTask = new Handler();

        if (mContext instanceof Mainprofile_Activity) {
            mainProfileAct = (Mainprofile_Activity) mContext;
        }
    }

    public void startUpdatingLocations() {
        Log.d("Start", "Driver Loc");
        MarkerOptions markerOptions_driver = new MarkerOptions();
        markerOptions_driver.position(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));

        markerOptions_driver.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_driver)).anchor(0.5f, 0.5f);

        marker = null;
        marker = gMap.addMarker(markerOptions_driver);
        stopUdatingLocation();
        this.run();
    }

    @Override
    public void run() {
//        Log.d("driverLocUpdateTask", "Run");
        if (updateDriverLoc != null) {
            updateDriverLoc.cancel(true);
            updateDriverLoc = null;
        }
        updateDriverLoc = new UpdateAssignedDriverLocation();
        updateDriverLoc.execute();

        mHandler_UpdateTask.postDelayed(this, mInterval);
    }

    public void stopUdatingLocation() {
        Log.d("Stop", "Loc Update");
        mHandler_UpdateTask.removeCallbacks(this);
    }

    public class UpdateAssignedDriverLocation extends AsyncTask<String, String, String> {
        String driverLocation_json = "";
        boolean errorConnection = false;

        public String getDriverLocation() {
            // String baseUrl = CommonUtilities.SERVER_URL_GET_DETAIL;
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&DriverId=%s";
            String responseString = "";

            if (URLEncoder.encode(driverId) != null) {

                String registerUrl = baseUrl + String.format(registerParam, "getAssignedDriverLocation", driverId);

//                Log.d("Assigned", ":: Driver loc:" + registerUrl);
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(registerUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    responseString = OutputStreamReader.readStream(in);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    errorConnection = true;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }

                }
            }
            return responseString;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            driverLocation_json = getDriverLocation();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (errorConnection == false) {
//                Log.d("parsing","::");
                processJson_driver_loction(driverLocation_json);
            }

        }

    }

    public void processJson_driver_loction(String json_driver_location) {

        JSONObject getLocations = null;
        String dLatitude = "";
        String dLongitude = "";
        try {
            getLocations = new JSONObject(json_driver_location);
            dLatitude = getLocations.getString("DLatitude");
            dLongitude = getLocations.getString("DLongitude");


            if (mainProfileAct != null) {
                mainProfileAct.setDriverLocation(Double.parseDouble(dLatitude), Double.parseDouble(dLongitude));
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (getLocations != null) {

            double driver_loc_latitude = Double.parseDouble(dLatitude);
            double driver_loc_longitude = Double.parseDouble(dLongitude);

            animateMarker(marker, new LatLng(driver_loc_latitude, driver_loc_longitude), false);

        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = gMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }
}

package com.utils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import android.app.Activity;

public final class CommonUtilities {
	/* new local */

//	public static final String SERVER_URL_PAYMENT_PAYPAL = "http://192.168.1.131/hwindi";
//	public static final String SERVER_URL = "http://192.168.1.131/hwindi/webservices.php?";
//	public static final String SERVER_URL_PHOTOS = "http://192.168.1.131/hwindi/webimages/";

	/* new local finished */
	/*webservices_23052016_trial*/
	public static final String SERVER_URL_PAYMENT_PAYPAL = "http://hwindi.com/hail";
	public static final String SERVER_URL = "http://hwindi.com/hail/webservices.php?";
	public static final String SERVER_URL_PHOTOS = "http://hwindi.com/hail/webimages/";

	public static String storedImageFolderName = "/HwindiPassenger/ProfileImage";
	public static final String SERVER_URL_ICON_HELP_PAGE = SERVER_URL_PHOTOS + "icons/help/";

	public static final String SERVER_URL_LABELS_DEFAULT = SERVER_URL + "type=language_label";

	public static final String SERVER_URL_LANGUAGE_LIST = SERVER_URL + "type=Language";

	public static final String SERVER_URL_LINK_CONFIGURATION = SERVER_URL
			+ "type=GetLinksConfiguration&UserType=Passenger";

	public static final String SERVER_URL_EASTIMATE_CALC_FARE = SERVER_URL
			+ "type=estimateFare&city=%s&distance=%s&time=%s&SelectedCar=%s&iUserId=%s";

	public static final String GOOGLE_SERVER_URL_ROUTE = "http://maps.googleapis.com/maps/api/directions/json?";

	public static final String GOOGLE_SERVER_URL_GET_ADDRESS = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s";

	public static String SENDER_ID = "";

	public static String FB_PROJECT_ID = "";

	// Paypal configuration ID

//	public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
	public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

	public static String CONFIG_CLIENT_ID = "";

	public static final String DISPLAY_TAG = "Hwindi";
	
	public static final float defaultZommLevel = (float) 16.5;

	public static final String iUserId = "iUserId";
	public static final String vFbId = "vFbId";
	public static final String vName = "vName";
	public static final String vEmail = "vEmail";
	public static final String vPhone = "vPhone";
	public static final String eGender = "eGender";
	public static final String vCreditCard = "vCreditCard";
	public static final String vExpMonth = "vExpMonth";
	public static final String vExpYear = "vExpYear";
	public static final String vCvv = "vCvv";
	public static final String vPasswordUser = "vPassword";
	public static final String vImgName = "vImgName";
	public static final String vAvgRating = "vAvgRating";
	public static final String vLogoutDev = "vLogoutDev";
	public static final String iGcmRegId = "iGcmRegId";
	public static final String vCallFromDriver = "vCallFromDriver";
	public static final String iTripId = "iTripId";
	public static final String vTripStatus = "vTripStatus";

	/* USER DATABASE FEILDS FOR JSON PARSING FINISHED */

	/* DRIVER DATABASE FEILDS FOR JSON PARSING */

	public static final String iDriverId = "iDriverId";
	public static final String vName_driver = "vName";
	public static final String vLoginId = "vLoginId";
	public static final String vPassword = "vPassword";
	public static final String vLatitude = "vLatitude";
	public static final String vLongitude = "vLongitude";
	public static final String vServiceLoc = "vServiceLoc";
	public static final String vAvailability = "vAvailability";
	public static final String vCarType = "vCarType";
	public static final String vAvgRating_driver = "vAvgRating";
	public static final String iGcmRegId_driver = "iGcmRegId";
	public static final String vDriverImg = "vImage";
	public static final String vDriverEmailId = "vDriverEmailId";
	public static final String vCreditCard_driver = "vCreditCard";
	public static final String vExpMonth_driver = "vExpMonth";
	public static final String vExpYear_driver = "vExpYear";
	public static final String vCvv_driver = "vCvv";
	public static final String vPhone_driver = "vPhone";
	public static final String iTripId_driver = "iTripId";
	public static final String vTripStatus_driver = "vTripStatus";
	public static final String iDriverVehicleId = "iDriverVehicleId";
	
	
	public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	public static String MOBILENUM_VERIFICATION_ENABLE = "";

	/* DRIVER DATABASE FEILDS FOR JSON PARSING FINISHED */
	
	
}

package com.hwindiapp.passenger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.DrawRoute.Create_directionUrl;
import com.DrawRoute.GetListOFRoutes;
import com.fonts.Text.MyTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.infteh.comboseekbar.ComboSeekBar;
import com.listAdapter.files.NavDrawerItem;
import com.listAdapter.files.NavDrawerListAdapter;
import com.mainProfile.classFiles.CheckCityOfLocation;
import com.mainProfile.classFiles.DownloadProfileImg;
import com.mainProfile.classFiles.Internet_warning_dialog_run;
import com.mainProfile.classFiles.OutputStreamReader;
import com.mainProfile.classFiles.Internet_warning_dialog_run.NoInternetListner;
import com.mainProfile.classFiles.UpdateDriverLocationsOnMap;
import com.mainProfile.classFiles.Update_List_driver_nearest;
import com.slidinguppanel.SlidingUpPanelLayout;
import com.hwindiapp.passenger.customDialogs.FareDetailDialog;
import com.hwindiapp.passenger.customDialogs.RequestTaxiNearest;
import com.utils.CommonUtilities;
import com.utils.MapRelativeLayout;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

@SuppressWarnings("ResourceType")
@SuppressLint("NewApi")
public class Mainprofile_Activity extends AppCompatActivity implements /* OnSlideMenuItemClickListener, */
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener, OnMapReadyCallback, NoInternetListner {

    private Toolbar mToolbar;
    // ActionBar actionBar;
    GoogleMap map;

    int RESTRICTION_KM_NEAREST_TAXI_int = 4;
    SupportMapFragment map_fragment_profile_fm;

    int DRIVER_LOC_FETCH_TIME_INTERVAL = 8;
    int ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL = 1;
    int DESTINATION_UPDATE_TIME_INTERVAL = 2;

    DBConnect dbConnect;
    String language_labels_get_frm_sqLite = "";

    private static String APP_ID = CommonUtilities.FB_PROJECT_ID; // Replace
    // with your

    GCM_broadCastReceiver_call_frm_driver broad_DriverCallBack_receiver;

    GetAlarmReceiver get_alarm_broadcast_receiver;
    private SharedPreferences mPrefs;

    public String trip_id_final = "";
    String trip_number = "";

    String driver_image_final_url = "";
    public static String selected_driver_id = "";
    String driverId = "";
    String driver_assign_name = "";

    ArrayList<HashMap<String, String>> drivers_List = new ArrayList<HashMap<String, String>>();

    ArrayList<HashMap<String, String>> notify_driver_list = new ArrayList<HashMap<String, String>>();

    ArrayList<HashMap<String, String>> final_driver_Detail_notify = new ArrayList<HashMap<String, String>>();
    ;
    ArrayList<Marker> driver_marker = new ArrayList<Marker>();
    public static ArrayList<HashMap<String, Object>> Map_all_placesListItems = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> placesListItems = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, String>> autoplacesList = new ArrayList<HashMap<String, String>>();

    PolylineOptions lineOptions_poly_sourcetoEnd = new PolylineOptions();

    PowerManager.WakeLock Power_wl;
    PowerManager power_manager;

    int totalFare_int;

    int index_current_elements = 0;

    ListView lv_loadDest_places;

    Circle circle;

    Location location_user;

    double value_dest_latitude_loc;
    double value_dest_longitude_loc;
    String value_dest_address_loc = "";

    LatLng center;

    GetAddressFromLocation getAddress_obj = null;

    RequestTaxiNearest dialog_RequestTaxiNearest = null;

    String Address_loc = "";

    double source_lat;
    double source_lon;

    FrameLayout frame_main_map;

    RelativeLayout main_layout_profile;
    FrameLayout source_selection;
    FrameLayout source_selection_confirm;
    RelativeLayout confirm_btn_frame;

    LinearLayout locationMarker_layout;
    RelativeLayout driver_detail_layout_frame;
    View taxiSelectionLayout;

    RelativeLayout marker_source_selection_AREA;
    MyTextView locationMarkertext;

    MyTextView source_location;

    ImageView header_img;
    MyTextView confirm_source_text_header;
    ImageView img_navigation_drawer;
    ImageView back_navigation;

    MyTextView Confirm_destination_text_header;
    MyTextView select_destination_text_header;
    MyTextView confirm_source_location;

    MyTextView pick_up_txt;

    ImageView back_navigation_frm_destination;
    ImageView back_navigation_frm_confirm_destination;

    RippleStyleButton btn_confirm_location_request;
    Button add_dest_btn;

    MyTextView btn_cancle_trip;
    MyTextView btn_message_to_driver;
    RelativeLayout btn_share_container;
    LinearLayout subBtnDetail_layout;

    boolean sourceLocationConfirm = false;
    boolean DestinationLocationConfirm = false;

    boolean trip_retrival_start = false;

    boolean driver_assigned = false;

    boolean signOut = false;

    boolean fromLocationChanged = false;

    boolean tripStart = false;

    boolean tripEnd = false;

    boolean isFirstCallFrmAlarm = true;

    boolean isFirstLaunch = true;

    boolean isFirstLocUpdate = true;

    boolean active_trip_already = false;

    public static boolean language_changed = false;

    boolean onPause_animate = false;

    boolean isShow_slideMenu = false;

    int count_nearstTaxi = 0;

    public static Drawable facebook_profile_image_main;

    String id_facebook;
    public static String Name;
    String Email;
    String Gender;
    String user_id;
    String json_responseString_profile;
    String vTripStatus;
    static String vImgName_str = "";

    public static String updated_json_responseString_profile = "";
    public static boolean updateProfileTrue = false;

    private static final String TAG = Mainprofile_Activity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    Location mCurrentLocation;

    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;

    LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 2000; // 10 sec
    private static int FATEST_INTERVAL = 1000; // 5 sec
    private static int DISPLACEMENT = 4; // 10 meters

    long START_DURATION_UPDATE_USER_LOCATION = 0;
    int MAX_DURATION_UPDATE_USER_LOCATION = 5000;

    final int SELECT_DESTINATION_POINT_AFTER_TRIP_ACCEPT = 122;
    final int SELECT_PICKUP_POINT = 216;

    String tripDestinationLatitude = "Not Set";
    String tripDestinationLongitude = "Not Set";
    String tripDestinationAddress = "Not Set";
    double latitude;
    double longitude;

    double sourceLocation_latitude;
    double sourceLocation_longitude;

    double retrive_lat_Driver_assigned = 0.0;
    double retrive_lon_Driver_final_assigned = 0.0;

    Marker marker_user;
    Marker destination_point_marker;
    Marker source_point_marker;
    Marker driver_point_marker;
    Marker time_driver_marker;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public static String paymentEnabledOrNot = "";
    public String CurrencySymbol_str = "";

    ProgressBar LoadingMapProgressBar;

    Bitmap bmp_driver_pic = null;

    String retrive_driver_assign_rating = "" + 0.0;
    String retrive_car_type = "";
    String retrive_driver_name = "";

    String retrive_driver_img = "";

    String retrive_eCarGo_str = "";
    String retrive_eCarX_str = "";
    String retrive_vLicencePlate_str = "";
    String retrive_car_name_make_title_str = "";
    String retrive_car_model_title_str = "";
    String retrive_driver_phone_str = "";

    String LBL_SOURCE_CONFIRM_HEADER_TXT_str = "";
    String LBL_SELECT_DESTINATION_HEADER_TXT_str = "";
    String LBL_CONFIRM_DEST_HEADER_TXT_str = "";
    String LBL_PICKUP_LOCATION_HEADER_TXT_str = "";
    String LBL_SET_PICK_UP_LOCATION_TXT_str = "";
    String LBL_BTN_REQUEST_PICKUP_TXT_str = "";
    String LBL_TAXI_SELECTION_GO_TXT_str = "";
    String LBL_TAXI_SELECTION_X_TXT_str = "";
    String LBL_TAXI_SELECTION_FAMILY_TXT_str = "";
    String LBL_BTN_CONFIRM_DEST_TXT_str = "";
    String LBL_BTN_CANCEL_TRIP_TXT_str = "";
    String LBL_BTN_MESSAGE_TXT_str = "";
    String LBL_SEARCH_PLACE_HINT_TXT_str = "";
    String LBL_MY_PROFILE_HEADER_TXT_str = "";
    String LBL_BOOKING_HISTROY_TXT_str = "";
    String LBL_INVITE_PAGE_TITLE_TXT_str = "";
    String LBL_REFERRAL_POINTS_H_TXT_str = "";
    String LBL_ABOUT_US_TXT_str = "";
    String LBL_CONTACT_US_TXT_str = "";
    String LBL_HELP_TXT_str = "";
    String LBL_SIGNOUT_TXT_str = "";
    String LBL_LOADING_REQUEST_RIDE_TXT_str = "";
    String LBL_LOADING_PLACE_TXT_str = "";
    String LBL_NO_DRIVER_AVAIL_TXT_str = "";
    String LBL_SORRY_PROBLEM_TXT_str = "";
    String LBL_LOADING_CANCEL_TRIP_TXT_str = "";
    String LBL_ERROR_OCCURED_str = "";
    String LBL_FAILED_TRIP_CANCEL_str = "";
    String LBL_INFO_SET_TXT_str = "";
    String LBL_START_TRIP_DIALOG_TXT_str = "";
    String LBL_END_TRIP_DIALOG_TXT_str = "";
    String LBL_DEVICE_NOT_SUPPORTED_TXT_str = "";
    String LBL_TRIP_CANCEL_TXT_str = "";
    String LBL_BTN_TRIP_CANCEL_CONFIRM_TXT_str = "";
    String LBL_BTN_OK_TXT_str = "";
    String LBL_SELECTING_LOCATION_TXT_str = "";
    String LBL_CANCEL_BTN_TXT_NO_INTERNET_str = "";
    String LBL_REQUEST_CANCEL_FAILED_TXT_str = "";
    String LBL_ETA_TXT_str = "";
    String LBL_MAX_SIZE_TXT_str = "";
    String LBL_MIN_FARE_TXT_str = "";
    String LBL_GET_FARE_EST_TXT_str = "";
    String LBL_PEOPLE_TXT_str = "";
    String LBL_CASH_TXT_str = "";
    String LBL_PAYPAL_TXT_str = "";
    String LBL_FARE_ESTIMATE_TXT_str = "";
    String LBL_PRMO_TXT_str = "";
    String LBL_MIN_SMALL_TXT_str = "";
    String LBL_NO_CAR_AVAIL_TXT_str = "";
    String LBL_EN_ROUTE_TXT_str = "";
    String LBL_CALL_TXT_str = "";
    String LBL_MESSAGE_TXT_str = "";
    String LBL_ARRIVING_TXT_str = "";
    String LBL_ON_TRIP_TXT_str = "";
    String LBL_SEND_STATUS_CONTENT_TXT_str = "";
    String LBL_ACT_RESULTS_CANCEL_TXT_str = "";
    String LBL_CHAT_TXT_str = "";
    String LBL_SEARCH_CAR_WAIT_TXT_str = "";
    String LBL_SHARE_BTN_TXT_str = "";
    String LBL_PAYPAL_DISABLE_TXT_str = "";
    String LBL_DRIVER_ARRIVING_TXT_str = "";
    String LBL_TRIP_CONFIRMED_TXT_str = "";
    String LBL_DRIVER_ARRIVED_TXT_str = "";
    String LBL_ADD_DESTINATION_BTN_TXT_str = "";
    String LBL_SOME_ERROR_TXT_str = "";

    NotificationManager notificationmanager;
    int NOTIFICATION_ID = 1;

    int timeParKM_in_MINUTE = 2;

    int DRIVER_ARRIVED_MIN_TIME_PER_MINUTE_int = 3;
    double lowestKM = 0;
    boolean lowestKM_firstCall = true;
    MyTextView txt_time_nearest_taxi;

    MyTextView confirm_dest_location;
    ImageView line_start_end_point_img;

    MyTextView txt_min_fare_value;
    MyTextView txt_time_eta;
    MyTextView txt_size_person;

    MyTextView driving_status_text_header;

    MapRelativeLayout map_root_layout;

    SlidingUpPanelLayout sliding_layout;

    ImageView selection_cash_img;
    ImageView selection_paypal_img;

    boolean cashPayment = true;

    MyTextView txt_btn_get_fare_estimate;

    ComboSeekBar csb;

    List<HashMap<String, String>> carTypeDetailList = new ArrayList<HashMap<String, String>>();
    int selectedCarType = 1;

    String Selected_iBaseFare_str = "";
    String Selected_fPricePerMin_str = "";
    String Selected_fPricePerKM_str = "";
    String Selected_vVehicleType_str = "";
    String Selected_vVehicle_Person_size_str = "";

    String currentCityofLocation = "";

    String driverCarNumberPlate_str = "";
    String driverMobileNumber_str = "";

    CheckCityOfLocation checkCityOfLocation;
    Update_List_driver_nearest update_list_driver_nearest;

    FareDetailDialog faredetailDialog;

    PolylineOptions route_to_destination_polyLine_options;

    getRoute_GOOGLE destination_Point_getRoute_GOOGLE;

    RelativeLayout btn_contact_container_assigned_driver;
    MyTextView btn_share_txt;
    MyTextView share_btn_txt;

    int PICK_CONTACT = 21212;

    MyTextView txt_time_eta_header;
    MyTextView txt_size_person_header;
    MyTextView txt_min_fare_header;
    MyTextView txt_cash;
    MyTextView txt_online_payment;
    MyTextView txt_fare_est;
    MyTextView txt_promo;
    MyTextView addDestinationTxtView;

    androidx.appcompat.app.AlertDialog alert_problem;
    androidx.appcompat.app.AlertDialog generate_alert;
    androidx.appcompat.app.AlertDialog tripCancelByDriver;

    InternetConnection intCheck;
    Internet_warning_dialog_run no_internet_dialog;

    private DrawerLayout mDrawerLayout;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter NavDrawerAdapter;
    private ListView mDrawerList;

    static RoundedImageView profile_header_img;
    MyTextView profile_header_name;

    int notification_count = 0;

    long previousNotificationTime = 0;

    RelativeLayout online_payment_mode_area;

    MyTextView refPointsHtxt;
    MyTextView refPointsVtxt;

    String vAverageRating = "0.0";
    String vPhone_passeneger = "";
    String vPhoneCode_passeneger = "";

    boolean driverArrived = false;

    UpdateDriverLocationsOnMap updateDriverLocAfterStartTrip;

    double driverLoc_latitude = 0.0;
    double driverLoc_longitude = 0.0;

    ImageView goToMyLocImg;
    EditText et_promocode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainprofile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            String restratValue_str = savedInstanceState.getString("RESTART_STATE");

            if (restratValue_str != null && !restratValue_str.equals("") && restratValue_str.trim().equals("true")) {
                Intent send_data = new Intent(Mainprofile_Activity.this, Launcher_TaxiServiceActivity.class);
                send_data.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(send_data);
            }
        }


        isFirstLaunch = true;

        updateProfileTrue = false;
        updated_json_responseString_profile = "";

        mLocationRequest = new LocationRequest();

        dbConnect = new DBConnect(this, "UC_labels.db");

        mPrefs = PreferenceManager.getDefaultSharedPreferences(Mainprofile_Activity.this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.list_menu);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        profile_header_img = (RoundedImageView) findViewById(R.id.profile_header_img);
        profile_header_name = (MyTextView) findViewById(R.id.profile_header_name);
        et_promocode = (EditText) findViewById(R.id.et_promocode);

		/* Initialize Receiver to receive call back from driver */

        broad_DriverCallBack_receiver = new GCM_broadCastReceiver_call_frm_driver();
        broad_DriverCallBack_receiver.setMainProfileActivityHandler(this);

        get_alarm_broadcast_receiver = new GetAlarmReceiver();
        get_alarm_broadcast_receiver.setMainprofileActivityHandler(this);

        intCheck = new InternetConnection(this);

        no_internet_dialog = new Internet_warning_dialog_run(false, Mainprofile_Activity.this);
        no_internet_dialog.setListener(Mainprofile_Activity.this);

		/* Initialization of receiver is finished */
        power_manager = (PowerManager) getSystemService(this.POWER_SERVICE);
        Power_wl = power_manager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
                "com.hwindiapp.passenger");

        LoadingMapProgressBar = (ProgressBar) findViewById(R.id.LoadingMapProgressBar);

        main_layout_profile = (RelativeLayout) findViewById(R.id.main_layout_profile);
        taxiSelectionLayout = (View) findViewById(R.id.taxiSelection);

        driver_detail_layout_frame = (RelativeLayout) findViewById(R.id.driver_detail_layout_frame);

        add_dest_btn = (Button) findViewById(R.id.add_dest_btn);

        header_img = (ImageView) findViewById(R.id.header_img);
        goToMyLocImg = (ImageView) findViewById(R.id.goToMyLocImg);
        confirm_source_text_header = (MyTextView) findViewById(R.id.confirm_source_text_header);
        refPointsHtxt = (MyTextView) findViewById(R.id.refPointsHtxt);
        refPointsVtxt = (MyTextView) findViewById(R.id.refPointsVtxt);
        addDestinationTxtView = (MyTextView) findViewById(R.id.addDestinationTxtView);

        back_navigation = (ImageView) findViewById(R.id.back_navigation);

        Confirm_destination_text_header = (MyTextView) findViewById(R.id.Confirm_destination_text_header);
        select_destination_text_header = (MyTextView) findViewById(R.id.select_destination_text_header);
        back_navigation_frm_destination = (ImageView) findViewById(R.id.back_navigation_frm_destination);
        back_navigation_frm_confirm_destination = (ImageView) findViewById(R.id.back_navigation_frm_confirm_destination);
        marker_source_selection_AREA = (RelativeLayout) findViewById(R.id.location_selection_area);
        locationMarkertext = (MyTextView) findViewById(R.id.locationMarkertext);
        locationMarker_layout = (LinearLayout) findViewById(R.id.locationMarker);
        source_location = (MyTextView) findViewById(R.id.source_location);
        frame_main_map = (FrameLayout) findViewById(R.id.frame_main_map);
        source_selection = (FrameLayout) findViewById(R.id.source_selection);
        source_selection_confirm = (FrameLayout) findViewById(R.id.source_selection_confirm);

        confirm_btn_frame = (RelativeLayout) findViewById(R.id.confirm_btn_frame);

        confirm_source_location = (MyTextView) findViewById(R.id.confirm_source_location);
        btn_confirm_location_request = (RippleStyleButton) findViewById(R.id.btn_confirm_location_request);

        btn_cancle_trip = (MyTextView) findViewById(R.id.btn_cancle_trip);
        btn_message_to_driver = (MyTextView) findViewById(R.id.btn_message_driver);

        btn_share_container = (RelativeLayout) findViewById(R.id.btn_share_container);
        subBtnDetail_layout = (LinearLayout) findViewById(R.id.subBtnDetail_layout);

        pick_up_txt = (MyTextView) findViewById(R.id.pick_up_txt);

        txt_time_nearest_taxi = (MyTextView) findViewById(R.id.txt_time_nearest_taxi);

        confirm_dest_location = (MyTextView) findViewById(R.id.confirm_dest_location);
        line_start_end_point_img = (ImageView) findViewById(R.id.line_start_end_point_img);

        map_root_layout = (MapRelativeLayout) findViewById(R.id.map_root_layout);
        map_root_layout.setMainProfileHandler(this);

        sliding_layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        txt_min_fare_value = (MyTextView) findViewById(R.id.txt_min_fare_value);
        txt_time_eta = (MyTextView) findViewById(R.id.txt_time_eta);
        txt_size_person = (MyTextView) findViewById(R.id.txt_size_person);

        online_payment_mode_area = (RelativeLayout) findViewById(R.id.online_payment_mode_area);
        selection_cash_img = (ImageView) findViewById(R.id.selection_cash_img);
        selection_paypal_img = (ImageView) findViewById(R.id.selection_paypal_img);
        txt_btn_get_fare_estimate = (MyTextView) findViewById(R.id.txt_btn_get_fare_estimate);
        driving_status_text_header = (MyTextView) findViewById(R.id.driving_status_text_header);

        csb = (ComboSeekBar) findViewById(R.id.seekbar);

        btn_contact_container_assigned_driver = (RelativeLayout) findViewById(R.id.btn_contact_container);
        btn_share_txt = (MyTextView) findViewById(R.id.btn_share_txt);
        share_btn_txt = (MyTextView) findViewById(R.id.share_btn);

        txt_time_eta_header = (MyTextView) findViewById(R.id.txt_time_eta_header);
        txt_size_person_header = (MyTextView) findViewById(R.id.txt_size_person_header);
        txt_min_fare_header = (MyTextView) findViewById(R.id.txt_min_fare_header);
        txt_cash = (MyTextView) findViewById(R.id.txt_cash);
        txt_online_payment = (MyTextView) findViewById(R.id.txt_online_payment);
        txt_fare_est = (MyTextView) findViewById(R.id.txt_fare_est);
        txt_promo = (MyTextView) findViewById(R.id.txt_promo);

		/* Set Labels */
        getLanguageLabelsFrmSqLite();
        /* Set Labels Finished */

        Bundle bundle = getIntent().getExtras();
        id_facebook = bundle.getString("ID");
        Name = bundle.getString("Name");
        Email = bundle.getString("Email");
        Gender = bundle.getString("Gender");
        user_id = bundle.getString("user_id");
        json_responseString_profile = bundle.getString("json_responseString");
        vTripStatus = bundle.getString("vTripStatus");
        vImgName_str = bundle.getString("fProfile_image_bitmap");

        Name = Name.replace("@", "");

        setConfigurationData();

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        // setNoCarConfigData();
        setSearchCarConfigData();

        checkProfileImage();
        CreateSlideMenu();

        img_navigation_drawer = (ImageView) findViewById(R.id.img_navigation_drawer);
        img_navigation_drawer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // slidemenu.show();
                // isShow_slideMenu = true;

                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT) == true) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        map_fragment_profile_fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapV2);

        map_fragment_profile_fm.getMapAsync(Mainprofile_Activity.this);

        marker_source_selection_AREA.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {

                    marker_source_selection_AREA.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                float scale = getResources().getDisplayMetrics().density;

                int dp_sliding_panel_height = (int) (221 * scale + 0.5f);
                sliding_layout.setPanelHeight(dp_sliding_panel_height);

                source_selection.setVisibility(View.GONE);

                taxiSelectionLayout.setVisibility(View.GONE);

                header_img.setVisibility(View.GONE);
                img_navigation_drawer.setVisibility(View.GONE);
                back_navigation.setVisibility(View.VISIBLE);
                confirm_source_text_header.setVisibility(View.VISIBLE);

                confirm_source_location.setText(Address_loc);

                source_selection_confirm.setVisibility(View.VISIBLE);
                confirm_btn_frame.setVisibility(View.VISIBLE);

                add_dest_btn.setVisibility(View.VISIBLE);
                confirm_dest_location.setVisibility(View.GONE);
                line_start_end_point_img.setVisibility(View.GONE);
                confirm_dest_location.setText("");

                int padding_dp = (int) (15 * scale + 0.5f);
                confirm_source_location.setPadding(padding_dp, 0, 0, 0);
                confirm_source_location.setCompoundDrawablesWithIntrinsicBounds(
                        getResources().getDrawable(R.drawable.green_source), null, null, null);

            }
        });

        back_navigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                setPanelHeightToDefault();

                header_img.setVisibility(View.VISIBLE);
                img_navigation_drawer.setVisibility(View.VISIBLE);
                back_navigation.setVisibility(View.GONE);
                confirm_source_text_header.setVisibility(View.GONE);
                source_selection_confirm.setVisibility(View.GONE);

                confirm_btn_frame.setVisibility(View.GONE);
                taxiSelectionLayout.setVisibility(View.VISIBLE);
                marker_source_selection_AREA.setVisibility(View.VISIBLE);
                source_selection.setVisibility(View.VISIBLE);

                sourceLocationConfirm = false;
                DestinationLocationConfirm = false;

                if (destination_point_marker != null) {
                    destination_point_marker.remove();
                    destination_point_marker = null;
                }
            }
        });

        btn_confirm_location_request.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

                    no_internet_dialog.run();
                } else {
                    RetryOnSourcePointSelect();
                }

            }
        });


        source_location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mainprofile_Activity.this, SelectDestinationActivity.class);
                intent.putExtra("Selected_latitude", "Not Set");
                intent.putExtra("Selected_longitude", "Not Set");
                intent.putExtra("Selected_address", "Not Set");
                intent.putExtra("isSourceLocation", "true");
                startActivityForResult(intent, SELECT_PICKUP_POINT);
            }
        });

        goToMyLocImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (latitude != 0.0 && longitude != 0.0 && getMap() != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude))
                            .zoom(getMap().getCameraPosition().zoom).build();
                    getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });

    }

    public void RetryOnSourcePointSelect() {
        sourceLocationConfirm = true;

        dialog_RequestTaxiNearest = new RequestTaxiNearest(Mainprofile_Activity.this, Mainprofile_Activity.this,
                user_id, LBL_REQUEST_CANCEL_FAILED_TXT_str);

        dialog_RequestTaxiNearest.run();

        sourceLocation_latitude = center.latitude;
        sourceLocation_longitude = center.longitude;

        onSourcePointSelect();
    }

    public void setConfigurationData() {

        JSONObject obj_user_profile;
        try {
            obj_user_profile = new JSONObject(json_responseString_profile);
            String payPalEnabledORNOT = obj_user_profile.getString("PayPalConfiguration");
            String RESTRICTION_KM_NEAREST_TAXI_str = obj_user_profile.getString("RESTRICTION_KM_NEAREST_TAXI");
            String DRIVER_LOC_FETCH_TIME_INTERVAL_str = obj_user_profile.getString("DRIVER_LOC_FETCH_TIME_INTERVAL");
            String ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL_str = obj_user_profile.getString("ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL");
            String DESTINATION_UPDATE_TIME_INTERVAL_str = obj_user_profile.getString("DESTINATION_UPDATE_TIME_INTERVAL");

            CurrencySymbol_str = obj_user_profile.getString("CurrencySymbol");

            refPointsVtxt.setText(obj_user_profile.getString("TotalReferralPoints"));

            String DRIVER_ARRIVED_MIN_TIME_PER_MINUTE_str = obj_user_profile
                    .getString("DRIVER_ARRIVED_MIN_TIME_PER_MINUTE");
            paymentEnabledOrNot = payPalEnabledORNOT;

            if (!payPalEnabledORNOT.equals("Yes")) {
                online_payment_mode_area.setVisibility(View.VISIBLE);
            }

            if (RESTRICTION_KM_NEAREST_TAXI_str != null && !RESTRICTION_KM_NEAREST_TAXI_str.equals("")) {

                try {
                    RESTRICTION_KM_NEAREST_TAXI_int = Integer.parseInt(RESTRICTION_KM_NEAREST_TAXI_str);

                } catch (Exception e) {
                    // TODO: handle exception
                    RESTRICTION_KM_NEAREST_TAXI_int = 4;
                }

            }

            if (DRIVER_ARRIVED_MIN_TIME_PER_MINUTE_str != null && !DRIVER_ARRIVED_MIN_TIME_PER_MINUTE_str.equals("")) {

                try {
                    DRIVER_ARRIVED_MIN_TIME_PER_MINUTE_int = Integer.parseInt(DRIVER_ARRIVED_MIN_TIME_PER_MINUTE_str);

                    timeParKM_in_MINUTE = DRIVER_ARRIVED_MIN_TIME_PER_MINUTE_int;

                } catch (Exception e) {
                    // TODO: handle exception
                    DRIVER_ARRIVED_MIN_TIME_PER_MINUTE_int = 3;
                    timeParKM_in_MINUTE = DRIVER_ARRIVED_MIN_TIME_PER_MINUTE_int;
                }

            }


            if (DRIVER_LOC_FETCH_TIME_INTERVAL_str != null && !DRIVER_LOC_FETCH_TIME_INTERVAL_str.equals("")) {

                try {
                    DRIVER_LOC_FETCH_TIME_INTERVAL = Integer.parseInt(DRIVER_LOC_FETCH_TIME_INTERVAL_str);

                } catch (Exception e) {
                    // TODO: handle exception
                    DRIVER_LOC_FETCH_TIME_INTERVAL = 8;
                }

            }

            if (ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL_str != null && !ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL_str.equals("")) {

                try {
                    ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL = Integer.parseInt(ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL_str);

                } catch (Exception e) {
                    // TODO: handle exception
                    ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL = 1;
                }

            }

            if (DESTINATION_UPDATE_TIME_INTERVAL_str != null && !DESTINATION_UPDATE_TIME_INTERVAL_str.equals("")) {

                try {
                    DESTINATION_UPDATE_TIME_INTERVAL = Integer.parseInt(DESTINATION_UPDATE_TIME_INTERVAL_str);

                } catch (Exception e) {
                    // TODO: handle exception
                    DESTINATION_UPDATE_TIME_INTERVAL = 2;
                }

            }

            vAverageRating = obj_user_profile.getString("vAvgRating");
            vPhone_passeneger = obj_user_profile.getString("vPhone");
            vPhoneCode_passeneger = obj_user_profile.getString("vPhoneCode");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            vAverageRating = "0.0";
        }

        try {
            generateCarTypes(json_responseString_profile);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

//            Toast.makeText(Mainprofile_Activity.this, "Sorry for the problem. Please try again later.",
//                    Toast.LENGTH_LONG).show();

            buildAlertMessageERROR();

            return;
        }

        try {
            setGeneralConfigurationsData(json_responseString_profile);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

//            Toast.makeText(Mainprofile_Activity.this, "Sorry for the problem. Please try again later.",
//                    Toast.LENGTH_LONG).show();

            // if (alert_problem == null && alert_problem.isShowing() == false)
            // {
            buildAlertMessageERROR();
            // }

            return;
        }


//        csb.setSelection(2);
//        csb.setSelection(selectedCarType - 1);
//		Log.d("DRIVER_LOC_FETCH_TIME_INTERVAL::", "::"+DRIVER_LOC_FETCH_TIME_INTERVAL+"::"+ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL+"::"+DESTINATION_UPDATE_TIME_INTERVAL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        outState.putString("RESTART_STATE", "true");
        super.onSaveInstanceState(outState);
    }

    public void generateCarTypes(String json_responseString_profile) throws JSONException {

        if (carTypeDetailList != null && carTypeDetailList.size() > 0) {
            carTypeDetailList.clear();

        }

        JSONObject obj_user_profile = new JSONObject(json_responseString_profile);
        JSONArray obj_arr_vehivleTypes = obj_user_profile.getJSONArray("VehicleTypes");

        List<String> names_carTypes = new ArrayList<String>();

        for (int i = 0; i < obj_arr_vehivleTypes.length(); i++) {
            JSONObject obj_temp_vehicleType = obj_arr_vehivleTypes.getJSONObject(i);

            HashMap<String, String> car_type_hash_map = new HashMap<String, String>();

            String iVehicleTypeId_str = obj_temp_vehicleType.getString("iVehicleTypeId");
            String vVehicleType_str = obj_temp_vehicleType.getString("vVehicleType");
            String fPricePerKM_str = obj_temp_vehicleType.getString("fPricePerKM");
            String fPricePerMin_str = obj_temp_vehicleType.getString("fPricePerMin");
            String iBaseFare_str = obj_temp_vehicleType.getString("iBaseFare");
            String iPersonSize_str = obj_temp_vehicleType.getString("iPersonSize");

            car_type_hash_map.put("iVehicleTypeId", "" + iVehicleTypeId_str);
            car_type_hash_map.put("vVehicleType", "" + vVehicleType_str);
            car_type_hash_map.put("fPricePerKM", "" + fPricePerKM_str);
            car_type_hash_map.put("fPricePerMin", "" + fPricePerMin_str);
            car_type_hash_map.put("iBaseFare", "" + iBaseFare_str);
            car_type_hash_map.put("iPersonSize", "" + iPersonSize_str);

            names_carTypes.add(vVehicleType_str);

            carTypeDetailList.add(car_type_hash_map);

        }

        csb.setAdapter(names_carTypes);

        csb.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

//                Log.d("Execute", "On click CSB");
                selectedCarType = position + 1;

                for (Entry<String, String> mapEntry : carTypeDetailList.get(position).entrySet()) {
                    String key = mapEntry.getKey();
                    String value = mapEntry.getValue();

                    if (key == "iBaseFare") {
                        Selected_iBaseFare_str = value;
                    } else if (key == "fPricePerMin") {
                        Selected_fPricePerMin_str = value;
                    } else if (key == "fPricePerKM") {
                        Selected_fPricePerKM_str = value;
                    } else if (key == "vVehicleType") {
                        Selected_vVehicleType_str = value;
                    } else if (key == "iPersonSize") {
                        Selected_vVehicle_Person_size_str = value;
                    }
                }

                txt_min_fare_value.setText(CurrencySymbol_str + " " + Selected_iBaseFare_str);
                txt_size_person.setText(Selected_vVehicle_Person_size_str + " " + LBL_PEOPLE_TXT_str);

                if (center != null) {
                    new add_driver_type(center.latitude, center.longitude).execute();
                }

            }
        });

    }

    public void setGeneralConfigurationsData(String json_responseString_profile) throws JSONException {

        JSONObject obj_configuration = new JSONObject(json_responseString_profile);

        String CONFIG_CLIENT_ID_str = obj_configuration.getString("CONFIG_CLIENT_ID");
        String MOBILENUM_VERIFICATION_ENABLE_str = obj_configuration.getString("MOBILE_VERIFICATION_ENABLE");

        CommonUtilities.CONFIG_CLIENT_ID = CONFIG_CLIENT_ID_str;
        CommonUtilities.MOBILENUM_VERIFICATION_ENABLE = MOBILENUM_VERIFICATION_ENABLE_str;
    }

    public void buildAlertMessageERROR() {

        if (alert_problem != null) {
            alert_problem.dismiss();
        }

        if (LBL_ERROR_OCCURED_str == null || LBL_ERROR_OCCURED_str.trim().equals("")
                || LBL_ERROR_OCCURED_str.trim().length() == 0 || LBL_ERROR_OCCURED_str.trim().equals("null")) {
            LBL_ERROR_OCCURED_str = "Sorry. Some Error Occurred. Please try again Later.";
        }

        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("" + LBL_ERROR_OCCURED_str).setCancelable(false).setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                        finish();
                    }
                });
        alert_problem = builder.create();
        alert_problem.show();
    }

    public void generateAlertMessage(String msg) {

        if (generate_alert != null) {
            generate_alert.dismiss();
        }

        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("" + msg).setCancelable(false).setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        generate_alert.dismiss();
                    }
                });
        generate_alert = builder.create();
        generate_alert.show();
    }

    public void generateDestAddedMessage(String msg) {

        if (generate_alert != null) {
            generate_alert.dismiss();
        }

        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("" + msg).setCancelable(false).setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        generate_alert.dismiss();

                        setDestionPoint();
                    }
                });
        generate_alert = builder.create();
        generate_alert.setCancelable(false);
        generate_alert.setCanceledOnTouchOutside(false);
        generate_alert.show();
    }

    public void carRequestCancel() {
        header_img.setVisibility(View.VISIBLE);
        img_navigation_drawer.setVisibility(View.VISIBLE);
        back_navigation.setVisibility(View.GONE);
        confirm_source_text_header.setVisibility(View.GONE);
        source_selection_confirm.setVisibility(View.GONE);

        setPanelHeightToDefault();

        confirm_btn_frame.setVisibility(View.GONE);
        taxiSelectionLayout.setVisibility(View.VISIBLE);
        marker_source_selection_AREA.setVisibility(View.VISIBLE);
        source_selection.setVisibility(View.VISIBLE);

        sourceLocationConfirm = false;
    }

    public void setPanelHeightToDefault() {
        float scale = getResources().getDisplayMetrics().density;

        int dp_sliding_panel_height = (int) (100 * scale + 0.5f);
        sliding_layout.setPanelHeight(dp_sliding_panel_height);
    }

    public void checkProfileImage() {
        String access_token_main = mPrefs.getString("access_token", null);

        if (access_token_main != null && id_facebook != null && !id_facebook.trim().equals("")
                && !id_facebook.trim().equals("0")) {

            String imageUrl = "http://graph.facebook.com/" + id_facebook + "/picture?type=large&width=200&height=200";

            deleteFbPhoto();

            new DownloadProfileImg(Mainprofile_Activity.this, profile_header_img, imageUrl, "" + id_facebook + ".jpg")
                    .execute();

        } else {
            if (vImgName_str == null || vImgName_str.equals("") || vImgName_str.equals("NONE")) {
                profile_header_img.setImageResource(R.drawable.friends_main);
            } else {
                new DownloadProfileImg(Mainprofile_Activity.this, profile_header_img,
                        CommonUtilities.SERVER_URL_PHOTOS + "upload/Passenger/" + user_id + "/" + vImgName_str,
                        vImgName_str).execute();
            }
        }

    }

    public void deleteFbPhoto() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + CommonUtilities.storedImageFolderName);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File(myDir, "" + id_facebook + ".jpg");
        if (file.exists()) {
            file.delete();
        }
    }

    public void CreateSlideMenu() {

        if (navMenuTitles != null) {
            navMenuTitles = null;
        }

        if (navDrawerItems != null) {
            navDrawerItems.clear();
            navDrawerItems = null;
        }

        if (navMenuIcons != null) {
            navMenuIcons.recycle();
            navMenuIcons = null;
        }

        if (NavDrawerAdapter != null) {
            NavDrawerAdapter = null;
        }

        navMenuTitles = new String[7];

        navMenuTitles[0] = "" + LBL_MY_PROFILE_HEADER_TXT_str;
        navMenuTitles[1] = "" + LBL_BOOKING_HISTROY_TXT_str;
        navMenuTitles[2] = "" + LBL_INVITE_PAGE_TITLE_TXT_str;
        navMenuTitles[3] = "" + LBL_ABOUT_US_TXT_str;
        navMenuTitles[4] = "" + LBL_CONTACT_US_TXT_str;
        navMenuTitles[5] = "" + LBL_HELP_TXT_str;
        navMenuTitles[6] = "" + LBL_SIGNOUT_TXT_str;

        navDrawerItems = new ArrayList<NavDrawerItem>();

        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));

        NavDrawerAdapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(NavDrawerAdapter);
        NavDrawerAdapter.notifyDataSetChanged();

        navMenuIcons.recycle();

        profile_header_name.setText("" + Name);

    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/arial.ttf");

        Paint paint = new Paint();
        paint.setStyle(Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(convertToPixels(getApplicationContext(), 18));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        // If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4))
            paint.setTextSize(convertToPixels(getApplicationContext(), 7));

        int xPos = (canvas.getWidth() / 2) - 2; // -2 is for regulating the x
        // position offset

        // baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 4) - ((paint.descent() + paint.ascent()) / 2));

        // canvas.save();

        for (String line : text.split("\n")) {
            canvas.drawText(line, xPos, yPos, paint);
            paint.setTextSize(convertToPixels(getApplicationContext(), 22));
            yPos += paint.descent() - paint.ascent();
        }

        return bm;
    }

    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    public void dest_choose_btn(View v) {

        Intent intent = new Intent(Mainprofile_Activity.this, SelectDestinationActivity.class);
        intent.putExtra("Selected_latitude", "Not Set");
        intent.putExtra("Selected_longitude", "Not Set");
        intent.putExtra("Selected_address", "Not Set");
        startActivityForResult(intent, 2155);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        // TODO Auto-generated method stub

        setMap(map);

        LoadingMapProgressBar.setVisibility(View.GONE);
        initializeMapFun();

        // Log.d("vTripStatus::", "vTripStatus::" + vTripStatus);
        if (vTripStatus != null && (vTripStatus.equals("Active") || vTripStatus.equals("On Going Trip"))) {
            reAssignDeriver(json_responseString_profile);
        } else if (vTripStatus != null && vTripStatus.equals("Requesting")) {

            marker_source_selection_AREA.performClick();

            dialog_RequestTaxiNearest = new RequestTaxiNearest(Mainprofile_Activity.this, Mainprofile_Activity.this,
                    user_id, LBL_REQUEST_CANCEL_FAILED_TXT_str);

            dialog_RequestTaxiNearest.run();
        }
    }


    public void handleMapDrag() {
        if (sourceLocationConfirm == false) {
            source_location.setText("" + LBL_SELECTING_LOCATION_TXT_str);

            if (confirm_source_location != null
                    /* && destLocationConfirm == false */ && sourceLocationConfirm == false) {
                confirm_source_location.setText("" + LBL_SELECTING_LOCATION_TXT_str);
            }
        }
    }

    public void initializeMapFun() {

        marker_source_selection_AREA.setVisibility(View.VISIBLE);
        getMap().getUiSettings().setZoomControlsEnabled(false);
        getMap().getUiSettings().setZoomGesturesEnabled(false);

        getMap().setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                // TODO Auto-generated method stub
                // marker.get
                marker.hideInfoWindow();

                return true;
            }
        });

        getMap().setOnCameraChangeListener(new OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // TODO Auto-generated method stub
                if (sourceLocationConfirm == false) {

                    center = getMap().getCameraPosition().target;
                    sourceLocation_latitude = center.latitude;
                    sourceLocation_longitude = center.longitude;

                    isCityChanged(sourceLocation_latitude, sourceLocation_longitude);

                }

                if (/* destLocationConfirm == false && */sourceLocationConfirm == false) {
                    try {

                        if (getAddress_obj != null) {
                            getAddress_obj.cancel(true);
                        }
                        getAddress_obj = new GetAddressFromLocation(center.latitude, center.longitude,
                                sourceLocationConfirm/* , destLocationConfirm */);
                        getAddress_obj.execute();

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public GoogleMap getMap() {
        return this.map;
    }

    public void isCityChanged(double sourceLocation_latitude, double sourceLocation_longitude) {

        if (checkCityOfLocation != null) {
            checkCityOfLocation.cancel(true);
            checkCityOfLocation = null;
        }

        checkCityOfLocation = new CheckCityOfLocation(Mainprofile_Activity.this, Mainprofile_Activity.this,
                sourceLocation_latitude, sourceLocation_longitude);
        checkCityOfLocation.execute();

    }

    public void updateDriverMarkersFrmMap(String cityName, double sourceLocation_latitude,
                                          double sourceLocation_longitude) {

        lowestKM = 0;
        txt_time_nearest_taxi.setText("--");
        // timeParKM_in_MINUTE = 2;
        timeParKM_in_MINUTE = DRIVER_ARRIVED_MIN_TIME_PER_MINUTE_int;

        lowestKM_firstCall = true;

        if (cityName != null && !currentCityofLocation.equalsIgnoreCase(cityName)) {
            if (update_list_driver_nearest != null) {
                update_list_driver_nearest.cancel(true);
                update_list_driver_nearest = null;

            }

            update_list_driver_nearest = new Update_List_driver_nearest(Mainprofile_Activity.this, cityName);
            update_list_driver_nearest.execute();

            setSearchCarConfigData();

            currentCityofLocation = cityName;
        } else {
            new add_driver_type(sourceLocation_latitude, sourceLocation_longitude).execute();
        }

    }

    public void callToUpdateNearestTaxi() {
        if (update_list_driver_nearest != null) {
            update_list_driver_nearest.cancel(true);
            update_list_driver_nearest = null;

        }

        update_list_driver_nearest = new Update_List_driver_nearest(Mainprofile_Activity.this, currentCityofLocation);
        update_list_driver_nearest.execute();

        setSearchCarConfigData();
    }

    public void updateNearestTaxiList(String json_response_driverList, boolean errorConnection) {
        try {
            finalize();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (errorConnection == false && sourceLocationConfirm == false) {
            if (drivers_List != null && drivers_List.size() > 0) {
                drivers_List.clear();
            }

            JSONArray taxi_Drivers_details;
            try {
                JSONObject taxiDrivers_obj = new JSONObject(json_response_driverList);

                taxi_Drivers_details = taxiDrivers_obj.getJSONArray("getAvailableTaxi");

                // looping through All Contacts
                for (int i = 0; i < taxi_Drivers_details.length(); i++) {
                    JSONObject c = taxi_Drivers_details.getJSONObject(i);

                    String name_driver = c.getString(CommonUtilities.vName_driver);
                    String lat_driver = c.getString(CommonUtilities.vLatitude);
                    String vCompany = c.getString("vCompany");
                    String lon_driver = c.getString(CommonUtilities.vLongitude);

                    String driver_id_auto = c.getString(CommonUtilities.iDriverId);
                    // String car_type =
                    // c.getString(CommonUtilities.vCarType);
                    String driver_img = c.getString(CommonUtilities.vDriverImg);

                    String average_rating = c.getString(CommonUtilities.vAvgRating_driver);

                    String gcm_id_driver = c.getString(CommonUtilities.iGcmRegId_driver);

                    String vPhone_driver_str = c.getString(CommonUtilities.vPhone_driver);

                    JSONObject driverCarDetail = c.getJSONObject("DriverCarDetails");

                    String eCarGo_str = driverCarDetail.getString("eCarGo");
                    String eCarX_str = driverCarDetail.getString("eCarX");
                    String vCarType_str = driverCarDetail.getString("vCarType");

                    String vLicencePlate_str = driverCarDetail.getString("vLicencePlate");
                    String make_title_str = driverCarDetail.getString("make_title");
                    String model_title_str = driverCarDetail.getString("model_title");
                    // tmp hashmap for single contact
                    HashMap<String, String> driver = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    driver.put("driver_id", driver_id_auto);
                    driver.put("Name", name_driver);
                    driver.put("Latitude", lat_driver);
                    driver.put("Longitude", lon_driver);
                    driver.put("GCMID", gcm_id_driver);
                    // driver.put("car_type", car_type);
                    driver.put("car_type", "car type");
                    driver.put("driver_img", driver_img);

                    driver.put("average_rating", average_rating);
                    driver.put("eCarGo_str", eCarGo_str);
                    driver.put("eCarX_str", eCarX_str);
                    driver.put("vCarType", vCarType_str);

                    driver.put("vPhone_driver", vPhone_driver_str);
                    driver.put("vLicencePlate", vLicencePlate_str);
                    driver.put("make_title", make_title_str);
                    driver.put("model_title", model_title_str);
                    driver.put("vCompany", vCompany);
                    // adding contact to contact list

                    drivers_List.add(driver);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                errorConnection = true;
            }

            new add_driver_type(sourceLocation_latitude, sourceLocation_longitude).execute();
        }
    }

    public void getLanguageLabelsFrmSqLite() {

        Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

        cursor.moveToPosition(0);

        language_labels_get_frm_sqLite = cursor.getString(0);

        JSONObject obj_language_labels = null;
        try {
            obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
            LBL_SOURCE_CONFIRM_HEADER_TXT_str = obj_language_labels.getString("LBL_SOURCE_CONFIRM_HEADER_TXT");
            LBL_SELECT_DESTINATION_HEADER_TXT_str = obj_language_labels.getString("LBL_SELECT_DESTINATION_HEADER_TXT");
            LBL_CONFIRM_DEST_HEADER_TXT_str = obj_language_labels.getString("LBL_SELECT_DESTINATION_HEADER_TXT");
            LBL_PICKUP_LOCATION_HEADER_TXT_str = obj_language_labels.getString("LBL_PICKUP_LOCATION_HEADER_TXT");
            LBL_SET_PICK_UP_LOCATION_TXT_str = obj_language_labels.getString("LBL_SET_PICK_UP_LOCATION_TXT");
            LBL_BTN_REQUEST_PICKUP_TXT_str = obj_language_labels.getString("LBL_BTN_REQUEST_PICKUP_TXT");
            LBL_TAXI_SELECTION_GO_TXT_str = obj_language_labels.getString("LBL_TAXI_SELECTION_GO_TXT");
            LBL_TAXI_SELECTION_X_TXT_str = obj_language_labels.getString("LBL_TAXI_SELECTION_X_TXT");
            LBL_TAXI_SELECTION_FAMILY_TXT_str = obj_language_labels.getString("LBL_TAXI_SELECTION_FAMILY_TXT");
            LBL_BTN_CONFIRM_DEST_TXT_str = obj_language_labels.getString("LBL_BTN_CONFIRM_DEST_TXT");
            LBL_BTN_CANCEL_TRIP_TXT_str = obj_language_labels.getString("LBL_BTN_CANCEL_TRIP_TXT");
            LBL_BTN_MESSAGE_TXT_str = obj_language_labels.getString("LBL_BTN_MESSAGE_TXT");
            LBL_SEARCH_PLACE_HINT_TXT_str = obj_language_labels.getString("LBL_SEARCH_PLACE_HINT_TXT");
            LBL_MY_PROFILE_HEADER_TXT_str = obj_language_labels.getString("LBL_MY_PROFILE_HEADER_TXT");
            LBL_BOOKING_HISTROY_TXT_str = obj_language_labels.getString("LBL_BOOKING_HISTROY_TXT");
            LBL_ABOUT_US_TXT_str = obj_language_labels.getString("LBL_ABOUT_US_TXT");
            LBL_CONTACT_US_TXT_str = obj_language_labels.getString("LBL_CONTACT_US_TXT");
            LBL_HELP_TXT_str = obj_language_labels.getString("LBL_HELP_TXT");
            LBL_SIGNOUT_TXT_str = obj_language_labels.getString("LBL_SIGNOUT_TXT");
            LBL_DRIVER_ARRIVING_TXT_str = obj_language_labels.getString("LBL_DRIVER_ARRIVING_TXT");

            LBL_LOADING_REQUEST_RIDE_TXT_str = obj_language_labels.getString("LBL_LOADING_REQUEST_RIDE_TXT");
            LBL_LOADING_PLACE_TXT_str = obj_language_labels.getString("LBL_LOADING_PLACE_TXT");
            LBL_NO_DRIVER_AVAIL_TXT_str = obj_language_labels.getString("LBL_NO_DRIVER_AVAIL_TXT");
            LBL_SORRY_PROBLEM_TXT_str = obj_language_labels.getString("LBL_SORRY_PROBLEM_TXT");
            LBL_LOADING_CANCEL_TRIP_TXT_str = obj_language_labels.getString("LBL_LOADING_CANCEL_TRIP_TXT");
            LBL_ERROR_OCCURED_str = obj_language_labels.getString("LBL_ERROR_OCCURED");
            LBL_FAILED_TRIP_CANCEL_str = obj_language_labels.getString("LBL_FAILED_TRIP_CANCEL");
            LBL_INFO_SET_TXT_str = obj_language_labels.getString("LBL_INFO_SET_TXT");
            LBL_START_TRIP_DIALOG_TXT_str = obj_language_labels.getString("LBL_START_TRIP_DIALOG_TXT");
            LBL_END_TRIP_DIALOG_TXT_str = obj_language_labels.getString("LBL_END_TRIP_DIALOG_TXT");
            LBL_DEVICE_NOT_SUPPORTED_TXT_str = obj_language_labels.getString("LBL_DEVICE_NOT_SUPPORTED_TXT");
            LBL_TRIP_CANCEL_TXT_str = obj_language_labels.getString("LBL_TRIP_CANCEL_TXT");
            LBL_BTN_TRIP_CANCEL_CONFIRM_TXT_str = obj_language_labels.getString("LBL_BTN_TRIP_CANCEL_CONFIRM_TXT");
            LBL_BTN_OK_TXT_str = obj_language_labels.getString("LBL_BTN_OK_TXT");

            LBL_SELECTING_LOCATION_TXT_str = obj_language_labels.getString("LBL_SELECTING_LOCATION_TXT");

            LBL_CANCEL_BTN_TXT_NO_INTERNET_str = obj_language_labels.getString("LBL_CANCEL_BTN_TXT_NO_INTERNET");

            LBL_REQUEST_CANCEL_FAILED_TXT_str = obj_language_labels.getString("LBL_REQUEST_CANCEL_FAILED_TXT");
            LBL_ETA_TXT_str = obj_language_labels.getString("LBL_ETA_TXT");
            LBL_MAX_SIZE_TXT_str = obj_language_labels.getString("LBL_MAX_SIZE_TXT");
            LBL_MIN_FARE_TXT_str = obj_language_labels.getString("LBL_MIN_FARE_TXT");
            LBL_GET_FARE_EST_TXT_str = obj_language_labels.getString("LBL_GET_FARE_EST_TXT");
            LBL_PEOPLE_TXT_str = obj_language_labels.getString("LBL_PEOPLE_TXT");
            LBL_CASH_TXT_str = obj_language_labels.getString("LBL_CASH_TXT");
            LBL_PAYPAL_TXT_str = obj_language_labels.getString("LBL_PAYPAL_TXT");
            LBL_FARE_ESTIMATE_TXT_str = obj_language_labels.getString("LBL_FARE_ESTIMATE_TXT");
            LBL_PRMO_TXT_str = obj_language_labels.getString("LBL_PRMO_TXT");
            LBL_MIN_SMALL_TXT_str = obj_language_labels.getString("LBL_MIN_SMALL_TXT");
            LBL_NO_CAR_AVAIL_TXT_str = obj_language_labels.getString("LBL_NO_CAR_AVAIL_TXT");
            LBL_EN_ROUTE_TXT_str = obj_language_labels.getString("LBL_EN_ROUTE_TXT");
            LBL_CALL_TXT_str = obj_language_labels.getString("LBL_CALL_TXT");
            LBL_MESSAGE_TXT_str = obj_language_labels.getString("LBL_MESSAGE_TXT");
            LBL_ARRIVING_TXT_str = obj_language_labels.getString("LBL_ARRIVING_TXT");
            LBL_ON_TRIP_TXT_str = obj_language_labels.getString("LBL_ON_TRIP_TXT");
            LBL_SEND_STATUS_CONTENT_TXT_str = obj_language_labels.getString("LBL_SEND_STATUS_CONTENT_TXT");
            LBL_ACT_RESULTS_CANCEL_TXT_str = obj_language_labels.getString("LBL_ACT_RESULTS_CANCEL_TXT");
            LBL_CHAT_TXT_str = obj_language_labels.getString("LBL_CHAT_TXT");
            LBL_SEARCH_CAR_WAIT_TXT_str = obj_language_labels.getString("LBL_SEARCH_CAR_WAIT_TXT");
            LBL_SHARE_BTN_TXT_str = obj_language_labels.getString("LBL_SHARE_BTN_TXT");
            LBL_PAYPAL_DISABLE_TXT_str = obj_language_labels.getString("LBL_PAYPAL_DISABLE_TXT");
            LBL_INVITE_PAGE_TITLE_TXT_str = obj_language_labels.getString("LBL_INVITE_PAGE_TITLE_TXT");
            LBL_REFERRAL_POINTS_H_TXT_str = obj_language_labels.getString("LBL_REFERRAL_POINTS_H_TXT");
            LBL_TRIP_CONFIRMED_TXT_str = obj_language_labels.getString("LBL_TRIP_CONFIRMED_TXT");
            LBL_DRIVER_ARRIVED_TXT_str = obj_language_labels.getString("LBL_DRIVER_ARRIVED_TXT");
            LBL_ADD_DESTINATION_BTN_TXT_str = obj_language_labels.getString("LBL_ADD_DESTINATION_BTN_TXT");
            LBL_SOME_ERROR_TXT_str = obj_language_labels.getString("LBL_SOME_ERROR_TXT");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (obj_language_labels != null) {
            confirm_source_text_header.setText("" + LBL_SOURCE_CONFIRM_HEADER_TXT_str);
            select_destination_text_header.setText("" + LBL_SELECT_DESTINATION_HEADER_TXT_str);
            Confirm_destination_text_header.setText("" + LBL_CONFIRM_DEST_HEADER_TXT_str);
            pick_up_txt.setText("" + LBL_PICKUP_LOCATION_HEADER_TXT_str);
            locationMarkertext.setText("" + LBL_SET_PICK_UP_LOCATION_TXT_str);
            btn_confirm_location_request.setText("" + LBL_BTN_REQUEST_PICKUP_TXT_str);

            btn_cancle_trip.setText("" + LBL_BTN_CANCEL_TRIP_TXT_str);
            btn_message_to_driver.setText("" + LBL_CHAT_TXT_str);
            txt_time_eta_header.setText("" + LBL_ETA_TXT_str);
            txt_size_person_header.setText("" + LBL_MAX_SIZE_TXT_str);
            txt_min_fare_header.setText("" + LBL_MIN_FARE_TXT_str);
            txt_btn_get_fare_estimate.setText("" + LBL_GET_FARE_EST_TXT_str);
            txt_cash.setText("" + LBL_CASH_TXT_str);
            txt_online_payment.setText("" + LBL_PAYPAL_TXT_str);
            txt_fare_est.setText("" + LBL_FARE_ESTIMATE_TXT_str);
            txt_promo.setText("" + LBL_PRMO_TXT_str);

            btn_share_txt.setText("" + LBL_SHARE_BTN_TXT_str);
            share_btn_txt.setText("" + LBL_SHARE_BTN_TXT_str);
            refPointsHtxt.setText("" + LBL_REFERRAL_POINTS_H_TXT_str);
        }

    }

    public void selectOnlinePaymentMode(View v) {
        if (!paymentEnabledOrNot.equals("Yes")) {
            generateAlertMessage(LBL_PAYPAL_DISABLE_TXT_str);
            return;
        }
        cashPayment = false;
        selection_paypal_img.setVisibility(View.VISIBLE);
        selection_cash_img.setVisibility(View.GONE);
    }

    public void selectCashPaymentMode(View v) {
        cashPayment = true;

        selection_cash_img.setVisibility(View.VISIBLE);
        selection_paypal_img.setVisibility(View.GONE);
    }

    public void openFarDetailDialog(View v) {

        faredetailDialog = new FareDetailDialog(Mainprofile_Activity.this, Selected_iBaseFare_str,
                Selected_fPricePerMin_str, Selected_fPricePerKM_str, CurrencySymbol_str);
        faredetailDialog.run();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

    }

    public static void updateHeaderImage() {
        // slidemenu.setHeaderImage(facebook_profile_image_main);

        Bitmap bitmap_profile_img = ((BitmapDrawable) facebook_profile_image_main).getBitmap();
        profile_header_img.setImageBitmap(bitmap_profile_img);
    }

    public class drawRoute extends AsyncTask<String, String, String> {
        ArrayList<LatLng> custom_point = new ArrayList<LatLng>();
        LatLng s_latLng;
        LatLng d_latLng;
        GetListOFRoutes routeList;

        public drawRoute(LatLng sourceLoc, LatLng destLoc) {
            // TODO Auto-generated constructor stub
            this.s_latLng = sourceLoc;
            this.d_latLng = destLoc;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            Create_directionUrl getJson_url = new Create_directionUrl();
            String url = getJson_url.getDirectionsUrl(s_latLng, d_latLng);

            routeList = new GetListOFRoutes();
            routeList.Download_subLocations_Task(url);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            ArrayList<PolylineOptions> ListOfPolyLineOptions_routes = routeList.getListOfOptions_routes();

            if (ListOfPolyLineOptions_routes.size() == 0) {
                return;
            }
            route_to_destination_polyLine_options = ListOfPolyLineOptions_routes.get(0);

            Polyline route_direction = getMap().addPolyline(route_to_destination_polyLine_options);

        }

    }

    public void navigateToUser(View v) {
        update_userLocation();

    }

    public class GetAddressFromLocation extends AsyncTask<String, String, String> {

        double lat_add;
        double lon_add;

        boolean error_connection = false;
        boolean sourceLocationConfirm;

        // boolean destLocationConfirm;

        String json_addresses = "";

        public GetAddressFromLocation(double lat_add, double lon_add,
                                      boolean sourceLocationConfirm/*
                                                 * , boolean destLocationConfirm
												 */) {
            // TODO Auto-generated constructor stub
            this.lat_add = lat_add;
            this.lon_add = lon_add;
            this.sourceLocationConfirm = sourceLocationConfirm;
            /* this.destLocationConfirm = destLocationConfirm; */
        }

        public String getAddress_json() {
            String responseString = "";

            String address_frm_location_url = CommonUtilities.GOOGLE_SERVER_URL_GET_ADDRESS + "&key=";

            String url_final_get_address = String.format(address_frm_location_url, "" + lat_add + "," + lon_add,
                    getResources().getString(R.string.google_api_get_address_from_location_serverApi));

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(url_final_get_address);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = OutputStreamReader.readStream(in);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                error_connection = true;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }

            return responseString;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            json_addresses = getAddress_json();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            JSONObject obj_address_result;
            try {
                obj_address_result = new JSONObject(json_addresses);

                if (obj_address_result != null) {
                    JSONArray obj_arr_results = obj_address_result.getJSONArray("results");
                    JSONObject getAddress_frm_arr = obj_arr_results.getJSONObject(0);
                    String[] finalAddress = getAddress_frm_arr.getString("formatted_address").split(",");

                    boolean first_input = true;
                    for (int i = 0; i < finalAddress.length; i++) {
                        if (!finalAddress[i].contains("Unnamed") && !finalAddress[i].contains("null")) {

                            if (i == 0 && !finalAddress[0].matches("[a-zA-Z]+")) {
                                continue;
                            }
                            if (first_input == true) {
                                Address_loc = finalAddress[i];
                                first_input = false;
                            } else {
                                Address_loc = Address_loc + ", " + finalAddress[i];
                            }

                        }
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                error_connection = true;
            }

            if (error_connection == false /* && destLocationConfirm == false */ && sourceLocationConfirm == false) {

                source_lat = center.latitude;
                source_lon = center.longitude;
                source_location.setText("" + Address_loc.toString());

                if (confirm_source_location != null) {
                    confirm_source_location.setText("" + Address_loc.toString());

                }

            }

        }

    }

    public String getLocationAddress(double lat_add, double lon_add) throws IOException {
        Geocoder geocoder = new Geocoder(Mainprofile_Activity.this, Locale.getDefault());
        List<Address> addresses;
        String addressString = "";
        StringBuilder sb = new StringBuilder();

        addresses = geocoder.getFromLocation(lat_add, lon_add, 1);
        if (addresses.size() > 0) {
            Address address = addresses.get(0);

            if (address != null && address.getAddressLine(0) != null
                    && !address.getAddressLine(0).contains("Unnamed")) {
                if (address.getAddressLine(0).matches(".*[a-zA-Z]+.*")) {
                    sb.append(address.getAddressLine(0) + ", ");
                }

            }
            if (address != null && address.getAddressLine(1) != null
                    && !address.getAddressLine(1).contains("Unnamed")) {
                sb.append(address.getAddressLine(1) + ", ");
            }
            String locality = "" + address.getLocality();
            if (address != null && !locality.contains("null")) {
                sb.append(address.getLocality() + ", ");
            }

            sb.append(address.getCountryName());

        }

        addressString = sb.toString();

        return addressString;
    }

    public void FareEstimateCalcluate(View v) {

        Intent startFareCalcluation = new Intent(Mainprofile_Activity.this, FareCalculationActivity.class);
        startFareCalcluation.putExtra("SourceLocation_lattitude", sourceLocation_latitude);
        startFareCalcluation.putExtra("SourceLocation_longitude", sourceLocation_longitude);
        startFareCalcluation.putExtra("SourceLocation_address", "" + Address_loc);
        startFareCalcluation.putExtra("selectedCarType", "" + selectedCarType);
        startFareCalcluation.putExtra("currencySymbol", "" + CurrencySymbol_str);
        startFareCalcluation.putExtra("UserId", "" + user_id);

        startFareCalcluation.putExtra("fare_config", "" + Selected_iBaseFare_str + "@" + Selected_fPricePerMin_str + "@"
                + Selected_fPricePerKM_str + "@" + Selected_vVehicleType_str);
        startFareCalcluation.putExtra("selectedCarType", "" + selectedCarType);
        startFareCalcluation.putExtra("selectedCarType", "" + selectedCarType);
        startFareCalcluation.putExtra("selectedCarType", "" + selectedCarType);

        if (DestinationLocationConfirm == false) {
            startFareCalcluation.putExtra("DestinationLocation_lattitude", 0.0);
            startFareCalcluation.putExtra("DestinationLocation_longitude", 0.0);
            startFareCalcluation.putExtra("DestinationLocation_address", "Not Set");

        } else if (DestinationLocationConfirm == true) {

            startFareCalcluation.putExtra("DestinationLocation_lattitude", value_dest_latitude_loc);
            startFareCalcluation.putExtra("DestinationLocation_longitude", value_dest_longitude_loc);
            startFareCalcluation.putExtra("DestinationLocation_address", value_dest_address_loc);
        }

        startActivity(startFareCalcluation);

    }

    public void callBackFrmAlarmBroadCastReceiver() {
//        Log.d("Alarm", "Called");
        if (isFirstCallFrmAlarm == true && sourceLocationConfirm == false) {
            // Log.d("No Call to show taxi", "no");
        } else if (sourceLocationConfirm == false) {

            callToUpdateNearestTaxi();
        } else if (driver_assigned == true && tripStart == false) {

            if (driverArrived == true) {
                return;
            }
            new updateAssignedDriverLocation().execute();
        } else if (tripStart == true && DestinationLocationConfirm == true) {
//            Log.d("Start:", "::destination");
            updateDestinationTimeMarker();
        }
        isFirstCallFrmAlarm = false;
    }

    public class updateAssignedDriverLocation extends AsyncTask<String, String, String> {
        String driverLocation_json = "";
        boolean errorConnection = false;

        public String getDriverLocation() {
            // String baseUrl = CommonUtilities.SERVER_URL_GET_DETAIL;
            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&DriverId=%s";
            String responseString = "";

            if (URLEncoder.encode(driverId) != null) {

                String registerUrl = baseUrl + String.format(registerParam, "getAssignedDriverLocation", driverId);

                Log.d("registerUrl", "::Assigned Driver loc:" + registerUrl);
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
            String check_driver_vTripStatus = getLocations.getString("vTripStatus");

            this.driverLoc_latitude = Double.parseDouble(dLatitude);
            this.driverLoc_longitude = Double.parseDouble(dLongitude);
            if (check_driver_vTripStatus.equals("Arrived")) {
                if (alarmManager != null) {
                    unregisterReceiver(get_alarm_broadcast_receiver);
                }

                if (notificationmanager != null) {
                    notificationmanager.cancel(NOTIFICATION_ID);
                }

                driverArrived = true;

                driving_status_text_header.setText("" + LBL_DRIVER_ARRIVED_TXT_str);
                CustomNotification("--", " ", Name, driver_assign_name, ((BitmapDrawable) profile_header_img.getDrawable()).getBitmap(), LBL_DRIVER_ARRIVED_TXT_str, true);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (getLocations != null) {

            MarkerOptions markerOptions_driver = new MarkerOptions();

            double driver_loc_latitude = Double.parseDouble(dLatitude);
            double driver_loc_longitude = Double.parseDouble(dLongitude);

            if (driver_point_marker != null && tripStart == false && driverArrived == false) {


                animateMarker(driver_point_marker, new LatLng(driver_loc_latitude, driver_loc_longitude), false);

                new getRoute_GOOGLE(sourceLocation_latitude, sourceLocation_longitude, driver_loc_latitude,
                        driver_loc_longitude, false).execute();

            }


        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = getMap().getProjection();
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
                    // Post again 16ms later.
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

    public class add_driver_type extends AsyncTask<String, String, String> {

        ArrayList<HashMap<String, String>> drivers_List_rectify = new ArrayList<HashMap<String, String>>();

        double userLat;
        double userLon;

        public add_driver_type(double usr_lat, double usr_lon) {
            // TODO Auto-generated constructor stub
            this.userLat = usr_lat;
            this.userLon = usr_lon;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            for (HashMap<String, String> map : drivers_List) {
                String DriverName = "";
                double lat_Driver_final = 0.0;
                double lon_Driver_final = 0.0;
                for (Entry<String, String> mapEntry : map.entrySet()) {
                    String key = mapEntry.getKey();
                    String value = mapEntry.getValue();
                    // Log.d("LIST:", "" + key + "::" + value);

                    if (key == "Name") {
                        DriverName = value;
                    } else if (key == "Latitude") {
                        lat_Driver_final = Double.parseDouble(value);
                    } else if (key == "Longitude") {
                        lon_Driver_final = Double.parseDouble(value);
                    }
                }

                double dist_usr_driver = CalculationByLocation(userLat, userLon, lat_Driver_final, lon_Driver_final);

                if (lowestKM == 0 && lowestKM_firstCall == true) {
//                    lowestKM = (int) dist_usr_driver;
                    lowestKM = dist_usr_driver;
                    lowestKM_firstCall = false;
                }

                if (dist_usr_driver < lowestKM) {
//                    lowestKM = (int) dist_usr_driver;
                    lowestKM = dist_usr_driver;
                }


                if (dist_usr_driver < RESTRICTION_KM_NEAREST_TAXI_int) {
                    drivers_List_rectify.add(map);

                }

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // Log.d("PRE EXECUTE:", "ya");
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            for (HashMap<String, String> map : drivers_List_rectify) {
                String DriverName = "";
                String car_type_str = "";
                String eCarX_str = "";
                String eCarGo_str = "";
                String vCarType_str = "";

                // Log.d("INSIDE HASH", "yup");
                double lat_Driver_final = 0.0;
                double lon_Driver_final = 0.0;
                for (Entry<String, String> mapEntry : map.entrySet()) {
                    String key = mapEntry.getKey();
                    String value = mapEntry.getValue();
                    // Log.d("LIST:", "" + key + "::" + value);

                    if (key == "Name") {
                        DriverName = value;
                    } else if (key == "Latitude") {
                        lat_Driver_final = Double.parseDouble(value);
                    } else if (key == "Longitude") {
                        lon_Driver_final = Double.parseDouble(value);
                    } else if (key == "car_type") {
                        car_type_str = value;
                    } else if (key == "eCarX_str") {
                        eCarX_str = value;
                    } else if (key == "eCarGo_str") {
                        eCarGo_str = value;
                    } else if (key == "vCarType") {
                        vCarType_str = value;
                    }
                }

                String[] arr_temp_carType = vCarType_str.split(",");

                for (int i = 0; i < arr_temp_carType.length; i++) {

                    String temp_index = arr_temp_carType[i];

                    int driver_car_type = Integer.parseInt(temp_index);
                    if (selectedCarType == driver_car_type) {

                        drawMarker(new LatLng(lat_Driver_final, lon_Driver_final), DriverName);
                        notify_driver_list.add(map);
                    }

                }

            }

            if (driver_marker.size() > 0) {

                if (isFirstLaunch == true) {
                    isFirstLaunch = false;
                    index_current_elements = notify_driver_list.size();
                } else {
                    for (int i = 0; i < index_current_elements; i++) {

                        if (driver_marker.size() > 0) {
                            notify_driver_list.remove(0);
                            Marker marker_temp = driver_marker.get(0);

                            marker_temp.remove();
                            driver_marker.remove(0);

                        }
                    }
                }
            }

            for (int i = 0; i < driver_marker.size(); i++) {

                driver_marker.get(i).setVisible(true);
            }
            index_current_elements = notify_driver_list.size();

            String min_time = "";

            if (driver_marker.size() > 0) {
                marker_source_selection_AREA.setClickable(true);
                locationMarkertext.setText("" + LBL_SET_PICK_UP_LOCATION_TXT_str);

                if (lowestKM < 1) {
                    min_time = "" + timeParKM_in_MINUTE + "\n" + LBL_MIN_SMALL_TXT_str;
                    txt_time_eta.setText("" + timeParKM_in_MINUTE + " " + LBL_MIN_SMALL_TXT_str);
                } else {

                    int timeParKM_in_MINUTE_final = (int) (timeParKM_in_MINUTE * lowestKM);

                    min_time = "" + timeParKM_in_MINUTE_final + "\n" + LBL_MIN_SMALL_TXT_str;

                    txt_time_eta.setText("" + timeParKM_in_MINUTE_final + " " + LBL_MIN_SMALL_TXT_str);
                }
            } else {
                min_time = "--";
                setNoCarConfigData();
            }
            txt_time_nearest_taxi.setText("" + min_time);

        }

    }

    public void setNoCarConfigData() {
        marker_source_selection_AREA.setClickable(false);
        String min_time = "--";
        locationMarkertext.setText("" + LBL_NO_CAR_AVAIL_TXT_str);

        txt_time_eta.setText("" + min_time);
        txt_time_nearest_taxi.setText("" + min_time);
    }

    public void setSearchCarConfigData() {
        marker_source_selection_AREA.setClickable(false);
        String min_time = "--";

        if (LBL_SEARCH_CAR_WAIT_TXT_str.equals("") || LBL_SEARCH_CAR_WAIT_TXT_str.trim().length() == 0) {
            LBL_SEARCH_CAR_WAIT_TXT_str = "Searching Car";
        }
        locationMarkertext.setText("" + LBL_SEARCH_CAR_WAIT_TXT_str);

        txt_time_eta.setText("" + min_time);
        txt_time_nearest_taxi.setText("" + min_time);
    }

    public double CalculationByLocation(double lat1, double lon1, double lat2, double lon2) {
        int Radius = 6371;// radius of earth in Km
        double lat1_s = lat1;
        double lat2_d = lat2;
        double lon1_s = lon1;
        double lon2_d = lon2;
        double dLat = Math.toRadians(lat2_d - lat1_s);
        double dLon = Math.toRadians(lon2_d - lon1_s);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1_s))
                * Math.cos(Math.toRadians(lat2_d)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        // Log.i("Radius Value", "" + valueResult + " KM " + kmInDec
        // + " Meter " + meterInDec);

        return Radius * c;
    }


    public void onSourcePointSelect() {

        ArrayList<HashMap<String, String>> final_driver_notify = new ArrayList<HashMap<String, String>>();
        for (HashMap<String, String> map : notify_driver_list) {
            String DriverName = "";
            double lat_Driver_final = 0.0;
            double lon_Driver_final = 0.0;
            for (Entry<String, String> mapEntry : map.entrySet()) {
                String key = mapEntry.getKey();
                String value = mapEntry.getValue();

                if (key == "Name") {
                    DriverName = value;
                } else if (key == "Latitude") {
                    lat_Driver_final = Double.parseDouble(value);
                } else if (key == "Longitude") {
                    lon_Driver_final = Double.parseDouble(value);
                } else if (key == "GCMID") {
                    if (!value.contains("null")) {
                        // notify_driver_list.add(map);
                        final_driver_notify.add(map);
                    }

                }
            }

        }

        if (final_driver_notify.size() > 0) {

            String DriverIDAuto = "";
            for (HashMap<String, String> map : final_driver_notify) {

                double lat_Driver_final = 0.0;
                double lon_Driver_final = 0.0;
                for (Entry<String, String> mapEntry : map.entrySet()) {
                    String key = mapEntry.getKey();
                    String value = mapEntry.getValue();
                    // Log.d("LIST:", "" + key + "::" + value);

                    if (key == "driver_id") {
                        DriverIDAuto = DriverIDAuto + "," + value;
                    }
                }

            }

            final_driver_Detail_notify = final_driver_notify;
            String FinalDriverIDautoList = DriverIDAuto.substring(1);

            long driverCallTime = Calendar.getInstance().getTimeInMillis();

            JSONObject cabRequestedJson = new JSONObject();
            try {
                cabRequestedJson.put("Message", "CabRequested");
                cabRequestedJson.put("sourceLatitude", "" + sourceLocation_latitude);
                cabRequestedJson.put("sourceLongitude", "" + sourceLocation_longitude);
                cabRequestedJson.put("PassengerId", user_id);
                cabRequestedJson.put("PName", profile_header_name.getText().toString()/*.replace(" ", "_")*/);
                cabRequestedJson.put("PPicName", vImgName_str);
                cabRequestedJson.put("PFId", id_facebook);
                cabRequestedJson.put("PRating", vAverageRating);
                cabRequestedJson.put("PPhone", vPhone_passeneger);
                cabRequestedJson.put("PPhoneC", vPhoneCode_passeneger);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            new callToDriverFromUser(FinalDriverIDautoList, cabRequestedJson.toString()).execute();

        } else {

            if (dialog_RequestTaxiNearest != null) {
                dialog_RequestTaxiNearest.dismissDialog();
            }

            setPanelHeightToDefault();

            Toast.makeText(Mainprofile_Activity.this, "" + LBL_NO_DRIVER_AVAIL_TXT_str, Toast.LENGTH_LONG).show();

            header_img.setVisibility(View.VISIBLE);
            img_navigation_drawer.setVisibility(View.VISIBLE);
            back_navigation.setVisibility(View.GONE);
            confirm_source_text_header.setVisibility(View.GONE);
            source_selection_confirm.setVisibility(View.GONE);

            confirm_btn_frame.setVisibility(View.GONE);
            taxiSelectionLayout.setVisibility(View.VISIBLE);
            marker_source_selection_AREA.setVisibility(View.VISIBLE);
            source_selection.setVisibility(View.VISIBLE);

            sourceLocationConfirm = false;
        }

    }

    public void reAssignDeriver(String json_responseString_profile) {

        taxiSelectionLayout.setVisibility(View.GONE);
        active_trip_already = true;

        sourceLocationConfirm = true;

        String source_point_lat_trip = "";
        String source_point_lon_trip = "";

        String dest_point_lat_trip = "";
        String dest_point_lon_trip = "";
        String destAddress = "";

        String ratings_driver_assigned_retrive = "";
        String car_type_driver_retrive = "";
        String latitude_location_driver_retrive = "";
        String longitude_location_driver_retrive = "";
        String img_driver_retrive = "";
        String name_driver_retrive = "";

        String vVehicleType_str = "";

        String custom_message = "";

        String retrive_trip_id_final = "";
        String retrive_trip_number = "";

        String sourceAddress = "";

        String driver_phone_str = "";
        String vCompany = "";

        String iDriver_ID_str = "";

        String eCarGo_str = "";
        String eCarX_str = "";
        String vLicencePlate_str = "";
        String car_name_make_title_str = "";
        String car_model_title_str = "";

        JSONObject retrive_trip_status_obj = null;
        JSONObject trip_detail_json = null;
        JSONObject DriverDetails_json = null;
        JSONObject DriverCarDetails_json = null;

        try {
            retrive_trip_status_obj = new JSONObject(json_responseString_profile);
            trip_detail_json = retrive_trip_status_obj.getJSONObject("TripDetails");
            DriverDetails_json = retrive_trip_status_obj.getJSONObject("DriverDetails");
            DriverCarDetails_json = retrive_trip_status_obj.getJSONObject("DriverCarDetails");

            source_point_lat_trip = trip_detail_json.getString("tStartLat");
            source_point_lon_trip = trip_detail_json.getString("tStartLong");

            dest_point_lat_trip = trip_detail_json.getString("tEndLat");
            dest_point_lon_trip = trip_detail_json.getString("tEndLong");
            destAddress = trip_detail_json.getString("tDaddress");
            vVehicleType_str = trip_detail_json.getString("vVehicleType");

            driver_phone_str = DriverDetails_json.getString("vPhone");
            vCompany = DriverDetails_json.getString("vCompany");

            ratings_driver_assigned_retrive = DriverDetails_json.getString("vAvgRating");
            car_type_driver_retrive = DriverDetails_json.getString("vCarType");
            latitude_location_driver_retrive = DriverDetails_json.getString("vLatitude");
            longitude_location_driver_retrive = DriverDetails_json.getString("vLongitude");
            img_driver_retrive = DriverDetails_json.getString(CommonUtilities.vDriverImg);
            name_driver_retrive = DriverDetails_json.getString("vName");
            retrive_trip_id_final = trip_detail_json.getString("iTripId");
            retrive_trip_number = trip_detail_json.getString("iVerificationCode");
            sourceAddress = trip_detail_json.getString("tSaddress");

            eCarGo_str = DriverCarDetails_json.getString("eCarGo");
            eCarX_str = DriverCarDetails_json.getString("eCarX");
            vLicencePlate_str = DriverCarDetails_json.getString("vLicencePlate");
            car_name_make_title_str = DriverCarDetails_json.getString("make_title");
            car_model_title_str = DriverCarDetails_json.getString("model_title");

            iDriver_ID_str = DriverDetails_json.getString("iDriverId");

            JSONObject callBackDriverJson = new JSONObject();
            try {
                callBackDriverJson.put("Message", "CabRequestAccepted");
                callBackDriverJson.put("iDriverId", "" + iDriver_ID_str);
                callBackDriverJson.put("iTripId", "" + retrive_trip_id_final);
                callBackDriverJson.put("iTripVerificationCode", retrive_trip_number);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            custom_message = callBackDriverJson.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!dest_point_lat_trip.equals("0.0") && !dest_point_lon_trip.equals("0.0")
                && !destAddress.equals("Not Set") && !dest_point_lat_trip.equals("") && !dest_point_lon_trip.equals("")
                && !destAddress.equals("")) {
            DestinationLocationConfirm = true;
            value_dest_address_loc = destAddress;
            value_dest_latitude_loc = Double.parseDouble("" + dest_point_lat_trip);
            value_dest_longitude_loc = Double.parseDouble("" + dest_point_lon_trip);
        }

        Selected_vVehicleType_str = vVehicleType_str;

        source_location.setText("" + sourceAddress);

        Address_loc = sourceAddress;

        sourceLocation_latitude = Double.parseDouble(source_point_lat_trip);
        sourceLocation_longitude = Double.parseDouble(source_point_lon_trip);

        retrive_driver_assign_rating = ratings_driver_assigned_retrive;
        retrive_car_type = car_type_driver_retrive;

        retrive_lat_Driver_assigned = Double.parseDouble(latitude_location_driver_retrive);
        retrive_lon_Driver_final_assigned = Double.parseDouble(longitude_location_driver_retrive);

        retrive_driver_img = img_driver_retrive;
        retrive_driver_name = name_driver_retrive + " " + vCompany;

        trip_id_final = retrive_trip_id_final;
        trip_number = retrive_trip_number;

        retrive_eCarGo_str = eCarGo_str;
        retrive_eCarX_str = eCarX_str;
        retrive_vLicencePlate_str = vLicencePlate_str;
        retrive_car_name_make_title_str = car_name_make_title_str;
        retrive_car_model_title_str = car_model_title_str;
        retrive_driver_phone_str = driver_phone_str;

        if (vTripStatus.equals("On Going Trip")) {
            trip_retrival_start = true;
        }

        callbackFromDriver(this, custom_message);

        if (vTripStatus.equals("On Going Trip")) {
            tripStart(Mainprofile_Activity.this);
        }

    }

    public void changeMessage() {
        driving_status_text_header.setText("" + LBL_EN_ROUTE_TXT_str);
    }


    public void callbackFromDriver(Context cont, String message) {

        RelativeLayout trip_start_header_area = (RelativeLayout) findViewById(R.id.trip_start_header_area);
        trip_start_header_area.setVisibility(View.VISIBLE);
        MyTextView header_source_location = (MyTextView) findViewById(R.id.header_source_location);
        header_source_location.setText("" + Address_loc);

        source_selection.setVisibility(View.GONE);
        source_selection_confirm.setVisibility(View.GONE);
        header_img.setVisibility(View.GONE);
//        driving_status_text_header.setText("" + LBL_EN_ROUTE_TXT_str);
        driving_status_text_header.setText("" + LBL_TRIP_CONFIRMED_TXT_str);
        driving_status_text_header.setVisibility(View.VISIBLE);

        MarkerOptions markerOptions_sourceLocation = new MarkerOptions();
        MarkerOptions markerOptions_driver = new MarkerOptions();

		/* Setting Up Layouts */

        setPanelHeightToDefault();

        // header_img.setVisibility(View.VISIBLE);
        img_navigation_drawer.setVisibility(View.VISIBLE);
        back_navigation.setVisibility(View.GONE);
        confirm_source_text_header.setVisibility(View.GONE);
        confirm_btn_frame.setVisibility(View.GONE);

        markerOptions_sourceLocation.position(new LatLng(sourceLocation_latitude, sourceLocation_longitude));

        // Changing marker icon
        markerOptions_sourceLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.source_marker)).anchor(0.5f,
                0.5f);

        // Adding marker on the Google Map

        source_point_marker = getMap().addMarker(markerOptions_sourceLocation);
        locationMarker_layout.setVisibility(View.GONE);

		/* Setting Up Layout Finished */

        driver_assigned = true;

		/* set Alarm to get updated locations */

        if (alarmManager != null) {
            unregisterReceiver(get_alarm_broadcast_receiver);
        }

        if (trip_retrival_start == false) {
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(Mainprofile_Activity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(Mainprofile_Activity.this, 0, intent, 0);

            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                    1 * DRIVER_LOC_FETCH_TIME_INTERVAL * 1000, pendingIntent);

            IntentFilter filter_alrmReceiver = new IntentFilter();
            filter_alrmReceiver.addAction("com.hwindiapp.passenger.ALARMCALLED");

            registerReceiver(get_alarm_broadcast_receiver, filter_alrmReceiver);
        }

		/* Setting Finished Alarm to get updated locations */

        if (dialog_RequestTaxiNearest != null) {
            dialog_RequestTaxiNearest.dismissDialog();
        }

        ArrayList<HashMap<String, String>> driver_assign = new ArrayList<HashMap<String, String>>();

        driver_detail_layout_frame.setVisibility(View.VISIBLE);

        try {
            JSONObject json_callBack_driver = new JSONObject(message);

            driverId = json_callBack_driver.getString("iDriverId");

            trip_id_final = json_callBack_driver.getString("iTripId");
            trip_number = json_callBack_driver.getString("iTripVerificationCode");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        String get_retrive_eCarGo_str = "";
        String get_retrive_eCarX_str = "";
        String get_retrive_vLicencePlate_str = "";
        String get_retrive_car_name_make_title_str = "";
        String get_retrive_car_model_title_str = "";
        String get_retrive_driver_phone_str = "";

        String driver_assign_rating = "" + 0.0;
        String car_type = "";

        double lat_Driver_assigned = 0.0;
        double lon_Driver_final_assigned = 0.0;
        selected_driver_id = driverId;

        String driver_img = "";

        if (active_trip_already == false) {
            String vCompany = "";

            for (HashMap<String, String> map : final_driver_Detail_notify) {
                String DriverName = "";
                double lat_Driver_final = 0.0;
                double lon_Driver_final = 0.0;
                for (Entry<String, String> mapEntry : map.entrySet()) {
                    String key = mapEntry.getKey();
                    String value = mapEntry.getValue();

                    if (key.equals("driver_id")) {
                        if (value.equals("" + driverId)) {
                            // notify_driver_list.add(map);
                            driver_assign.add(map);
                        }

                    }
                }

            }

            for (HashMap<String, String> map : driver_assign) {
                String DriverName = "";

                for (Entry<String, String> mapEntry : map.entrySet()) {
                    String key = mapEntry.getKey();
                    String value = mapEntry.getValue();
                    // Log.d("LIST:", "" + key + "::" + value);

                    if (key.equals("Name")) {
                        driver_assign_name = value;
                    } else if (key.equals("Latitude")) {
                        lat_Driver_assigned = Double.parseDouble(value);
                    } else if (key.equals("Longitude")) {
                        lon_Driver_final_assigned = Double.parseDouble(value);
                    } else if (key.equals("average_rating")) {
                        driver_assign_rating = value;
                    } else if (key.equals("car_type")) {
                        car_type = value;
                    } else if (key.equals("driver_img")) {
                        driver_img = value;
                    } else if (key.equals("eCarGo_str")) {
                        get_retrive_eCarGo_str = value;
                    } else if (key.equals("eCarX_str")) {
                        get_retrive_eCarX_str = value;
                    } else if (key.equals("vLicencePlate")) {
                        get_retrive_vLicencePlate_str = value;
                    } else if (key.equals("vPhone_driver")) {
                        get_retrive_driver_phone_str = value;
                    } else if (key.equals("make_title")) {
                        get_retrive_car_name_make_title_str = value;
                    } else if (key.equals("model_title")) {
                        get_retrive_car_model_title_str = value;
                    } else if (key.equals("vCompany")) {
                        vCompany = value;
                    }

                }

                driver_assign_name = driver_assign_name + " " + vCompany;
            }
        } else if (active_trip_already == true) {

            driver_assign_rating = retrive_driver_assign_rating;
            car_type = retrive_car_type;

            lat_Driver_assigned = retrive_lat_Driver_assigned;
            lon_Driver_final_assigned = retrive_lon_Driver_final_assigned;

            driver_img = retrive_driver_img;

            driver_assign_name = retrive_driver_name;

            get_retrive_eCarGo_str = retrive_eCarGo_str;
            get_retrive_eCarX_str = retrive_eCarX_str;
            get_retrive_vLicencePlate_str = retrive_vLicencePlate_str;
            get_retrive_car_name_make_title_str = retrive_car_name_make_title_str;
            get_retrive_car_model_title_str = retrive_car_model_title_str;
            get_retrive_driver_phone_str = retrive_driver_phone_str;
        }

        setDriverCarNumberPlate(get_retrive_vLicencePlate_str, get_retrive_driver_phone_str);

        getMap().clear();
        update_userLocation();

        if (DestinationLocationConfirm == true) {
            setDestionPoint();
        } else {
            addDestinationTxtView.setVisibility(View.VISIBLE);
            addDestinationTxtView.setText("" + LBL_ADD_DESTINATION_BTN_TXT_str);
            addDestinationTxtView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Mainprofile_Activity.this, SelectDestinationActivity.class);
                    intent.putExtra("Selected_latitude", tripDestinationLatitude);
                    intent.putExtra("Selected_longitude", tripDestinationLongitude);
                    intent.putExtra("Selected_address", tripDestinationAddress);
                    startActivityForResult(intent, SELECT_DESTINATION_POINT_AFTER_TRIP_ACCEPT);
                }
            });
        }

        source_point_marker = getMap().addMarker(markerOptions_sourceLocation);

        markerOptions_driver.position(new LatLng(lat_Driver_assigned, lon_Driver_final_assigned));

        // Changing marker icon
        markerOptions_driver.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_driver)).anchor(0.5f, 0.5f);

        // Adding marker on the Google Map
        driver_point_marker = getMap().addMarker(markerOptions_driver);

        updateDriverLocAfterStartTrip = new UpdateDriverLocationsOnMap(driver_point_marker, getMap(), Mainprofile_Activity.this,
                driverId, 1 * DRIVER_LOC_FETCH_TIME_INTERVAL * 1000);

        setLatLonBound();

        if (trip_retrival_start == false) {
            new getRoute_GOOGLE(sourceLocation_latitude, sourceLocation_longitude, lat_Driver_assigned,
                    lon_Driver_final_assigned, false).execute();
        }

        RoundedImageView driver_img_markerImage = (RoundedImageView) findViewById(R.id.driver_img);

        String driver_image_url = CommonUtilities.SERVER_URL_PHOTOS + "upload/Driver/" + driverId + "/"
                + URLEncoder.encode(driver_img);

        driver_image_final_url = driver_image_url;

        if (driver_img.length() == 0 || driver_img.equals("NONE") || driver_img.equals("null") || driver_img.equals("")
                || driver_img.equals("3_")) {
            // Log.d("img_name:img_name:", ""+img_name);
            driver_image_final_url = "NONE";
            try {
                Bitmap bmp_usr_pic = BitmapFactory.decodeResource(Mainprofile_Activity.this.getResources(),
                        R.drawable.friends_main);
                driver_img_markerImage.setImageBitmap(bmp_usr_pic);
            } catch (Exception e2) {
                // TODO: handle exception
            }
        } else {
            DriverImageLoadTask process_driver_img = new DriverImageLoadTask(driver_image_url, driver_img_markerImage);

            process_driver_img.execute();
        }

        MyTextView marker_driver_name = (MyTextView) findViewById(R.id.driver_name);
        marker_driver_name.setText(driver_assign_name);

        MyTextView driver_car_type = (MyTextView) findViewById(R.id.driver_car_type);

        driver_car_type.setText(Selected_vVehicleType_str);

        float driver_assigned_rating_average = 0;
        try {
            driver_assigned_rating_average = Float.parseFloat(driver_assign_rating);
        } catch (Exception e) {
            // TODO: handle exception
            driver_assigned_rating_average = 0;
        }

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        driver_assign_rating = df.format(driver_assigned_rating_average);

        MyTextView txt_rating = (MyTextView) findViewById(R.id.txt_rating);
        txt_rating.setText("" + driver_assign_rating);

        MyTextView driver_car_name = (MyTextView) findViewById(R.id.driver_car_name);
        driver_car_name.setText(get_retrive_car_name_make_title_str);

        MyTextView driver_car_model = (MyTextView) findViewById(R.id.driver_car_model);
        driver_car_model.setText(get_retrive_car_model_title_str);

        MyTextView numberPlate_txt = (MyTextView) findViewById(R.id.numberPlate_txt);
        numberPlate_txt.setText(get_retrive_vLicencePlate_str);

        btn_cancle_trip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new Cancel_warning_dialog_run(true).run();

            }
        });

        final String access_sign_token_user_id_auto = mPrefs.getString("access_sign_token_user_id_auto", null);

        btn_message_to_driver.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent UserToDriverMessage = new Intent(Mainprofile_Activity.this, UserTODriverMessagingActivity.class);
                UserToDriverMessage.putExtra("DriverID", "" + driverId);
                UserToDriverMessage.putExtra("UserID", "" + access_sign_token_user_id_auto);
                UserToDriverMessage.putExtra("UserName", Name);
                UserToDriverMessage.putExtra("DriverName", driver_assign_name);
                UserToDriverMessage.putExtra("GeneratedtripID", "" + trip_id_final);
                startActivity(UserToDriverMessage);
            }
        });

        registerForContextMenu(btn_contact_container_assigned_driver);
        btn_contact_container_assigned_driver.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                openContextMenu(v);
            }
        });

        btn_share_txt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
    }

    public void setLatLonBound() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(source_point_marker.getPosition());
        builder.include(driver_point_marker.getPosition());
        LatLngBounds bounds = builder.build();
//		int padding = 100; // offset from edges of the map in pixels

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);

        CameraUpdate cu_showBoth = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        getMap().animateCamera(cu_showBoth);
    }

    public void setDriverCarNumberPlate(String numberPlate, String driverMobileNumber_str) {
        this.driverCarNumberPlate_str = numberPlate;
        this.driverMobileNumber_str = driverMobileNumber_str;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, 1, 0, "" + LBL_CALL_TXT_str);
        menu.add(0, 2, 0, "" + LBL_MESSAGE_TXT_str);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        if (item.getItemId() == 1) {

            try {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + driverMobileNumber_str));
                startActivity(callIntent);

            } catch (Exception e) {
                // TODO: handle exception
            }

        } else if (item.getItemId() == 2) {

            try {

                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "" + driverMobileNumber_str);
                startActivity(smsIntent);

            } catch (Exception e) {
                // TODO: handle exception
            }

        }
        return super.onContextItemSelected(item);

        // return false;
    }

    public void setDriverLocation(double latitude, double longitude) {

        if (latitude == 0.0 || longitude == 0.0) {
            return;
        }

        this.driverLoc_latitude = latitude;
        this.driverLoc_longitude = longitude;
    }

    public void setDestionPoint() {
        MarkerOptions markerOptions_destinationLocation = new MarkerOptions();
        markerOptions_destinationLocation.position(new LatLng(value_dest_latitude_loc, value_dest_longitude_loc));

        // Changing marker icon
        markerOptions_destinationLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker))
                .anchor(0.5f, 0.5f);

        destination_point_marker = getMap().addMarker(markerOptions_destinationLocation);

        RelativeLayout destination_area_container = (RelativeLayout) findViewById(R.id.destination_area_container);
        destination_area_container.setVisibility(View.VISIBLE);
        MyTextView trip_start_dest_location = (MyTextView) findViewById(R.id.trip_start_dest_location);
        trip_start_dest_location.setText("" + value_dest_address_loc);

        new drawRoute(new LatLng(sourceLocation_latitude, sourceLocation_longitude),
                new LatLng(value_dest_latitude_loc, value_dest_longitude_loc)).execute();
    }

    public void updateDestinationTimeMarker() {

        if (driverLoc_latitude == 0.0 || driverLoc_longitude == 0.0) {
            return;
        }

        boolean reQuestFromDestinationPoint = true;

        if (destination_Point_getRoute_GOOGLE != null) {
            destination_Point_getRoute_GOOGLE.cancel(true);
            destination_Point_getRoute_GOOGLE = null;
        }
        destination_Point_getRoute_GOOGLE = new getRoute_GOOGLE(driverLoc_latitude, driverLoc_longitude,
                value_dest_latitude_loc, value_dest_longitude_loc, reQuestFromDestinationPoint);
        destination_Point_getRoute_GOOGLE.execute();

    }

    public class getRoute_GOOGLE extends AsyncTask<String, String, String> {
        String json_route_locations = "";
        double source_point_latitude;
        double source_point_longitude;
        double driver_point_latitude;
        double driver_point_longitude;
        boolean reQuestFromDestinationPoint;

        public getRoute_GOOGLE(double source_point_latitude, double source_point_longitude,
                               double driver_point_latitude, double driver_point_longitude, boolean reQuestFromDestinationPoint) {
            // TODO Auto-generated constructor stub

            this.source_point_latitude = source_point_latitude;
            this.source_point_longitude = source_point_longitude;
            this.driver_point_latitude = driver_point_latitude;
            this.driver_point_longitude = driver_point_longitude;
            this.reQuestFromDestinationPoint = reQuestFromDestinationPoint;

        }

        public String get_json_route() {
            String json_route = "";

            String baseUrl = CommonUtilities.GOOGLE_SERVER_URL_ROUTE;
            String registerParam = "origin=%s&destination=%s&sensor=%s";

            String registerUrl = baseUrl
                    + String.format(registerParam, "" + source_point_latitude + "," + source_point_longitude,
                    "" + driver_point_latitude + "," + driver_point_longitude, "true");

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(registerUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                json_route = OutputStreamReader.readStream(in);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }

            return json_route;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            json_route_locations = get_json_route();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//            Log.d("Route", "::" + json_route_locations);
            processTimeMarker(json_route_locations, reQuestFromDestinationPoint);
        }
    }

    public void processTimeMarker(String json_route_locations, boolean reQuestFromDestinationPoint) {
//        Log.d("log1", "log1");
        if (reQuestFromDestinationPoint == false) {
//            Log.d("log2", "log2");
            if (tripStart == true || driverArrived == true) {
//                Log.d("log3", "log3");
                return;
            }
        }


//        Log.d("log4", "log4");
        JSONObject json;
        JSONArray routeArray;
        JSONObject routes;
        JSONArray newTempARr = null;
        JSONObject newDisTimeOb;
        JSONObject distOb;
        JSONObject timeOb;
        String distance_str = "";
        String time_str = "";
        String time_value = "60";
        try {
            json = new JSONObject(json_route_locations);
            routeArray = json.getJSONArray("routes");
            routes = routeArray.getJSONObject(0);
            newTempARr = routes.getJSONArray("legs");
            newDisTimeOb = newTempARr.getJSONObject(0);
            distOb = newDisTimeOb.getJSONObject("distance");
            timeOb = newDisTimeOb.getJSONObject("duration");
            distance_str = distOb.getString("text");
            time_str = timeOb.getString("text");
            time_value = timeOb.getString("value");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            time_value = "" + ((int) Math.round(((Double.parseDouble(time_value) / 60))));
        } catch (Exception e) {
            // TODO: handle exception
        }

        if (reQuestFromDestinationPoint == false) {

            if (time_value.equals("0") || Integer.parseInt(time_value) < 3) {
                driving_status_text_header.setText("" + LBL_ARRIVING_TXT_str);
                driving_status_text_header.setVisibility(View.VISIBLE);
            }

            if (time_value.equals("0") || Integer.parseInt(time_value) == 3 || Integer.parseInt(time_value) == 1) {

                Bitmap bitmap_user = ((BitmapDrawable) profile_header_img.getDrawable()).getBitmap();

                if (time_value.equals("0") || time_value.equals("")) {
                    time_value = "1";
                }

                if (notification_count < 3) {
                    CustomNotification(time_value + "\n" + LBL_MIN_SMALL_TXT_str, "", Name, driver_assign_name,
                            bitmap_user, LBL_DRIVER_ARRIVING_TXT_str, false);
                }

            }

            Bitmap marker_time_ic = writeTextOnDrawable(R.drawable.driver_time_marker,
                    time_value + "\n" + LBL_MIN_SMALL_TXT_str);

            if (time_driver_marker != null) {
                time_driver_marker.remove();
            }
            time_driver_marker = getMap().addMarker(
                    new MarkerOptions().position(new LatLng(sourceLocation_latitude, sourceLocation_longitude))
                            .icon(BitmapDescriptorFactory.fromBitmap(marker_time_ic)));

        } else if (reQuestFromDestinationPoint == true) {

            if (destination_point_marker != null) {
                destination_point_marker.remove();
                destination_point_marker = null;
            }
            Bitmap marker_time_ic = writeTextOnDrawable(R.drawable.driver_time_marker,
                    time_value + "\n" + LBL_MIN_SMALL_TXT_str);
            MarkerOptions markerOptions_destinationLocation = new MarkerOptions();
            markerOptions_destinationLocation.position(new LatLng(value_dest_latitude_loc, value_dest_longitude_loc));

            markerOptions_destinationLocation.icon(BitmapDescriptorFactory.fromBitmap(marker_time_ic));

            destination_point_marker = getMap().addMarker(markerOptions_destinationLocation);
        }

    }

    public class DriverImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private RoundedImageView imageView;
        Bitmap myBitmap = null;

        public DriverImageLoadTask(String url, RoundedImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {

                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) conn;
                // conn.connect();
                httpConn.connect();

                int response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {

                    InputStream is = httpConn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    BitmapFactory.Options options_bmp = new BitmapFactory.Options();
                    options_bmp.inSampleSize = calculateInSampleSize(options_bmp, 150, 150);
                    myBitmap = BitmapFactory.decodeStream(bis, null, options_bmp);
                    bis.close();
                    is.close();

                    if (myBitmap == null) {
                        bmp_driver_pic = BitmapFactory.decodeResource(Mainprofile_Activity.this.getResources(),
                                R.drawable.friends_main);
                    } else {
                        bmp_driver_pic = myBitmap;
                    }
                } else {
                    return null;
                }
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result == null) {
                try {
                    Bitmap bmp_usr_pic = BitmapFactory.decodeResource(Mainprofile_Activity.this.getResources(),
                            R.drawable.friends_main);

                    bmp_driver_pic = bmp_usr_pic;
                    imageView.setImageBitmap(bmp_usr_pic);
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            } else {
                imageView.setImageBitmap(result);
            }

        }

        public Bitmap getDriverImg() {
            return myBitmap;

        }

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public class getUpdatedDetailsOfPassenger extends AsyncTask<String, String, String> {

        String userId_auto = "";

        String responseString_json = "";

        public getUpdatedDetailsOfPassenger(String userId_auto) {
            // TODO Auto-generated constructor stub
            this.userId_auto = userId_auto;
        }

        public String getPassengerDetails() {
            String responseString = "";

            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&access_sign_token_user_id_auto=%s";

            String registerUrl = baseUrl + String.format(registerParam, "getDetail", URLEncoder.encode(userId_auto));

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(registerUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = OutputStreamReader.readStream(in);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }

            return responseString;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            responseString_json = getPassengerDetails();
            return null;
        }

    }

    public class cancleTrip extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        String responseMessage_cancleTrip = "";
        String driver_id_auto = "";
        String responseString = "";
        String trip_id = "";

        RelativeLayout driver_detail_layout_frame;

        public cancleTrip(RelativeLayout driver_detail_layout_frame, String trip_id, String driverId_auto) {
            // TODO Auto-generated constructor stub

            this.driver_id_auto = driverId_auto;
            this.driver_detail_layout_frame = driver_detail_layout_frame;
            this.trip_id = trip_id;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(Mainprofile_Activity.this, R.style.DialogTheme_custom);
            pDialog.setMessage("" + LBL_LOADING_CANCEL_TRIP_TXT_str);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public String cancleTripMethod() {

            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&DautoId=%s&UautoId=%s&trip_id_auto=%s";

            if (URLEncoder.encode(driver_id_auto) != null) {

                String registerUrl = baseUrl + String.format(registerParam, URLEncoder.encode("callToCancleTrip"),
                        URLEncoder.encode(driver_id_auto), URLEncoder.encode(user_id), URLEncoder.encode(trip_id));

                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(registerUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    responseString = OutputStreamReader.readStream(in);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

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

            responseMessage_cancleTrip = cancleTripMethod();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            pDialog.dismiss();

            if (responseMessage_cancleTrip.trim().equals("Success")) {

                Intent send_data = new Intent(Mainprofile_Activity.this, Launcher_TaxiServiceActivity.class);
                send_data.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(send_data);

            } else {
                Toast.makeText(Mainprofile_Activity.this, "" + LBL_FAILED_TRIP_CANCEL_str, Toast.LENGTH_SHORT).show();
            }

        }

    }

    public class callToDriverFromUser extends AsyncTask<String, String, String> {

        String driver_id_auto = "";
        String responseString = "";
        String call_to_driver_res_str;

        boolean errorInConnection = false;

        String cabRequestJson = "";

        public callToDriverFromUser(String driverId_auto, String cabRequestJson) {
            // TODO Auto-generated constructor stub

            this.driver_id_auto = driverId_auto;
            this.cabRequestJson = cabRequestJson;
        }

        public String callDriverGCM() {

            String baseUrl = CommonUtilities.SERVER_URL;
            String registerParam = "function=%s&DautoId=%s&message=%s&userId=%s&CashPayment=%s&SelectedCarTypeID=%s"
                    + "&DestLatitude=%s&DestLongitude=%s&DestAddress=%s";

            String latitude_dest = "";
            String longitude_dest = "";
            String address_dest = "";
            if (DestinationLocationConfirm == false) {
                value_dest_latitude_loc = 0.0;
                value_dest_longitude_loc = 0.0;
                value_dest_address_loc = "Not Set";

                latitude_dest = "";
                longitude_dest = "";
                address_dest = "";
            } else {
                latitude_dest = "" + value_dest_latitude_loc;
                longitude_dest = "" + value_dest_longitude_loc;
                address_dest = value_dest_address_loc;
            }

            mPrefs = PreferenceManager.getDefaultSharedPreferences(Mainprofile_Activity.this);
            String access_sign_token_user_id_auto = mPrefs.getString("access_sign_token_user_id_auto", null);

            /*if (URLEncoder.encode(driver_id_auto) != null) {

                String registerUrl = baseUrl + String.format(registerParam, URLEncoder.encode("callToDriver"),
                        URLEncoder.encode(driver_id_auto), cabRequestJson,
                        URLEncoder.encode("" + access_sign_token_user_id_auto), URLEncoder.encode("" + cashPayment),
                        URLEncoder.encode("" + selectedCarType), URLEncoder.encode("" + latitude_dest),
                        URLEncoder.encode("" + longitude_dest),
                        URLEncoder.encode("" + address_dest));

//                Log.e("Driver calling Url", ":" + registerUrl);


                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(registerUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    responseString = OutputStreamReader.readStream(in);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    errorInConnection = true;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }

                }

            }*/
            HashMap<String, String> requestCabData = new HashMap<String, String>();
            requestCabData.put("function", "callToDriver");
            requestCabData.put("DautoId", driver_id_auto);
            requestCabData.put("message", cabRequestJson);
            requestCabData.put("userId", access_sign_token_user_id_auto);
            requestCabData.put("CashPayment", "" + cashPayment);

            /*// add promo code params
            if (!TextUtils.isEmpty(et_promocode.getText()))
            requestCabData.put("coupan_code", "" + et_promocode.getText().toString());*/

            requestCabData.put("SelectedCarTypeID", "" + selectedCarType);
            requestCabData.put("DestLatitude", latitude_dest);
            requestCabData.put("DestLongitude", longitude_dest);
            requestCabData.put("DestAddress", address_dest);

            responseString = OutputStreamReader.performPostCall(CommonUtilities.SERVER_URL, requestCabData);

            return responseString;

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            call_to_driver_res_str = callDriverGCM();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (errorInConnection == true || call_to_driver_res_str == null
                    || !call_to_driver_res_str.trim().equals("SUCCESS")) {
                if (call_to_driver_res_str != null && call_to_driver_res_str.trim().equals("NoCarsAvail")) {
                    Toast.makeText(Mainprofile_Activity.this, "" + LBL_NO_CAR_AVAIL_TXT_str, Toast.LENGTH_LONG).show();
                    dialog_RequestTaxiNearest.dismissDialog();
                    carRequestCancel();

                    update_list_driver_nearest = new Update_List_driver_nearest(Mainprofile_Activity.this, currentCityofLocation);
                    update_list_driver_nearest.execute();

                    setSearchCarConfigData();

                } else {
                    generateErrorOnTaxiRequest();
                }
            }
            Mainprofile_Activity.this.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
            Mainprofile_Activity.this.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));
        }

    }

    public class addDestDuringTrip extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        String latitude;
        String longitude;
        String address;

        String responseString = "";

        public addDestDuringTrip(String latitude, String longitude, String address) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Mainprofile_Activity.this, R.style.DialogTheme_custom);
            pDialog.setMessage("Loading");
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String url_add_dest = CommonUtilities.SERVER_URL + "type=addDestination&Latitude=" + URLEncoder.encode("" + latitude) +
                    "&Longitude=" + URLEncoder.encode("" + longitude) + "&Address=" + URLEncoder.encode("" + address) + "&UserId=" + user_id;

            Log.d("url_add_dest", "::" + url_add_dest);
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(url_add_dest);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = OutputStreamReader.readStream(in);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if (responseString != null && !responseString.trim().equals("") && responseString.trim().equals("SUCCESS")) {
                addDestinationTxtView.setVisibility(View.GONE);
                value_dest_latitude_loc = Double.parseDouble(latitude);
                value_dest_longitude_loc = Double.parseDouble(longitude);
                DestinationLocationConfirm = true;
                value_dest_address_loc = address;

                setDestionPoint();
            } else {
                generateAlertMessage(LBL_SOME_ERROR_TXT_str);
            }
        }
    }

    public void destAddedByDriver(String message) {

        try {
            JSONObject jsonObj_temp = new JSONObject(message);

            addDestinationTxtView.setVisibility(View.GONE);
            value_dest_latitude_loc = Double.parseDouble(jsonObj_temp.getString("DLatitude"));
            value_dest_longitude_loc = Double.parseDouble(jsonObj_temp.getString("DLongitude"));
            DestinationLocationConfirm = true;
            value_dest_address_loc = jsonObj_temp.getString("DAddress");

            generateDestAddedMessage("Destination is added by driver.");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void generateErrorOnTaxiRequest() {
        sourceLocationConfirm = false;

        if (dialog_RequestTaxiNearest != null) {
            dialog_RequestTaxiNearest.dismissDialog();
        }

        no_internet_dialog = new Internet_warning_dialog_run(false, Mainprofile_Activity.this);
        no_internet_dialog.setListener(Mainprofile_Activity.this);
        no_internet_dialog.run();
    }

    public void drawMarker(LatLng point, String Name) {

        try {
            finalize();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point).title("" + Name).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_driver))
                .anchor(0.5f, 0.5f);

        // Adding marker on the Google Map
        Marker marker = getMap().addMarker(markerOptions);

        marker.setVisible(false);

        driver_marker.add(marker);
    }

    public void tripStart(Context context) {

        if (alarmManager != null && trip_retrival_start == false) {
            try {
                unregisterReceiver(get_alarm_broadcast_receiver);
            } catch (Exception e) {

            }

        }

        if (notificationmanager != null) {
            notificationmanager.cancel(NOTIFICATION_ID);
        }

        Power_wl.acquire();

        tripStart = true;

        getMap().clear();

        update_userLocation();

        MarkerOptions markerOptions_sourceLocation = new MarkerOptions();
        markerOptions_sourceLocation.position(new LatLng(sourceLocation_latitude, sourceLocation_longitude));
        markerOptions_sourceLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.source_marker)).anchor(0.5f,
                0.5f);
        source_point_marker = getMap().addMarker(markerOptions_sourceLocation);

        if (DestinationLocationConfirm == true /*&& route_to_destination_polyLine_options != null*/) {

            if (route_to_destination_polyLine_options != null) {
                Polyline route_direction = getMap().addPolyline(route_to_destination_polyLine_options);
            }

            MarkerOptions markerOptions_destinationLocation = new MarkerOptions();
            markerOptions_destinationLocation.position(new LatLng(value_dest_latitude_loc, value_dest_longitude_loc));

            // Changing marker icon
            markerOptions_destinationLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker))
                    .anchor(0.5f, 0.5f);

            // Adding marker on the Google Map
            destination_point_marker = getMap().addMarker(markerOptions_destinationLocation);
        }

        driving_status_text_header.setText("" + LBL_ON_TRIP_TXT_str);
        driving_status_text_header.setVisibility(View.VISIBLE);

        btn_cancle_trip.setVisibility(View.GONE);
        btn_contact_container_assigned_driver.setVisibility(View.GONE);
        btn_share_container.setVisibility(View.VISIBLE);
        subBtnDetail_layout.setVisibility(View.GONE);
        setPanelHeightToDefault();

        btn_share_container.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        if (DestinationLocationConfirm == true) {
            tripStartAlarmFORDestination();
        }

        if (trip_retrival_start == false) {
            new tripStart_dialog_run().run();
        } else {
            Toast.makeText(context, "" + LBL_START_TRIP_DIALOG_TXT_str, Toast.LENGTH_LONG).show();
        }

        if (updateDriverLocAfterStartTrip != null) {
            updateDriverLocAfterStartTrip.startUpdatingLocations();
        }

    }

    public void tripStartAlarmFORDestination() {
//        Log.d("Update Ti", "::" + DESTINATION_UPDATE_TIME_INTERVAL);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(Mainprofile_Activity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(Mainprofile_Activity.this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), DESTINATION_UPDATE_TIME_INTERVAL * 60 * 1000,
                pendingIntent);

        IntentFilter filter_alrmReceiver = new IntentFilter();
        filter_alrmReceiver.addAction("com.hwindiapp.passenger.ALARMCALLED");

        registerReceiver(get_alarm_broadcast_receiver, filter_alrmReceiver);
    }

    public void tripEnd(Context context) {
        tripEnd = true;

        Toast.makeText(context, "" + LBL_END_TRIP_DIALOG_TXT_str, Toast.LENGTH_LONG).show();

        calcFareAndDistance();
    }

    public void calcFareAndDistance() {

        if (Power_wl != null) {
            Power_wl.release();
        }

        stopDriverLocUpdateAfterTrip();

        new tripEnd_dialog_run(driver_image_final_url, user_id, driverId, trip_id_final, trip_number).run();

    }

    public void tripCanceledByDriver(String message) {

        if (tripCancelByDriver != null) {
            tripCancelByDriver.dismiss();
        }

        String reason = "";
        String isTripStarted = "";
        try {
            JSONObject jsonObj_temp = new JSONObject(message);

            reason = jsonObj_temp.getString("Reason");
            isTripStarted = jsonObj_temp.getString("isTripStarted");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        final String finalIsTripStarted = isTripStarted;
        builder.setMessage("Trip is cancelled by driver. Reason: " + reason).setCancelable(false).setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        tripCancelByDriver.dismiss();

                        if (finalIsTripStarted.equals("true")) {
                            goToRating(driver_image_final_url, user_id, driverId, trip_id_final, trip_number);
                        } else {
                            Intent send_data = new Intent(Mainprofile_Activity.this, Launcher_TaxiServiceActivity.class);
                            send_data.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(send_data);
                        }
                    }
                });
        tripCancelByDriver = builder.create();
        tripCancelByDriver.setCancelable(false);
        tripCancelByDriver.setCanceledOnTouchOutside(false);
        tripCancelByDriver.show();
    }

    public void update_userLocation() {

        if (latitude != 0.0 && longitude != 0.0) {

            START_DURATION_UPDATE_USER_LOCATION = Calendar.getInstance().getTimeInMillis();
            count_nearstTaxi++;

            if (marker_user != null) {
                marker_user.remove();
            }
            MarkerOptions marker_opt = new MarkerOptions().position(new LatLng(latitude, longitude))
                    .title("Hello " + Name);

            marker_opt.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location)).anchor(0.5f, 0.5f);

            marker_user = getMap().addMarker(marker_opt);

            double currentZoomLevel = getMap().getCameraPosition().zoom;

            if (CommonUtilities.defaultZommLevel > currentZoomLevel) {
                currentZoomLevel = CommonUtilities.defaultZommLevel;
            }

            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude))
                    .zoom((float) currentZoomLevel).build();

            if (onPause_animate == false && isFirstLocUpdate == true) {

                if (count_nearstTaxi == 1) {
                    getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else {
                    getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                isFirstLocUpdate = false;
            } else {
                onPause_animate = false;
            }

            if (circle != null) {

                circle.remove();
            }

            float mAccuracy = mCurrentLocation.getAccuracy();
            CircleOptions circleOptions = new CircleOptions().center(new LatLng(latitude, longitude)) // set
                    // center
                    .radius(mAccuracy) // set radius in meters
                    .fillColor(Color.argb(50, 100, 100, 255)) // semi-transparent
                    .strokeColor(Color.argb(150, 100, 100, 255)).strokeWidth(2);
            circle = getMap().addCircle(circleOptions);
        }
    }

    public int getZoomLevel(Circle circle) {
        int zoomLevel = 16;
        if (circle != null) {
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "" + LBL_DEVICE_NOT_SUPPORTED_TXT_str, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void createUserProfile() {
        Intent profil_int = new Intent(Mainprofile_Activity.this, UserProfileActivity.class);

        Bundle bn_send_data = new Bundle();
        bn_send_data.putString("Name", Name);
        bn_send_data.putString("Email", Email);
        bn_send_data.putString("fProfile_image_bitmap", vImgName_str);
        bn_send_data.putString("FBID", id_facebook);
        bn_send_data.putString("json_responseString_profile", json_responseString_profile);


        profil_int.putExtras(bn_send_data);
        startActivity(profil_int);

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            // selectItem(position);

            if (position == 0) {

                createUserProfile();

                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }

            if (position == 1) {
                Intent startBookingHistroy = new Intent(Mainprofile_Activity.this, BookingHistroyActivity.class);

                startBookingHistroy.putExtra("user_id", "" + user_id);
                startActivity(startBookingHistroy);

                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }

            if (position == 2) {
                Intent invite_start = new Intent(Mainprofile_Activity.this, ShareRefCodeActivity.class);
                invite_start.putExtra("user_id", "" + user_id);
                invite_start.putExtra("Json", json_responseString_profile);
                startActivity(invite_start);

                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }

            if (position == 3) {
                Intent about_start = new Intent(Mainprofile_Activity.this, AboutUsActivity.class);
                about_start.putExtra("user_id", "" + user_id);
                startActivity(about_start);

                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }

            if (position == 4) {
                Intent contact_start = new Intent(Mainprofile_Activity.this, ContactUsActivity.class);
                contact_start.putExtra("UserID", "" + user_id);
                startActivity(contact_start);

                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }

            if (position == 5) {
                Intent startHelp = new Intent(Mainprofile_Activity.this, HelpActivity.class);
                startHelp.putExtra("user_id", "" + user_id);
                startActivity(startHelp);

                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }

            if (position == 6) {
                signOut();
                ActivityCompat.finishAffinity(Mainprofile_Activity.this);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2155 && data != null) {

            DestinationLocationConfirm = true;

            if (destination_point_marker != null) {
                destination_point_marker.remove();
            }
            final String dest_latitude = data.getStringExtra("Selected_latitude");
            final String dest_longitude = data.getStringExtra("Selected_longitude");
            final String dest_address = data.getStringExtra("Selected_address");

            final float scale = getResources().getDisplayMetrics().density;
            int padding_dp = (int) (25 * scale + 0.5f);

            confirm_dest_location.setText("" + dest_address);
            confirm_dest_location.setVisibility(View.VISIBLE);
            line_start_end_point_img.setVisibility(View.VISIBLE);
            add_dest_btn.setVisibility(View.GONE);

            confirm_source_location.setPadding(padding_dp, 0, 0, 0);

            confirm_source_location.setCompoundDrawables(null, null, null, null);

            double dest_point_latitude = Double.parseDouble(dest_latitude);
            double dest_point_longitude = Double.parseDouble(dest_longitude);

            value_dest_latitude_loc = dest_point_latitude;
            value_dest_longitude_loc = dest_point_longitude;
            value_dest_address_loc = dest_address;

            MarkerOptions markerOptions_destinationLocation = new MarkerOptions();
            markerOptions_destinationLocation.position(new LatLng(value_dest_latitude_loc, value_dest_longitude_loc));

            markerOptions_destinationLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker))
                    .anchor(0.5f, 0.5f);

            // Adding marker on the Google Map
            destination_point_marker = getMap().addMarker(markerOptions_destinationLocation);

            confirm_dest_location.setOnTouchListener(new OnTouchListener() {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int leftEdgeOfRightDrawable = confirm_dest_location.getRight()
                                - confirm_dest_location.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                        int padding_destination_dp = (int) (30 * scale + 0.5f);

                        if (event.getRawX() >= leftEdgeOfRightDrawable) {

                            add_dest_btn.setVisibility(View.VISIBLE);
                            confirm_dest_location.setVisibility(View.GONE);

                            DestinationLocationConfirm = false;

                            line_start_end_point_img.setVisibility(View.GONE);
                            confirm_dest_location.setText("");

                            float scale = getResources().getDisplayMetrics().density;
                            int padding_dp = (int) (15 * scale + 0.5f);
                            confirm_source_location.setPadding(padding_dp, 0, 0, 0);
                            confirm_source_location.setCompoundDrawablesWithIntrinsicBounds(
                                    getResources().getDrawable(R.drawable.green_source), null, null, null);

                            return true;
                        }
                    }
                    return false;
                }
            });

            confirm_dest_location.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Intent intent = new Intent(Mainprofile_Activity.this, SelectDestinationActivity.class);
                    intent.putExtra("Selected_latitude", "" + dest_latitude);
                    intent.putExtra("Selected_longitude", "" + dest_longitude);
                    intent.putExtra("Selected_address", "" + dest_address);
                    startActivityForResult(intent, 2155);

                }
            });

        } else if (requestCode == PICK_CONTACT && data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    c = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.TYPE}, null, null, null);

                    if (c != null && c.moveToFirst()) {
                        String number = c.getString(0);
                        int type = c.getInt(1);

                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", "" + number);

                        String link_location = "http://maps.google.com/?q=" + latitude + "," + longitude;
                        smsIntent.putExtra("sms_body", LBL_SEND_STATUS_CONTENT_TXT_str + " " + link_location);
                        startActivity(smsIntent);
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }

        } else if (requestCode == SELECT_DESTINATION_POINT_AFTER_TRIP_ACCEPT) {
            if (resultCode == RESULT_OK) {
                final String dest_latitude = data.getStringExtra("Selected_latitude");
                final String dest_longitude = data.getStringExtra("Selected_longitude");
                final String dest_address = data.getStringExtra("Selected_address");

                new addDestDuringTrip(dest_latitude, dest_longitude, dest_address).execute();
            }
        } else if (requestCode == SELECT_PICKUP_POINT && resultCode == RESULT_OK && data != null) {
            final String pickUp_latitude = data.getStringExtra("Selected_latitude");
            final String pickUp_longitude = data.getStringExtra("Selected_longitude");
            final String dest_address = data.getStringExtra("Selected_address");

            double pickUp_point_latitude = Double.parseDouble(pickUp_latitude);
            double pickUp_point_longitude = Double.parseDouble(pickUp_longitude);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(pickUp_point_latitude, pickUp_point_longitude))
                    .zoom(getMap().getCameraPosition().zoom).build();
            getMap().moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

		/*
         * else if (requestCode == 2156 && data != null) {
		 * DestinationLocationConfirm = true;
		 *
		 * if (destination_point_marker != null) {
		 * destination_point_marker.remove(); } final String dest_latitude =
		 * data.getStringExtra("Selected_latitude"); final String dest_longitude
		 * = data.getStringExtra("Selected_longitude"); final String
		 * dest_address = data.getStringExtra("Selected_address");
		 *
		 * double dest_point_latitude = Double.parseDouble(dest_latitude);
		 * double dest_point_longitude = Double.parseDouble(dest_longitude);
		 * trip_start_add_dest_btn.setVisibility(View.GONE);
		 * value_dest_latitude_loc = dest_point_latitude;
		 * value_dest_longitude_loc = dest_point_longitude;
		 * value_dest_address_loc = dest_address;
		 *
		 * setDestionPoint(); }
		 */

        else {
//            Toast.makeText(Mainprofile_Activity.this, "" + LBL_ACT_RESULTS_CANCEL_TXT_str, Toast.LENGTH_LONG).show();
        }
    }

    public void signOut() {
        SharedPreferences mPrefs;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(Mainprofile_Activity.this);

        mPrefs.edit().remove("access_sign_token_email").commit();
        mPrefs.edit().remove("access_sign_token_pass").commit();
        mPrefs.edit().remove("access_sign_token_user_id_auto").commit();
        mPrefs.edit().clear().commit();

        signOut = true;
        Intent signOut_intent_to_Main = new Intent(getApplicationContext(), Launcher_TaxiServiceActivity.class);
        signOut_intent_to_Main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        signOut_intent_to_Main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        finish();
        startActivity(signOut_intent_to_Main);

        System.gc();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkPlayServices()) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        if (language_changed == true) {

            getLanguageLabelsFrmSqLite();

            CreateSlideMenu();
            language_changed = false;
        }

        try {
            finalize();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (driver_assigned == false) {
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(Mainprofile_Activity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(Mainprofile_Activity.this, 0, intent, 0);

            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                    ONLINE_DRIVER_LIST_UPDATE_TIME_INTERVAL * 60 * 1000, pendingIntent);

            IntentFilter filter_alrmReceiver = new IntentFilter();
            filter_alrmReceiver.addAction("com.hwindiapp.passenger.ALARMCALLED");

            registerReceiver(get_alarm_broadcast_receiver, filter_alrmReceiver);
        } else if (driver_assigned == true) {
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(Mainprofile_Activity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(Mainprofile_Activity.this, 0, intent, 0);

            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                    1 * DRIVER_LOC_FETCH_TIME_INTERVAL * 1000, pendingIntent);

            IntentFilter filter_alrmReceiver = new IntentFilter();
            filter_alrmReceiver.addAction("com.hwindiapp.passenger.ALARMCALLED");

            registerReceiver(get_alarm_broadcast_receiver, filter_alrmReceiver);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hwindiapp.passenger.DriverCalling");

        registerReceiver(broad_DriverCallBack_receiver, filter);

        if (updateProfileTrue == true) {
            String Name_update_json = "";
            json_responseString_profile = updated_json_responseString_profile;
            String parse_profile = updated_json_responseString_profile;

            JSONObject jsonObject_userDetail_update = null;
            try {
                jsonObject_userDetail_update = new JSONObject(parse_profile);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                Name_update_json = jsonObject_userDetail_update.getString("vName") + " "
                        + jsonObject_userDetail_update.getString("vLastName");
            } catch (Exception e) {

            }
            profile_header_name.setText("" + Name_update_json);

            setConfigurationData();
            updateProfileTrue = false;

            final int currentSelectedCab = selectedCarType;

            if (carTypeDetailList.size() == selectedCarType) {
                csb.setSelection(0);
                csb.setSelection(currentSelectedCab - 1);
            } else {
                csb.setSelection(currentSelectedCab);
                csb.setSelection(currentSelectedCab - 1);
            }

        }

        if (tripStart == true) {
            updateDriverLocAfterStartTrip.startUpdatingLocations();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (signOut == true) {
            finish();
        }
        if (checkPlayServices() && mGoogleApiClient.isConnected()) {

            if (tripStart == false && tripEnd == true) {
                stopLocationUpdates();
            }

        }

        try {
            finalize();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        onPause_animate = true;

        alarmManager.cancel(pendingIntent);

        if (tripStart == true) {
            stopDriverLocUpdateAfterTrip();
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        unregisterReceiver(broad_DriverCallBack_receiver);

        try {
            unregisterReceiver(get_alarm_broadcast_receiver);
        } catch (Exception e) {
            // TODO: handle exception
        }

        stopDriverLocUpdateAfterTrip();
    }

    public void stopDriverLocUpdateAfterTrip() {
        if (updateDriverLocAfterStartTrip != null) {
            updateDriverLocAfterStartTrip.stopUdatingLocation();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (confirm_btn_frame != null && confirm_btn_frame.getVisibility() == View.VISIBLE) {

                setPanelHeightToDefault();

                header_img.setVisibility(View.VISIBLE);
                img_navigation_drawer.setVisibility(View.VISIBLE);
                back_navigation.setVisibility(View.GONE);
                confirm_source_text_header.setVisibility(View.GONE);
                source_selection_confirm.setVisibility(View.GONE);

                confirm_btn_frame.setVisibility(View.GONE);
                taxiSelectionLayout.setVisibility(View.VISIBLE);
                marker_source_selection_AREA.setVisibility(View.VISIBLE);
                source_selection.setVisibility(View.VISIBLE);

                sourceLocationConfirm = false;

                if (destination_point_marker != null) {
                    destination_point_marker.remove();
                    destination_point_marker = null;
                }

                return true;
            }

            if (main_layout_profile != null && main_layout_profile
                    .getVisibility() == View.VISIBLE /* && slidemenu != null */
                    && isShow_slideMenu == false) {
                finish();
            }

            return true;
        } else {
            // Return
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {

        // mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(Mainprofile_Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(Mainprofile_Activity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
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

        mCurrentLocation = location;

        fromLocationChanged = true;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if (START_DURATION_UPDATE_USER_LOCATION != 0) {
            long updateDuration = Calendar.getInstance().getTimeInMillis() - START_DURATION_UPDATE_USER_LOCATION;

            if (updateDuration > MAX_DURATION_UPDATE_USER_LOCATION) {

                update_userLocation();
            }

        } else {
            update_userLocation();
        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(Mainprofile_Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(Mainprofile_Activity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();

            update_userLocation();

        } else {

        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // TODO Auto-generated method stub

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

        mGoogleApiClient.connect();
    }

    class Cancel_warning_dialog_run implements Runnable {

        boolean error_connection_server;

        public Cancel_warning_dialog_run(boolean flag_error_serverConnection) {
            // TODO Auto-generated constructor stub
            this.error_connection_server = flag_error_serverConnection;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

            final Dialog dialog_check_internet = new Dialog(Mainprofile_Activity.this,
                    R.style.DialogInternet_checkSlideAnim);

            dialog_check_internet.setContentView(R.layout.custom_dialog_no_internet);

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

            RippleStyleButton retry_btn = (RippleStyleButton) dialog_check_internet
                    .findViewById(R.id.retry_btn_internet);
            RippleStyleButton cancle_btn = (RippleStyleButton) dialog_check_internet
                    .findViewById(R.id.cancle_btn_internet);

            MyTextView check_internet_conn_one = (MyTextView) dialog_check_internet
                    .findViewById(R.id.check_internet_conn_one);

            if (error_connection_server == true) {
                check_internet_conn_one.setText("" + LBL_TRIP_CANCEL_TXT_str);
            }

            Window window = dialog_check_internet.getWindow();
            window.setGravity(Gravity.CENTER);

            window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            LayoutParams params_retry_btn = retry_btn.getLayoutParams();
            params_retry_btn.width = width / 3;

            retry_btn.setLayoutParams(params_retry_btn);

            LayoutParams params_cancle_btn = cancle_btn.getLayoutParams();
            params_cancle_btn.width = width / 3;
            cancle_btn.setLayoutParams(params_cancle_btn);

            retry_btn.setColor(getResources().getColor(R.color.btn_dialog_hover_color));
            retry_btn.setClipRadius(2);

            cancle_btn.setColor(getResources().getColor(R.color.btn_dialog_hover_color));
            cancle_btn.setClipRadius(2);

            cancle_btn.setText("" + LBL_CANCEL_BTN_TXT_NO_INTERNET_str);

            retry_btn.setText("" + LBL_BTN_TRIP_CANCEL_CONFIRM_TXT_str);
            retry_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog_check_internet.dismiss();
                    // check_intAgain(dialog_check_internet);
                    new cancleTrip(driver_detail_layout_frame, trip_id_final, driverId).execute();
                }
            });

            cancle_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog_check_internet.dismiss();
                    // finish();
                }
            });

            dialog_check_internet.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            dialog_check_internet.show();
            dialog_check_internet.setCanceledOnTouchOutside(false);
            dialog_check_internet.setCancelable(false);

        }

    }

    class tripStart_dialog_run implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            final Dialog dialog_check_internet = new Dialog(Mainprofile_Activity.this,
                    R.style.DialogInternet_checkSlideAnim);

            dialog_check_internet.setContentView(R.layout.custom_dialog_no_internet);

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

            RippleStyleButton cancle_btn_internet = (RippleStyleButton) dialog_check_internet
                    .findViewById(R.id.cancle_btn_internet);

            cancle_btn_internet.setVisibility(View.GONE);

            RippleStyleButton retry_btn = (RippleStyleButton) dialog_check_internet
                    .findViewById(R.id.retry_btn_internet);

            MyTextView check_internet_conn_one = (MyTextView) dialog_check_internet
                    .findViewById(R.id.check_internet_conn_one);

            check_internet_conn_one.setText("" + LBL_START_TRIP_DIALOG_TXT_str);


            Window window = dialog_check_internet.getWindow();
            window.setGravity(Gravity.CENTER);

            window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            LayoutParams params_retry_btn = retry_btn.getLayoutParams();
            params_retry_btn.width = width - 40;

            retry_btn.setLayoutParams(params_retry_btn);

            retry_btn.setColor(getResources().getColor(R.color.btn_dialog_hover_color));
            retry_btn.setClipRadius(2);

            retry_btn.setText("" + LBL_BTN_OK_TXT_str);
            retry_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog_check_internet.dismiss();
                    // check_intAgain(dialog_check_internet);

                }
            });

            dialog_check_internet.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            dialog_check_internet.show();
            dialog_check_internet.setCanceledOnTouchOutside(false);
            dialog_check_internet.setCancelable(false);

        }

    }

    class tripEnd_dialog_run implements Runnable {

        String driver_image_final_url = "";
        String user_id = "";
        String driverId = "";
        String trip_id_final = "";
        String trip_number = "";
        // String totalDistance = "";

        public tripEnd_dialog_run(String driver_image_final_url, String user_id, String driverId, String trip_id_final,
                                  String trip_number/* , String totalDistance */) {
            // TODO Auto-generated constructor stub
            this.driver_image_final_url = driver_image_final_url;
            this.user_id = user_id;
            this.driverId = driverId;
            this.trip_id_final = trip_id_final;
            this.trip_number = trip_number;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

            final Dialog dialog_check_internet = new Dialog(Mainprofile_Activity.this,
                    R.style.DialogInternet_checkSlideAnim);

            dialog_check_internet.setContentView(R.layout.custom_dialog_no_internet);

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

            RippleStyleButton retry_btn = (RippleStyleButton) dialog_check_internet
                    .findViewById(R.id.retry_btn_internet);

            RippleStyleButton cancle_btn_internet = (RippleStyleButton) dialog_check_internet
                    .findViewById(R.id.cancle_btn_internet);

            cancle_btn_internet.setVisibility(View.GONE);
            MyTextView check_internet_conn_one = (MyTextView) dialog_check_internet
                    .findViewById(R.id.check_internet_conn_one);

            check_internet_conn_one.setText("" + LBL_END_TRIP_DIALOG_TXT_str);

            Window window = dialog_check_internet.getWindow();
            window.setGravity(Gravity.CENTER);

            window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            LayoutParams params_retry_btn = retry_btn.getLayoutParams();
            params_retry_btn.width = width - 40;

            retry_btn.setLayoutParams(params_retry_btn);

            retry_btn.setColor(getResources().getColor(R.color.btn_dialog_hover_color));
            retry_btn.setClipRadius(2);

            retry_btn.setText("" + LBL_BTN_OK_TXT_str);
            retry_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog_check_internet.dismiss();

//                    Intent startRating_page = new Intent(Mainprofile_Activity.this,
//                            User_to_Driver_ratingActivity.class);
//                    startRating_page.putExtra("driver_img_url", driver_image_final_url);
//                    startRating_page.putExtra("user_id", user_id);
//                    startRating_page.putExtra("driver_id", driverId);
//                    startRating_page.putExtra("trip_id_final", trip_id_final);
//                    startRating_page.putExtra("cash_payment", "" + cashPayment);
//                    startRating_page.putExtra("trip_number", trip_number);
//                    startRating_page.putExtra("fromMainProfile", "true");
//                    startRating_page.putExtra("profile_json", "" + json_responseString_profile);
//
//                    startRating_page.putExtra("paymentEnabledOrNot", paymentEnabledOrNot);
//                    startActivity(startRating_page);

                    goToRating(driver_image_final_url, user_id, driverId, trip_id_final, trip_number);

                }
            });

            dialog_check_internet.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            dialog_check_internet.show();
            dialog_check_internet.setCanceledOnTouchOutside(false);
            dialog_check_internet.setCancelable(false);

        }

    }

    public void goToRating(String driver_image_final_url, String user_id, String driverId, String trip_id_final,
                           String trip_number) {
        Intent startRating_page = new Intent(Mainprofile_Activity.this,
                User_to_Driver_ratingActivity.class);
        startRating_page.putExtra("driver_img_url", driver_image_final_url);
        startRating_page.putExtra("user_id", user_id);
        startRating_page.putExtra("driver_id", driverId);
        startRating_page.putExtra("trip_id_final", trip_id_final);
        startRating_page.putExtra("cash_payment", "" + cashPayment);
        startRating_page.putExtra("trip_number", trip_number);
        startRating_page.putExtra("fromMainProfile", "true");
        startRating_page.putExtra("profile_json", "" + json_responseString_profile);

        startRating_page.putExtra("paymentEnabledOrNot", paymentEnabledOrNot);
        startActivity(startRating_page);
    }

    public void CustomNotification(String time, String numberPlate, String user_name, String driver_name,
                                   Bitmap user_img_bm, String status_str, boolean skip) {

        long currentTime = System.currentTimeMillis();


        if (skip == false) {
            if (previousNotificationTime != 0 && (currentTime - previousNotificationTime) < 60000) {
                return;
            } else {
                previousNotificationTime = currentTime;
            }
        }

        notification_count++;

        Notification foregroundNote = null;

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(Mainprofile_Activity.this.getPackageName(),
                R.layout.notification_layout);
        Intent intent = new Intent(Mainprofile_Activity.this, CancelNotificationReceiver.class);
        intent.putExtra("notificationId", NOTIFICATION_ID);

        // Create the PendingIntent
        PendingIntent btPendingIntent = PendingIntent.getBroadcast(Mainprofile_Activity.this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(Mainprofile_Activity.this);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewBitmap(R.id.user_img, createRoundedBitmap(user_img_bm));

        if (bmp_driver_pic == null) {
            bmp_driver_pic = BitmapFactory.decodeResource(Mainprofile_Activity.this.getResources(),
                    R.drawable.friends_main);

        }

        remoteViews.setImageViewBitmap(R.id.driver_img, createRoundedBitmap(bmp_driver_pic));

        remoteViews.setTextViewText(R.id.txt_user_name, "" + user_name);
        remoteViews.setTextViewText(R.id.txt_driver_name, "" + driver_name);
        remoteViews.setTextViewText(R.id.car_number_txt, "" + driverCarNumberPlate_str);
        remoteViews.setTextViewText(R.id.status_txt, "" + status_str);
        remoteViews.setTextViewText(R.id.time_txt, "" + time);
        foregroundNote = builder.setSmallIcon(R.mipmap.ic_launcher)
                // Set Ticker Message
                .setTicker("Hello")
                // Set Title
                .setContentTitle("Hello")
                // Set Text
                .setContentText("Hello").setPriority(2)
                // Dismiss Notification
                .setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true).setContent(remoteViews)
                // Set PendingIntent into Notification
                .setContentIntent(btPendingIntent).build();

        foregroundNote.priority = NotificationCompat.FLAG_HIGH_PRIORITY;

        foregroundNote.bigContentView = remoteViews;
        // Create Notification Manager

        if (notificationmanager != null) {
            notificationmanager = null;
        }

        notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager

        notificationmanager.notify(NOTIFICATION_ID, foregroundNote);

    }

    public Bitmap createRoundedBitmap(Bitmap bitmap) {
        // int targetWidth = 50;
        // int targetHeight = 50;

        int targetWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
                getResources().getDisplayMetrics());

        int targetHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
                getResources().getDisplayMetrics());

        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        BitmapShader shader;
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        paint.setStyle(Paint.Style.STROKE);
        canvas.clipPath(path);
        Bitmap sourceBitmap = bitmap;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);

        return targetBitmap;
    }

    @Override
    public void handleNoInternetBTNCallback(int id_btn) {
        // TODO Auto-generated method stub
        if (id_btn == 0) {

            RetryOnSourcePointSelect();

        } else if (id_btn == 1) {

        }
    }

}
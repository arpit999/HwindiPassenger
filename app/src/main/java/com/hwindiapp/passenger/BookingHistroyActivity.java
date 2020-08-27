package com.hwindiapp.passenger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.VolleyLibFiles.BookingHistroy_item;
import com.VolleyLibFiles.CustomListAdapter;
import com.fonts.Text.MyTextView;
import com.mainProfile.classFiles.OutputStreamReader;
import com.utils.CommonUtilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BookingHistroyActivity extends AppCompatActivity {

	// ActionBar actionbar;
	TextView text_header;

	String bookingURL = CommonUtilities.SERVER_URL + "&function=getBookingDetail_user";
	CustomListAdapter adapter;
	private List<BookingHistroy_item> bookingItemList = new ArrayList<BookingHistroy_item>();

	ListView list_bookingHistroy;
	String user_id = "";

	ArrayList<String> list_language_labels = new ArrayList<String>();
	DBConnect dbConnect;

	String language_labels_get_frm_sqLite = "";
	String LBL_AT_TXT_str = "";
	String LBL_DOLLAR_SIGN_TXT_str = "";
	String LBL_CANCELED_TXT_str = "";
	String LBL_ON_WAY_TXT_str = "";
	String LBL_FINISHED_TXT_str = "";
	String LBL_NO_RIDES_TXT_str = "";
	String LBL_BOOKING_HISTROY_TXT_str = "";
	String LBL_CHECK_INTERNET_TXT_str = "";

	ProgressBar loading_booking_items;

	MyTextView no_history;
	// MyTextView header_txt;

	View footerView;

	String nextPage_str = "";

	boolean loadingMore = false;
	boolean isNextPageAvailable = false;

	AlertDialog alert_error_internet;

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking_histroy);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);

		Intent getuserId = getIntent();

		user_id = getuserId.getStringExtra("user_id");

		bookingURL = bookingURL + "&UserId=" + user_id;

		dbConnect = new DBConnect(this, "UC_labels.db");

		no_history = (MyTextView) findViewById(R.id.no_history);
		// header_txt = (MyTextView) findViewById(R.id.header_txt);
		text_header = (TextView) findViewById(R.id.text_header);

		/* Set Labels */
		getLanguageLabelsFrmSqLite();
		/* Set Labels Finished */

		adapter = new CustomListAdapter(this, bookingItemList, list_language_labels);

		list_bookingHistroy = (ListView) findViewById(R.id.list_bookingHistroy);

		list_bookingHistroy.setAdapter(adapter);

		footerView = ((LayoutInflater) BookingHistroyActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.booking_history_list_footer, null, false);

		footerView.setVisibility(View.GONE);
		footerView.setPadding(0, -1 * footerView.getHeight(), 0, 0);

		list_bookingHistroy.addFooterView(footerView);

		loading_booking_items = (ProgressBar) findViewById(R.id.loading_booking_items);

		ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
		back_navigation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BookingHistroyActivity.super.onBackPressed();
			}
		});

		new getBookingJson("0").execute();

		list_bookingHistroy.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView trip_json_str = (TextView) view.findViewById(R.id.trip_json_str);

				NetworkRoundedImageView thumbNail = (NetworkRoundedImageView) view.findViewById(R.id.img_driver);

				Drawable img_driver_profile_pic = null;

				if (thumbNail.getDrawable() != null) {
					img_driver_profile_pic = thumbNail.getDrawable();
				} else {
					img_driver_profile_pic = getResources().getDrawable(R.drawable.friends_main);
				}

				ByteArrayOutputStream stream_bit_driver_pic = new ByteArrayOutputStream();

				Bitmap bit_driver_pic = ((BitmapDrawable) img_driver_profile_pic).getBitmap();

				if(bit_driver_pic != null){
					bit_driver_pic.compress(Bitmap.CompressFormat.JPEG, 50, stream_bit_driver_pic);
				}else{
					bit_driver_pic = ((BitmapDrawable) getResources().getDrawable(R.drawable.friends_main)).getBitmap();
					bit_driver_pic.compress(Bitmap.CompressFormat.JPEG, 50, stream_bit_driver_pic);
				}

				byte[] byteArray_bit_driver_pic = stream_bit_driver_pic.toByteArray();

				String json_particularTrip = trip_json_str.getText().toString();

				Intent start_booking_detail_page = new Intent(BookingHistroyActivity.this, BookingDetailActivity.class);

				start_booking_detail_page.putExtra("driver_profile_pic", byteArray_bit_driver_pic);

				start_booking_detail_page.putExtra("trip_json_str", "" + json_particularTrip);
				startActivity(start_booking_detail_page);

			}
		});

		list_bookingHistroy.setOnScrollListener(new OnScrollListener() {
			// useless here, skip!
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			// dumdumdum
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// what is the bottom iten that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;
				// is the bottom item visible & not loading more already ? Load
				// more !
				if ((lastInScreen == totalItemCount) && !(loadingMore) && isNextPageAvailable == true) {

					loadingMore = true;

					new getBookingJson("" + nextPage_str).execute();
				}
			}
		});

	}

	public void getLanguageLabelsFrmSqLite() {

		Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

		cursor.moveToPosition(0);

		language_labels_get_frm_sqLite = cursor.getString(0);

		JSONObject obj_language_labels = null;

		try {
			obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
			LBL_AT_TXT_str = obj_language_labels.getString("LBL_AT_TXT");
			LBL_DOLLAR_SIGN_TXT_str = obj_language_labels.getString("LBL_DOLLAR_SIGN_TXT");
			LBL_CANCELED_TXT_str = obj_language_labels.getString("LBL_CANCELED_TXT");
			LBL_ON_WAY_TXT_str = obj_language_labels.getString("LBL_ON_WAY_TXT");
			LBL_FINISHED_TXT_str = obj_language_labels.getString("LBL_FINISHED_TXT");
			LBL_NO_RIDES_TXT_str = obj_language_labels.getString("LBL_NO_RIDES_TXT");
			LBL_BOOKING_HISTROY_TXT_str = obj_language_labels.getString("LBL_BOOKING_HISTROY_TXT");
			LBL_CHECK_INTERNET_TXT_str = obj_language_labels.getString("LBL_CHECK_INTERNET_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {

			list_language_labels.add("" + LBL_AT_TXT_str);
			list_language_labels.add("" + LBL_DOLLAR_SIGN_TXT_str);
			list_language_labels.add("" + LBL_CANCELED_TXT_str);
			list_language_labels.add("" + LBL_ON_WAY_TXT_str);
			list_language_labels.add("" + LBL_FINISHED_TXT_str);

			no_history.setText("" + LBL_NO_RIDES_TXT_str);
			text_header.setText("" + LBL_BOOKING_HISTROY_TXT_str);
		}

	}

	private void buildAlertMsgErrorInternet(final boolean noData) {

		if (alert_error_internet != null) {
			alert_error_internet.dismiss();
		}

		final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LightThemeGPSalert);
		builder.setMessage("" + LBL_CHECK_INTERNET_TXT_str).setCancelable(false).setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						// dialog.cancel();

						if (noData == true) {
							alert_error_internet.dismiss();
							finish();
						} else {
							list_bookingHistroy.setSelectionAfterHeaderView();
							alert_error_internet.dismiss();
						}

					}
				});
		alert_error_internet = builder.create();
		alert_error_internet.show();
	}

	public class getBookingJson extends AsyncTask<String, String, String> {

		String responseString = "";
		String json_callback = "";

		String page_str = "";

		boolean errorTOGetData = false;

		public getBookingJson(String page_str) {
			// TODO Auto-generated constructor stub
			this.page_str = page_str;
		}

		public String download_json_booking() {

			String final_bookingURL = bookingURL;
			if (page_str != null && !page_str.trim().equals("") && !page_str.trim().equals("0")) {
				final_bookingURL = final_bookingURL + "&page=" + page_str;
			}

			Log.d("final_bookingURL","::"+final_bookingURL);
			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL(final_bookingURL);
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				responseString = OutputStreamReader.readStream(in);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorTOGetData = true;
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

			json_callback = download_json_booking();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// Log.e("on post Booking detail:", "" + errorTOGetData);
			booking_detail_process(errorTOGetData, json_callback);
		}

	}

	public void booking_detail_process(boolean error, String json_all_booking_details) {

		loadingMore = false;

		if (error == false && json_all_booking_details != null && !json_all_booking_details.trim().equals("")
				&& json_all_booking_details.trim().length() > 4) {

			JSONObject response_update;
			JSONArray booking_array_json = null;
			String nextPage_avail_str = "";

			try {
				response_update = new JSONObject(json_all_booking_details);
				booking_array_json = response_update.getJSONArray("getBookingDetail_user");
				nextPage_avail_str = response_update.getString("NextPage");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// bookingItemList.clear();

			loading_booking_items.setVisibility(View.GONE);
			// Parsing json
			if (booking_array_json != null && booking_array_json.length() > 0) {

				for (int i = 0; i < booking_array_json.length(); i++) {
					try {

						// Log.d("json_particular_trip:",
						// ":"+json_particular_trip);
						JSONObject obj = booking_array_json.getJSONObject(i);

						String json_particular_trip = obj.toString();

						JSONObject obj_driverDetail = obj.getJSONObject("DriverDetails");
						BookingHistroy_item setCouponDetail = new BookingHistroy_item();
						setCouponDetail.setdriver_id(obj_driverDetail.getString(CommonUtilities.iDriverId));
						setCouponDetail.setStartDate(obj.getString("tTripRequestDate"));
						setCouponDetail.setlatitude(obj.getString("tStartLat"));
						setCouponDetail.setlongitude(obj.getString("tStartLong"));
						setCouponDetail.settotal_fare(obj.getString("iFare"));
						setCouponDetail.setCurrencySymbol(obj.getString("CurrencySymbol"));
						String eCancelled = obj.getString("eCancelled");
						if(eCancelled.equals("Yes")){
							setCouponDetail.setstatus("Cancelled");
						}else{
							setCouponDetail.setstatus(obj.getString("iActive"));
						}

						setCouponDetail.setDriverName(obj_driverDetail.getString(CommonUtilities.vName_driver));

						setCouponDetail.setTrip_json(json_particular_trip);

						setCouponDetail.setdriver_img(obj_driverDetail.getString(CommonUtilities.vDriverImg));

						setCouponDetail.setCarType(obj.getString("vVehicleType"));

						bookingItemList.add(setCouponDetail);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				if (nextPage_avail_str != null && !nextPage_avail_str.trim().equals("")
						&& !nextPage_avail_str.trim().equals("0")) {
					nextPage_str = nextPage_avail_str;
					isNextPageAvailable = true;

					footerView.setVisibility(View.VISIBLE);
					footerView.setPadding(0, 0, 0, 0);
				} else {
					// Log.d("list_container",
					// "list_container:"+list_container.size());
					nextPage_str = "";
					isNextPageAvailable = false;

					footerView.setVisibility(View.GONE);
					footerView.setPadding(0, -1 * footerView.getHeight(), 0, 0);
				}

				// notifying list adapter about data changes
				// so that it renders the list view with updated data
				adapter.notifyDataSetChanged();
				// list_bookingHistroy.setAdapter(adapter);

			} else {
				no_history.setVisibility(View.VISIBLE);
			}

		} else {
			if (bookingItemList.size() == 0) {
				buildAlertMsgErrorInternet(true);
			} else {
				buildAlertMsgErrorInternet(false);
			}

		}
	}

}

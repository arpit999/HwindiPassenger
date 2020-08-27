package com.hwindiapp.passenger;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fonts.Text.MyEditText;
import com.listAdapter.files.MessageAdapter;
import com.listAdapter.files.Message_user_driver;
import com.mainProfile.classFiles.OutputStreamReader;
import com.mainProfile.classFiles.UpdateMessageFrmDriver;
import com.utils.CommonUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/

public class UserTODriverMessagingActivity extends AppCompatActivity
		implements com.mainProfile.classFiles.UpdateMessageFrmDriver.NewMessageListner {

	// ActionBar actionbar;
	TextView text_header;

	UpdateMessageFrmDriver updateMessageFrmDriver;

	SQLiteDatabase db;
	MessageAdapter message_adapter;
	public static DBConnect dbConnect;
	List<Message_user_driver> message_list;

	String user_id = "";
	String driver_id = "";

	String name_passenger = "";

	String name_user = "";

	String DriverName = "";
	String GeneratedtripID_str = "";

	RippleStyleButton sendCmt_btn;
	MyEditText write_comment;
	ListView list;

	// MyTextView header_text;

	DBConnect dbConnect_languages;

	String language_labels_get_frm_sqLite = "";

	String LBL_MESSAGE_PAGE_HEADER_TXT_str = "";
	String LBL_WRITE_MESSAGE_HINT_TXT_str = "";
	String LBL_BTN_SEND_TXT_str = "";

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_todriver_messaging);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);

		// broad_DriverCallBack_receiver = new
		// GCM_broadCastReceiver_call_frm_driver();
		// broad_DriverCallBack_receiver.setUserToDriverMessagingActivityHandler(UserTODriverMessagingActivity.this);

		// actionbar = getSupportActionBar();
		// actionbar.setCustomView(R.layout.cutom_action_bar_user_profile);
		//
		// actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |
		// ActionBar.DISPLAY_SHOW_HOME);
		//
		// actionbar.setDisplayUseLogoEnabled(false);
		// actionbar.setDisplayShowHomeEnabled(true);
		//
		// actionbar.setLogo(null); // forgot why this one but it
		// // helped
		//
		// View homeIcon = findViewById(Build.VERSION.SDK_INT >=
		// Build.VERSION_CODES.HONEYCOMB ? android.R.id.home
		// : android.support.v7.appcompat.R.id.home);
		// ((View) homeIcon.getParent()).setVisibility(View.GONE);
		// ((View) homeIcon).setVisibility(View.GONE);
		//
		// actionbar.setDisplayShowTitleEnabled(false);

		updateMessageFrmDriver = new UpdateMessageFrmDriver();
		updateMessageFrmDriver.setNewMessageListener(this);

		Intent intent_getID = getIntent();
		user_id = intent_getID.getStringExtra("UserID");
		driver_id = intent_getID.getStringExtra("DriverID");
		name_user = intent_getID.getStringExtra("UserName");
		DriverName = intent_getID.getStringExtra("DriverName");
		GeneratedtripID_str = intent_getID.getStringExtra("GeneratedtripID");

		String timeStamp = new SimpleDateFormat("MMMM dd yyyy ## hh:mm a").format(Calendar.getInstance().getTime());

		String CurrentTIme = timeStamp.replace("##", "at");

		com.fonts.Text.MyTextView time_area = (com.fonts.Text.MyTextView) findViewById(R.id.time_area);
		time_area.setText(" " + CurrentTIme + " ");

		// header_text = (MyTextView) findViewById(R.id.header_text);
		text_header = (TextView) findViewById(R.id.text_header);
		write_comment = (MyEditText) findViewById(R.id.write_comment);
		sendCmt_btn = (RippleStyleButton) findViewById(R.id.sendCmt_btn);

		list = (ListView) findViewById(R.id.list);

		// UserTODriverMessagingActivity.dbConnect = new
		// DBConnect(UserTODriverMessagingActivity.this, "txt_messagdDB.db");
		UserTODriverMessagingActivity.dbConnect = new DBConnect(UserTODriverMessagingActivity.this,
				"UberCloneMessageDB.db");

		dbConnect_languages = new DBConnect(this, "UC_labels.db");

		/* Set Labels */
		getLanguageLabelsFrmSqLite();
		/* Set Labels Finished */

		write_comment.addTextChangedListener(new TextWatcher() {

			String text_message = write_comment.getText().toString();
			int char_message = text_message.length();

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// if (char_message > 1) {
				//
				// sendCmt_btn.setEnabled(true);
				// }
			}
		});

		ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
		back_navigation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserTODriverMessagingActivity.super.onBackPressed();
			}
		});

		sendCmt_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text_message = write_comment.getText().toString();
				write_comment.setText("");
				new sendMessageToDriver("" + text_message).execute();
				insert("Passenger:" + text_message);
			}
		});

		// Cursor cursor1 = UserTODriverMessagingActivity.dbConnect
		// .execQuery("CREATE TABLE IF NOT EXISTS Record");
		// insert();

		// upDateItemList();
	}

	public class sendMessageToDriver extends AsyncTask<String, String, String> {

		String message = "";
		String responseString = "";
		String callBackString = "";

		public sendMessageToDriver(String message) {
			// TODO Auto-generated constructor stub
			this.message = message;
		}

		public String callDriverGCM() {

			// String baseUrl = SERVER_URL_SEND_MESSAGE +
			// "send_push_notification_message.php?";
			String baseUrl = CommonUtilities.SERVER_URL;
			String registerParam = "function=%s&DautoId=%s&UautoId=%s&message_rec=%s&message=%s&tripID=%s";

			if (URLEncoder.encode(user_id) != null) {

				String message_toDriver = user_id + ":Driver::-callMessageToDriver:Passenger:";

				// message_toDriver = "Driver::-" +
				// message_toDriver+":"+message;

				String registerUrl = baseUrl + String.format(registerParam, URLEncoder.encode("callToDriver_Message"),
						URLEncoder.encode(driver_id), URLEncoder.encode(user_id), URLEncoder.encode(message_toDriver),
						URLEncoder.encode(message), "" + GeneratedtripID_str);
//				HttpClient client = new DefaultHttpClient();
//				// Log.e("Message to driver Url", ":" + registerUrl);
//
//				HttpGet httpGet = new HttpGet(registerUrl);
//				try {
//					HttpResponse response1 = client.execute(httpGet);
//
//					HttpEntity httpEntity = response1.getEntity();
//					responseString = EntityUtils.toString(httpEntity);
//					// Log.e("Message Response:", "responseString : " +
//					// responseString);
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
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
			callBackString = callDriverGCM();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			UserTODriverMessagingActivity.this
					.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
			UserTODriverMessagingActivity.this
					.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));
		}

	}

	public void getLanguageLabelsFrmSqLite() {

		Cursor cursor = dbConnect_languages.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

		cursor.moveToPosition(0);

		language_labels_get_frm_sqLite = cursor.getString(0);

		JSONObject obj_language_labels = null;
		try {
			obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
			LBL_MESSAGE_PAGE_HEADER_TXT_str = obj_language_labels.getString("LBL_MESSAGE_PAGE_HEADER_TXT");
			LBL_WRITE_MESSAGE_HINT_TXT_str = obj_language_labels.getString("LBL_WRITE_MESSAGE_HINT_TXT");
			LBL_BTN_SEND_TXT_str = obj_language_labels.getString("LBL_BTN_SEND_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {
			text_header.setText("" + LBL_MESSAGE_PAGE_HEADER_TXT_str);

			write_comment.setHint("" + LBL_WRITE_MESSAGE_HINT_TXT_str);

			sendCmt_btn.setText("" + LBL_BTN_SEND_TXT_str);
		}

	}

	public void insert(String txt_messages) {
		// String txt_messages = "HI hello How Are You";

		String sql = "insert into Messages(iTripId,iDriver_id,iUser_id,tMessagesDriverUser) values('%s','%s','%s','%s')";

		sql = String.format(sql, GeneratedtripID_str, driver_id, user_id, txt_messages);

		UserTODriverMessagingActivity.dbConnect.execNonQuery(sql);

		upDateItemList();

	}

	public void upDateItemList() {

		Log.d("In method received", "Yes");
		message_list = new ArrayList<Message_user_driver>();

		Cursor cursor = UserTODriverMessagingActivity.dbConnect
				.execQuery("select * from Messages WHERE iDriver_id=" + driver_id);

		// String query = "SELECT item_name FROM todo WHERE category =" + vg ;

		String txt_message;

		while (cursor.moveToNext()) {
			txt_message = cursor.getString(4);
			// txt_message = cursor.getColumnNames()[1];

			// Log.d("txt_message", "" + txt_message);
			message_list.add(new Message_user_driver(txt_message));
		}
		cursor.close();

		message_adapter = new MessageAdapter(this, message_list, name_user, DriverName);

		list.setAdapter(message_adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter filter = new IntentFilter();
		filter.addAction("com.hwindiapp.passenger.MessageFrmDriver");

		registerReceiver(updateMessageFrmDriver, filter);

		// message_list = new ArrayList<Message_user_driver>();
		// Cursor cursor = UserTODriverMessagingActivity.dbConnect
		// .execQuery("select * from Messages");
		//
		//
		// String txt_message;
		//
		// while (cursor.moveToNext()) {
		// txt_message = cursor.getString(0);
		//
		// message_list.add(new Message_user_driver(txt_message));
		// }
		// cursor.close();
		//
		// message_adapter = new MessageAdapter(this,
		// android.R.layout.simple_list_item_1, message_list);
		//
		// list.setAdapter(message_adapter);
		upDateItemList();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		unregisterReceiver(updateMessageFrmDriver);
	}

	@Override
	public void handleDriverMessageCallback(String message_str) {
		// TODO Auto-generated method stub
		insert(message_str);
	}
}

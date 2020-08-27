package com.hwindiapp.passenger;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mainProfile.classFiles.OutputStreamReader;
import com.utils.CommonUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AboutUsActivity extends AppCompatActivity {

	private Toolbar mToolbar;
	// ActionBar actionbar;
	TextView text_header;
	// MyTextView header_text;
//	MyTextView content_txt;
	DBConnect dbConnect;

	String language_labels_get_frm_sqLite = "";
	String LBL_ABOUT_US_HEADER_TXT_str = "";

	ProgressBar loading_aboutUs;
	
	LinearLayout linear_about_us_desc_container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);
		
		loading_aboutUs = (ProgressBar) findViewById(R.id.loading_aboutUs);

		
		text_header = (TextView) findViewById(R.id.text_header);
		linear_about_us_desc_container = (LinearLayout) findViewById(R.id.linear_about_us_desc_container);

		dbConnect = new DBConnect(this, "UC_labels.db");

		/* Set Labels */
		getLanguageLabelsFrmSqLite();
		/* Set Labels Finished */

		ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
		back_navigation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutUsActivity.super.onBackPressed();
			}
		});


		new getAboutUsjson().execute();

	}
	
	public class getAboutUsjson extends AsyncTask<String, String, String> {

		String json_faq_str = "";
		boolean errorToGetData = false;

		public String getAboutUsDescription() {
			String responseString = "";
			HttpURLConnection urlConnection = null;
			try {
				
//				Log.d("About url", "::" + CommonUtilities.SERVER_URL+ "type=staticPage&iPageId=1");
				URL url = new URL(CommonUtilities.SERVER_URL+ "type=staticPage&iPageId=1&appType=Passenger&iMemberId=" + getIntent().getStringExtra("user_id"));
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				responseString = OutputStreamReader.readStream(in);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorToGetData = true;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}

			}
			return responseString;
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			json_faq_str = getAboutUsDescription();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				process_json_aboutUs(errorToGetData, json_faq_str);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void process_json_aboutUs(boolean errorInConnection,String about_us_page_json) throws JSONException{
		
		if(errorInConnection == false){
			JSONObject obj_aboutUs = new JSONObject(about_us_page_json);
			
			String page_desc_str = obj_aboutUs.getString("page_desc");
			
			WebView view = new WebView(this);
			view.setVerticalScrollBarEnabled(false);

			view.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return true;
				}
			});
			view.setLongClickable(false);
			view.setHapticFeedbackEnabled(false);

			linear_about_us_desc_container.addView(view);

			String html_one = "<html> <head></head> <body style=\"text-align:justify;color:#000000;background-color:#ffffff;font-size:18px ;\">";
			String html_two = "</body></html>";

			view.loadData(html_one + page_desc_str.replace("\n", "<br>") + html_two, "text/html", "utf-8");

			loading_aboutUs.setVisibility(View.GONE);
			
		}
		
	}
	
	
	public void getLanguageLabelsFrmSqLite() {

		Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

		cursor.moveToPosition(0);

		language_labels_get_frm_sqLite = cursor.getString(0);

		JSONObject obj_language_labels = null;

		try {
			obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
			LBL_ABOUT_US_HEADER_TXT_str = obj_language_labels.getString("LBL_ABOUT_US_HEADER_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {

			text_header.setText("" + LBL_ABOUT_US_HEADER_TXT_str);
		}

	}

}

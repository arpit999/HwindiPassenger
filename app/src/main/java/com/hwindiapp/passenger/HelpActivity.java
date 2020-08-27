package com.hwindiapp.passenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fonts.Text.MyTextView;
import com.listAdapter.files.CustomAdapter_FAQtype;
import com.listAdapter.files.FAQItem;
import com.mainProfile.classFiles.OutputStreamReader;
import com.utils.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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

public class HelpActivity extends AppCompatActivity {

	String url_FAQ = CommonUtilities.SERVER_URL + "function=getFAQ";

	List<FAQItem> FAQItem_list = new ArrayList<FAQItem>();

	// ActionBar actionBar;
	TextView header_txt;
	ImageView back_navigation_custom_view;
	ListView list_faq_category;

	MyTextView choose_txt;
	private CustomAdapter_FAQtype adapter;

	ProgressBar loading_FAQ;

	public static boolean help_page = true;

	String icon_category_url = CommonUtilities.SERVER_URL_ICON_HELP_PAGE;

	DBConnect dbConnect;

	String language_labels_get_frm_sqLite = "";
	String LBL_HOW_HELP_HEADER_TXT_str = "";
	String LBL_CHECK_INTERNET_TXT_str = "";

	AlertDialog alert_error_internet;

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);

		// actionBar = getSupportActionBar();
		// // add the custom view to the action bar
		// actionBar.setCustomView(R.layout.custom_action_bar_view);
		//
		// actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |
		// ActionBar.DISPLAY_SHOW_HOME);
		//
		// actionBar.setDisplayUseLogoEnabled(false);
		// actionBar.setDisplayShowHomeEnabled(true);
		//
		// actionBar.setLogo(null); // forgot why this one but it helped
		//
		// View homeIcon = findViewById(Build.VERSION.SDK_INT >=
		// Build.VERSION_CODES.HONEYCOMB ? android.R.id.home
		// : android.support.v7.appcompat.R.id.home);
		// ((View) homeIcon.getParent()).setVisibility(View.GONE);
		// ((View) homeIcon).setVisibility(View.GONE);
		//
		// actionBar.setDisplayShowTitleEnabled(false);

		dbConnect = new DBConnect(this, "UC_labels.db");

		header_txt = (TextView) findViewById(R.id.text_header);

		/* Set Labels */
		getLanguageLabelsFrmSqLite();
		/* Set Labels Finished */

		back_navigation_custom_view = (ImageView) findViewById(R.id.back_navigation);
		back_navigation_custom_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				HelpActivity.super.onBackPressed();
			}
		});

		list_faq_category = (ListView) findViewById(R.id.list_faq_category);
		adapter = new CustomAdapter_FAQtype(HelpActivity.this, FAQItem_list, true);

		choose_txt = (MyTextView) findViewById(R.id.choose_txt);

		loading_FAQ = (ProgressBar) findViewById(R.id.loading_FAQ);

		new getFAQjson().execute();

		list_faq_category.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				com.fonts.Text.MyTextView vTitle_EN = (com.fonts.Text.MyTextView) arg1
						.findViewById(R.id.category_txt_faq);
				com.fonts.Text.MyTextView category_txt_faq_questions = (com.fonts.Text.MyTextView) arg1
						.findViewById(R.id.category_txt_faq_questions);

				String title_str = "" + vTitle_EN.getText().toString();
				String questions_str = "" + category_txt_faq_questions.getText().toString();

				// Log.d("questions_str:", "::" + questions_str);
				Intent send_questions = new Intent(HelpActivity.this, Questions_helpActivity.class);
				send_questions.putExtra("vTitle", "" + title_str);
				send_questions.putExtra("questions", "" + questions_str);

				startActivity(send_questions);

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
			LBL_HOW_HELP_HEADER_TXT_str = obj_language_labels.getString("LBL_HOW_HELP_HEADER_TXT");
			LBL_CHECK_INTERNET_TXT_str = obj_language_labels.getString("LBL_CHECK_INTERNET_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {
			header_txt.setText("" + LBL_HOW_HELP_HEADER_TXT_str);
		}

	}

	public class getFAQjson extends AsyncTask<String, String, String> {

		String json_faq_str = "";
		boolean errorToGetData = false;

		public String getFAQ() {
			String responseString = "";
//			HttpClient client = new DefaultHttpClient();
//
//			// Log.d("url_faq:", "::" + url_FAQ);
//			HttpGet httpGet = new HttpGet(url_FAQ);
//			try {
//				HttpResponse response1 = client.execute(httpGet);
//
//				HttpEntity httpEntity = response1.getEntity();
//				responseString = EntityUtils.toString(httpEntity);
//
//				// Log.d("help:res:", "responseString:" + responseString);
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				errorToGetData = true;
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				errorToGetData = true;
//			}
			HttpURLConnection urlConnection = null;
			url_FAQ = url_FAQ+"&appType=Passenger&iMemberId=" + getIntent().getStringExtra("user_id");
			Log.d("url_FAQ","url_FAQ:"+url_FAQ);
			try {
				URL url = new URL(url_FAQ);
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
			json_faq_str = getFAQ();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			process_json_faq(errorToGetData, json_faq_str);
		}
	}

	public void process_json_faq(boolean errorToGetData, String json_str_faq) {

		if (errorToGetData == false && json_str_faq != null && !json_str_faq.trim().equals("")
				&& json_str_faq.trim().length() > 4) {

			JSONObject response_update = null;
			JSONArray array_json_faq = null;

			try {
				response_update = new JSONObject(json_str_faq);
				array_json_faq = response_update.getJSONArray("getFAQ");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (response_update != null && array_json_faq != null) {

				if (array_json_faq.length() > 0) {
					for (int i = 0; i < array_json_faq.length(); i++) {
						try {

							JSONObject obj = array_json_faq.getJSONObject(i);
							FAQItem setFAQItem = new FAQItem();

							setFAQItem.setiFaqcategoryId(obj.getString("iFaqcategoryId"));
							setFAQItem.setvTitle_EN(obj.getString("vTitle"));

							String iconName = obj.getString("vImage");
							setFAQItem.setICON_url(icon_category_url + iconName);

							setFAQItem.setQuestions("" + obj.getJSONArray("Questions").toString());

							FAQItem_list.add(setFAQItem);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					loading_FAQ.setVisibility(View.GONE);

					list_faq_category.setVisibility(View.VISIBLE);

					adapter.notifyDataSetChanged();

					list_faq_category.setAdapter(adapter);

				}
			} else {
				buildAlertMsgErrorInternet();
			}

		} else {
			buildAlertMsgErrorInternet();
		}

	}

	private void buildAlertMsgErrorInternet() {

		if (alert_error_internet != null) {
			alert_error_internet.dismiss();
		}

		final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LightThemeGPSalert);
		builder.setMessage("" + LBL_CHECK_INTERNET_TXT_str).setCancelable(false).setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						// dialog.cancel();

						alert_error_internet.dismiss();
						finish();

					}
				});
		alert_error_internet = builder.create();
		alert_error_internet.show();
	}
}

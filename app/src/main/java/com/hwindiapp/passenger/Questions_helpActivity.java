package com.hwindiapp.passenger;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Questions_helpActivity extends AppCompatActivity {

	List<FAQItem> FAQItem_list_questions = new ArrayList<FAQItem>();

	// ActionBar actionBar;
	TextView header_txt;
	ImageView back_navigation_custom_view;
	ListView list_faq_category_questions;

	private CustomAdapter_FAQtype adapter;

	ProgressBar loading_FAQ;

	String list_questions = "";
	String category_help_title = "";
	MyTextView choose_txt;

	public static boolean help_page = false;

	DBConnect dbConnect;

	String language_labels_get_frm_sqLite = "";
	String LBL_CHOOSE_QUESTION_TXT_str = "";

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions_help);

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

		choose_txt = (MyTextView) findViewById(R.id.choose_txt);

		/* Set Labels */
		getLanguageLabelsFrmSqLite();
		/* Set Labels Finished */

		Intent getQuestionData = getIntent();

		category_help_title = getQuestionData.getStringExtra("vTitle");
		list_questions = getQuestionData.getStringExtra("questions");

		// Log.d("list_questions:", "::" + list_questions);

		header_txt = (TextView) findViewById(R.id.text_header);
		header_txt.setText("" + category_help_title);

		back_navigation_custom_view = (ImageView) findViewById(R.id.back_navigation);
		back_navigation_custom_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Questions_helpActivity.super.onBackPressed();
			}
		});

		list_faq_category_questions = (ListView) findViewById(R.id.list_faq_category_questions);
		adapter = new CustomAdapter_FAQtype(Questions_helpActivity.this, FAQItem_list_questions, false);

		loading_FAQ = (ProgressBar) findViewById(R.id.loading_FAQ_questios);

		process_json_faq(list_questions);

		list_faq_category_questions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
				// TODO Auto-generated method stub

				MyTextView vTitle_EN = (MyTextView) view.findViewById(R.id.category_txt_faq);
				MyTextView category_txt_faq_questions = (MyTextView) view.findViewById(R.id.category_txt_faq_questions);

				String question_title_str = "" + vTitle_EN.getText().toString();
				String answer_str = "" + category_txt_faq_questions.getText().toString();

				// Log.d("questions_str:", "::" + answer_str);
				Intent send_questions = new Intent(Questions_helpActivity.this, Answer_Questions_detailActivity.class);
				send_questions.putExtra("questionTitle", "" + question_title_str);
				send_questions.putExtra("answer_txt", "" + answer_str);
				send_questions.putExtra("category_help_title", "" + category_help_title);

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
			LBL_CHOOSE_QUESTION_TXT_str = obj_language_labels.getString("LBL_HOW_HELP_HEADER_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {

			choose_txt.setText("" + LBL_CHOOSE_QUESTION_TXT_str);
		}

	}

	public void process_json_faq(String json_str_faq) {

		if (json_str_faq.toString().equals("[]")) {

		} else {

			String json_faq = "{\"FAQList\":" + json_str_faq + "}";

			JSONObject response_update;
			JSONArray array_json_faq = null;

			try {
				response_update = new JSONObject(json_faq);
				array_json_faq = response_update.getJSONArray("FAQList");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			for (int i = 0; i < array_json_faq.length(); i++) {
				try {

					JSONObject obj = array_json_faq.getJSONObject(i);
					FAQItem setFAQItem = new FAQItem();

//					setFAQItem.setiFaqcategoryId(obj.getString("iFaqcategoryId"));
					setFAQItem.setiFaqcategoryId("");
					setFAQItem.setvTitle_EN(obj.getString("vTitle"));

					setFAQItem.setQuestions("" + obj.getString("tAnswer").toString());

					FAQItem_list_questions.add(setFAQItem);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			loading_FAQ.setVisibility(View.GONE);

			choose_txt.setVisibility(View.VISIBLE);

			adapter.notifyDataSetChanged();

			list_faq_category_questions.setVisibility(View.VISIBLE);
			list_faq_category_questions.setAdapter(adapter);
		}
	}
}

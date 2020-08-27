package com.hwindiapp.passenger;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Answer_Questions_detailActivity extends AppCompatActivity {

//	ActionBar actionBar;
	TextView header_txt;
	ImageView back_navigation_custom_view;
	com.fonts.Text.MyTextView txt_answer;
	com.fonts.Text.MyTextView txt_header_question;

	String question_title = "";
	String category_help_title = "";
	String answer_txt = "";

	DBConnect dbConnect;

	String language_labels_get_frm_sqLite = "";
	String LBL_LEARN_MORE_TXT_str = "";
	
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer__questions_detail);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		
		setSupportActionBar(mToolbar);
		
//		actionBar = getSupportActionBar();
//		// add the custom view to the action bar
//		actionBar.setCustomView(R.layout.custom_action_bar_view);
//
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
//
//		actionBar.setDisplayUseLogoEnabled(false);
//		actionBar.setDisplayShowHomeEnabled(true);
//
//		actionBar.setLogo(null); // forgot why this one but it helped
//
//		View homeIcon = findViewById(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.id.home
//				: android.support.v7.appcompat.R.id.home);
//		((View) homeIcon.getParent()).setVisibility(View.GONE);
//		((View) homeIcon).setVisibility(View.GONE);
//
//		actionBar.setDisplayShowTitleEnabled(false);

		dbConnect = new DBConnect(this, "UC_labels.db");

		header_txt = (TextView) findViewById(R.id.text_header);

		/* Set Labels */
		getLanguageLabelsFrmSqLite();
		/* Set Labels Finished */

		Intent getQuestionData = getIntent();

		category_help_title = getQuestionData.getStringExtra("category_help_title");
		question_title = getQuestionData.getStringExtra("questionTitle");
		answer_txt = getQuestionData.getStringExtra("answer_txt");

//		Log.d("question_title:", "::" + question_title);

		// header_txt.setText("" + category_help_title);

		back_navigation_custom_view = (ImageView) findViewById(R.id.back_navigation);
		back_navigation_custom_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Answer_Questions_detailActivity.super.onBackPressed();
			}
		});

		txt_header_question = (com.fonts.Text.MyTextView) findViewById(R.id.txt_header_question);
		txt_answer = (com.fonts.Text.MyTextView) findViewById(R.id.txt_answer);

		txt_header_question.setText(question_title);

		txt_answer.setText(answer_txt);

		LinearLayout linear_answer_layout = (LinearLayout) findViewById(R.id.linear_answer_layout);

//		JustifiedTextView jtv = new JustifiedTextView(getApplicationContext(), answer_txt);

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

		linear_answer_layout.addView(view);

		String html_one = "<html> <head></head> <body style=\"text-align:justify;color:#666666;background-color:#f2f2f2;font-size:18px ;\">";
		String html_two = "</body></html>";

		view.loadData(html_one + answer_txt.replace("\n", "<br>") + html_two, "text/html", "utf-8");
	}

	public void getLanguageLabelsFrmSqLite() {

		Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

		cursor.moveToPosition(0);

		language_labels_get_frm_sqLite = cursor.getString(0);

		JSONObject obj_language_labels = null;

		try {
			obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
			LBL_LEARN_MORE_TXT_str = obj_language_labels.getString("LBL_LEARN_MORE_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {

			header_txt.setText("" + LBL_LEARN_MORE_TXT_str);
		}

	}
}

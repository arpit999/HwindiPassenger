package com.hwindiapp.passenger;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fonts.Text.MyEditText;
import com.fonts.Text.MyTextView;
import com.mainProfile.classFiles.SendContactQuery;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUsActivity extends AppCompatActivity {

	// ActionBar actionbar;
	TextView text_header;

	// MyTextView header_text;
	MyEditText content_txt;
	MyEditText subject_txt;
	MyTextView Contact_header_txt;

	RippleStyleButton send_query_btn;

	String subject_str = "";
	String message_str = "";

	boolean errorInMessage = false;
	boolean errorInSubject = false;

	DBConnect dbConnect;

	String language_labels_get_frm_sqLite = "";
	String LBL_CONTACT_US_HEADER_TXT_str = "";
	String LBL_CONTACT_US_WRITE_EMAIL_TXT_str = "";
	String LBL_WRITE_BELOW_CONTACT_US_TXT_str = "";
	String LBL_SEND_QUERY_BTN_TXT_str = "";
	String LBL_FAILED_SEND_CONTACT_QUERY_TXT_str = "";
	String LBL_SENT_CONTACT_QUERY_SUCCESS_TXT_str = "";
	String LBL_SEND_CONTACT_QUERY_LOADING_TXT_str = "";
	String LBL_ADD_SUBJECT_HINT_CONTACT_TXT_str = "";
	String LBL_ADD_CORRECT_DETAIL_TXT_str = "";
	
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_us);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);

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

		text_header = (TextView) findViewById(R.id.text_header);
		// header_text = (MyTextView) findViewById(R.id.header_text);
		content_txt = (MyEditText) findViewById(R.id.content_txt);
		subject_txt = (MyEditText) findViewById(R.id.subject_txt);
		Contact_header_txt = (MyTextView) findViewById(R.id.Contact_header_txt);
		send_query_btn = (RippleStyleButton) findViewById(R.id.send_query_btn);

		dbConnect = new DBConnect(this, "UC_labels.db");

		/* Set Labels */
		getLanguageLabelsFrmSqLite();
		/* Set Labels Finished */

		ImageView back_navigation = (ImageView) findViewById(R.id.back_navigation);
		back_navigation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContactUsActivity.super.onBackPressed();
			}
		});

		Intent getUserID = getIntent();

		final String userId_str = getUserID.getStringExtra("UserID");

		send_query_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				subject_str = subject_txt.getText().toString();
				message_str = content_txt.getText().toString();

				if (subject_str.trim().length() > 5) {
					errorInMessage = false;
				} else {
					errorInMessage = true;
				}

				if (message_str.trim().length() > 5) {
					errorInSubject = false;
				} else {
					errorInSubject = true;
				}

				if (errorInMessage == true || errorInSubject == true) {
					Toast.makeText(ContactUsActivity.this, "" + LBL_ADD_CORRECT_DETAIL_TXT_str, Toast.LENGTH_LONG)
							.show();
				}

				if (errorInMessage == false && errorInSubject == false) {

					new SendContactQuery(ContactUsActivity.this, userId_str, LBL_FAILED_SEND_CONTACT_QUERY_TXT_str,
							LBL_SENT_CONTACT_QUERY_SUCCESS_TXT_str, LBL_SEND_CONTACT_QUERY_LOADING_TXT_str, subject_str,
							message_str, subject_txt, content_txt).execute();
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
			LBL_CONTACT_US_HEADER_TXT_str = obj_language_labels.getString("LBL_CONTACT_US_HEADER_TXT");
			LBL_CONTACT_US_WRITE_EMAIL_TXT_str = obj_language_labels.getString("LBL_CONTACT_US_WRITE_EMAIL_TXT");
			LBL_WRITE_BELOW_CONTACT_US_TXT_str = obj_language_labels.getString("LBL_WRITE_BELOW_CONTACT_US_TXT");
			LBL_SEND_QUERY_BTN_TXT_str = obj_language_labels.getString("LBL_SEND_QUERY_BTN_TXT");
			LBL_FAILED_SEND_CONTACT_QUERY_TXT_str = obj_language_labels.getString("LBL_FAILED_SEND_CONTACT_QUERY_TXT");
			LBL_SENT_CONTACT_QUERY_SUCCESS_TXT_str = obj_language_labels
					.getString("LBL_SENT_CONTACT_QUERY_SUCCESS_TXT");
			LBL_SEND_CONTACT_QUERY_LOADING_TXT_str = obj_language_labels
					.getString("LBL_SEND_CONTACT_QUERY_LOADING_TXT");
			LBL_ADD_SUBJECT_HINT_CONTACT_TXT_str = obj_language_labels.getString("LBL_ADD_SUBJECT_HINT_CONTACT_TXT");
			LBL_ADD_CORRECT_DETAIL_TXT_str = obj_language_labels.getString("LBL_ADD_CORRECT_DETAIL_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {

			text_header.setText("" + LBL_CONTACT_US_HEADER_TXT_str);
			Contact_header_txt.setText("" + LBL_WRITE_BELOW_CONTACT_US_TXT_str);
			content_txt.setHint("" + LBL_CONTACT_US_WRITE_EMAIL_TXT_str);
			send_query_btn.setText("" + LBL_SEND_QUERY_BTN_TXT_str);
			subject_txt.setHint("" + LBL_ADD_SUBJECT_HINT_CONTACT_TXT_str);
		}

	}
}

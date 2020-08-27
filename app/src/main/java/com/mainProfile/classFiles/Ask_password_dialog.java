package com.mainProfile.classFiles;

import org.json.JSONException;
import org.json.JSONObject;

import com.fonts.Text.MyEditText;
import com.fonts.Text.MyTextView;
import com.materialedittext.MaterialEditText;
import com.hwindiapp.passenger.DBConnect;
import com.hwindiapp.passenger.R;
import com.hwindiapp.passenger.UserProfileActivity;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Ask_password_dialog implements Runnable {

	Context mContext;
	UserProfileActivity userProfileActivity;
	String oldPassword = "";

	boolean password_match = false;
	boolean password_correct = false;
	boolean Repassword_correct = false;

//	MyTextView Old_pass_header;
//	MyTextView New_pass_header;
//	MyTextView ReNew_pass_header;

	MaterialEditText old_pass_editText;
	MaterialEditText new_pass_ediText;
	MaterialEditText renew_pass_ediText;

//	MyTextView error_password;
//	MyTextView error_Oldpassword;
//	MyTextView error_Repassword;

	DBConnect dbConnect;

	String language_labels_get_frm_sqLite = "";
	String LBL_FEILD_PASSWORD_ERROR_TXT_str = "";
	String LBL_FEILD_REQUIRD_ERROR_TXT_str = "";
	String LBL_ERROR_OCCURED_str = "";
	String LBL_CURR_PASS_HEADER_str = "";
	String LBL_PASSWORD_ERROR_TXT_str = "";
	String LBL_UPDATE_PASSWORD_HEADER_TXT_str = "";
	String LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str = "";
	String LBL_SAME_PASS_ERROR_TXT_str = "";

	public Ask_password_dialog(Context mContext, UserProfileActivity userProfileActivity, String oldPassword) {
		// TODO Auto-generated constructor stub
		this.userProfileActivity = userProfileActivity;
		this.oldPassword = oldPassword;
		this.mContext = mContext;
		// mContext = userProfileActivity.getApplicationContext();

		dbConnect = new DBConnect(mContext, "UC_labels.db");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		final Dialog dialog_country_selection = new Dialog(mContext, R.style.DialogTheme_custom);
		dialog_country_selection.setContentView(R.layout.design_dialog_ask_password);

//		Old_pass_header = (MyTextView) dialog_country_selection.findViewById(R.id.Old_pass_header);
//		New_pass_header = (MyTextView) dialog_country_selection.findViewById(R.id.New_pass_header);
//		ReNew_pass_header = (MyTextView) dialog_country_selection.findViewById(R.id.ReNew_pass_header);

		old_pass_editText = (MaterialEditText) dialog_country_selection.findViewById(R.id.old_pass_editText);
		new_pass_ediText = (MaterialEditText) dialog_country_selection.findViewById(R.id.new_pass_ediText);
		renew_pass_ediText = (MaterialEditText) dialog_country_selection.findViewById(R.id.renew_pass_ediText);

		// old_pass_editText.setOnFocusChangeListener(new
		// GenericFocusChangedListner());
		// new_pass_ediText.setOnFocusChangeListener(new
		// GenericFocusChangedListner());
		// renew_pass_ediText.setOnFocusChangeListener(new
		// GenericFocusChangedListner());

		old_pass_editText.addTextChangedListener(new GenericTextWatcher(old_pass_editText));
		new_pass_ediText.addTextChangedListener(new GenericTextWatcher(new_pass_ediText));
		renew_pass_ediText.addTextChangedListener(new GenericTextWatcher(renew_pass_ediText));

		if(oldPassword.trim().equals("")){
			old_pass_editText.setVisibility(View.GONE);
		}

//		error_password = (MyTextView) dialog_country_selection.findViewById(R.id.error_password);
//		error_Oldpassword = (MyTextView) dialog_country_selection.findViewById(R.id.error_Oldpassword);
//		error_Repassword = (MyTextView) dialog_country_selection.findViewById(R.id.error_Repassword);

		/* Set Labels */
		getLanguageLabelsFrmSqLite();
		/* Set Labels Finished */

//		error_password.setText("Password must be 5 to 10 character long with at least one lower case,"
//				+ "upper case letter, number, any of special Character(@#$;%^&_), and no white spaces.");

		Window window = dialog_country_selection.getWindow();
		window.setGravity(Gravity.CENTER);

		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		dialog_country_selection.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		Button button_cancel = (Button) dialog_country_selection.findViewById(R.id.button_cancel);
		button_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog_country_selection.dismiss();
			}
		});

		Button button_save = (Button) dialog_country_selection.findViewById(R.id.button_save);
		button_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// dialog_country_selection.dismiss();
				String Old_pass_str = old_pass_editText.getText().toString();
				String password_str = new_pass_ediText.getText().toString();
				String Repassword_str = renew_pass_ediText.getText().toString();



				if (oldPassword.trim().equals("") || Old_pass_str.equals("" + oldPassword)) {
					password_match = true;
				} else {
//					error_Oldpassword.setVisibility(View.VISIBLE);
					old_pass_editText.setError(LBL_PASSWORD_ERROR_TXT_str);
					password_match = false;
				}
				if (password_str.trim().length() > 0) {

					password_correct = true;

				} else {
					password_correct = false;

//					error_password.setText("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);

					if (password_str.trim().length() > 0) {
//						error_password.setText("" + LBL_FEILD_PASSWORD_ERROR_TXT_str);
//						new_pass_ediText.setError(LBL_FEILD_PASSWORD_ERROR_TXT_str);
						new_pass_ediText.setError(LBL_FEILD_REQUIRD_ERROR_TXT_str);
					} else {
//						error_password.setText("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);

//						new_pass_ediText.setError(LBL_FEILD_PASSWORD_ERROR_TXT_str);
						new_pass_ediText.setError(LBL_FEILD_REQUIRD_ERROR_TXT_str);
					}

//					error_password.setVisibility(View.VISIBLE);
				}

				if (password_str.equals(Repassword_str)) {
					Repassword_correct = true;

				} else {
					Repassword_correct = false;
//					error_Repassword.setVisibility(View.VISIBLE);
					renew_pass_ediText.setError(LBL_SAME_PASS_ERROR_TXT_str);
				}

				if (password_match == true && password_correct == true && Repassword_correct == true) {

					if (userProfileActivity != null) {
						dialog_country_selection.dismiss();
						userProfileActivity.changePassword_Update(password_str);
					} else {
						Toast.makeText(mContext, "" + LBL_ERROR_OCCURED_str, Toast.LENGTH_LONG).show();
					}

				}

			}
		});

		dialog_country_selection.show();
		dialog_country_selection.setCanceledOnTouchOutside(false);
		dialog_country_selection.setCancelable(true);

	}

	public boolean isPasswordCorrect(String password) {
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&_])(?=\\S+$).{5,10}";
		// System.out.println(password.matches(pattern));

		boolean pass_matchOrNot = password.matches(pattern);

		return pass_matchOrNot;
	}

	public class GenericTextWatcher implements TextWatcher {

		private View view;

		private GenericTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void afterTextChanged(Editable editable) {
			String text = editable.toString();
			switch (view.getId()) {

				case R.id.old_pass_editText:
//				error_Oldpassword.setVisibility(View.GONE);
					old_pass_editText.setError(null);
					break;
				case R.id.new_pass_ediText:
//				error_password.setVisibility(View.GONE);
					new_pass_ediText.setError(null);
					break;

				case R.id.renew_pass_ediText:
//				error_Repassword.setVisibility(View.GONE);
					renew_pass_ediText.setError(null);
					break;

			}
		}
	}

	public void getLanguageLabelsFrmSqLite() {

		Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

		cursor.moveToPosition(0);

		language_labels_get_frm_sqLite = cursor.getString(0);

		JSONObject obj_language_labels = null;
		try {
			obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
			LBL_FEILD_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_PASSWORD_ERROR_TXT");
			LBL_FEILD_REQUIRD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_REQUIRD_ERROR_TXT");
			LBL_ERROR_OCCURED_str = obj_language_labels.getString("LBL_ERROR_OCCURED");
			LBL_CURR_PASS_HEADER_str = obj_language_labels.getString("LBL_CURR_PASS_HEADER");
			LBL_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_PASSWORD_ERROR_TXT");
			LBL_UPDATE_PASSWORD_HEADER_TXT_str = obj_language_labels.getString("LBL_UPDATE_PASSWORD_HEADER_TXT");
			LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str = obj_language_labels
					.getString("LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT");
			LBL_SAME_PASS_ERROR_TXT_str = obj_language_labels.getString("LBL_SAME_PASS_ERROR_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {

			old_pass_editText.setFloatingLabelText("" + LBL_CURR_PASS_HEADER_str);
			new_pass_ediText.setFloatingLabelText("" + LBL_UPDATE_PASSWORD_HEADER_TXT_str);
			renew_pass_ediText.setFloatingLabelText("" + LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str);

			old_pass_editText.setHint("" + LBL_CURR_PASS_HEADER_str);
			new_pass_ediText.setHint("" + LBL_UPDATE_PASSWORD_HEADER_TXT_str);
			renew_pass_ediText.setHint("" + LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str);

//			Old_pass_header.setText("" + LBL_CURR_PASS_HEADER_str);
//			New_pass_header.setText("" + LBL_UPDATE_PASSWORD_HEADER_TXT_str);
//			ReNew_pass_header.setText("" + LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str);

//			error_Oldpassword.setText("" + LBL_PASSWORD_ERROR_TXT_str);
//			error_Repassword.setText("" + LBL_SAME_PASS_ERROR_TXT_str);
		}

	}

}

/*

public class Ask_password_dialog implements Runnable {

	Context mContext;
	UserProfileActivity userProfileActivity;
	String oldPassword = "";

	boolean password_match = false;
	boolean password_correct = false;
	boolean Repassword_correct = false;

	MyTextView Old_pass_header;
	MyTextView New_pass_header;
	MyTextView ReNew_pass_header;

	MyEditText old_pass_editText;
	MyEditText new_pass_ediText;
	MyEditText renew_pass_ediText;

	MyTextView error_password;
	MyTextView error_Oldpassword;
	MyTextView error_Repassword;

	DBConnect dbConnect;

	String language_labels_get_frm_sqLite = "";
	String LBL_FEILD_PASSWORD_ERROR_TXT_str = "";
	String LBL_FEILD_REQUIRD_ERROR_TXT_str = "";
	String LBL_ERROR_OCCURED_str = "";
	String LBL_CURR_PASS_HEADER_str = "";
	String LBL_PASSWORD_ERROR_TXT_str = "";
	String LBL_UPDATE_PASSWORD_HEADER_TXT_str = "";
	String LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str = "";
	String LBL_SAME_PASS_ERROR_TXT_str = "";

	RelativeLayout Old_password_area;

	public Ask_password_dialog(Context mContext, UserProfileActivity userProfileActivity, String oldPassword) {
		// TODO Auto-generated constructor stub
		this.userProfileActivity = userProfileActivity;
		this.oldPassword = oldPassword;
		this.mContext = mContext;
		// mContext = userProfileActivity.getApplicationContext();

		dbConnect = new DBConnect(mContext, "UC_labels.db");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		final Dialog dialog_country_selection = new Dialog(mContext, R.style.DialogTheme_custom);
		dialog_country_selection.setContentView(R.layout.design_dialog_ask_password);

		Old_password_area = (RelativeLayout) dialog_country_selection.findViewById(R.id.Old_password_area);
		Old_pass_header = (MyTextView) dialog_country_selection.findViewById(R.id.Old_pass_header);
		New_pass_header = (MyTextView) dialog_country_selection.findViewById(R.id.New_pass_header);
		ReNew_pass_header = (MyTextView) dialog_country_selection.findViewById(R.id.ReNew_pass_header);

		old_pass_editText = (MyEditText) dialog_country_selection.findViewById(R.id.old_pass_editText);
		new_pass_ediText = (MyEditText) dialog_country_selection.findViewById(R.id.new_pass_ediText);
		renew_pass_ediText = (MyEditText) dialog_country_selection.findViewById(R.id.renew_pass_ediText);

		// old_pass_editText.setOnFocusChangeListener(new
		// GenericFocusChangedListner());
		// new_pass_ediText.setOnFocusChangeListener(new
		// GenericFocusChangedListner());
		// renew_pass_ediText.setOnFocusChangeListener(new
		// GenericFocusChangedListner());

		old_pass_editText.addTextChangedListener(new GenericTextWatcher(old_pass_editText));
		new_pass_ediText.addTextChangedListener(new GenericTextWatcher(new_pass_ediText));
		renew_pass_ediText.addTextChangedListener(new GenericTextWatcher(renew_pass_ediText));

		error_password = (MyTextView) dialog_country_selection.findViewById(R.id.error_password);
		error_Oldpassword = (MyTextView) dialog_country_selection.findViewById(R.id.error_Oldpassword);
		error_Repassword = (MyTextView) dialog_country_selection.findViewById(R.id.error_Repassword);

		if(oldPassword.trim().equals("")){
			Old_password_area.setVisibility(View.GONE);
		}

		*/
/* Set Labels *//*

		getLanguageLabelsFrmSqLite();
		*/
/* Set Labels Finished *//*


		error_password.setText("Password must be 5 to 10 character long with at least one lower case,"
				+ "upper case letter, number, any of special Character(@#$;%^&_), and no white spaces.");

		Window window = dialog_country_selection.getWindow();
		window.setGravity(Gravity.CENTER);

		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		dialog_country_selection.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		Button button_cancel = (Button) dialog_country_selection.findViewById(R.id.button_cancel);
		button_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog_country_selection.dismiss();
			}
		});

		Button button_save = (Button) dialog_country_selection.findViewById(R.id.button_save);
		button_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// dialog_country_selection.dismiss();
				String Old_pass_str = old_pass_editText.getText().toString();
				String password_str = new_pass_ediText.getText().toString();
				String Repassword_str = renew_pass_ediText.getText().toString();

				if (oldPassword.trim().equals("") || Old_pass_str.equals("" + oldPassword)) {
					password_match = true;
				} else {
					error_Oldpassword.setVisibility(View.VISIBLE);
					password_match = false;
				}
				if (password_str.trim().length() > 0) {

					// Log.d("pass_matchOrNot:", "::" +
					// isPasswordCorrect(password_str));
//					if (isPasswordCorrect(password_str) == true) {
//						password_correct = true;
//
//					} else {
//						password_correct = false;
//						error_password.setText("" + LBL_FEILD_PASSWORD_ERROR_TXT_str);
//						error_password.setVisibility(View.VISIBLE);
//					}
					
					password_correct = true;
					error_password.setVisibility(View.GONE);
					
				} else {
					password_correct = false;

//					error_password.setText("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
					
					if (password_str.trim().length() > 0) {
						error_password.setText("" + LBL_FEILD_PASSWORD_ERROR_TXT_str);
					} else {
						error_password.setText("" + LBL_FEILD_REQUIRD_ERROR_TXT_str);
					}
					error_password.setVisibility(View.VISIBLE);
				}

				if (password_str.equals(Repassword_str)) {
					Repassword_correct = true;

				} else {
					Repassword_correct = false;
					error_Repassword.setVisibility(View.VISIBLE);
				}

				if (password_match == true && password_correct == true && Repassword_correct == true) {

					if (userProfileActivity != null) {
						dialog_country_selection.dismiss();
						userProfileActivity.changePassword_Update(password_str);
					} else {
						Toast.makeText(mContext, "" + LBL_ERROR_OCCURED_str, Toast.LENGTH_LONG).show();
					}

				}

			}
		});

		dialog_country_selection.show();
		dialog_country_selection.setCanceledOnTouchOutside(false);
		dialog_country_selection.setCancelable(true);

	}

	public boolean isPasswordCorrect(String password) {
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&_])(?=\\S+$).{5,10}";
		// System.out.println(password.matches(pattern));

		boolean pass_matchOrNot = password.matches(pattern);

		return pass_matchOrNot;
	}

	public class GenericTextWatcher implements TextWatcher {

		private View view;

		private GenericTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void afterTextChanged(Editable editable) {
			String text = editable.toString();
			switch (view.getId()) {

			case R.id.old_pass_editText:
				error_Oldpassword.setVisibility(View.GONE);
				break;
			case R.id.new_pass_ediText:
				error_password.setVisibility(View.GONE);
				break;

			case R.id.renew_pass_ediText:
				error_Repassword.setVisibility(View.GONE);
				break;

			}
		}
	}

	public void getLanguageLabelsFrmSqLite() {

		Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

		cursor.moveToPosition(0);

		language_labels_get_frm_sqLite = cursor.getString(0);

		JSONObject obj_language_labels = null;
		try {
			obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
			LBL_FEILD_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_PASSWORD_ERROR_TXT");
			LBL_FEILD_REQUIRD_ERROR_TXT_str = obj_language_labels.getString("LBL_FEILD_REQUIRD_ERROR_TXT");
			LBL_ERROR_OCCURED_str = obj_language_labels.getString("LBL_ERROR_OCCURED");
			LBL_CURR_PASS_HEADER_str = obj_language_labels.getString("LBL_CURR_PASS_HEADER");
			LBL_PASSWORD_ERROR_TXT_str = obj_language_labels.getString("LBL_PASSWORD_ERROR_TXT");
			LBL_UPDATE_PASSWORD_HEADER_TXT_str = obj_language_labels.getString("LBL_UPDATE_PASSWORD_HEADER_TXT");
			LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str = obj_language_labels
					.getString("LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT");
			LBL_SAME_PASS_ERROR_TXT_str = obj_language_labels.getString("LBL_SAME_PASS_ERROR_TXT");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj_language_labels != null) {

			Old_pass_header.setText("" + LBL_CURR_PASS_HEADER_str);
			New_pass_header.setText("" + LBL_UPDATE_PASSWORD_HEADER_TXT_str);
			ReNew_pass_header.setText("" + LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT_str);

			error_Oldpassword.setText("" + LBL_PASSWORD_ERROR_TXT_str);
			error_Repassword.setText("" + LBL_SAME_PASS_ERROR_TXT_str);
		}

	}

}
*/

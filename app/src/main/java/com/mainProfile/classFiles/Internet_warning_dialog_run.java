package com.mainProfile.classFiles;

import org.json.JSONException;
import org.json.JSONObject;

import com.hwindiapp.passenger.DBConnect;
import com.hwindiapp.passenger.InternetConnection;
import com.hwindiapp.passenger.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Internet_warning_dialog_run implements Runnable,DialogInterface.OnClickListener{

	boolean error_connection_server;
	Context mContext;
	DBConnect dbConnect;
	String language_labels_get_frm_sqLite = "";
	String LBL_ERROR_OCCURED_str = "";

	String LBL_RETRY_BTN_TXT_NO_INTERNET_str = "RETRY";
	String LBL_CANCEL_BTN_TXT_NO_INTERNET_str = "CANCEL";
	String LBL_NO_INTERNET_TXT_str = "NO Internet Connection...";

	InternetConnection intCheck;

	private NoInternetListner NoInternetListner;

	public Internet_warning_dialog_run(boolean flag_error_serverConnection, Context context) {
		// TODO Auto-generated constructor stub
		this.error_connection_server = flag_error_serverConnection;
		this.mContext = context;
		dbConnect = new DBConnect(mContext, "UC_labels.db");
		intCheck = new InternetConnection(mContext);
	}

	public void changeErrorConnection(boolean flag_error_serverConnection) {
		this.error_connection_server = flag_error_serverConnection;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		setLanguageLabels();
		// final Dialog dialog_check_internet = new Dialog(mContext,
		// R.style.DialogInternet_checkSlideAnim);
		//
		// dialog_check_internet.setContentView(R.layout.custom_dialog_no_internet);
		//
		// WindowManager winManager = (WindowManager)
		// mContext.getSystemService(Context.WINDOW_SERVICE);
		// Display display = winManager.getDefaultDisplay();
		// int width = display.getWidth();
		//
		// RippleStyleButton retry_btn = (RippleStyleButton)
		// dialog_check_internet.findViewById(R.id.retry_btn_internet);
		// RippleStyleButton cancle_btn = (RippleStyleButton)
		// dialog_check_internet.findViewById(R.id.cancle_btn_internet);
		//
		// MyTextView check_internet_conn_one = (MyTextView)
		// dialog_check_internet
		// .findViewById(R.id.check_internet_conn_one);
		//
		// setLanguageLabels();
		//
		// check_internet_conn_one.setText("" + LBL_NO_INTERNET_TXT_str);
		//
		// if (error_connection_server == true) {
		// // imageAnim.setVisibility(View.GONE);
		// // wait_text.setVisibility(View.GONE);
		// check_internet_conn_one.setText("" + LBL_ERROR_OCCURED_str);
		// }
		//
		// Window window = dialog_check_internet.getWindow();
		// window.setGravity(Gravity.CENTER);
		//
		// window.setLayout(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT);
		//
		// LayoutParams params_retry_btn = retry_btn.getLayoutParams();
		// params_retry_btn.width = width / 3;
		//
		// retry_btn.setLayoutParams(params_retry_btn);
		//
		// LayoutParams params_cancle_btn = cancle_btn.getLayoutParams();
		// params_cancle_btn.width = width / 3;
		// cancle_btn.setLayoutParams(params_cancle_btn);
		//
		// retry_btn.setColor("#81C6DD");
		// retry_btn.setClipRadius(2);
		//
		// cancle_btn.setColor("#81C6DD");
		// cancle_btn.setClipRadius(2);
		//
		// cancle_btn.setText("" + LBL_CANCEL_BTN_TXT_NO_INTERNET_str);
		// retry_btn.setText("" + LBL_RETRY_BTN_TXT_NO_INTERNET_str);
		//
		// retry_btn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // dialog_check_Internet.dismiss();
		// // check_intAgain(dialog_check_internet);
		//
		// if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
		//
		// } else {
		// dialog_check_internet.dismiss();
		// if (NoInternetListner != null) {
		// NoInternetListner.handleNoInternetBTNCallback(0);
		// }
		// }
		//
		// }
		// });
		//
		// cancle_btn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// dialog_check_internet.dismiss();
		// // finish();
		//
		// if (NoInternetListner != null) {
		// NoInternetListner.handleNoInternetBTNCallback(1);
		// }
		// }
		// });
		//
		// dialog_check_internet.getWindow().setLayout(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT);
		//
		// dialog_check_internet.show();
		// dialog_check_internet.setCanceledOnTouchOutside(false);
		// dialog_check_internet.setCancelable(false);

		final AlertDialog adb = new AlertDialog.Builder(mContext, R.style.LightThemeGPSalert).create();

		/* adb.setTitle("Title of alert dialog"); */
		
		if (LBL_ERROR_OCCURED_str == null || LBL_ERROR_OCCURED_str.equals("")
				|| LBL_ERROR_OCCURED_str.trim().length() < 5) {
			LBL_ERROR_OCCURED_str = "Sorry. Some Error Occurred. Please try again Later.";
		}
		
		if (LBL_NO_INTERNET_TXT_str == null || LBL_NO_INTERNET_TXT_str.equals("")
				|| LBL_NO_INTERNET_TXT_str.trim().length() < 5) {
			LBL_NO_INTERNET_TXT_str = "NO Internet Connection...";
		}
		
		if (LBL_RETRY_BTN_TXT_NO_INTERNET_str == null || LBL_RETRY_BTN_TXT_NO_INTERNET_str.equals("")
				|| LBL_RETRY_BTN_TXT_NO_INTERNET_str.trim().length() < 2) {
			LBL_RETRY_BTN_TXT_NO_INTERNET_str = "RETRY";
		}
		
		if (LBL_CANCEL_BTN_TXT_NO_INTERNET_str == null || LBL_CANCEL_BTN_TXT_NO_INTERNET_str.equals("")
				|| LBL_CANCEL_BTN_TXT_NO_INTERNET_str.trim().length() < 2) {
			LBL_CANCEL_BTN_TXT_NO_INTERNET_str = "CANCEL";
		}

		if (error_connection_server == true) {
			// imageAnim.setVisibility(View.GONE);
			// wait_text.setVisibility(View.GONE);
			adb.setMessage("" + LBL_ERROR_OCCURED_str);
		} else {
			adb.setMessage("" + LBL_NO_INTERNET_TXT_str);
		}

		adb.setIcon(android.R.drawable.ic_dialog_alert);
		
		adb.setButton(DialogInterface.BUTTON_POSITIVE, ""+LBL_RETRY_BTN_TXT_NO_INTERNET_str, this);
		adb.setButton(DialogInterface.BUTTON_NEGATIVE, ""+LBL_CANCEL_BTN_TXT_NO_INTERNET_str, this);
		
		adb.show();

		adb.setCancelable(false);
		
		Button theButton = adb.getButton(DialogInterface.BUTTON_POSITIVE);
		theButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

				} else {
					adb.dismiss();
					if (NoInternetListner != null) {
						NoInternetListner.handleNoInternetBTNCallback(0);
					}
				}
			}
		});
		
		Button negativeButton = adb.getButton(DialogInterface.BUTTON_NEGATIVE);
		negativeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adb.dismiss();
				if (NoInternetListner != null) {
					NoInternetListner.handleNoInternetBTNCallback(1);
				}
			}
		});

//		adb.setsetPositiveButton("" + LBL_RETRY_BTN_TXT_NO_INTERNET_str, this);
//		
//		Button theButton = adb.getButton(DialogInterface.BUTTON_POSITIVE);
//
//		adb.setNegativeButton("" + LBL_CANCEL_BTN_TXT_NO_INTERNET_str, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				if (NoInternetListner != null) {
//					NoInternetListner.handleNoInternetBTNCallback(1);
//				}
//			}
//		});
		

	}

	public void setLanguageLabels() {
		// TODO Auto-generated method stub

		if (CheckIsDataAlreadyInDBorNot("labels", "vLabel", "Language_labels") == true) {

			Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

			cursor.moveToPosition(0);

			language_labels_get_frm_sqLite = cursor.getString(0);

			JSONObject obj_language_labels = null;
			try {
				obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
				LBL_RETRY_BTN_TXT_NO_INTERNET_str = obj_language_labels.getString("LBL_RETRY_BTN_TXT_NO_INTERNET");
				LBL_CANCEL_BTN_TXT_NO_INTERNET_str = obj_language_labels.getString("LBL_CANCEL_BTN_TXT_NO_INTERNET");
				LBL_NO_INTERNET_TXT_str = obj_language_labels.getString("LBL_NO_INTERNET_TXT");
				LBL_ERROR_OCCURED_str = obj_language_labels.getString("LBL_ERROR_OCCURED");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

	public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {

		String Query = "SELECT * from `labels` WHERE vLabel= \"" + fieldValue + "\"";
		Cursor cursor = dbConnect.execQuery(Query);

		if (cursor != null) {
			// Log.d("cursor.getCount()::", "cc::" + cursor.getCount());
			if (cursor.getCount() <= 0) {
				cursor.close();
				return false;
			}
			cursor.close();
			return true;
		}
		return false;
	}

	public interface NoInternetListner {
		void handleNoInternetBTNCallback(int id_btn);
	}

	public void setListener(NoInternetListner NoInternetListner) {
		this.NoInternetListner = NoInternetListner;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
		
//		if(DialogInterface.BUTTON_POSITIVE==which){
//			Log.d("BUTTON_POSITIVE:", "which:"+which+":"+DialogInterface.BUTTON_POSITIVE);
//			if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
//
//			} else {
//				dialog.dismiss();
//				if (NoInternetListner != null) {
//					NoInternetListner.handleNoInternetBTNCallback(0);
//				}
//			}
//			
//		}else if(DialogInterface.BUTTON_NEGATIVE==which){
//			Log.d("BUTTON_NEGATIVE:", "which:"+which+":"+DialogInterface.BUTTON_NEGATIVE);
//			dialog.dismiss();
//			if (NoInternetListner != null) {
//				NoInternetListner.handleNoInternetBTNCallback(1);
//			}
//		}
		
	}

}

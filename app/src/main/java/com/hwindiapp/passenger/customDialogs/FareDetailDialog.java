package com.hwindiapp.passenger.customDialogs;

import org.json.JSONException;
import org.json.JSONObject;

import com.fonts.Text.MyTextView;
import com.hwindiapp.passenger.DBConnect;
import com.hwindiapp.passenger.Mainprofile_Activity;
import com.hwindiapp.passenger.R;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.ViewGroup.LayoutParams;

public class FareDetailDialog implements Runnable {

	Context mContext;

	String base_fare_value_str = "";
	String per_min_fare_value_str = "";
	String per_km_fare_value_str = "";

	MyTextView fare_header_txt;
	MyTextView base_fare_header_txt;
	MyTextView par_min_fare_header_txt;
	MyTextView par_km_fare_header_txt;
	MyTextView and_txt;

	DBConnect dbConnect;
	String language_labels_get_frm_sqLite = "";
	String LBL_FARE_DETAIL_TXT_str = "";
	String LBL_BASE_FARE_TXT_str = "";
	String LBL_MIN_TXT_str = "";
	String LBL_KM_TXT_str = "";
	String LBL_AND_TXT_str = "";

	String defaultCurrencySign = "";

	public FareDetailDialog(Context context, String base_fare_value_str, String per_min_fare_value_str,
			String per_km_fare_value_str,String CurrencySymbol_str) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.base_fare_value_str = base_fare_value_str;
		this.per_min_fare_value_str = per_min_fare_value_str;
		this.per_km_fare_value_str = per_km_fare_value_str;
		this.defaultCurrencySign = CurrencySymbol_str;

		dbConnect = new DBConnect(mContext, "UC_labels.db");
	}

	Dialog dialog_fare_detail;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		dialog_fare_detail = new Dialog(mContext, R.style.FairDetailDialogStyle);

//		defaultCurrencySign = Mainprofile_Activity.defaultCurrencySign_str;

		dialog_fare_detail.setContentView(R.layout.custom_dialog_fair_detail_profile);

		dialog_fare_detail.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		fare_header_txt = (MyTextView) dialog_fare_detail.findViewById(R.id.fare_header_txt);
		base_fare_header_txt = (MyTextView) dialog_fare_detail.findViewById(R.id.base_fare_header_txt);
		par_min_fare_header_txt = (MyTextView) dialog_fare_detail.findViewById(R.id.par_min_fare_header_txt);
		par_km_fare_header_txt = (MyTextView) dialog_fare_detail.findViewById(R.id.par_km_fare_header_txt);
		and_txt = (MyTextView) dialog_fare_detail.findViewById(R.id.and_txt);

		setLangugeLabels();

		MyTextView base_fare_value_txt = (MyTextView) dialog_fare_detail.findViewById(R.id.base_fare_value_txt);
		MyTextView per_min_fare_value_txt = (MyTextView) dialog_fare_detail.findViewById(R.id.per_min_fare_value_txt);
		MyTextView per_km_fare_value_txt = (MyTextView) dialog_fare_detail.findViewById(R.id.per_km_fare_value_txt);

		base_fare_value_txt.setText("" + base_fare_value_str + " "+defaultCurrencySign);
		per_min_fare_value_txt.setText("" + per_min_fare_value_str + " "+defaultCurrencySign);
		per_km_fare_value_txt.setText("" + per_km_fare_value_str + " "+defaultCurrencySign);

		dialog_fare_detail.setCancelable(true);
		dialog_fare_detail.setCanceledOnTouchOutside(true);
		dialog_fare_detail.show();
	}

	public void setLangugeLabels() {
		if (CheckIsDataAlreadyInDBorNot("labels", "vLabel", "Language_labels") == true) {

			Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

			cursor.moveToPosition(0);

			language_labels_get_frm_sqLite = cursor.getString(0);

			JSONObject obj_language_labels = null;
			try {
				obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
				LBL_FARE_DETAIL_TXT_str = obj_language_labels.getString("LBL_FARE_DETAIL_TXT");
				LBL_BASE_FARE_TXT_str = obj_language_labels.getString("LBL_BASE_FARE_TXT");
				LBL_MIN_TXT_str = obj_language_labels.getString("LBL_MIN_TXT");
				LBL_KM_TXT_str = obj_language_labels.getString("LBL_KM_TXT");
				LBL_AND_TXT_str = obj_language_labels.getString("LBL_AND_TXT");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (obj_language_labels != null) {
				fare_header_txt.setText("" + LBL_FARE_DETAIL_TXT_str);
				base_fare_header_txt.setText(" " + LBL_BASE_FARE_TXT_str);
				par_min_fare_header_txt.setText(" / " + LBL_MIN_TXT_str);
				par_km_fare_header_txt.setText(" / " + LBL_KM_TXT_str);
				and_txt.setText("" + LBL_AND_TXT_str);
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

}

package com.hwindiapp.passenger.customDialogs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/
import org.json.JSONException;
import org.json.JSONObject;

import com.fonts.Text.MyTextView;
import com.mainProfile.classFiles.OutputStreamReader;
import com.hwindiapp.passenger.DBConnect;
import com.hwindiapp.passenger.Mainprofile_Activity;
import com.hwindiapp.passenger.R;
import com.utils.CommonUtilities;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RequestTaxiNearest implements Runnable {

	Context mContext;
	ProgressBar mProgressBar;
	CountDownTimer countDownTimer;
	long InitialClickDuration = 0;

	MyTextView request_txt;
	ImageView cancel_request_img;
	String user_id = "";
	String LBL_REQUEST_CANCEL_FAILED_TXT_str = "";
	String LBL_REQUEST_CANCELED_TXT_str = "";

	Mainprofile_Activity act_main = null;

	Dialog dialog_request_taxi_nearest;

	CancelRequest CancelRequest_process = null;

	DBConnect dbConnect;
	String language_labels_get_frm_sqLite = "";
	String LBL_REQUESTING_TXT_str = "";
	String LBL_HOLD_TO_CANCEL_TXT_str = "";
	String LBL_CANCELING_TXT_str = "";

	public RequestTaxiNearest(Mainprofile_Activity act_main, Context mcontext, String user_id,
			String LBL_REQUEST_CANCEL_FAILED_TXT_str) {
		// TODO Auto-generated constructor stub
		this.act_main = act_main;
		this.mContext = mcontext;
		this.user_id = user_id;
		this.LBL_REQUEST_CANCEL_FAILED_TXT_str = LBL_REQUEST_CANCEL_FAILED_TXT_str;

		dbConnect = new DBConnect(mContext, "UC_labels.db");

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		dialog_request_taxi_nearest = new Dialog(mContext, R.style.FullScreenDialog);

		dialog_request_taxi_nearest.setContentView(R.layout.custom_dialog_request_taxi_layout);

		mProgressBar = (ProgressBar) dialog_request_taxi_nearest.findViewById(R.id.loading_requestTaxi);
		// Window window = dialog_request_taxi_nearest.getWindow();
		// window.setGravity(Gravity.TOP);
		//
		// window.setLayout(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);

		dialog_request_taxi_nearest.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		cancel_request_img = (ImageView) dialog_request_taxi_nearest.findViewById(R.id.cancel_request_img);

		request_txt = (MyTextView) dialog_request_taxi_nearest.findViewById(R.id.request_txt);
		
		setLangugeLabels();
		
		mProgressBar.setIndeterminate(true);

		// cancel_request_img.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// dialog_request_taxi_nearest.dismiss();
		//
		// }
		// });

		cancel_request_img.setOnTouchListener(new OnTouchListener() {
			boolean timerFinish = false;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
					mProgressBar.setIndeterminate(false);
					InitialClickDuration = Calendar.getInstance().getTimeInMillis();

					request_txt.setText(LBL_HOLD_TO_CANCEL_TXT_str + "");

					mProgressBar.setMax(4);

					countDownTimer = new CountDownTimer(4000, 1000) {

						public void onTick(long millisUntilFinished) {

							mProgressBar.setProgress((int) (millisUntilFinished / 1000));

						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							timerFinish = true;
							request_txt.setText("" + LBL_CANCELING_TXT_str);
							mProgressBar.setProgress(0);
							mProgressBar.setIndeterminate(true);
							cancel_request_img.setEnabled(false);

							if (CancelRequest_process != null) {
								CancelRequest_process.cancel(true);
								CancelRequest_process = null;
							}
							CancelRequest_process = new CancelRequest(user_id);
							CancelRequest_process.execute();
						}

					}.start();
					return true;
				case MotionEvent.ACTION_UP:
					// Log.d("Up", "Called:" + InitialClickDuration);
					long clickDuration = Calendar.getInstance().getTimeInMillis() - InitialClickDuration;

					if (clickDuration >= 4200 && timerFinish == true) {

					} else {

						request_txt.setText("" + LBL_REQUESTING_TXT_str);
						mProgressBar.setProgress(0);
						mProgressBar.setIndeterminate(true);
						if (countDownTimer != null) {
							countDownTimer.cancel();
						}
					}
					return true;
				}
				return true;
			}
		});

		dialog_request_taxi_nearest.setCancelable(false);
		dialog_request_taxi_nearest.setCanceledOnTouchOutside(false);
		dialog_request_taxi_nearest.show();

	}

	public void dismissDialog() {
		if (dialog_request_taxi_nearest != null) {
			dialog_request_taxi_nearest.dismiss();
		}
	}

	public class CancelRequest extends AsyncTask<String, String, String> {

		String user_id = "";
		String responseString = "";

		public CancelRequest(String user_id) {
			// TODO Auto-generated constructor stub
			this.user_id = user_id;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String url_str = CommonUtilities.SERVER_URL + "function=cancelRequest_CAR" + "&userId=" + user_id;

//			HttpClient client = new DefaultHttpClient();
//
//			HttpGet httpGet = new HttpGet(url);
//			try {
//				HttpResponse response = client.execute(httpGet);
//
//				HttpEntity httpEntity = response.getEntity();
//				responseString = EntityUtils.toString(httpEntity);
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL(url_str);
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

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (responseString != null && !responseString.equals("") && responseString.contains("Update Successful")) {

				if (act_main != null) {
					dismissDialog();
					act_main.carRequestCancel();
					Toast.makeText(mContext, "" + LBL_REQUEST_CANCELED_TXT_str, Toast.LENGTH_LONG).show();
				} else {

					request_txt.setText("" + LBL_REQUESTING_TXT_str);
					mProgressBar.setProgress(0);
					mProgressBar.setIndeterminate(true);
					if (countDownTimer != null) {
						countDownTimer.cancel();
					}
					cancel_request_img.setEnabled(true);
					Toast.makeText(mContext, "" + LBL_REQUEST_CANCEL_FAILED_TXT_str, Toast.LENGTH_LONG).show();
				}

			} else {

				request_txt.setText("" + LBL_REQUESTING_TXT_str);
				mProgressBar.setProgress(0);
				mProgressBar.setIndeterminate(true);
				if (countDownTimer != null) {
					countDownTimer.cancel();
				}

				cancel_request_img.setEnabled(true);
				Toast.makeText(mContext, "" + LBL_REQUEST_CANCEL_FAILED_TXT_str, Toast.LENGTH_LONG).show();
			}
		}

	}

	public void setLangugeLabels() {
		if (CheckIsDataAlreadyInDBorNot("labels", "vLabel", "Language_labels") == true) {

			Cursor cursor = dbConnect.execQuery("select vValue from labels WHERE vLabel=\"Language_labels\"");

			cursor.moveToPosition(0);

			language_labels_get_frm_sqLite = cursor.getString(0);

			JSONObject obj_language_labels = null;
			try {
				obj_language_labels = new JSONObject(language_labels_get_frm_sqLite);
				LBL_REQUESTING_TXT_str = obj_language_labels.getString("LBL_REQUESTING_TXT");
				LBL_HOLD_TO_CANCEL_TXT_str = obj_language_labels.getString("LBL_HOLD_TO_CANCEL_TXT");
				LBL_CANCELING_TXT_str = obj_language_labels.getString("LBL_CANCELING_TXT");
				LBL_REQUEST_CANCELED_TXT_str= obj_language_labels.getString("LBL_REQUEST_CANCELED_TXT");
				LBL_REQUEST_CANCEL_FAILED_TXT_str= obj_language_labels.getString("LBL_REQUEST_CANCEL_FAILED_TXT");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (obj_language_labels != null) {

				request_txt.setText("" + LBL_REQUESTING_TXT_str);
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

	// public void startCountDownTimer() {
	// // mProgressBar.setIndeterminate(false);
	//
	// // mProgressBar.setProgress(5);
	// CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
	//
	// public void onTick(long millisUntilFinished) {
	//
	// mProgressBar.setProgress((int) (millisUntilFinished / 1000));
	//
	// }
	//
	// @Override
	// public void onFinish() {
	// // TODO Auto-generated method stub
	// mProgressBar.setProgress(0);
	// }
	//
	// }.start();
	// }

}

package com.mainProfile.classFiles;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/

import com.hwindiapp.passenger.R;
import com.utils.CommonUtilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SendEmailReceipt extends AsyncTask<String, String, String> {

	String receiptURL = CommonUtilities.SERVER_URL;

	String responseString = "";

	String LBL_FAILED_SEND_RECEIPT_EMAIL_TXT_str = "";
	String LBL_CHECK_INBOX_TXT_str = "";
	String LBL_SEND_EMAIL_LOADING_TXT_str = "";
	String tripID = "";
	Context mContext;

	ProgressDialog pDialog;

	boolean errorINConnection = false;

	public SendEmailReceipt(Context mContext, String tripID, String LBL_FAILED_SEND_RECEIPT_EMAIL_TXT_str,
			String LBL_CHECK_INBOX_TXT_str, String LBL_SEND_EMAIL_LOADING_TXT_str) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.tripID = tripID;
		this.LBL_FAILED_SEND_RECEIPT_EMAIL_TXT_str = LBL_FAILED_SEND_RECEIPT_EMAIL_TXT_str;
		this.LBL_CHECK_INBOX_TXT_str = LBL_CHECK_INBOX_TXT_str;
		this.LBL_SEND_EMAIL_LOADING_TXT_str = LBL_SEND_EMAIL_LOADING_TXT_str;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

		pDialog = new ProgressDialog(mContext, R.style.DialogTheme_custom);
		pDialog.setMessage("" + LBL_SEND_EMAIL_LOADING_TXT_str);
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		String receiptURL_final = receiptURL + "type=getReceipt&iTripId=" + tripID;
//		HttpClient client = new DefaultHttpClient();
//
		Log.e("Receipt url", "Url : " + receiptURL_final);
//
//		HttpGet httpGet = new HttpGet(receiptURL_final);
//		try {
//			HttpResponse response = client.execute(httpGet);
//
//			HttpEntity httpEntity = response.getEntity();
//			responseString = EntityUtils.toString(httpEntity);
//
////			Log.e("Registration", "responseString : " + responseString);
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			errorINConnection = true;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			errorINConnection = true;
//		}
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(receiptURL_final);
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			responseString = OutputStreamReader.readStream(in);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorINConnection = true;
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

		if (pDialog != null) {
			pDialog.dismiss();
		}

		if (errorINConnection == false) {

			if (responseString != null && !responseString.trim().equals("") && responseString.trim().equals("1")) {
				Toast.makeText(mContext, "" + LBL_CHECK_INBOX_TXT_str, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(mContext, "" + LBL_FAILED_SEND_RECEIPT_EMAIL_TXT_str, Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(mContext, "" + LBL_FAILED_SEND_RECEIPT_EMAIL_TXT_str, Toast.LENGTH_LONG).show();
		}
	}

}

package com.mainProfile.classFiles;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/

import com.fonts.Text.MyEditText;
import com.hwindiapp.passenger.R;
import com.utils.CommonUtilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class SendContactQuery extends AsyncTask<String, String, String> {

	String contactURL = CommonUtilities.SERVER_URL;

	String responseString = "";
	String subject_str = "";
	String message_str = "";

	String LBL_FAILED_SEND_CONTACT_QUERY_TXT_str = "";
	String LBL_SENT_CONTACT_QUERY_SUCCESS_TXT_str = "";
	String LBL_SEND_CONTACT_QUERY_LOADING_TXT_str = "";
	String userID = "";
	Context mContext;

	ProgressDialog pDialog;
	MyEditText subject_txt;
	MyEditText content_txt;

	boolean errorINConnection = false;

	public SendContactQuery(Context mContext, String userID, String LBL_FAILED_SEND_CONTACT_QUERY_TXT_str,
			String LBL_SENT_CONTACT_QUERY_SUCCESS_TXT_str, String LBL_SEND_CONTACT_QUERY_LOADING_TXT_str,
			String subject_str, String message_str, MyEditText subject_txt, MyEditText content_txt) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.userID = userID;
		this.LBL_FAILED_SEND_CONTACT_QUERY_TXT_str = LBL_FAILED_SEND_CONTACT_QUERY_TXT_str;
		this.LBL_SENT_CONTACT_QUERY_SUCCESS_TXT_str = LBL_SENT_CONTACT_QUERY_SUCCESS_TXT_str;
		this.LBL_SEND_CONTACT_QUERY_LOADING_TXT_str = LBL_SEND_CONTACT_QUERY_LOADING_TXT_str;
		this.subject_str = subject_str;
		this.message_str = message_str;
		this.subject_txt = subject_txt;
		this.content_txt = content_txt;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

		pDialog = new ProgressDialog(mContext, R.style.DialogTheme_custom);
		pDialog.setMessage("" + LBL_SEND_CONTACT_QUERY_LOADING_TXT_str);
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		String contactURL_final = contactURL + "type=sendContactQuery&UserType=passenger&UserId=" + userID + "&message="
				+ URLEncoder.encode(""+message_str) + "&subject=" + URLEncoder.encode(""+subject_str);
//		HttpClient client = new DefaultHttpClient();
//
//		// Log.e("Receipt url", "Url : " + receiptURL_final);
//
//		HttpGet httpGet = new HttpGet(contactURL_final);
//		try {
//			HttpResponse response = client.execute(httpGet);
//
//			HttpEntity httpEntity = response.getEntity();
//			responseString = EntityUtils.toString(httpEntity);
//
//			// Log.e("Registration", "responseString : " + responseString);
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
			URL url = new URL(contactURL_final);
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
				Toast.makeText(mContext, "" + LBL_SENT_CONTACT_QUERY_SUCCESS_TXT_str, Toast.LENGTH_LONG).show();
				
				subject_txt.setText("");
				content_txt.setText("");
			} else {
				Toast.makeText(mContext, "" + LBL_FAILED_SEND_CONTACT_QUERY_TXT_str, Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(mContext, "" + LBL_FAILED_SEND_CONTACT_QUERY_TXT_str, Toast.LENGTH_LONG).show();
		}
	}

}

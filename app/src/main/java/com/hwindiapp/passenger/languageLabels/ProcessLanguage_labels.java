package com.hwindiapp.passenger.languageLabels;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mainProfile.classFiles.OutputStreamReader;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/

import com.utils.CommonUtilities;

import android.os.AsyncTask;

public class ProcessLanguage_labels extends AsyncTask<String, String, String> {

	// Launcher_TaxiServiceActivity launcherActivity;

	String languageLabels_json_str = "";
	String languageList_json_str = "";
	String configurationLinks_json_str = "";

	private UpdateLanguageListner languageListner;

	boolean InitialCall;
	String url_language_labels = "";
	boolean errorToGetData = false;

	public ProcessLanguage_labels(boolean InitialCall, String url_language_labels) {
		// TODO Auto-generated constructor stub
		this.InitialCall = InitialCall;
		this.url_language_labels = url_language_labels;
	}

	public String language_labels_get() {
		String responseString = "";
		
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(url_language_labels);
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

	public String language_list_get() {
		String responseString = "";

		String language_default_url = CommonUtilities.SERVER_URL_LANGUAGE_LIST;
		
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(language_default_url);
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

	public String configurationLinks_get() {
		String responseString = "";

		String language_default_url = CommonUtilities.SERVER_URL_LINK_CONFIGURATION;

		
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(language_default_url);
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
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		languageLabels_json_str = language_labels_get();

		if (InitialCall == true) {
			languageList_json_str = language_list_get();
			configurationLinks_json_str = configurationLinks_get();
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if (errorToGetData == false) {

			if (InitialCall == true) {
				languageListner.handleDefaultLangCallback(languageLabels_json_str, languageList_json_str,
						configurationLinks_json_str);
			} else if (InitialCall == false) {
				languageListner.handleUpdateLangCallback(languageLabels_json_str);
			}
		} else {
			languageListner.handleErrorOnLangCallback(errorToGetData);
		}

	}

	public interface UpdateLanguageListner {
		void handleUpdateLangCallback(String Labels_lang);

		void handleErrorOnLangCallback(boolean error);

		void handleDefaultLangCallback(String languageLabels, String languageList, String countryList);
	}

	public void setUpdateLanguageListener(UpdateLanguageListner languageListner) {
		this.languageListner = languageListner;
	}
}

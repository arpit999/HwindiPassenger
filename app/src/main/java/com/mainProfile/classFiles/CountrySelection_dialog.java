package com.mainProfile.classFiles;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.VolleyLibFiles.CountryListAdapter;
import com.VolleyLibFiles.Country_List_Item;
import com.fonts.Text.MyEditText;
import com.fonts.Text.MyTextView;
import com.hwindiapp.passenger.R;
import com.utils.CommonUtilities;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CountrySelection_dialog implements Runnable {

	Context mContext;
	boolean firstClick;
	String json_country = "";

	MyTextView problem_country_loading;
	ListView country_listView;
	ProgressBar loading_country_codes;
	CountryListAdapter adapter_list_country;
	List<Country_List_Item> Country_List;

	private CountrySelectionListner countrySelectionListner;

	public CountrySelection_dialog(Context mContext, boolean firstClick, String json_country) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.firstClick = firstClick;
		this.json_country = json_country;
	}

	public void changeFirstClickValue(String countryListJSON) {
		this.firstClick = false;
		this.json_country = countryListJSON;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		final Dialog dialog_country_selection = new Dialog(mContext, R.style.DialogTheme_Country);
		dialog_country_selection.setContentView(R.layout.country_selection_popup_layout);

		final MyEditText search_country = (MyEditText) dialog_country_selection.findViewById(R.id.search_country);
		country_listView = (ListView) dialog_country_selection.findViewById(R.id.country_listView);

		loading_country_codes = (ProgressBar) dialog_country_selection.findViewById(R.id.loading_country_codes);

		problem_country_loading = (MyTextView) dialog_country_selection.findViewById(R.id.problem_country_loading);

		Country_List = new ArrayList<Country_List_Item>();

		Window window = dialog_country_selection.getWindow();
		window.setGravity(Gravity.CENTER);

		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		dialog_country_selection.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		dialog_country_selection.show();
		dialog_country_selection.setCanceledOnTouchOutside(false);
		dialog_country_selection.setCancelable(true);

		if (firstClick == true) {
			new loadCountryList().execute();
		} else {

			loadCountryJSON(json_country);
		}

		country_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				TextView country_number = (TextView) view.findViewById(R.id.country_number);
				TextView countryCode = (TextView) view.findViewById(R.id.countryCode);

				String phoneNumber_str = country_number.getText().toString();
				String countryCode_str = countryCode.getText().toString();

				if (countrySelectionListner != null) {
					countrySelectionListner.handleSelectedCountryCallback(phoneNumber_str,countryCode_str);
				}

				dialog_country_selection.dismiss();

			}
		});

		search_country.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String text = search_country.getText().toString().toLowerCase(Locale.US);
				// adapter_list_country.filter(text);

				if (adapter_list_country != null) {
					adapter_list_country.getFilter().filter(text.toString());
				}
			}
		});

	}

	public class loadCountryList extends AsyncTask<String, String, String> {

		boolean errorONConnection = false;

		String countryList_json = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading_country_codes.setVisibility(View.VISIBLE);
		}

		public String getCountryList() {
			String responseString = "";
			String baseUrl = CommonUtilities.SERVER_URL;
			String registerParam = "type=%s";

			String registerUrl = baseUrl + String.format(registerParam, "countryList");

			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL(registerUrl);
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				responseString = OutputStreamReader.readStream(in);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorONConnection = true;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}

			}

			return responseString;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			countryList_json = getCountryList();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			loading_country_codes.setVisibility(View.GONE);

			if (errorONConnection == false) {

				loadCountryJSON(countryList_json);

				if (countrySelectionListner != null) {
					countrySelectionListner.handleCountryJSONCallback(countryList_json);
				}
			} else {
				problem_country_loading.setVisibility(View.VISIBLE);
			}
		}

	}

	public void loadCountryJSON(String json_country) {
		JSONObject obj_country_list = null;

		JSONArray arr_list_of_country = null;
		try {
			obj_country_list = new JSONObject(json_country);
			arr_list_of_country = obj_country_list.getJSONArray("countryList");

			for (int i = 0; i < arr_list_of_country.length(); i++) {

				JSONObject obj_temp = arr_list_of_country.getJSONObject(i);

				String vCountry_str = obj_temp.getString("vCountry");
				String vPhoneCode_str = obj_temp.getString("vPhoneCode");
				String vCountryCode_str = obj_temp.getString("vCountryCode");

				Country_List_Item item_country = new Country_List_Item();
				item_country.setcountry_name(vCountry_str);
				item_country.setcountry_name_phone(vPhoneCode_str);
				item_country.setCountry_Code(vCountryCode_str);
				item_country.setimg_flag("");

				Country_List.add(item_country);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		country_listView.setVisibility(View.VISIBLE);

		adapter_list_country = new CountryListAdapter(mContext, R.layout.country_list_row, Country_List);

		country_listView.setAdapter(adapter_list_country);
		adapter_list_country.notifyDataSetChanged();
	}

	public interface CountrySelectionListner {
		void handleSelectedCountryCallback(String phoneCode,String countryCode);

		void handleCountryJSONCallback(String json_countryList);
	}

	public void setCountryListener(CountrySelectionListner countrySelectionListner) {
		this.countrySelectionListner = countrySelectionListner;
	}

}

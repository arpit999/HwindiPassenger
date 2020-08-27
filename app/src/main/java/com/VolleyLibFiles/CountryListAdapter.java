package com.VolleyLibFiles;


import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hwindiapp.passenger.R;

public class CountryListAdapter extends ArrayAdapter<Country_List_Item> implements Filterable{

	private List<Country_List_Item> original;
	private ArrayList<Country_List_Item> mUnfilteredData;
	
	private Context activity;
	private LayoutInflater inflater;
	private List<Country_List_Item> countryItems;
	private List<Country_List_Item> OriginalcountryItems;
	
	private Filter filter;
	
	int layRessID;
	
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CountryListAdapter(Context activity,int textViewResourceId, List<Country_List_Item> countryItems) {
		super(activity, textViewResourceId, countryItems);
		this.layRessID = textViewResourceId;
		inflater = LayoutInflater.from(activity);
		
		this.activity = activity;
		this.original=countryItems;
//		this.countryItems = countryItems;
//		this.OriginalcountryItems.addAll(countryItems);
	}

//	@Override
//	public int getCount() {
//		return countryItems.size();
//	}
//
//	@Override
//	public Country_List_Item getItem(int location) {
//		return countryItems.get(location);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}

//	public void filter(String charText) {
//		charText = charText.toLowerCase(Locale.US);
//		countryItems.clear();
//
//		if (charText.length() == 0) {
//			countryItems.addAll(OriginalcountryItems);
//		} else {
//			Log.d("OriginalcountryItems:", "OriginalcountryItems:" + OriginalcountryItems.size());
//			for (int i = 0; i < OriginalcountryItems.size(); i++) {
//				String country_txt = OriginalcountryItems.get(i).getcountry_name();
//
//				if (country_txt.toLowerCase(Locale.US).contains("" + charText)) {
//					countryItems.add(OriginalcountryItems.get(i));
//				}
//			}
//
//		}
//		notifyDataSetChanged();
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

//		if (inflater == null)
//			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		if (convertView == null)
//			convertView = inflater.inflate(R.layout.country_list_row, null);
		
		Country_List_Item country_item = getItem(position);
		
		if (convertView == null) {
			convertView = inflater.inflate(layRessID, parent, false);
		}

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.img_country);
		TextView country_name_txt = (TextView) convertView.findViewById(R.id.country_name_txt);

		TextView country_number = (TextView) convertView.findViewById(R.id.country_number);

		TextView countryCode = (TextView) convertView.findViewById(R.id.countryCode);
		// TextView rating = (TextView) convertView.findViewById(R.id.rating);

		// TextView year = (TextView)
		// convertView.findViewById(R.id.releaseYear);

		// getting movie data for the row
//		Country_List_Item country_item = countryItems.get(position);

		country_name_txt.setText("" + country_item.getcountry_name());
		country_number.setText("" + country_item.getcountry_name_phone());
		countryCode.setText("" + country_item.getCountry_Code());
		// String image_url = SERVER_URL + "upload/Driver/" + m.getdriver_id() +
		// "/"
		// + URLEncoder.encode(m.getdriver_img());
		// thumbnail image
		// thumbNail.setImageUrl(image_url, imageLoader);

		return convertView;
	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new NameFilter();

		return filter;
	}

	private class NameFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mUnfilteredData == null) {
				mUnfilteredData = new ArrayList<Country_List_Item>(original);
			}

			if (prefix == null || prefix.length() == 0) {
				ArrayList<Country_List_Item> list = mUnfilteredData;
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();
				ArrayList<Country_List_Item> unfilteredValues = mUnfilteredData;
				int count = unfilteredValues.size();

				ArrayList<Country_List_Item> newValues = new ArrayList<Country_List_Item>(count);

				for (int i = 0; i < count; i++) {
					Country_List_Item fri = unfilteredValues.get(i);
					String value = fri.getcountry_name().toLowerCase();

					if (value.startsWith(prefixString)) {
						newValues.add(fri);
					}
				}

				results.values = newValues;
				results.count = newValues.size();

			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			original = (List<Country_List_Item>) results.values;

			clear();
			int count = original.size();
			int i = 0;

			for (i = 0; i < count; i++) {
				Country_List_Item name = (Country_List_Item) original.get(i);
				add(name);
			}

			if (original.size() > 0)
				notifyDataSetChanged();

			else
				notifyDataSetInvalidated();
		}

	}

}

/*
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hwindiapp.passenger.R;

public class CountryListAdapter extends BaseAdapter {

	private Activity activity;
	private LayoutInflater inflater;
	private List<Country_List_Item> countryItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CountryListAdapter(Activity activity, List<Country_List_Item> countryItems) {
		this.activity = activity;
		this.countryItems = countryItems;
	}

	@Override
	public int getCount() {
		return countryItems.size();
	}

	@Override
	public Object getItem(int location) {
		return countryItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.country_list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.img_country);
		TextView country_name_txt = (TextView) convertView.findViewById(R.id.country_name_txt);

		TextView country_number = (TextView) convertView.findViewById(R.id.country_number);

		// TextView rating = (TextView) convertView.findViewById(R.id.rating);

		// TextView year = (TextView)
		// convertView.findViewById(R.id.releaseYear);

		// getting movie data for the row
		Country_List_Item country_item = countryItems.get(position);

		country_name_txt.setText("" + country_item.getcountry_name());
		country_number.setText("" + country_item.getcountry_name_phone());
		// String image_url = SERVER_URL + "upload/Driver/" + m.getdriver_id() +
		// "/"
		// + URLEncoder.encode(m.getdriver_img());
		// thumbnail image
		// thumbNail.setImageUrl(image_url, imageLoader);

		return convertView;
	}

}*/
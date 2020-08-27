package com.VolleyLibFiles;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hwindiapp.passenger.Mainprofile_Activity;
import com.hwindiapp.passenger.R;
import com.utils.CommonUtilities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {

	private Activity activity;
	private LayoutInflater inflater;
	private List<BookingHistroy_item> couponItems;
	ArrayList<String> labels_language;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<BookingHistroy_item> couponItems,
			ArrayList<String> labels_language) {
		this.activity = activity;
		this.couponItems = couponItems;
		this.labels_language = labels_language;
	}

	@Override
	public int getCount() {
		return couponItems.size();
	}

	@Override
	public Object getItem(int location) {
		return couponItems.get(location);
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
			convertView = inflater.inflate(R.layout.booking_histroy_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		com.hwindiapp.passenger.NetworkRoundedImageView thumbNail = (com.hwindiapp.passenger.NetworkRoundedImageView) convertView
				.findViewById(R.id.img_driver);
		TextView DriverName = (TextView) convertView.findViewById(R.id.driver_name);

		TextView driver_id = (TextView) convertView.findViewById(R.id.driver_id);

		NetworkImageView img_map = (NetworkImageView) convertView.findViewById(R.id.img_map);

		BookingHistroy_item m = couponItems.get(position);

		String image_url = CommonUtilities.SERVER_URL_PHOTOS + "upload/Driver/" + m.getdriver_id() + "/"
				+ URLEncoder.encode(m.getdriver_img());
		// thumbnail image

		if (m.getdriver_img().trim().length() == 0 || m.getdriver_img().equals("NONE") || m.getdriver_img().equals("null")
				|| m.getdriver_img().trim().equals("") || m.getdriver_img().equals("3_")) {
			// Log.d("img_name:img_name:", ""+img_name);
			try {
				Bitmap bmp_usr_pic = BitmapFactory.decodeResource(activity.getResources(), R.drawable.friends_main);
				thumbNail.setImageBitmap(
						decodeSampledBitmapFromResource(activity.getResources(), R.drawable.friends_main, 120, 120));
			} catch (Exception e2) {
				// TODO: handle exception
			}
		} else {
			try {
				thumbNail.setImageUrl(image_url, imageLoader);
				thumbNail.setErrorImageResId(R.drawable.friends_main);
			} catch (Exception e) {
				// TODO: handle exception
				try {
					Bitmap bmp_usr_pic = BitmapFactory.decodeResource(activity.getResources(), R.drawable.friends_main);
					thumbNail.setImageBitmap(decodeSampledBitmapFromResource(activity.getResources(),
							R.drawable.friends_main, 120, 120));
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		}

		DriverName.setText(m.getDriverName());

		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);

		int height = (int) ((140 * metrics.density) + 0.5);
		String mapURL = "https://maps.googleapis.com/maps/api/staticmap?zoom=16&size="
				+ (int) (metrics.widthPixels / 1.5) + "x" + (int) (height / 1.5) + "&maptype=roadmap&"
				+ "markers=color:blue|label:Sorce|" + m.getlatitude() + "," + m.getlongitude();

//		Log.d("mapURL", "mapURL:" + mapURL);

		DriverName.setText(m.getDriverName());

		// String mapURL =
		// "https://maps.googleapis.com/maps/api/staticmap?zoom=16&size=400x220&maptype=roadmap&"
		// + "markers=color:blue|label:Sorce|" + m.getlatitude() + "," +
		// m.getlongitude();

		try {
			img_map.setImageUrl(mapURL, imageLoader);
		} catch (Exception e) {
			// TODO: handle exception
		}

		TextView trip_date = (TextView) convertView.findViewById(R.id.trip_date);
		TextView driver_car_type = (TextView) convertView.findViewById(R.id.driver_car_type);
		TextView fare_display = (TextView) convertView.findViewById(R.id.fare_display);
		TextView trip_cancledOrNot = (TextView) convertView.findViewById(R.id.trip_cancledOrNot);

		TextView trip_json_str = (TextView) convertView.findViewById(R.id.trip_json_str);
		trip_json_str.setText(m.getTrip_json());

		TextView startLat = (TextView) convertView.findViewById(R.id.startLat);
		TextView startLon = (TextView) convertView.findViewById(R.id.startLon);

		driver_id.setText(m.getdriver_id());

//		String startDate_str = m.getStartDate();

//		DateFormat date_formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// replace with your start date string
//		Date date_str = null;
//		try {
//			date_str = date_formate.parse(startDate_str);
//		} catch (ParseException e) {
//			 TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		String REdate_formate = new SimpleDateFormat("dd/MM ## hh:mm a").format(date_str);

//		String defaultCurrencyCodeSign_str = Mainprofile_Activity.defaultCurrencySign_str;

		String LBL_AT_TXT_str = "at";
		String LBL_CANCELED_TXT_str = "Canceled";
		String LBL_ON_WAY_TXT_str = "On the Way";
		String LBL_FINISHED_TXT_str = "Finished";
		if (labels_language.size() > 4) {
			LBL_AT_TXT_str = labels_language.get(0);
			LBL_CANCELED_TXT_str = labels_language.get(2);
			LBL_ON_WAY_TXT_str = labels_language.get(3);
			LBL_FINISHED_TXT_str = labels_language.get(4);
		}
//		String CurrentTIme = REdate_formate.replace("##", "" + LBL_AT_TXT_str);

		trip_date.setText(m.getStartDate());
		driver_car_type.setText(m.getcarType());
		fare_display.setText(m.getCurrencySymbol() + " " + m.gettotal_fare());

		if (m.getstatus().equals("Canceled")) {
			trip_cancledOrNot.setText("" + LBL_CANCELED_TXT_str);
		} else if (m.getstatus().equals("Active")) {
			trip_cancledOrNot.setText("" + LBL_ON_WAY_TXT_str);
		} else if (m.getstatus().equals("Finished")) {
			trip_cancledOrNot.setText("" + LBL_FINISHED_TXT_str);
		}

		startLat.setText(m.getlatitude());
		startLon.setText(m.getlongitude());

		return convertView;
	}

	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
}
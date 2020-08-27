package com.listAdapter.files;

import java.util.List;

import com.VolleyLibFiles.AppController;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hwindiapp.passenger.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomAdapter_FAQtype extends BaseAdapter {

	private Activity activity;
	private LayoutInflater inflater;
	private List<FAQItem> faqItems;
	private boolean show_icon;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomAdapter_FAQtype(Activity activity, List<FAQItem> couponItems,boolean show_icon) {
		this.activity = activity;
		this.faqItems = couponItems;
		this.show_icon=show_icon;
	}

	@Override
	public int getCount() {
		return faqItems.size();
	}

	@Override
	public Object getItem(int location) {
		return faqItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater
					.inflate(R.layout.faq_category_list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		
		com.fonts.Text.MyTextView category_txt_faq = (com.fonts.Text.MyTextView) convertView
				.findViewById(R.id.category_txt_faq);
		com.fonts.Text.MyTextView category_txt_faq_id = (com.fonts.Text.MyTextView) convertView
				.findViewById(R.id.category_txt_faq_id);

		com.fonts.Text.MyTextView category_txt_faq_questions = (com.fonts.Text.MyTextView) convertView
				.findViewById(R.id.category_txt_faq_questions);

		// getting movie data for the row
		FAQItem faq_names = faqItems.get(position);

		// thumbnail image

		category_txt_faq.setText(faq_names.getvTitle_EN());

		category_txt_faq_id.setText(faq_names.getiFaqcategoryId());

		category_txt_faq_questions.setText(faq_names.getQuestions());
		
		if(show_icon==true){
			NetworkImageView thumbnail_icon=(NetworkImageView) convertView.findViewById(R.id.thumbnail_icon);
			thumbnail_icon.setImageUrl(faq_names.getICON_url(), imageLoader);
			thumbnail_icon.setVisibility(View.VISIBLE);
			thumbnail_icon.setErrorImageResId(R.mipmap.ic_launcher);
		}

		return convertView;
	}

}

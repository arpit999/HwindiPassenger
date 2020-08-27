package com.listAdapter.files;

import java.util.List;

import com.hwindiapp.passenger.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MessageAdapter extends BaseAdapter {

	private Context context;
	private List<Message_user_driver> messagesItems;
	private String PassengerName = "";
	private String DriverName = "";

	public MessageAdapter(Context context, List<Message_user_driver> objects, String PassengerName, String DriverName) {
		this.context = context;
		this.messagesItems = objects;
		this.PassengerName = PassengerName;
		this.DriverName = DriverName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		Message_user_driver message_adapter = messagesItems.get(position);

		// com.fonts.Text.MyTextView tv = (com.fonts.Text.MyTextView)
		// convertView.findViewById(android.R.id.text1);

		// String text = message_adapter.messages_user_driver;

		String message_txt = message_adapter.messages_user_driver;

		if (message_txt.contains("Passenger")) {
			convertView = mInflater.inflate(R.layout.list_item_message_right, null);

			com.fonts.Text.MyTextView lblFrom = (com.fonts.Text.MyTextView) convertView.findViewById(R.id.lblMsgFrom);
			com.fonts.Text.MyTextView txtMsg = (com.fonts.Text.MyTextView) convertView.findViewById(R.id.txtMsg);

			txtMsg.setText(message_txt.replace("Passenger:", ""));

			try {
				String name_passenger = Character.toString(PassengerName.charAt(0)).toUpperCase()
						+ PassengerName.substring(1);
				lblFrom.setText(name_passenger);
			} catch (Exception e) {
				// TODO: handle exception
				String name_passenger = PassengerName;
				lblFrom.setText(name_passenger);
			}

			// tv.setText(message_txt.replace("Passenger:", ""));
			// tv.setGravity(Gravity.RIGHT);

		} else {
			convertView = mInflater.inflate(R.layout.list_item_message_left, null);

			com.fonts.Text.MyTextView lblFrom = (com.fonts.Text.MyTextView) convertView.findViewById(R.id.lblMsgFrom);
			com.fonts.Text.MyTextView txtMsg = (com.fonts.Text.MyTextView) convertView.findViewById(R.id.txtMsg);

			txtMsg.setText(message_txt.replace("Driver:", ""));

			try {
				String name_driver = Character.toString(DriverName.charAt(0)).toUpperCase() + DriverName.substring(1);

				lblFrom.setText(name_driver);
			} catch (Exception e) {
				// TODO: handle exception
				String name_driver = DriverName;

				lblFrom.setText(name_driver);
			}

			// tv.setText(message_txt.replace("Driver:", ""));
			// tv.setGravity(Gravity.LEFT);
		}

		// tv.setText(text);

		return convertView;
	}

	@Override
	public int getCount() {
		return messagesItems.size();
	}

	@Override
	public Object getItem(int position) {
		return messagesItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}

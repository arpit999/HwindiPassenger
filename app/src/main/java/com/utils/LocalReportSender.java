package com.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class LocalReportSender implements ReportSender {

	private final Map<ReportField, String> mMapping = new HashMap<ReportField, String>();
	private FileWriter crashReport = null;

	public LocalReportSender(Context ctx) {
		// the destination
		// File logFile = new File(Environment.getExternalStorageDirectory(),
		// "Flowerlog.txt");
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/Hwindi");
		myDir.mkdirs();

		File logFile = new File(myDir, "HwindiApplog.txt");
		try {
			crashReport = new FileWriter(logFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class writeLogInBackGround extends AsyncTask<String, String, String> {

		CrashReportData report;

		public writeLogInBackGround(CrashReportData report) {
			// TODO Auto-generated constructor stub
			this.report = report;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			final Map<String, String> finalReport = remap(report);

			try {
				BufferedWriter buf = new BufferedWriter(crashReport);

				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				// get current date time with Date()
				Date date = new Date();
				System.out.println(dateFormat.format(date));

				String Current_Date = "" + dateFormat.format(date);

				buf.append("\r\n\r\n\r\n\r\nNew Crash Log: At: " + Current_Date + " \r\n\r\n");

				Set<Entry<String, String>> set = finalReport.entrySet();
				Iterator<Entry<String, String>> i = set.iterator();

				while (i.hasNext()) {
					Map.Entry<String, String> me = (Entry<String, String>) i.next();
					buf.append("[" + me.getKey() + "]=" + me.getValue());
				}

				buf.flush();
				buf.close();
			} catch (IOException e) {
				Log.e("TAG", "IO ERROR", e);
			}

			return null;
		}

	}

	@Override
	public void send(Context cont, CrashReportData report) throws ReportSenderException {

		new writeLogInBackGround(report).execute();
	}

	private boolean isNull(String aString) {
		return aString == null || ACRAConstants.NULL_VALUE.equals(aString);
	}

	private Map<String, String> remap(Map<ReportField, String> report) {

		ReportField[] fields = ACRA.getConfig().customReportContent();
		if (fields.length == 0) {
			fields = ACRAConstants.DEFAULT_MAIL_REPORT_FIELDS;
		}

		final Map<String, String> finalReport = new HashMap<String, String>(report.size());
		for (ReportField field : fields) {
			if (mMapping == null || mMapping.get(field) == null) {
				finalReport.put(field.toString(), report.get(field));
			} else {
				finalReport.put(mMapping.get(field), report.get(field));
			}
		}
		return finalReport;
	}

}
package com.utils;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import android.app.Application;

//@ReportsCrashes( // will not be used
//mailTo = "reports@yourdomain.com", customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
//		ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE,
//		ReportField.LOGCAT }, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)

 @ReportsCrashes()
public class UberClone extends Application {

	@Override
	public void onCreate() {
		// The following line triggers the initialization of ACRA
		super.onCreate();
		ACRA.init(this);
		ACRA.getErrorReporter().setReportSender(new LocalReportSender(this));
	}
}

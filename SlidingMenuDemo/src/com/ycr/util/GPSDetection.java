package com.ycr.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class GPSDetection {
	private LocationManager locationManager;
	private Activity activity;

	public GPSDetection(Activity activity) {
		this.activity = activity;
		this.locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public boolean GPSProvider() {
		boolean GPS_status = this.locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return GPS_status;
	}
	public void OpenGPS() {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			this.activity.startActivityForResult(intent, 0);

		} catch (ActivityNotFoundException ex) {
			// The Android SDK doc says that the location settings activity
			// may not be found. In that case show the general settings.
			// General settings activity
			intent.setAction(Settings.ACTION_SETTINGS);
			try {
				this.activity.startActivityForResult(intent, 0);
			} catch (Exception e) {
			}
		}
	}
}

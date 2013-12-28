package com.apolloplanner.update;

import com.apolloplanner.DataManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		
		//Immediately check for missed posts
		if (DataManager.getDoBackgroundUpdates(arg0)) {
			Intent i = new Intent(arg0, BackgroundUpdater.class);
			arg0.startService(i);
		}
	}
}

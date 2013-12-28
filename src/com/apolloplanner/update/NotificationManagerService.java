package com.apolloplanner.update;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class NotificationManagerService extends Service{

	public static int MODE_REMOVE = 0;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		/**
		 * Handles the actions to perform on the notifications. Pass the NotificationManagerService int for mode
		 * 
		 * pass it:
		 * "mode" - what mode to do
		 * "id" - the Notification's id
		 */
		Bundle b = intent.getExtras();
		int mode = b.getInt("mode");
		int id = b.getInt("id");
		
		if (mode == MODE_REMOVE) {
			//remove notification
			NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(id);
		}
		
		stopSelf();
		
		return super.onStartCommand(intent, flags, startId);
	}

}

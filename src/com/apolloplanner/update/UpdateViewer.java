package com.apolloplanner.update;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.apolloplanner.Functions;
import com.apolloplanner.R;

public class UpdateViewer extends Activity {

	
	/**
	 * Requires bundled parameters:
	 * 
	 * id
	 * name
	 * message
	 * date
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_viewer_layout);

		//Get variables
		Bundle b = getIntent().getExtras();
		String name = b.getString("name");
		String message = b.getString("message");
		String[] dateTime = b.getString("date").split(" ");
		String date = Functions.formatSingleDate(dateTime[0]);
		String time = Functions.getTimeFormat(dateTime[1]);
		int id = b.getInt("id");
		
		//Dismiss notification
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(id);
		
		//Edit view values
		TextView nameView = (TextView) findViewById(R.id.update_name);
		TextView dateView = (TextView) findViewById(R.id.update_date);
		TextView messageView = (TextView) findViewById(R.id.update_message);
		nameView.setText(name);
		dateView.setText(date + ", " + time);
		messageView.setText(message);
	}

}

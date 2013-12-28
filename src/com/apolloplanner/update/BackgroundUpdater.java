package com.apolloplanner.update;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.apolloplanner.DataManager;
import com.apolloplanner.Functions;
import com.apolloplanner.R;
import com.apolloplanner.User;

public class BackgroundUpdater extends Service{

	private String response;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void doNotification(String response) {
		
		if (response.equals("error") || response.equals("unknown") || response.equals("none")) {
			//Either error, bad password, or no updates, so don't do anything
			System.out.println("Background check got nothing");
			return;
		}
		
		//Parse XML/check valid format
		Document doc = null;
		try {
			doc = User.getDomElement(response);
			doc.getElementsByTagName("post");
		} catch (Exception e) {
			//error in XML format, shouldn't happen...
			return;
		}
		
		// Check to play notification
		if (DataManager.getDoUpdateSound(getApplicationContext())) {
			try {
				Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
				r.play();
			} catch (Exception e) {
			}
		}

		Random r = new Random();
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NodeList posts = doc.getElementsByTagName("post");
		for (int i = 0; i < posts.getLength(); i ++) {
			Element post = (Element) posts.item(i);
			String name = getValue(post, "name");
			String message = getValue(post, "message");
			String date = getValue(post, "date");
			
			//Get random integer for notification and pending intent ID, chances are good enough :\
			int id = r.nextInt(Integer.MAX_VALUE - 2);
			
			//Create pending intent to view in dialog
			Intent intent = new Intent(getApplicationContext(), UpdateViewer.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.putExtra("id", id);
			intent.putExtra("name", name);
			intent.putExtra("message", message);
			intent.putExtra("date", date);
			PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			
			//Intent for dismissal
			Intent dismiss = new Intent(getApplicationContext(), NotificationManagerService.class);
			dismiss.putExtra("mode", NotificationManagerService.MODE_REMOVE);
			dismiss.putExtra("id", id);
			PendingIntent dismissPi = PendingIntent.getService(getApplicationContext(), id + 1, dismiss, PendingIntent.FLAG_CANCEL_CURRENT);
			
			//Post notification
			NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
			builder.setSmallIcon(R.drawable.notification);
			builder.setContentTitle(name);
			builder.setContentInfo("Tap to view");
			builder.setTicker(name + " - New Post!");
			builder.setContentIntent(pi);
			builder.setContentText("New post!");
			
			//For big notification
			builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
			builder.addAction(R.drawable.ic_action_cancel, "Dismiss", dismissPi);
			builder.addAction(R.drawable.ic_action_accept, "View", pi);
			Notification notification = builder.build();
			notification.defaults = Notification.DEFAULT_LIGHTS;
			nm.notify(id, notification);
		}
		
		stopSelf();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if (DataManager.getDoBackgroundUpdates(getApplicationContext())) {
			// Check for set username and password
			if (DataManager.getEmail(getApplicationContext()) == null || DataManager.getPassword(getApplicationContext()) == null) {
				// Don't start
				stopSelf();
			} else {
				//Start async task
				new DataReceiver().execute();
			}
			rerun();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * Set it up to run again
	 */
	private void rerun() {
		
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent ii = new Intent(this, BackgroundUpdater.class);
		PendingIntent pii = PendingIntent.getService(getApplicationContext(), Integer.MAX_VALUE, ii, PendingIntent.FLAG_CANCEL_CURRENT);
		
		int checkFreq = DataManager.getUpdateFrequency(getApplicationContext());
		
		Calendar c = Calendar.getInstance();

		if (checkFreq < 60)
			c.add(Calendar.MINUTE, checkFreq);
		else {
			//You're in hours
			checkFreq /= 60;
			c.add(Calendar.HOUR, checkFreq);
		}
		am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pii);
	}
	
	/**
	 * Runs the Async task to fetch the XML data
	 */
	private class DataReceiver extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			//Get the XML data as a string
			System.out.println("Start Background check");
			DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(DataManager.UPDATE_URL + "?email=" + DataManager.getEmail(getApplicationContext()) + "&password=" + DataManager.getPassword(getApplicationContext()));
            response = "";
            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			System.out.println("Background check return");
			doNotification(response);
		}
	}
	
	/**
	 * Returns an element
	 */
	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}

	/**
	 * Helps return element
	 */
	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}
}

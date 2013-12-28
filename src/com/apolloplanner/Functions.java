package com.apolloplanner;

import java.util.Calendar;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class Functions {
	
	public static void downloadFile(Context context, String file, String fileName, String classFeedName) {
		
		System.out.println("getting " + file);
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://localhost/apolloplanner/uploads/1_My%20Cat_513369150.jpg"));
		request.setDescription("File from " + classFeedName + ".");
		request.setTitle(fileName);
		request.allowScanningByMediaScanner();
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file);
		
		// get download service and enqueue file
		DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		manager.enqueue(request);
	}
	
	public static String formatMultiDate(String from, String to) {
		String[] s1 = from.split("-");
		int year1 = Integer.valueOf(s1[0]);
		int month1 = Integer.valueOf(s1[1]);
		int day1 = Integer.valueOf(s1[2]);
		
		String[] s2 = to.split("-");
		int year2 = Integer.valueOf(s2[0]);
		int month2 = Integer.valueOf(s2[1]);
		int day2 = Integer.valueOf(s2[2]);
		
		if (month1 == month2) {
			return getMonth(month1) + " " +  day1 + " - " + day2;
		} else {
			return getMonth(month1) + " " + day1 + " - " + getMonth(month2) + " " + day2;
		}
	}
	
	/**
	 * Converst 18:34:00 to 6:34 pm
	 */
	public static String getTimeFormat(String input) {
		String inputs[] = input.split(":");
		int hour = Integer.valueOf(inputs[0]);
		int minute = Integer.valueOf(inputs[1]);
		String sign = "am";
		if (hour >= 12) {
			hour -= 12;
			sign = "pm";
		}
		if (hour == 0)
			hour = 12;
		String minuteS = "" + minute;
		if (minute < 10)
			minuteS = "0" + minuteS;
			
		return hour + ":" + minuteS + " " + sign;
	}
	
	/**
	 * Converts 1995-10-29 to October 29
	 * 
	 * @param input
	 * @return
	 */
	public static String formatSingleDate(String input) {
		
		String[] s = input.split("-");
		int year = Integer.valueOf(s[0]);
		int month = Integer.valueOf(s[1]);
		int day = Integer.valueOf(s[2]);
		
		return getMonth(month) + " " + day;
	}
	
	public static String getMonth(int input) {
		switch(input) {
		case 1:
			return "January";
		case 2:
			return "February";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
		}
		return null;
	}
	
	/**
	 * Get's the current day/time as 2013-12-23 13:49:31
	 */
	public static String getTodayAsSQL() {
		Calendar c = Calendar.getInstance(); // VVV offset for Jan VVV
		return (c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
	}
	
}

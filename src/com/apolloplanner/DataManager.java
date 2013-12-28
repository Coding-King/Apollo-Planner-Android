package com.apolloplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Manages user information
 * @author Elijah Carbonaro
 * 
 */
public class DataManager {

	//Constants
	final public static String DOWNLOADS_URL = "http://50.53.55.210/apolloplanner/uploads/";
	final public static String MAIN_URL = "http://50.53.55.210/ApolloPlanner/mobile/main.php";
	final public static String UPDATE_URL = "http://50.53.55.210/ApolloPlanner/mobile/update2.php";
	
	
	/**
	 * Returns the user's saved email address, or null if it has not been set
	 */
	public static String getEmail(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("email", null);
	}
	
	/**
	 * Returns the user's saved password, or null if it has not been set
	 */
	public static String getPassword(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("password", null);
	}
	
	/**
	 * Sets the user's email
	 */
	public static void setEmail(String email, Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("email", email);
		editor.commit();
	}
	
	/**
	 * Sets the user's password
	 */
	public static void setPassword(String password, Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("password", password);
		editor.commit();
	}
	
	//Background stuff
	public static boolean getDoBackgroundUpdates(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("backgroundUpdates", true);
	}
	public static boolean getDoUpdateSound(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("updateSound", true);
	}
	public static int getUpdateFrequency(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return Integer.valueOf(prefs.getString("updateFrequency", "10"));
	}

}

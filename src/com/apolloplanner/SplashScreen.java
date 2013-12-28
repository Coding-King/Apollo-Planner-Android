package com.apolloplanner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends Activity{
	
	/**
	 * Change this URL to match whatever server the application is configured for
	 */
	
	private Context ctx;
	private ProgressBar loading;
	
	private String password;
	private String email;
	private String response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        ctx = this;
        
        //Check for first time login
        email = DataManager.getEmail(ctx);
        password = DataManager.getPassword(ctx);
        
        loading = (ProgressBar) findViewById(R.id.loading_loader);
        
        if (email == null || password == null) {
        	//First time login
        	loginPrompt(ctx, false);
        	loading.setVisibility(ProgressBar.INVISIBLE);
        } else {
        	//Try login
        	login();
        }
	}
	
	/**
	 * Starts the Async task to get the XML data and sets the loading animation
	 */
	private void login() {
		loading.setVisibility(ProgressBar.VISIBLE);
		new DataReceiver().execute();
	}
	
	/**
	 * Checks for error responses from server, starts main app if XML is ok
	 */
	private void checkData() {
		if (response.equals("error")) {
			loading.setVisibility(ProgressBar.INVISIBLE);
			Toast.makeText(ctx, "Warning: a fatal error occured. Please contact the dev team", Toast.LENGTH_LONG).show();
		} else if (response.equals("teacher")) {
			loading.setVisibility(ProgressBar.INVISIBLE);
			warnTeacher();
		} else if (response.equals("unknown")) {
			loading.setVisibility(ProgressBar.INVISIBLE);
			loginPrompt(ctx, true);
		} else {
			try {
				Document doc = User.getDomElement(response);
				doc.getElementsByTagName("user");
			} catch (Exception e) {
				//The XML couldn't be parsed
				System.out.println("XML ERROR");
				serverError();
				return;
			}
			Intent i = new Intent(this, HomeActivity.class);
			DataManager.setEmail(email, ctx);
			DataManager.setPassword(password, ctx);
			i.putExtra("data", response);
			startActivity(i);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			finish();
		}
	}
	
	/**
	 * Runs the Async task to fetch the XML data
	 */
	private class DataReceiver extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			//Get the XML data as a string
			DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(DataManager.MAIN_URL + "?email=" + email + "&password=" + password);
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
			checkData();
		}
	}
	
	/**
	 * Inflates the login prompt dialog
	 */
	private void loginPrompt(Context ctx, boolean wrongAnswer) {
		LayoutInflater layoutInflater = LayoutInflater.from(ctx);
		View promptView = layoutInflater.inflate(R.layout.login_prompt_layout, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setView(promptView);

		final EditText emailPrompt = (EditText) promptView.findViewById(R.id.email_prompt);
		final EditText passwordPrompt = (EditText) promptView.findViewById(R.id.password_prompt);
		final TextView promptText = (TextView) promptView.findViewById(R.id.prompt_text);
		
		if (wrongAnswer) {
			promptText.setText("Incorrect email or password.");
			emailPrompt.setText(email);
			passwordPrompt.setText(password);
		}

		builder.setCancelable(false);
		builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// try to login
						email = emailPrompt.getText().toString();
						password = passwordPrompt.getText().toString();
						dialog.dismiss();
						login();
					}
				});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// close the application
						dialog.dismiss();
						finish();
					}
				});

		AlertDialog alertD = builder.create();
		alertD.show();
	}
	
	/**
	 * Warns a teacher that they cannot use this app
	 */
	private void warnTeacher() {
		LayoutInflater layoutInflater = LayoutInflater.from(ctx);
		View promptView = layoutInflater.inflate(R.layout.warning_message_layout, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setView(promptView);

		builder.setCancelable(false);
		builder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						finish();
					}
				});

		AlertDialog alertD = builder.create();
		alertD.show();
	}
	
	private void serverError() {
		LayoutInflater layoutInflater = LayoutInflater.from(ctx);
		View promptView = layoutInflater.inflate(R.layout.warning_message_layout, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		TextView message = (TextView) promptView.findViewById(R.id.warning_message);
		message.setText("Unable to contact server. Please try again later.");
		builder.setView(promptView);
		builder.setCancelable(false);
		builder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						finish();
					}
				});

		AlertDialog alertD = builder.create();
		alertD.show();
	}
}

package com.apolloplanner;

import com.apolloplanner.update.BackgroundUpdater;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class Settings extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	close();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed() {
    	close();
    	super.onBackPressed();
    }
    
    public void close() {
    	//Rerun in case events were changed timing-wise
		Intent ii = new Intent(this, BackgroundUpdater.class);
		startService(ii);
    	finish();
		overridePendingTransition(R.anim.back_left_to_right, R.anim.back_right_to_left);
    }

}

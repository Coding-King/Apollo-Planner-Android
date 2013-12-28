package com.apolloplanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
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
    	finish();
		overridePendingTransition(R.anim.back_left_to_right, R.anim.back_right_to_left);
    }

}

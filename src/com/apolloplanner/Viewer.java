package com.apolloplanner;

import com.apolloplanner.adapter.AssignmentAdapter;
import com.apolloplanner.adapter.PostAdapter;
import com.apolloplanner.type.Assignment;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Viewer extends ListActivity {
	
	private Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewer_layout);
		ctx = this;
		
		Intent i = getIntent();
		Bundle b = i.getExtras();
		String type = b.getString("type");
		String title = b.getString("title");
		int indexId = (int) b.getLong("id");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(title);
		
		if (type.equals("class")) {
			// id will = 0 if its all classes or all feeds
			AssignmentAdapter adapter;
			if (indexId == 0) {
				adapter = new AssignmentAdapter(ctx, 2, HomeActivity.user.classes.get(indexId).assignments, true);
			} else {
				adapter = new AssignmentAdapter(ctx, 2, HomeActivity.user.classes.get(indexId).assignments, false);
			}			
			setListAdapter(adapter);
			
			if (HomeActivity.user.classes.get(indexId).assignments.size() == 0)
				Toast.makeText(ctx, "No Assignments", Toast.LENGTH_SHORT).show();
			
		} else {
			PostAdapter adapter;
			if (indexId == 0) {
				adapter = new PostAdapter(ctx, 2, HomeActivity.user.feedSummary, true);
			} else {
				adapter = new PostAdapter(ctx, 2, HomeActivity.user.feeds.get(indexId).posts, false);
			}
			setListAdapter(adapter);
			
			if (HomeActivity.user.classes.get(indexId).assignments.size() == 0)
				Toast.makeText(ctx, "No Posts", Toast.LENGTH_SHORT).show();
		}
		
		//Check for more posts
		if (HomeActivity.user.classes.get(indexId).numPosts > HomeActivity.user.classes.size()) {
			
		}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.viewer, menu);
		menu.getItem(0).setEnabled(false);
		return true;
	}

}

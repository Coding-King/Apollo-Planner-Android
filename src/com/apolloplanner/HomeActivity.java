package com.apolloplanner;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.apolloplanner.adapter.ClassAdapter;
import com.apolloplanner.adapter.DrawerAdapter;
import com.apolloplanner.adapter.FeedAdapter;
import com.apolloplanner.update.BackgroundUpdater;

public class HomeActivity extends FragmentActivity {

	PageAdapter pageAdapter;
	ViewPager viewpager;
	static User user;
	
	private static Context ctx;
	
	//For side menu
	private String[] options;
	private ArrayList<DrawerItem> drawerItems;
	private TypedArray drawerIcons;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private DrawerAdapter adapter;
	private boolean isDrawerOpen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);

		//Check for updates
		Intent ii = new Intent(this, BackgroundUpdater.class);
		startService(ii);
		
		ctx = this;
		
		//Parse XML
		Intent i = getIntent();
		Bundle b = i.getExtras();
		String XMLstring = b.getString("data");
		user = new User(XMLstring);
		
		//Set up the fancy view
		pageAdapter = new PageAdapter(getSupportFragmentManager());
		viewpager = (ViewPager) findViewById(R.id.pager);
		viewpager.setAdapter(pageAdapter);
		
		/**
		 * From here on down, it is setting up the menu drawer
		 */
		isDrawerOpen = false;
		options = getResources().getStringArray(R.array.drawer_items);
		drawerIcons = getResources().obtainTypedArray(R.array.drawer_icons);
		drawerItems = new ArrayList<DrawerItem>();
		drawerItems.add(new DrawerItem(user.name + " " + user.surname, drawerIcons.getResourceId(0, -1)));
		for (int j = 1; j < 5; j ++) {
			drawerItems.add(new DrawerItem(options[j], drawerIcons.getResourceId(j, -1)));
		}
		drawerIcons.recycle();
		adapter = new DrawerAdapter(getApplicationContext(), drawerItems);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close) {
            //Closed
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(getResources().getString(R.string.app_name));
                isDrawerOpen = false;
            }
            //Opened
            public void onDrawerOpened(View drawerView) {
            	isDrawerOpen = true;
                getActionBar().setTitle(getResources().getString(R.string.drawer_title));
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		drawerList = (ListView) findViewById(R.id.left_drawer);
		drawerList.setAdapter(adapter);
		drawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long id) {
				if (id == 0)
					return;
				
				switch((int) id) {
				case 0: // selected user name display
					return;
				case 1: // settings
					Intent i = new Intent(ctx, Settings.class);
					startActivity(i);
					overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
					break;
				case 2: // logout
					logoutPrompt();
					break;
				case 3: // help
					Intent i2 = new Intent(ctx, HelpActivity.class);
					startActivity(i2);
					overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
					break;
				case 4: // about
					Intent i3 = new Intent(ctx, AboutActivity.class);
					startActivity(i3);
					overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
					break;
				}
				
				drawerLayout.closeDrawers();
			}
		});
	}
	
	/**
	 * Open the drawer with the hard menu button
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (isDrawerOpen)
				drawerLayout.closeDrawers();
			else
				drawerLayout.openDrawer(drawerList);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Gets the fragments for each section
	 */
	public class PageAdapter extends FragmentPagerAdapter {

		public PageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new CustomListFragment();
			Bundle args = new Bundle();
			args.putInt(CustomListFragment.SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0)
				return "classes";
			else
				return "feeds";
		}
	}

	/**
	 * Supplies the custom fragment for class/feed
	 */
	public static class CustomListFragment extends ListFragment {

		public static final String SECTION_NUMBER = "section_number";

		public CustomListFragment() {
			
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			
			Intent i = new Intent(ctx, Viewer.class);
			if (isClasses()) {
				//Open class view
				i.putExtra("type", "class");
				i.putExtra("id", id);
				i.putExtra("title", user.classes.get(position).name);
			} else {
				//Open feed view
				i.putExtra("type", "feed");
				i.putExtra("id", id);
				i.putExtra("title", user.feeds.get(position).name);
			}
			
			getActivity().startActivity(i);
			getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
			
			super.onListItemClick(l, v, position, id);
		};
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.home_layout, container, false);
			if (isClasses()) {
				//Populate with classes
				ClassAdapter adapter = new ClassAdapter(ctx, 2, user.classes);
				setListAdapter(adapter);
			} else {
				//Fill with feeds
				FeedAdapter adapter = new FeedAdapter(ctx, 2, user.feeds);
				setListAdapter(adapter);
			}
			return view;
		}
		
		private boolean isClasses() {
			String id = Integer.toString(getArguments().getInt(SECTION_NUMBER));
			if (id.equals("1"))
				return true;
			else
				return false;
		}
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void logoutPrompt() {
		LayoutInflater layoutInflater = LayoutInflater.from(ctx);
		View promptView = layoutInflater.inflate(R.layout.warning_message_layout, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		TextView message = (TextView) promptView.findViewById(R.id.warning_message);
		message.setText("Log out of Apollo Planner?");
		builder.setView(promptView);
		builder.setCancelable(true);
		builder.setPositiveButton("Continue", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DataManager.setPassword(null, ctx);
				DataManager.setEmail(null, ctx);
				Intent i = new Intent(ctx, SplashScreen.class);
				startActivity(i);
				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
				finish();
				
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		AlertDialog alertD = builder.create();
		alertD.show();
    }

}
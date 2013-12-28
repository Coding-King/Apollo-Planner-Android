package com.apolloplanner.adapter;

import java.util.List;

import com.apolloplanner.Functions;
import com.apolloplanner.R;
import com.apolloplanner.R.id;
import com.apolloplanner.R.layout;
import com.apolloplanner.type.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PostAdapter extends ArrayAdapter<Post>{
	private Context context;
	private boolean isAllFeeds;
	int layoutResourceId;
	List<Post> posts;

	
	public PostAdapter(Context context, int layoutResourceId, List<Post> data, boolean isAllFeeds) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.posts = data;
		this.isAllFeeds = isAllFeeds;
	}

	public void remove(Post post) {
		super.remove(post);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.post_row, parent, false);
		
		TextView dateText = (TextView) v.findViewById(R.id.date);
		TextView messageText = (TextView) v.findViewById(R.id.message);
		TextView fileText = (TextView) v.findViewById(R.id.file);
		TextView time = (TextView) v.findViewById(R.id.time);
		
		String message = posts.get(position).message;
		final String file = posts.get(position).file;
		final String fileTitle = posts.get(position).fileTitle;
		
		messageText.setText(message);
		
		if (isAllFeeds) {
			dateText.setText(posts.get(position).feedName + "\n" + Functions.formatSingleDate(posts.get(position).date) + " at " + Functions.getTimeFormat(posts.get(position).time));
			time.setVisibility(TextView.INVISIBLE);
		} else {
			dateText.setText(Functions.formatSingleDate(posts.get(position).date));
			time.setText(Functions.getTimeFormat(posts.get(position).time));
		}
		if (posts.get(position).file == "") {
			fileText.setVisibility(TextView.GONE);
		} else {
			fileText.setText("Attached file: " + fileTitle);
			fileText.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Functions.downloadFile(getContext(), file, fileTitle, posts.get(position).feedName);
				}
			});
		}
		
		return v;
	}
}

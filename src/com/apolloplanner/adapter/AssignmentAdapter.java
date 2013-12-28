package com.apolloplanner.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apolloplanner.Functions;
import com.apolloplanner.R;
import com.apolloplanner.type.Assignment;

public class AssignmentAdapter extends ArrayAdapter<Assignment>{
	private Context context;
	private boolean isAllClasses;
	int layoutResourceId;
	List<Assignment> assignments;

	
	public AssignmentAdapter(Context context, int layoutResourceId, List<Assignment> data, boolean isAllClasses) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.assignments = data;
		this.isAllClasses = isAllClasses;
	}

	public void remove(Assignment post) {
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
		time.setVisibility(TextView.INVISIBLE);
		
		String dateFrom = assignments.get(position).dateFrom;
		String dateTo = assignments.get(position).dateTo;
		String message = assignments.get(position).message;
		final String file = assignments.get(position).file;
		final String fileTitle = assignments.get(position).fileTitle;
		
		String date = "";
		if (dateTo.equals("1970-01-01"))
			date = Functions.formatSingleDate(dateFrom);
		else
			date = Functions.formatMultiDate(dateFrom, dateTo);
		
		messageText.setText(message);

		if (isAllClasses) {
			dateText.setText(assignments.get(position).className + "\n" + date);
		} else {
			dateText.setText(date);
		}
		if (file.equals("")) {
			fileText.setVisibility(TextView.GONE);
		} else {
			fileText.setText("Attached file: " + fileTitle);
			fileText.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Functions.downloadFile(getContext(), file, fileTitle, assignments.get(position).className);
				}
			});
		}
		
		return v;
	}
}

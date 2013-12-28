package com.apolloplanner.adapter;

import java.util.List;

import com.apolloplanner.R;
import com.apolloplanner.R.id;
import com.apolloplanner.R.layout;
import com.apolloplanner.type.Class;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ClassAdapter extends ArrayAdapter<Class>{
	private Context context;
	int layoutResourceId;
	List<Class> data;

	
	public ClassAdapter(Context context, int layoutResourceId, List<Class> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	public void remove(Class obj) {
		super.remove(obj);
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.main_row, parent, false);
		
		TextView name = (TextView) v.findViewById(R.id.list_name);
		TextView teacher = (TextView) v.findViewById(R.id.list_teacher);
		ImageView website = (ImageView) v.findViewById(R.id.list_web_icon);
		
		name.setText(data.get(position).name);
		teacher.setText(data.get(position).teacher);
		
//		Changes if you're on allClasses
		if (data.get(position).teacher.equals("")) {
			name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
			teacher.setVisibility(TextView.GONE);
		}
		
		if (data.get(position).website.equals("none")) {
			website.setVisibility(ImageView.INVISIBLE);
		} else {
			website.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(position).website));
					context.startActivity(browserIntent);
				}
			});
		}

		return v;
	}
}

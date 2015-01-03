package com.alorma.github.ui.adapter.gists;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alorma.github.sdk.bean.dto.response.Gist;
import com.alorma.github.ui.adapter.LazyAdapter;

import java.util.List;

/**
 * Created by Bernat on 03/01/2015.
 */
public class GistsAdapter extends LazyAdapter<Gist> {
	
	public GistsAdapter(Context context, List<Gist> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = new TextView(getContext());
		Gist gist = getItem(position);
		
		tv.setText(gist.id);
		
		return tv;
	}
}

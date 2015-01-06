package com.alorma.github.ui.adapter.gists;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.response.Gist;
import com.alorma.github.ui.adapter.LazyAdapter;
import com.alorma.github.utils.AttributesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

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
		View v = inflate(R.layout.gist_row, parent, false);

		TextView title = (TextView) v.findViewById(R.id.title);
		TextView user = (TextView) v.findViewById(R.id.user);
		TextView files = (TextView) v.findViewById(R.id.files);
		ImageView avatar = (ImageView) v.findViewById(R.id.avatarAuthor);
		
		Gist gist = getItem(position);


		String author = "";
		if (gist.owner != null) {
			author = gist.owner.login;
			user.setText(gist.owner.login);
			if (gist.owner.avatar_url != null) {
				ImageLoader.getInstance().displayImage(gist.owner.avatar_url, avatar);
			}
		}

		String first = "";
		if (gist.files != null) {
			first = gist.files.keySet().iterator().next();
			files.setText(gist.files.size() + " files");
		}

		title.setText(author + "/" + first);
		
		return v;
	}
}

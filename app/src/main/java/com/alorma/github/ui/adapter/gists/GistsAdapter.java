package com.alorma.github.ui.adapter.gists;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.response.Gist;
import com.alorma.github.ui.adapter.LazyAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
			String time = "";
			if (gist.updated_at != null) {
				time = getTimeString(gist.updated_at);
			} else if (gist.created_at != null) {
				time = getTimeString(gist.created_at);
			}
			author = gist.owner.login;
			user.setText(gist.owner.login + " " + time);
			if (gist.owner.avatar_url != null) {
				ImageLoader.getInstance().displayImage(gist.owner.avatar_url, avatar);
			}
		}

		if (gist.files != null) {
			files.setText(gist.files.size() + " files");
		}

		String first = "";
		if (gist.files != null) {
			first = gist.files.keySet().iterator().next();
			files.setText(gist.files.size() + " files");
		}
		title.setText(first);

		return v;
	}

	private String getTimeString(String date) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

		DateTime dt = formatter.parseDateTime(date);
		DateTime dtNow = DateTime.now().withZone(DateTimeZone.UTC);

		Years years = Years.yearsBetween(dt.withTimeAtStartOfDay(), dtNow.withTimeAtStartOfDay());
		int text = R.string.gist_created_at_years;
		int time = years.getYears();

		if (time == 0) {
			Months months = Months.monthsBetween(dt.withTimeAtStartOfDay(), dtNow.withTimeAtStartOfDay());
			text = R.string.gist_created_at_days;
			time = months.getMonths();

			if (time == 0) {

				Days days = Days.daysBetween(dt.withTimeAtStartOfDay(), dtNow.withTimeAtStartOfDay());
				text = R.string.gist_created_at_days;
				time = days.getDays();

				if (time == 0) {
					Hours hours = Hours.hoursBetween(dt.toLocalDateTime(), dtNow.toLocalDateTime());
					time = hours.getHours();
					text = R.string.gist_created_at_hours;

					if (time == 0) {
						Minutes minutes = Minutes.minutesBetween(dt.toLocalDateTime(), dtNow.toLocalDateTime());
						time = minutes.getMinutes();
						text = R.string.gist_created_at_minutes;
						if (time == 0) {
							Seconds seconds = Seconds.secondsBetween(dt.toLocalDateTime(), dtNow.toLocalDateTime());
							time = seconds.getSeconds();
							text = R.string.gist_created_at_seconds;
						}
					}
				}
			}
		}

		return getContext().getResources().getString(text, time);
	}
}

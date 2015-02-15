package com.alorma.github.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alorma.github.R;
import com.alorma.github.sdk.services.user.GetAuthUserClient;
import com.alorma.github.ui.activity.base.BackActivity;
import com.alorma.github.ui.activity.base.BaseActivity;
import com.alorma.github.ui.fragment.gists.GistsListFragment;
import com.alorma.github.ui.fragment.gists.GistsStarredListFragment;
import com.alorma.github.ui.fragment.menu.MenuFragmentRepos;
import com.alorma.github.ui.view.SlidingTabLayout;
import com.alorma.github.utils.AttributesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bernat on 06/01/2015.
 */
public class GistsActivity extends BackActivity {

	private static final String USERNAME = "USERNAME";
	
	private ViewPager viewPager;
	private List<Fragment> listFragments;

	public static Intent launchIntent(Context context) {
		return new Intent(context, GistsActivity.class);
	}

	public static Intent launchIntent(Context context, String username) {
		Intent intent = new Intent(context, GistsActivity.class);

		intent.putExtra(USERNAME, username);

		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gists_activity);

		//String username = getIntent().getStringExtra(USERNAME);


		viewPager = (ViewPager) findViewById(R.id.pager);

		listFragments = new ArrayList<>();
		listFragments.add(GistsListFragment.newInstance());
		/*if (username == null) {
			listFragments.add(GistsStarredListFragment.newInstance());
		}*/

		viewPager.setAdapter(new NavigationPagerAdapter(getFragmentManager(), listFragments));

		viewPager.setOffscreenPageLimit(listFragments.size());

		SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabStrip);
		slidingTabLayout.setVisibility(View.GONE);
		/*if (username == null) {

			slidingTabLayout.setSelectedIndicatorColors(AttributesUtils.getAccentColor(this, R.style.AppTheme_Repos));
			slidingTabLayout.setDividerColors(Color.TRANSPARENT);
			slidingTabLayout.setViewPager(viewPager);
		}*/
	}

	private class NavigationPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> listFragments;

		public NavigationPagerAdapter(FragmentManager fm, List<Fragment> listFragments) {
			super(fm);
			this.listFragments = listFragments;
		}

		@Override
		public Fragment getItem(int position) {
			return listFragments.get(position);
		}

		@Override
		public int getCount() {
			return listFragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return getString(R.string.gists_user_fragment_title);
				case 1:
					return getString(R.string.gists_starred_fragment_title);
			}
			return "";
		}

	}
}

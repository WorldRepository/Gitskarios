package com.alorma.github.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;

import com.alorma.github.R;
import com.alorma.github.inapp.IabConstants;
import com.alorma.github.inapp.IabHelper;
import com.alorma.github.inapp.IabResult;
import com.alorma.github.inapp.Inventory;
import com.alorma.github.inapp.Purchase;
import com.alorma.github.sdk.services.user.GetAuthUserClient;
import com.alorma.github.sdk.utils.GitskariosSettings;
import com.alorma.github.ui.activity.base.BaseActivity;
import com.alorma.github.ui.fragment.events.EventsListFragment;
import com.alorma.github.ui.fragment.gists.GistsListFragment;
import com.alorma.github.ui.fragment.menu.MenuFragment;
import com.alorma.github.ui.fragment.menu.MenuItem;
import com.alorma.github.ui.fragment.orgs.OrganzationsFragment;
import com.alorma.github.ui.fragment.repos.ReposFragment;
import com.alorma.github.ui.fragment.repos.StarredReposFragment;
import com.alorma.github.ui.fragment.repos.WatchedReposFragment;
import com.alorma.github.ui.fragment.users.FollowersFragment;
import com.alorma.github.ui.fragment.users.FollowingFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener, MenuFragment.OnMenuItemSelectedListener, IabHelper.OnIabSetupFinishedListener,
		IabHelper.OnIabPurchaseFinishedListener, IabHelper.QueryInventoryFinishedListener {

	private MenuFragment menuFragment;

	private ReposFragment reposFragment;
	private StarredReposFragment starredFragment;
	private WatchedReposFragment watchedFragment;
	private FollowersFragment followersFragment;
	private FollowingFragment followingFragment;
	private IabHelper iabHelper;
	private boolean iabEnabled;
	private OrganzationsFragment organizationsFragmet;
	private EventsListFragment eventsFragment;
	private GistsListFragment gistsFragment;

	public static void startActivity(Activity context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_drawer);

		GetAuthUserClient client = new GetAuthUserClient(this);
		client.execute();

		checkIab();

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		menuFragment = MenuFragment.newInstance();
		menuFragment.setOnMenuItemSelectedListener(this);
		ft.replace(R.id.menuContent, menuFragment);
		ft.commit();
	}

	private void checkIab() {
		iabHelper = new IabHelper(this, IabConstants.KEY);
		iabHelper.startSetup(this);
	}

	@Override
	public void onIabSetupFinished(IabResult result) {
		iabEnabled = result.isSuccess();
		if (iabEnabled) {
			try {
				iabHelper.queryInventoryAsync(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.searchIcon:
				Intent search = SearchReposActivity.createLauncherIntent(this);
				startActivity(search);
				break;
		}
	}

	private void setFragment(Fragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.content, fragment);
		ft.commit();
	}

	@Override
	public void onProfileSelected() {
		Intent launcherIntent = ProfileActivity.createLauncherIntent(this);
		startActivity(launcherIntent);
	}

	@Override
	public void onReposSelected() {
		if (reposFragment == null) {
			reposFragment = ReposFragment.newInstance();
		}

		setFragment(reposFragment);
	}

	@Override
	public void onStarredSelected() {
		if (starredFragment == null) {
			starredFragment = StarredReposFragment.newInstance();
		}

		setFragment(starredFragment);
	}

	@Override
	public void onWatchedSelected() {
		if (watchedFragment == null) {
			watchedFragment = WatchedReposFragment.newInstance();
		}

		setFragment(watchedFragment);
	}

	@Override
	public void onFollowersSelected() {
		if (followersFragment == null) {
			followersFragment = FollowersFragment.newInstance();
		}

		setFragment(followersFragment);
	}

	@Override
	public void onFollowingSelected() {
		if (followingFragment == null) {
			followingFragment = FollowingFragment.newInstance();
		}

		setFragment(followingFragment);
	}

	@Override
	public void onMenuItemSelected(@NonNull MenuItem item, boolean changeTitle) {
		if (changeTitle) {
			setTitle(item.text);
		}
		closeMenu();
	}

	@Override
	public void onOrganizationsSelected() {
		if (organizationsFragmet == null) {
			organizationsFragmet = OrganzationsFragment.newInstance();
		}
		setFragment(organizationsFragmet);
	}

	@Override
	public void onUserEventsSelected() {
		GitskariosSettings settings = new GitskariosSettings(this);
		String user = settings.getAuthUser(null);
		if (user != null) {
			if (eventsFragment == null) {
				eventsFragment = EventsListFragment.newInstance(user);
			}
			setFragment(eventsFragment);
		} else {
			// TODO SHOW Error no user
		}
	}

	@Override
	public void onSettingsSelected() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override
	public void onAboutSelected() {
		Intent intent = AboutActivity.launchIntent(this);
		startActivity(intent);
	}

	@Override
	public void onGitsSelected() {
		Intent intent = GistsActivity.launchIntent(this);
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (iabHelper != null) {
			iabHelper.dispose();
		}
		iabHelper = null;
	}

	@Override
	public void onIabPurchaseFinished(IabResult result, Purchase info) {
		if (result.isFailure()) {
			invalidateOptionsMenu();
		} else if (info.getSku().equals(IabConstants.SKU_DONATE)) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean(IabConstants.SKU_DONATE, true);
			editor.apply();
			invalidateOptionsMenu();
		}
	}

	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inv) {
		if (result.isFailure()) {
			if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR) {
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(IabConstants.SKU_DONATE, true);
				editor.apply();
			}
			invalidateOptionsMenu();
		} else {
			boolean iabDonatePurchased = inv.hasPurchase(IabConstants.SKU_DONATE);
			if (iabDonatePurchased) {
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(IabConstants.SKU_DONATE, true);
				editor.apply();
			}
			invalidateOptionsMenu();
		}
	}
}

package com.alorma.github.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alorma.github.R;
import com.alorma.github.sdk.services.user.GetAuthUserClient;
import com.alorma.github.ui.activity.base.BackActivity;
import com.alorma.github.ui.activity.base.BaseActivity;
import com.alorma.github.ui.fragment.gists.GistsListFragment;
import com.alorma.github.ui.fragment.menu.MenuFragmentRepos;

/**
 * Created by Bernat on 06/01/2015.
 */
public class GistsActivity extends BackActivity {

	public static Intent launchIntent(Context context) {
		return new Intent(context, GistsActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gists_activity);

		GetAuthUserClient client = new GetAuthUserClient(this);
		client.execute();

		setFragment(GistsListFragment.newInstance());
	}

	private void setFragment(Fragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.content, fragment);
		ft.commit();
	}
}

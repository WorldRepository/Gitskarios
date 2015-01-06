package com.alorma.github.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alorma.github.R;
import com.alorma.github.sdk.services.user.GetAuthUserClient;
import com.alorma.github.ui.activity.base.BaseActivity;
import com.alorma.github.ui.fragment.gists.GistsListFragment;
import com.alorma.github.ui.fragment.menu.MenuFragment;

/**
 * Created by Bernat on 06/01/2015.
 */
public class GistsActivity extends BaseActivity{

	private MenuFragment menuFragment;

	public static Intent launchIntent(Context context) {
		return new Intent(context, GistsActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_drawer);

		GetAuthUserClient client = new GetAuthUserClient(this);
		client.execute();


		FragmentTransaction ft = getFragmentManager().beginTransaction();
		menuFragment = MenuFragment.newInstance(R.style.AppTheme_Gists);
		ft.replace(R.id.menuContent, menuFragment);
		ft.commit();
		
		setFragment(GistsListFragment.newInstance());
	}
	
	private void setFragment(Fragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.content, fragment);
		ft.commit();
	}
}

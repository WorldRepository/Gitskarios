package com.alorma.github.ui.fragment.orgs;

import android.os.Bundle;

import com.alorma.github.R;
import com.alorma.github.sdk.services.orgs.OrgsMembersClient;
import com.alorma.github.ui.fragment.users.BaseUsersListFragment;

/**
 * Created by Bernat on 13/07/2014.
 */
public class OrgsMembersFragment extends BaseUsersListFragment {
	private String org;

	public static OrgsMembersFragment newInstance() {
		return new OrgsMembersFragment();
	}

	public static OrgsMembersFragment newInstance(String org) {
		OrgsMembersFragment orgsMembersFragment = new OrgsMembersFragment();
		if (org != null) {
			Bundle bundle = new Bundle();
			bundle.putString(USERNAME, org);

			orgsMembersFragment.setArguments(bundle);
		}
		return orgsMembersFragment;
	}

	@Override
	protected void executeRequest() {
		OrgsMembersClient client = new OrgsMembersClient(getActivity(), org);
		client.setOnResultCallback(this);
		client.execute();
	}

	@Override
	protected void executePaginatedRequest(int page) {
		OrgsMembersClient client = new OrgsMembersClient(getActivity(), org, page);
		client.setOnResultCallback(this);
		client.execute();
	}

	@Override
	protected void loadArguments() {
		if (getArguments() != null) {
			org = getArguments().getString(USERNAME);
		}
	}

	@Override
	protected int getNoDataText() {
		return R.string.org_no_members;
	}
}

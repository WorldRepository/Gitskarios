package com.alorma.github.ui.fragment.gists;

import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.response.ListGists;
import com.alorma.github.sdk.services.gists.UserGistsClient;
import com.alorma.github.sdk.services.gists.UserStarredGistsClient;
import com.alorma.github.ui.adapter.gists.GistsAdapter;
import com.alorma.github.ui.fragment.base.PaginatedListFragment;
import com.alorma.githubicons.GithubIconify;

/**
 * Created by Bernat on 03/01/2015.
 */
public class GistsStarredListFragment extends GistsListFragment {

	@Override
	protected void executeRequest() {
		startRefresh();

		UserStarredGistsClient userGistsClient = new UserStarredGistsClient(getActivity());
		userGistsClient.setOnResultCallback(this);
		userGistsClient.execute();
	}

	@Override
	protected void executePaginatedRequest(int page) {
		startRefresh();

		if (gistsAdapter != null) {
			gistsAdapter.setLazyLoading(true);
		}

		UserStarredGistsClient userGistsClient = new UserStarredGistsClient(getActivity(), page);
		userGistsClient.setOnResultCallback(this);
		userGistsClient.execute();
	}

}

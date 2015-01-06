package com.alorma.github.ui.fragment.gists;

import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.response.ListGists;
import com.alorma.github.sdk.services.gists.UserGistsClient;
import com.alorma.github.ui.adapter.gists.GistsAdapter;
import com.alorma.github.ui.fragment.base.PaginatedListFragment;
import com.alorma.githubicons.GithubIconify;

/**
 * Created by Bernat on 03/01/2015.
 */
public class GistsListFragment extends PaginatedListFragment<ListGists> {

	private GistsAdapter gistsAdapter;

	public static GistsListFragment newInstance() {
		return new GistsListFragment();
	}

	@Override
	protected void executeRequest() {
		super.executeRequest();

		UserGistsClient userGistsClient = new UserGistsClient(getActivity());
		userGistsClient.setOnResultCallback(this);
		userGistsClient.execute();
	}

	@Override
	protected void executePaginatedRequest(int page) {
		super.executePaginatedRequest(page);

		if (gistsAdapter != null) {
			gistsAdapter.setLazyLoading(true);
		}
		
		UserGistsClient userGistsClient = new UserGistsClient(getActivity(), page);
		userGistsClient.setOnResultCallback(this);
		userGistsClient.execute();
	}

	@Override
	protected void onResponse(ListGists gists, boolean refreshing) {
		if (gists != null && gists.size() > 0) {

			if (gistsAdapter == null || refreshing) {
				gistsAdapter = new GistsAdapter(getActivity(), gists);
				setListAdapter(gistsAdapter);
			}

			if (gistsAdapter != null) {
				if (gistsAdapter.isLazyLoading()) {
					gistsAdapter.setLazyLoading(false);
					gistsAdapter.addAll(gists);
				}
			}
		}
	}

	@Override
	protected void loadArguments() {

	}

	@Override
	protected GithubIconify.IconValue getNoDataIcon() {
		return GithubIconify.IconValue.octicon_gist;
	}

	@Override
	protected int getNoDataText() {
		return R.string.no_gists;
	}

	@Override
	protected boolean useFAB() {
		return true;
	}

	@Override
	protected GithubIconify.IconValue getFABGithubIcon() {
		return GithubIconify.IconValue.octicon_plus;
	}
}

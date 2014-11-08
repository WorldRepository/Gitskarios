package com.alorma.github.ui.fragment.detail.repo;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.request.CreateRepoRequestDTO;
import com.alorma.github.sdk.bean.dto.response.GitIgnoreTemplates;
import com.alorma.github.sdk.services.client.BaseClient;
import com.alorma.github.sdk.services.gtignore.GitIgnoreClient;
import com.alorma.github.ui.popup.GitIgnorePopup;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bernat on 28/09/2014.
 */
public class RepoCreateFragment extends Fragment implements View.OnClickListener, GitIgnorePopup.OnGitIgnoresListener {


	private CheckedTextView repoPrivateCheck;
	private CheckedTextView repoIssuesCheck;
	private CheckedTextView repoWikiCheck;
	private CheckedTextView repoDownloadsCheck;
	private EditText editTitle;
	private EditText editBody;
	private Button buttonAddGitignore;
	private Button buttonAddLicense;
	private String definedIgnore;
	private ArrayAdapter<String> ignoresAdapter;
	private GitIgnorePopup gitIgnorePopup;
	private String definedLicense;

	public static RepoCreateFragment newInstance() {
		return new RepoCreateFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.repo_create_fragment, null, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findViews(view);

	}

	private void findViews(View view) {
		editTitle = (EditText) view.findViewById(R.id.editTitle);
		editBody = (EditText) view.findViewById(R.id.editBody);

		buttonAddGitignore = (Button) view.findViewById(R.id.add_gitignore);
		buttonAddLicense = (Button) view.findViewById(R.id.add_license);

		buttonAddGitignore.setOnClickListener(this);
		buttonAddLicense.setOnClickListener(this);

		findChecks(view);
	}

	private void findChecks(View view) {
		repoPrivateCheck = (CheckedTextView) view.findViewById(R.id.repoPrivateCheck);
		repoIssuesCheck = (CheckedTextView) view.findViewById(R.id.repoIssuesCheck);
		repoWikiCheck = (CheckedTextView) view.findViewById(R.id.repoWikiCheck);
		repoDownloadsCheck = (CheckedTextView) view.findViewById(R.id.repoDownloadsCheck);

		repoPrivateCheck.setCheckMarkDrawable(R.drawable.btn_check_material);
		repoIssuesCheck.setCheckMarkDrawable(R.drawable.btn_check_material);
		repoWikiCheck.setCheckMarkDrawable(R.drawable.btn_check_material);
		repoDownloadsCheck.setCheckMarkDrawable(R.drawable.btn_check_material);

		repoPrivateCheck.setOnClickListener(this);
		repoIssuesCheck.setOnClickListener(this);
		repoWikiCheck.setOnClickListener(this);
		repoDownloadsCheck.setOnClickListener(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.new_issue_comment, menu);

		MenuItem itemSend = menu.findItem(R.id.action_send);
		if (itemSend != null) {
			IconDrawable iconDrawable = new IconDrawable(getActivity(), Iconify.IconValue.fa_send);
			iconDrawable.color(Color.WHITE);
			iconDrawable.actionBarSize();
			itemSend.setIcon(iconDrawable);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		if (item.getItemId() == R.id.action_send) {
			createGithubRepo();
		}

		return true;
	}

	private void createGithubRepo() {
		String title = editTitle.getText().toString();
		String description = editBody.getText().toString();

		boolean isPrivate = repoPrivateCheck.isChecked();
		boolean hasIssues = repoIssuesCheck.isChecked();
		boolean hasWiki = repoWikiCheck.isChecked();
		boolean hasDownloads = repoDownloadsCheck.isChecked();

		CreateRepoRequestDTO repoRequestDTO = new CreateRepoRequestDTO();
		repoRequestDTO.name = title;
		repoRequestDTO.description = description;
		repoRequestDTO.isPrivate = isPrivate;
		repoRequestDTO.gitignore_template = definedIgnore;
		repoRequestDTO.has_issues = hasIssues;
		repoRequestDTO.has_wiki = hasWiki;
		repoRequestDTO.has_downloads = hasDownloads;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.repoPrivateCheck:
				changeCheckedState(v);
				break;
			case R.id.repoIssuesCheck:
				changeCheckedState(v);
				break;
			case R.id.repoWikiCheck:
				changeCheckedState(v);
				break;
			case R.id.repoDownloadsCheck:
				changeCheckedState(v);
				break;
			case R.id.add_gitignore:
				loadIgnores(v);
				break;
			case R.id.add_license:
				loadLicenses(v);
				break;
		}
	}

	private void loadIgnores(View v) {

		buttonAddGitignore.setEnabled(false);

		if (gitIgnorePopup == null) {
			gitIgnorePopup = new GitIgnorePopup(getActivity());
			gitIgnorePopup.setAnchorView(v);
			gitIgnorePopup.setOnGitIgnoresListener(this);
		}
		gitIgnorePopup.show();
	}

	private void loadLicenses(View v) {

	}

	private void changeCheckedState(View v) {
		CheckedTextView ctv = (CheckedTextView) v;
		changeCheckedState(ctv, !ctv.isChecked());
	}

	private void changeCheckedState(CheckedTextView ctv, boolean checked) {
		ctv.setChecked(checked);
	}

	@Override
	public void onGitIgnoreClear() {
		definedIgnore = null;
		buttonAddGitignore.setText(R.string.add_gitignore);
		buttonAddGitignore.setEnabled(true);
	}

	@Override
	public void onGitIgnoreSelected(String s) {
		definedIgnore = s;
		buttonAddGitignore.setText(s);
		buttonAddGitignore.setEnabled(true);
	}

	@Override
	public void onGitIgnoreFailed(RetrofitError error) {
		buttonAddGitignore.setEnabled(true);
	}

	@Override
	public void onGitIgnoreDismissed() {
		buttonAddGitignore.setEnabled(true);
		if (definedIgnore == null) {
			buttonAddGitignore.setText(R.string.add_gitignore);
		}
	}
}

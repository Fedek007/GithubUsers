package com.example.githubusers.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.githubusers.App;
import com.example.githubusers.R;
import com.example.githubusers.users.github.GithubUsersUserModel;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UsersFragment extends Fragment implements UserPresenter.View {
  private static UserPresenter presenter;

  @BindView(R.id.recycler)
  RecyclerView recyclerView;

  @BindView(R.id.progress)
  ProgressBar progressBar;

  private Unbinder unbinder;
  private UserRecycleAdapter adapter;

  public static UsersFragment newInstance() {
    Bundle args = new Bundle();
    UsersFragment fragment = new UsersFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_git_hub_users, container, false);
    unbinder = ButterKnife.bind(this, view);
    adapter = new UserRecycleAdapter();
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    UserModel userModel = new GithubUsersUserModel(((App) getActivity().getApplication()).getGitHubService());
    if (presenter == null) {
      presenter = new UserPresenter(userModel);
    }
    presenter.attachView(this);

    return view;
  }

  @Override
  public void onUserListLoaded(List<User> userList) {
    adapter.addUsers(userList);
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void onError(String errorMessage) {
    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    presenter.detachView();
    if (getActivity().isFinishing()) {
      presenter.stopLoading();
      presenter = null;
    }
    unbinder.unbind();
  }
}

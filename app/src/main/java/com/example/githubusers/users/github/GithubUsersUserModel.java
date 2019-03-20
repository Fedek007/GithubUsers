package com.example.githubusers.users.github;

import com.example.githubusers.users.User;
import com.example.githubusers.users.UserModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class GithubUsersUserModel implements UserModel {

  private GitHubService gitHubService;

  public GithubUsersUserModel(GitHubService gitHubService) {
    this.gitHubService = gitHubService;
  }

  @Override
  public Single<List<User>> getUsers() {
    return gitHubService.getUsers(0)
        .flatMapObservable(Observable::fromIterable)
        .map(GitHubUser::mapToUser)
        .toList();
  }


}

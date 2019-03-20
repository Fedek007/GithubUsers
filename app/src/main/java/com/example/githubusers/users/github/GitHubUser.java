package com.example.githubusers.users.github;

import com.example.githubusers.users.User;
import com.google.gson.annotations.SerializedName;


public class GitHubUser {

  @SerializedName("login") String name;
  @SerializedName("avatar_url") String avatar;
  @SerializedName("id") long userId;

  public User mapToUser() {
    return new User(name, avatar, userId);
  }
}

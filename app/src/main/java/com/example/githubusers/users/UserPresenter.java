package com.example.githubusers.users;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserPresenter {

  public interface View {

    void onUserListLoaded(List<User> userList);

    void onError(String errorMessage);
  }

  private UserModel userModel;
  private View view;
  private Disposable disposable;
  private List<User> userList;

  public UserPresenter(UserModel userModel) {
    this.userModel = userModel;
  }

  public void stopLoading() {
    if (disposable != null) {
      disposable.dispose();
    }
  }

  public void attachView(View view) {
    this.view = view;
    if (userList != null) {
      view.onUserListLoaded(userList);
    } else {
      loadUsers();
    }
  }

  public void detachView() {
    this.view = null;
  }

  private void loadUsers() {
    disposable = userModel.getUsers()
        .delay(5, TimeUnit.SECONDS)
        .doOnSuccess(list -> userList = list)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .filter(users -> view != null)
        .subscribe(users -> view.onUserListLoaded(users), error -> view.onError(error.getMessage()));
  }
}

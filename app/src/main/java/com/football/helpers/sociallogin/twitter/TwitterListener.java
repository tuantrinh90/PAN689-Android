package com.football.helpers.sociallogin.twitter;

public interface TwitterListener {
  void onTwitterError(String errorMessage);

  void onTwitterSignIn(String authToken, String secret, long userId);
}

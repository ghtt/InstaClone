package com.akrasnoyarov.instaclone;

public interface AuthListener {
    void onOAuthTokenReceived(String oAuthToken);
    void onUserTokenReceived(String token);
    void onUsernameReceived(String username);
    void onReauthRequired();
}

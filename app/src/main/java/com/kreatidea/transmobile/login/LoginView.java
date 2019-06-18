package com.kreatidea.transmobile.login;

import com.kreatidea.transmobile.model.User;

public interface LoginView {
    void showLoading();
    void hideLoading();
    void showError(String msg);
    void showHome();
    void savePreferences(User user);
}

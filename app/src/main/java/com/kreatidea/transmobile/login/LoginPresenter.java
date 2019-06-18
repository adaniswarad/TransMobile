package com.kreatidea.transmobile.login;

import com.kreatidea.transmobile.model.Respons;
import com.kreatidea.transmobile.model.User;
import com.kreatidea.transmobile.api.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {

    private LoginView view;
    private User user;

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    public void login(String email, String password) {
        view.showLoading();
        if (email.isEmpty() || password.isEmpty()) {
            view.hideLoading();
            view.showError("Mohon isi form login dengan benar");
        } else {
            Call<Respons> call = Client.getInstanceRetrofit().login(email, password);
            call.enqueue(new Callback<Respons>() {
                @Override
                public void onResponse(Call<Respons> call, Response<Respons> response) {
                    if (response.body().getUsers().isEmpty()) {
                        view.hideLoading();
                        view.showError("Email atau password anda salah");
                    } else {
                        user = response.body().getUsers().get(0);
                        view.savePreferences(user);
                        view.showHome();
                        view.hideLoading();
                    }
                }

                @Override
                public void onFailure(Call<Respons> call, Throwable t) {
                    view.hideLoading();
                    view.showError(t.getLocalizedMessage());
                }
            });
        }
    }
}

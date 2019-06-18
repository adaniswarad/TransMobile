package com.kreatidea.transmobile.home;

import com.kreatidea.transmobile.api.Client;
import com.kreatidea.transmobile.model.Respons;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter {

    private HomeView view;

    public HomePresenter(HomeView view) {
        this.view = view;
    }

    public void getPemakaianList(String userId) {
        view.showLoading();
        Call<Respons> call = Client.getInstanceRetrofit().getPemakaianList(userId);
        call.enqueue(new Callback<Respons>() {
            @Override
            public void onResponse(Call<Respons> call, Response<Respons> response) {
                if (response.body().getPemakaian().isEmpty()) {
                    view.showEmpty();
                } else {
                    view.hideEmpty();
                    Respons data = response.body();
                    view.showPemakaianList(data.getPemakaian());
                }
                view.hideLoading();
            }

            @Override
            public void onFailure(Call<Respons> call, Throwable t) {
                view.hideLoading();
            }
        });
    }
}

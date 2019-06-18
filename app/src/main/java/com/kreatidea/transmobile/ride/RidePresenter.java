package com.kreatidea.transmobile.ride;

import com.kreatidea.transmobile.api.Client;
import com.kreatidea.transmobile.model.Respons;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dennis on 20-Jan-19.
 */
class RidePresenter {

    private RideView view;

    public RidePresenter(RideView view) {
        this.view = view;
    }

    public void getPemakaianSekarang(String userId) {
        view.showLoading();
        Call<Respons> call = Client.getInstanceRetrofit().getPemakaianSekarang(userId);
        call.enqueue(new Callback<Respons>() {
            @Override
            public void onResponse(Call<Respons> call, Response<Respons> response) {
                if (response.body().getPemakaian().isEmpty()) {
                    view.hidePemakaianView();
                    view.showEmpty();
                } else {
                    view.hideEmpty();
                    Respons data = response.body();
                    view.showPemakaianSekarang(data.getPemakaian());
                    view.showPemakaianView();
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

package com.kreatidea.transmobile.ride;

import com.kreatidea.transmobile.model.Pemakaian;

import java.util.List;

/**
 * Created by Dennis on 20-Jan-19.
 */
public interface RideView {
    void showLoading();
    void hideLoading();
    void showEmpty();
    void hideEmpty();
    void showPemakaianView();
    void hidePemakaianView();
    void showPemakaianSekarang(List<Pemakaian> data);
}

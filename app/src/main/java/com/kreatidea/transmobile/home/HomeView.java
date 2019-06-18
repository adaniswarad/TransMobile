package com.kreatidea.transmobile.home;

import com.kreatidea.transmobile.model.Pemakaian;

import java.util.List;

public interface HomeView {
    void showLoading();
    void hideLoading();
    void showEmpty();
    void hideEmpty();
    void showMessage(String msg);
    void showPemakaianList(List<Pemakaian> data);
}

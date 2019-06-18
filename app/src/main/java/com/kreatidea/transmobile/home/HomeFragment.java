package com.kreatidea.transmobile.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.activity.AddPermohonanActivity;
import com.kreatidea.transmobile.activity.DetailPemakaianActivity;
import com.kreatidea.transmobile.model.Pemakaian;
import com.kreatidea.transmobile.util.Constants;
import com.kreatidea.transmobile.util.FragmentListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements HomeView {

    private static final String TAG = "HomeFragment";

    private HomePresenter presenter;
    private HomeAdapter adapter;
    private FragmentListener listener;

    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.empty_view) View emptyView;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listener.setToolbarTitle("Home");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        presenter = new HomePresenter(this);
        swipeRefresh.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light
        );
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getPemakaianList(getUserIdFromPref());
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddPermohonanActivity.class));
            }
        });
        presenter.getPemakaianList(getUserIdFromPref());
    }

    private String getUserIdFromPref() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showEmpty() {
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        emptyView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPemakaianList(List<Pemakaian> data) {
        adapter = new HomeAdapter(data, new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pemakaian item) {
                Intent intent = new Intent(getContext(), DetailPemakaianActivity.class);
                intent.putExtra("pemakaian_id", item.getPemakaianId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            listener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

package com.kreatidea.transmobile.ride;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.activity.EnterCodeActivity;
import com.kreatidea.transmobile.activity.RidingActivity;
import com.kreatidea.transmobile.activity.UploadKmAwalActivity;
import com.kreatidea.transmobile.api.Client;
import com.kreatidea.transmobile.berkendara.BerkendaraActivity;
import com.kreatidea.transmobile.model.Pemakaian;
import com.kreatidea.transmobile.model.Respons;
import com.kreatidea.transmobile.util.Constants;
import com.kreatidea.transmobile.util.FragmentListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Dennis on 19-Jan-19.
 */
public class RideFragment extends Fragment implements RideView {

    private RidePresenter presenter;
    private FragmentListener listener;
    private ProgressDialog progressDialog;
    private List<Pemakaian> pemakaian;
    private boolean activationStatus;

    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.pemakaian_sekarang) View pemakaianSekarangView;
    @BindView(R.id.tujuan_text_view) TextView tujuanTextView;
    @BindView(R.id.nopol_text_view) TextView nopolTextView;
    @BindView(R.id.tgl_pemakaian_text_view) TextView tglPemakaianTextView;
    @BindView(R.id.status_pemakaian_text_view) TextView statusPemakaianTextView;
    @BindView(R.id.empty_view) View emptyView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listener.setToolbarTitle("Ride");
        return inflater.inflate(R.layout.fragment_ride, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        presenter = new RidePresenter(this);
        swipeRefresh.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light
        );
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getPemakaianSekarang(getUserIdFromPref());
            }
        });
        pemakaianSekarangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAlreadyUploadKmAwal();
            }
        });
        presenter.getPemakaianSekarang(getUserIdFromPref());
    }

    private void isAlreadyUploadKmAwal() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<Respons> call = Client.getInstanceRetrofit().isAlreadyUploadKmAwal(pemakaian.get(0).getPemakaianId());
        call.enqueue(new Callback<Respons>() {
            @Override
            public void onResponse(Call<Respons> call, Response<Respons> response) {
                if (response.body().getRespons().equals("true")) {
                    Intent intent = new Intent(getContext(), RidingActivity.class);
                    intent.putExtra("pemakaian_id", pemakaian.get(0).getPemakaianId());
                    startActivity(intent);
                } else {
                    if (isActivated()) {
                        Intent intent = new Intent(getContext(), UploadKmAwalActivity.class);
                        intent.putExtra("pemakaian_id", pemakaian.get(0).getPemakaianId());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), EnterCodeActivity.class);
                        intent.putExtra("pemakaian_id", pemakaian.get(0).getPemakaianId());
                        startActivity(intent);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Respons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isActivated() {
        getUserPreferences();
        return activationStatus;
    }

    private void getUserPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        activationStatus = preferences.getBoolean("activation_status", false);
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
    public void showPemakaianView() {
        pemakaianSekarangView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePemakaianView() {
        pemakaianSekarangView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showPemakaianSekarang(List<Pemakaian> data) {
        pemakaian = data;
        tujuanTextView.setText(data.get(0).getTujuan());
        nopolTextView.setText(data.get(0).getNopol());
        tglPemakaianTextView.setText(data.get(0).getTglPemakaian());
        statusPemakaianTextView.setText(data.get(0).getStatusPemakaian());
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

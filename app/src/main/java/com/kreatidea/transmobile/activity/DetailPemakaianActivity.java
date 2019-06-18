package com.kreatidea.transmobile.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.model.Pemakaian;
import com.kreatidea.transmobile.model.Respons;
import com.kreatidea.transmobile.api.Client;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemakaianActivity extends AppCompatActivity {

    private static final String TAG = "DetailPemakaianActivity";

    private String pemakaianId;
    private ProgressDialog progressDialog;

    @BindView(R.id.tujuan_text_view) TextView tujuanTextView;
    @BindView(R.id.status_pemakaian_text_view) TextView statusPemakaianTextView;
    @BindView(R.id.keperluan_text_view) TextView keperluanTextView;
    @BindView(R.id.jum_penumpang_text_view) TextView jumPenumpangTextView;
    @BindView(R.id.tgl_pemakaian_text_view) TextView tglPemakaianTextView;
    @BindView(R.id.nopol_text_view) TextView nopolTextView;
    @BindView(R.id.km_awal_text_view) TextView kmAwalTextView;
    @BindView(R.id.km_akhir_text_view) TextView kmAkhirTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemakaian);
        ButterKnife.bind(this);
        setTitle("Detail Pemakaian");
        getIncomingIntent();
        getPemakaianDetail(pemakaianId);
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("pemakaian_id")) {
            pemakaianId = getIntent().getStringExtra("pemakaian_id");
        }
    }

    private void getPemakaianDetail(String pemakaianId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<Respons> call = Client.getInstanceRetrofit().getPemakaianDetail(pemakaianId);
        call.enqueue(new Callback<Respons>() {
            @Override
            public void onResponse(Call<Respons> call, Response<Respons> response) {
                if (response.body().getRespons().equals("sukses")) {
                    Pemakaian pemakaianDetail = response.body().getPemakaianDetail();
                    setPemakaianData(pemakaianDetail);
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(DetailPemakaianActivity.this, "Something wrong, dude!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailPemakaianActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPemakaianData(Pemakaian pemakaianDetail) {
        tujuanTextView.setText(pemakaianDetail.getTujuan());
        statusPemakaianTextView.setText(pemakaianDetail.getStatusPemakaian());
        keperluanTextView.setText(pemakaianDetail.getKeperluan());
        jumPenumpangTextView.setText(pemakaianDetail.getJumPenumpang());
        tglPemakaianTextView.setText(pemakaianDetail.getTglPemakaian());
        nopolTextView.setText(pemakaianDetail.getNopol());
        kmAwalTextView.setText(pemakaianDetail.getKmAwal());
        kmAkhirTextView.setText(pemakaianDetail.getKmAkhir());
    }
}

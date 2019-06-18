package com.kreatidea.transmobile.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.kreatidea.transmobile.home.HomeActivity;
import com.kreatidea.transmobile.util.Constants;
import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.fragment.DatePickerFragment;
import com.kreatidea.transmobile.model.Respons;
import com.kreatidea.transmobile.api.Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPermohonanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "AddPermohonanActivity";

    private ProgressDialog progressDialog;
    private String tglDbFormat;

    @BindView(R.id.tujuan_edit_text) EditText tujuanEditText;
    @BindView(R.id.keperluan_edit_text) EditText keperluanEditText;
    @BindView(R.id.jum_penumpang_edit_text) EditText jumPenumpangEditText;
    @BindView(R.id.lama_pemakaian_edit_text) EditText lamaPemakaianEditText;
    @BindView(R.id.dasar_pemakaian_edit_text) EditText dasarPemakaianEditText;
    @BindView(R.id.open_date_picker_button) Button openDatePickerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_permohonan);
        ButterKnife.bind(this);
        setTitle("Buat Permohonan");
        setTglHariIniDiTglPemakaian();
        openDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tglDbFormat = dateFormat.format(calendar.getTime());
        openDatePickerButton.setText(currentDate);
    }

    private void setTglHariIniDiTglPemakaian() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tglDbFormat = dateFormat.format(calendar.getTime());
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        openDatePickerButton.setText(currentDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_permohonan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                kirimPermohonan();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void kirimPermohonan() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String tujuan = tujuanEditText.getText().toString().trim();
        String keperluan = keperluanEditText.getText().toString().trim();
        String jumPenumpang = jumPenumpangEditText.getText().toString().trim();
        String lamaPemakaian = lamaPemakaianEditText.getText().toString().trim();
        String dasarPemakaian = dasarPemakaianEditText.getText().toString().trim();

        if (TextUtils.isEmpty(tujuan) || TextUtils.isEmpty(keperluan) ||
                TextUtils.isEmpty(jumPenumpang)) {
            progressDialog.dismiss();
            showUncompletedFormDialog();
        } else {
            String userId = getUserPreferences();
            Call<Respons> call = Client.getInstanceRetrofit()
                    .addPermohonan(userId, tujuan, keperluan, jumPenumpang, tglDbFormat, lamaPemakaian, dasarPemakaian);
            call.enqueue(new Callback<Respons>() {
                @Override
                public void onResponse(Call<Respons> call, Response<Respons> response) {
                    if (response.body().getRespons().equals("sukses")) {
                        startActivity(new Intent(AddPermohonanActivity.this, HomeActivity.class));
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(AddPermohonanActivity.this, "Permohonan berhasil dikirim", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AddPermohonanActivity.this, "Permohonan gagal dikirim", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Respons> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(AddPermohonanActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private String getUserPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    private void showUncompletedFormDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Mohon isi semua form");
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

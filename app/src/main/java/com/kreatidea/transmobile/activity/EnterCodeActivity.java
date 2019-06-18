package com.kreatidea.transmobile.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kreatidea.transmobile.util.Constants;
import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.model.Respons;
import com.kreatidea.transmobile.api.Client;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterCodeActivity extends AppCompatActivity {

    private static final String TAG = "EnterCodeActivity";

    private String pemakaianId;
    private ProgressDialog progressDialog;

    @BindView(R.id.first_digit_code_edit_text) EditText firstDigitCodeEditText;
    @BindView(R.id.second_digit_code_edit_text) EditText secondDigitCodeEditText;
    @BindView(R.id.third_digit_code_edit_text) EditText thirdDigitCodeEditText;
    @BindView(R.id.fourth_digit_code_edit_text) EditText fourthDigitCodeEditText;
    @BindView(R.id.verify_code_button) Button verifyCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);
        setTitle("Code Verification");
        ButterKnife.bind(this);
        getIncomingIntent();
        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode();
            }
        });
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("pemakaian_id")) {
            pemakaianId = getIntent().getStringExtra("pemakaian_id");
        }
    }

    private void verifyCode() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String firstDigit = firstDigitCodeEditText.getText().toString().trim();
        String secondDigit = secondDigitCodeEditText.getText().toString().trim();
        String thirdDigit = thirdDigitCodeEditText.getText().toString().trim();
        String fourthDigit = fourthDigitCodeEditText.getText().toString().trim();
        String kodePemakaian = firstDigit + secondDigit + thirdDigit + fourthDigit;

        Call<Respons> call = Client.getInstanceRetrofit().verifikasiKodePemakaian(pemakaianId, kodePemakaian);
        call.enqueue(new Callback<Respons>() {
            @Override
            public void onResponse(Call<Respons> call, Response<Respons> response) {
                if (response.body().getRespons().equals("sukses")) {
                    Intent intent = new Intent(EnterCodeActivity.this, UploadKmAwalActivity.class);
                    intent.putExtra("pemakaian_id", pemakaianId);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                } else {
                    firstDigitCodeEditText.setText("");
                    secondDigitCodeEditText.setText("");
                    thirdDigitCodeEditText.setText("");
                    fourthDigitCodeEditText.setText("");
                    progressDialog.dismiss();
                    displayAlert();
                }
            }

            @Override
            public void onFailure(Call<Respons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EnterCodeActivity.this, "Something went wrong, dude!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Kode yang anda masukkan salah!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

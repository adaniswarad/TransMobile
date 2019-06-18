package com.kreatidea.transmobile.enterCode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.activity.EnterCodeActivity;
import com.kreatidea.transmobile.activity.RidingActivity;
import com.kreatidea.transmobile.activity.UploadKmAwalActivity;
import com.kreatidea.transmobile.api.Client;
import com.kreatidea.transmobile.model.Respons;
import com.kreatidea.transmobile.model.User;
import com.kreatidea.transmobile.upload.UploadFragment;
import com.kreatidea.transmobile.util.Constants;
import com.kreatidea.transmobile.util.FragmentListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Dennis on 20-Jan-19.
 */
public class EnterCodeFragment extends Fragment {

    private FragmentListener listener;
    private ProgressDialog progressDialog;
    private String pemakaianId;

    @BindView(R.id.first_digit_code_edit_text) EditText firstDigitCodeEditText;
    @BindView(R.id.second_digit_code_edit_text) EditText secondDigitCodeEditText;
    @BindView(R.id.third_digit_code_edit_text) EditText thirdDigitCodeEditText;
    @BindView(R.id.fourth_digit_code_edit_text) EditText fourthDigitCodeEditText;
    @BindView(R.id.verify_code_button) Button verifyCodeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listener.setToolbarTitle("Code Verification");
        getIncomingIntent();
        return inflater.inflate(R.layout.fragment_enter_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode();
            }
        });
    }

    private void getIncomingIntent() {
        if (getActivity().getIntent().hasExtra("pemakaian_id")) {
            pemakaianId = getActivity().getIntent().getStringExtra("pemakaian_id");
        }
    }

    private void verifyCode() {
        progressDialog = new ProgressDialog(getContext());
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
                    savePreferences();
                    isAlreadyUploadKmAwal();
                } else {
                    firstDigitCodeEditText.setText("");
                    secondDigitCodeEditText.setText("");
                    thirdDigitCodeEditText.setText("");
                    fourthDigitCodeEditText.setText("");
                    displayAlert();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Respons> call, Throwable t) {
                Log.d("EnterCodeFragment", "Error: " + t.getLocalizedMessage());
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void isAlreadyUploadKmAwal() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<Respons> call = Client.getInstanceRetrofit().isAlreadyUploadKmAwal(pemakaianId);
        call.enqueue(new Callback<Respons>() {
            @Override
            public void onResponse(Call<Respons> call, Response<Respons> response) {
                if (response.body().getRespons().equals("uploaded")) {
                    startActivity(new Intent(getContext(), RidingActivity.class));
                } else {
                    Intent intent = new Intent(getContext(), UploadKmAwalActivity.class);
                    intent.putExtra("pemakaian_id", pemakaianId);
                    startActivity(intent);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Respons> call, Throwable t) {
                Log.d("EnterCodeFragment", "Error: " + t.getLocalizedMessage());
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void savePreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("activation_status", true);
        editor.apply();
    }

    private void displayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

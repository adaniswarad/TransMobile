package com.kreatidea.transmobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.model.Respons;
import com.kreatidea.transmobile.api.Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadKmAwalActivity extends AppCompatActivity {

    private static final String TAG = "UploadKmAwalActivity";
    private static final int IMG_REQUEST = 200;

    private String pemakaianId;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;

    @BindView(R.id.preview_image_view) ImageView previewImageView;
    @BindView(R.id.km_awal_edit_text) EditText kmAwalEditText;
    @BindView(R.id.choose_image_button) Button chooseImageButton;
    @BindView(R.id.upload_image_button) Button uploadImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_km_awal);
        setTitle("Upload KM Awal");
        ButterKnife.bind(this);
        getIncomingIntent();
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageKmAwal();
            }
        });
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageKmAwal();
            }
        });
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("pemakaian_id")) {
            pemakaianId = getIntent().getStringExtra("pemakaian_id");
        }
    }

    private void uploadImageKmAwal() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String image = imageToString();
        String kmAwal = kmAwalEditText.getText().toString().trim();
        Call<Respons> call = Client.getInstanceRetrofit().uploadKmAwal(pemakaianId, image, kmAwal);
        call.enqueue(new Callback<Respons>() {
            @Override
            public void onResponse(Call<Respons> call, Response<Respons> response) {
                if (response.body().getRespons().equals("sukses")) {
                    previewImageView.setImageResource(R.drawable.no_image);
                    kmAwalEditText.setText("");
                    kmAwalEditText.setVisibility(View.GONE);
                    chooseImageButton.setEnabled(true);
                    uploadImageButton.setEnabled(false);
                    Intent intent = new Intent(UploadKmAwalActivity.this, RidingActivity.class);
                    intent.putExtra("pemakaian_id", pemakaianId);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UploadKmAwalActivity.this, "Gagal. Cek koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UploadKmAwalActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void selectImageKmAwal() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    private String imageToString() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageByte = stream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                previewImageView.setImageBitmap(bitmap);
                kmAwalEditText.setVisibility(View.VISIBLE);
                chooseImageButton.setEnabled(false);
                uploadImageButton.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

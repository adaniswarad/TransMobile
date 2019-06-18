package com.kreatidea.transmobile.upload;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.activity.RidingActivity;
import com.kreatidea.transmobile.api.Client;
import com.kreatidea.transmobile.model.Respons;
import com.kreatidea.transmobile.util.FragmentListener;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dennis on 20-Jan-19.
 */
public class UploadFragment extends Fragment {

    private static final String TAG = "UploadFragment";
    private static final int IMG_REQUEST = 200;

    private FragmentListener listener;
    private String pemakaianId;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;

    @BindView(R.id.preview_image_view) ImageView previewImageView;
    @BindView(R.id.km_awal_edit_text) EditText kmAwalEditText;
    @BindView(R.id.choose_image_button) Button chooseImageButton;
    @BindView(R.id.upload_image_button) Button uploadImageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener.setToolbarTitle("Upload KM");
        getIncomingIntent();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
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
        if (getActivity().getIntent().hasExtra("pemakaian_id")) {
            pemakaianId = getActivity().getIntent().getStringExtra("pemakaian_id");
        }
    }

    private void uploadImageKmAwal() {
        progressDialog = new ProgressDialog(getContext());
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

                    progressDialog.dismiss();

                    Intent ridingIntent = new Intent(getContext(), RidingActivity.class);
                    ridingIntent.putExtra("pemakaian_id", pemakaianId);
                    startActivity(ridingIntent);
                    getActivity().finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Gagal. Cek koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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

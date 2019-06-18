package com.kreatidea.transmobile.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.model.Respons;
import com.kreatidea.transmobile.api.Client;
import com.kreatidea.transmobile.service.GpsService;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class RidingActivity extends AppCompatActivity {

    private static final String TAG = "RidingActivity";

    private BroadcastReceiver broadcastReceiver;
    private String pemakaianId;

    @BindView(R.id.catatan_edit_text) EditText catatanEditText;
    @BindView(R.id.stop_button) Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: executed");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);
        ButterKnife.bind(this);
        getIncomingIntent();
        if (!runtime_permissions()) {
            Intent i = new Intent(getApplicationContext(), GpsService.class);
            startService(i);
        }

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GpsService.class);
                stopService(i);

                String catatan = catatanEditText.getText().toString().trim();
                Intent uploadKmAkhirIntent = new Intent(RidingActivity.this, UploadKmAkhirActivity.class);
                uploadKmAkhirIntent.putExtra("pemakaian_id", pemakaianId);
                uploadKmAkhirIntent.putExtra("catatan", catatan);
                startActivity(uploadKmAkhirIntent);
                finish();
            }
        });
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("pemakaian_id")) {
            pemakaianId = getIntent().getStringExtra("pemakaian_id");
            Log.d(TAG, "pemakaianId: "+pemakaianId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String lat = String.valueOf(intent.getExtras().get("lat"));
                    String lng = String.valueOf(intent.getExtras().get("lng"));
                    sendCoordinate(lat, lng);
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    private void sendCoordinate(final String lat, final String lng) {
        Log.d(TAG, "pemakaianId: " + pemakaianId + " | " + lat + ", " + lng);
        Call<Respons> call = Client.getInstanceRetrofit().setCoordinate(pemakaianId, lat, lng);
        call.enqueue(new Callback<Respons>() {
            @Override
            public void onResponse(Call<Respons> call, retrofit2.Response<Respons> response) {
                if (response.body().getRespons().equals("sukses")) {
                    Log.d(TAG, "sukses input kordinat");
                } else {
                    Log.d(TAG, "gagal input koordniat");
                }
            }

            @Override
            public void onFailure(Call<Respons> call, Throwable t) {
                Toast.makeText(RidingActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* Permission */
    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);

            return true;
        }
        return false;
    }

    /* Request Permission */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(getApplicationContext(), GpsService.class);
                startService(i);
            } else {
                runtime_permissions();
            }
        }
    }
}

package com.kreatidea.transmobile.berkendara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.enterCode.EnterCodeFragment;
import com.kreatidea.transmobile.util.FragmentListener;

public class BerkendaraActivity extends AppCompatActivity implements FragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkendara);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new EnterCodeFragment())
                .commit();
    }

    @Override
    public void setToolbarTitle(String title) {
        setTitle(title);
    }
}

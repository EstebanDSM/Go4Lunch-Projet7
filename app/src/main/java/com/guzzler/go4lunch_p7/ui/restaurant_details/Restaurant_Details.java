package com.guzzler.go4lunch_p7.ui.restaurant_details;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.guzzler.go4lunch_p7.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Restaurant_Details extends AppCompatActivity {

    @BindView(R.id.textView1)
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant__details);
        ButterKnife.bind(this);

        String s = getIntent().getStringExtra("PlaceDetailResult");
        Log.e("TAG", "Intent reçu : " + s);

        textView.setText(s);
    }
}
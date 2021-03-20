package com.guzzler.go4lunch_p7.ui.restaurant_details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.guzzler.go4lunch_p7.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WebView_Activity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.webView_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.webView)
    WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        this.displayWebView();
        this.configureSwipeRefreshLayout();
    }

    private void configureSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this::displayWebView);
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void displayWebView() {
        String url = getIntent().getStringExtra("Website");
        if (url != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.loadUrl(url);
            mWebView.setWebViewClient(new WebViewClient());
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}

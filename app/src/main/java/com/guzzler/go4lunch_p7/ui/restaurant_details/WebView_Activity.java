package com.guzzler.go4lunch_p7.ui.restaurant_details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.guzzler.go4lunch_p7.databinding.ActivityWebViewBinding;


public class WebView_Activity extends AppCompatActivity {

    private ActivityWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.displayWebView();
        this.configureSwipeRefreshLayout();
    }

    private void configureSwipeRefreshLayout() {
        binding.webViewSwipeRefresh.setOnRefreshListener(this::displayWebView);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void displayWebView() {
        String url = getIntent().getStringExtra("Website");
        if (url != null) {
            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.getSettings().setLoadsImagesAutomatically(true);
            binding.webView.loadUrl(url);
            binding.webView.setWebViewClient(new WebViewClient());
        }
        binding.webViewSwipeRefresh.setRefreshing(false);
    }
}

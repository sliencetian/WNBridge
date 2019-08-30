package com.silence.demo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DemoWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/test_js_bridge.html");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

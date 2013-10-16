package com.androidbook.simpleweb;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class LocalWebViewActivity extends Activity {
	   private static final String DEBUG_TAG = "LocalWebViewActivity";

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.justawebview);

	        final WebView wv = (WebView) findViewById(R.id.web_content);
	        wv.loadUrl("file:///android_asset/webby.html");
	    }
	}
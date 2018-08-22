package cn.qingyuyu.yulauncher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView=findViewById(R.id.webView);

        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        };
        webView.setWebViewClient(wvc);
        webView.getSettings().setJavaScriptEnabled(true);

        Intent i=getIntent();

        if(i.getStringExtra("url")!=null)
        {
            Log.e("load url","xx"+i.getStringExtra("url"));
            webView.loadUrl(i.getStringExtra("url"));
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) ) {
            if(webView.canGoBack())
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

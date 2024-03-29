package demo.com.checkout.checkoutdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebViewActivity extends AppCompatActivity {

    String data;
    WebAppInterface webAppInterface;
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getIntent().getStringExtra("value1");
        setContentView(R.layout.activity_web_view);
        WebView wv1 = (WebView) findViewById(R.id.webView);
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv1.getSettings().setSupportZoom(false);
        wv1.getSettings().setJavaScriptEnabled(true);
        webAppInterface = new WebAppInterface(this);
        wv1.addJavascriptInterface(new WebAppInterface(this), "Android");
        wv1.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String data) {
                Map<String, String> extraHeaders = new HashMap<String, String>();
                extraHeaders.put("Referer", view.getUrl());
                view.loadUrl(data,extraHeaders);
                return false;
            }
            @Override
            public void onPageFinished(WebView view, final String data) {
            }
        });
        wv1.loadUrl(data);
    }


    public class WebAppInterface {
        Context mContext;
        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            data = toast;
            JSONObject myObject = null;
            try {
                myObject = new JSONObject(data);
                String code = myObject.getString("code");
                if(code.equals("400")){
                    Intent myIntent = new Intent(getBaseContext(), Error.class);
                    startActivity(myIntent);
                }else if(code.equals("499")){
                    Intent myIntent = new Intent(getBaseContext(), CancelActivity.class);
                    startActivity(myIntent);
                }else if(code.equals("SUCCESS")) {
                    Intent myIntent = new Intent(getBaseContext(), SuccessActivity.class);
                    myIntent.putExtra("value1",data);
                    startActivity(myIntent);
                }else if(code.equals("PENDING")) {
                    Intent myIntent = new Intent(getBaseContext(), InvoiceActivity.class);
                    myIntent.putExtra("value1",data);
                    startActivity(myIntent);
                }else {
                    Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(myIntent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
package com.silence.demo;

import android.webkit.WebView;
import android.widget.Toast;

import com.silence.wnbridge.WNBridgeRequest;
import com.silence.wnbridge.WNJsInterface;

/**
 * Author silence.
 * Time：2019-08-27.
 * Desc：
 */
public class JsInterface extends WNJsInterface {

    public JsInterface(WebView webView){
        super(webView);
    }

    public void showToast(WNBridgeRequest request) {
        Toast.makeText(webView.getContext(), request.methodParams.optString("msg"), Toast.LENGTH_SHORT).show();
    }

    public String showToastCallback(WNBridgeRequest request) {
        Toast.makeText(webView.getContext(), request.methodParams.optString("msg"), Toast.LENGTH_SHORT).show();
        return "true,1,2,3";
    }

}

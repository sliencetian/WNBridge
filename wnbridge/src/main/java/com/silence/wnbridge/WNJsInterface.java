package com.silence.wnbridge;


import android.text.TextUtils;
import android.webkit.WebView;

/**
 * Author silence.
 * Time：2019-08-27.
 * Desc：js 调用方法声明
 */
public abstract class WNJsInterface {

    private static final String CALLBACK_JS_FORMAT = "javascript:%s(%s);";

    protected WebView webView;

    public WNJsInterface(WebView webView) {
        this.webView = webView;
    }

    public void callBackH5(final WNBridgeRequest request,final String params) {
        WNBridge.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (request != null && webView != null
                        && !TextUtils.isEmpty(request.callbackFunction)) {
                    String execJs = String.format(CALLBACK_JS_FORMAT, request.callbackFunction,  params + "," + request.transferParams);
                    webView.loadUrl(execJs);
                }
            }
        });
    }

    public void onDestroy() {
        webView = null;
    }

}

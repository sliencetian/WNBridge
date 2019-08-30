package com.silence.wnbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author silence.
 * Time：2019-08-29.
 * Desc：封装 桥接 WebView ，拦截相应的 js 调用
 */
public abstract class WNBridgeWebView extends WebView {

    private WNBridgeWebChromeClient webChromeClient;

    public WNBridgeWebView(Context context) {
        this(context, null);
    }

    public WNBridgeWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WNBridgeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebViewSetting();
        initWebViewClient();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSetting() {
        if (BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        getSettings().setJavaScriptEnabled(true);
    }

    private void initWebViewClient() {
        webChromeClient = createWebChromeClient();
        if (webChromeClient != null) {
            setWebChromeClient(webChromeClient);
        }
        WNBridgeWebViewClient viewClient = createWebViewClient();
        if (viewClient != null) {
            setWebViewClient(viewClient);
        }
    }

    public WNBridgeWebChromeClient createWebChromeClient(){
        return new WNBridgeWebChromeClient(getJsInterface());
    }

    public abstract WNJsInterface getJsInterface();

    public WNBridgeWebViewClient createWebViewClient() {
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (webChromeClient != null) {
            webChromeClient.onDestroy();
        }
    }

    public static class WNBridgeWebChromeClient extends WebChromeClient {

        private WNJsInterface jsInterface;

        public WNBridgeWebChromeClient(WNJsInterface jsInterface) {
            this.jsInterface = jsInterface;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            WNBridge.handleJsBridge(jsInterface,message);
            result.cancel();
            return true;
        }

        private void onDestroy(){
            jsInterface.onDestroy();
        }

    }

    public static class WNBridgeWebViewClient extends WebViewClient {

        private WNJsInterface jsInterface;

        public WNBridgeWebViewClient(WNJsInterface jsInterface) {
            this.jsInterface = jsInterface;
        }

        /**
         * 返回true时，代表拦截这次请求，我们自己处理
         * 返回false时，代表不拦截这次请求，让WebView去处理这次请求
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (dispatchInterceptUrl(request.getUrl().toString())) {
                    return true;
                }
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (dispatchInterceptUrl(url)) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (dispatchInterceptUrl(request.getUrl().toString())) {
                    return null;
                }
            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (dispatchInterceptUrl(url)) {
                return null;
            }
            return super.shouldInterceptRequest(view, url);
        }


        protected String convertUrl2PromptParams(String url) {
            return null;
        }

        /**
         * 拦截处理相应的请求
         * @param url 请求协议
         * @return true 处理，false 未处理
         */
        private boolean dispatchInterceptUrl(String url){
            String promptParams = convertUrl2PromptParams(url);
            if (!TextUtils.isEmpty(promptParams)) {
                WNBridge.handleJsBridge(jsInterface,promptParams);
                return true;
            }
            return false;
        }

        protected String generatePromptParams(String method,String jsonParams,String callbackFunction,String transferParams){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("method",method);
                jsonObject.put("methodParams",jsonParams);
                jsonObject.put("callbackFunction",callbackFunction);
                jsonObject.put("transferParams",transferParams);
                return jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

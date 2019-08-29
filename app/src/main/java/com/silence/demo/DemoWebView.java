package com.silence.demo;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.silence.wnbridge.WNBridgeWebView;

/**
 * Author silence.
 * Time：2019-08-29.
 * Desc：
 */
public class DemoWebView extends WNBridgeWebView {

    private JsInterface jsInterface;

    public DemoWebView(Context context) {
        this(context,null);
    }

    public DemoWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DemoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public JsInterface getJsInterface() {
        if (jsInterface == null){
            jsInterface = new JsInterface(this);
        }
        return jsInterface;
    }

    @Override
    public WNBridgeWebViewClient createWebViewClient() {
        return new WNBridgeWebViewClient(getJsInterface()){
            @Override
            protected String convertUrl2PromptParams(String url) {
                Uri uri = Uri.parse(url);
                if ("WNBridge".equalsIgnoreCase(uri.getScheme())){
                    return generatePromptParams(uri.getHost(),"{\"msg\":\"这是 Scheme 的 Toast\"}","","");
                }
                return super.convertUrl2PromptParams(url);
            }
        };
    }
}

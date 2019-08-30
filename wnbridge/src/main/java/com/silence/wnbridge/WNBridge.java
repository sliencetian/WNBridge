package com.silence.wnbridge;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * Author silence.
 * Time：2019-08-29.
 * Desc：解析相关协议，回调相关 native 方法
 */
class WNBridge {

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    static void runOnUIThread(Runnable runnable){
        mainHandler.post(runnable);
    }

    static void handleJsBridge(final WNJsInterface jsInterface,final String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr) && jsInterface != null) {
            final WNBridgeRequest request = WNBridgeRequest.json2JsRequest(jsonStr);
            if (request == null) {
                return;
            }
            WNBridge.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    String params;
                    try {
                        //通过反射调用 WNJsInterface 相关方法
                        Method currMethod = jsInterface.getClass().getMethod(request.method, WNBridgeRequest.class);
                        Object result = currMethod.invoke(jsInterface, request);
                        if (result != null){
                            params = result.toString();
                        } else {
                            params = "null";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        params = e.getMessage();
                    }
                    jsInterface.callBackH5(request, params);
                }
            });
        }
    }
}

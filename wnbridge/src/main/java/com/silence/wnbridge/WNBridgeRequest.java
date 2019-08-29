package com.silence.wnbridge;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Author silence.
 * Time：2019-08-29.
 * Desc：
 */
public class WNBridgeRequest implements Serializable {

    public String method;
    public JSONObject methodParams;
    public String callbackFunction;
    public String transferParams;

    static WNBridgeRequest json2JsRequest(String jsonStr){
        if (TextUtils.isEmpty(jsonStr)){
            return null;
        }
        try {
            WNBridgeRequest request = new WNBridgeRequest();
            JSONObject jsonObject = new JSONObject(jsonStr);
            String method = jsonObject.optString("method");
            if (TextUtils.isEmpty(method)){
                return null;
            }
            request.method = method;
            if (jsonObject.has("methodParams")){
                request.methodParams = new JSONObject(jsonObject.optString("methodParams"));
            }
            if (jsonObject.has("callbackFunction")){
                request.callbackFunction = jsonObject.optString("callbackFunction");
                request.transferParams = jsonObject.optString("transferParams","");
            }
            return request;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}

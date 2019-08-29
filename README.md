# WNBridge
WebView 与 Android 交互

继承 WNBridgeWebView

```
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
```

在 **MainActivity** 中使用

```
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
        webView.onDestroy();
        super.onDestroy();
    }
}
```

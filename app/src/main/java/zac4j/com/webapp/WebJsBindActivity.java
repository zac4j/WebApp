package zac4j.com.webapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity for JavaScript Binding
 * Created by Administrator on 2017/7/21.
 */

public class WebJsBindActivity extends AppCompatActivity {

  private JavascriptCaller mCaller = new JavascriptCaller();

  private EditText mNameField;
  private WebView mBrowser;

  @SuppressLint("SetJavaScriptEnabled") @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_js_bind);

    mNameField = (EditText) findViewById(R.id.jsbind_et_name_field);
    findViewById(R.id.jsbind_btn_jscaller).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        callJsHelloMethod(getUsername(mNameField));
      }
    });
    mBrowser = (WebView) findViewById(R.id.webview);

    // enable javascript
    mBrowser.getSettings().setJavaScriptEnabled(true);
    // enable zoom control
    mBrowser.getSettings().setBuiltInZoomControls(true);
    mBrowser.setWebChromeClient(new MyWebChromeClient());
    mBrowser.addJavascriptInterface(mCaller, "jscaller");
    mBrowser.loadUrl("file:///android_asset/jsbind.html");
  }

  private void callJsHelloMethod(String username) {
    mBrowser.loadUrl("javascript:sayHelloFromJS('" + username + "')");
  }

  private String getUsername(EditText editText) {
    return editText == null ? "" : mNameField.getText().toString();
  }

  private class JavascriptCaller {
    @JavascriptInterface public String getNameFromEditText() {
      return mNameField.getText().toString();
    }

    @JavascriptInterface public void sayHelloFromAndroid(String actualData) {
      new AlertDialog.Builder(WebJsBindActivity.this).setTitle(
          "Android method called from JavaScript!")
          .setMessage("Android says:Hello " + actualData + " !!! Are you OK?")
          .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              //
            }
          })
          .setCancelable(false)
          .create()
          .show();
    }
  }

  private class MyWebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
      new AlertDialog.Builder(WebJsBindActivity.this).setTitle("WebApp Alert")
          .setMessage(message)
          .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              result.confirm();
            }
          })
          .setCancelable(false)
          .create()
          .show();
      return true;
    }
  }
}

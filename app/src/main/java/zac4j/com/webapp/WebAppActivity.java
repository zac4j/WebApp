package zac4j.com.webapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Activity for web app.
 * Created by Zaccc on 2017/7/20.
 */

public class WebAppActivity extends AppCompatActivity {

  private WebView mBrowser;

  @SuppressLint("SetJavaScriptEnabled") @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mBrowser = (WebView) findViewById(R.id.webview);
    mBrowser.getSettings().setJavaScriptEnabled(true);
    mBrowser.setWebChromeClient(new MyWebChromeClient());
    mBrowser.loadUrl("file:///android_asset/prompt.html");
  }

  private class MyWebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
      new AlertDialog.Builder(WebAppActivity.this).setTitle("WebApp Alert")
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

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
      new AlertDialog.Builder(WebAppActivity.this).setTitle("WebApp Confirm")
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

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
        final JsPromptResult result) {
      new AlertDialog.Builder(WebAppActivity.this).setTitle("WebApp Prompt")
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

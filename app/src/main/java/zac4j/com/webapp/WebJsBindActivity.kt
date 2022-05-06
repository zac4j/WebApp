package zac4j.com.webapp

import android.R.string
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import zac4j.com.webapp.R.id
import zac4j.com.webapp.R.layout

/**
 * Activity for JavaScript Binding
 * Created by Administrator on 2017/7/21.
 */
class WebJsBindActivity() : AppCompatActivity() {
  private val mCaller = JavascriptCaller()

  /**
   * NameInput enter your name
   */
  private lateinit var mNameInput: EditText

  /**
   * Web Container
   */
  private lateinit var mBrowser: WebView

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_js_bind)
    mNameInput = findViewById<View>(id.jsbind_et_name_field) as EditText
    findViewById<View>(
        id.jsbind_btn_jscaller
    ).setOnClickListener(
        View.OnClickListener { callJsHelloMethod(getUsername(mNameInput)) })
    mBrowser = findViewById<View>(id.webview) as WebView

    // enable javascript
    mBrowser.settings.javaScriptEnabled = true
    // enable zoom control
    mBrowser.settings.builtInZoomControls = true
    mBrowser.settings.allowUniversalAccessFromFileURLs
    mBrowser.webChromeClient = MyWebChromeClient()
    // add custom js interface
    mBrowser.addJavascriptInterface(mCaller, "jsCaller")
    mBrowser.loadUrl("file:///android_asset/jsbind.html")
  }

  private fun callJsHelloMethod(username: String) {
    //mBrowser.loadUrl("javascript:sayHelloFromJS('$username')")
    mBrowser.evaluateJavascript("sayHelloFromJS('$username')") {
      Log.i("ZacLog", "callJsHelloMethod: ")
    }
  }

  private fun getUsername(editText: EditText?): String {
    return if (editText == null) "" else mNameInput.text
        .toString()
  }

  private inner class JavascriptCaller() {
    @get:JavascriptInterface val nameFromEditText: String
      get() = mNameInput.text
          .toString()

    @JavascriptInterface fun sayHelloFromAndroid(actualData: String) {
      Builder(this@WebJsBindActivity)
          .setTitle(
              "Android method called from JavaScript!"
          )
          .setMessage("Android says:Hello $actualData !!! This call from Browser")
          .setPositiveButton(string.ok) { _, _ ->
            //
          }
          .setCancelable(false)
          .create()
          .show()
    }
  }

  private inner class MyWebViewClient() : WebViewClient() {
    override fun shouldOverrideUrlLoading(
      view: WebView?,
      request: WebResourceRequest?
    ): Boolean {
      return super.shouldOverrideUrlLoading(view, request)
    }
  }

  private inner class MyWebChromeClient() : WebChromeClient() {
    override fun onJsAlert(
      view: WebView,
      url: String,
      message: String,
      result: JsResult
    ): Boolean {
      Builder(this@WebJsBindActivity)
          .setTitle("WebApp Alert")
          .setMessage(message)
          .setPositiveButton(
              string.ok
          ) { _, _ -> result.confirm() }
          .setCancelable(false)
          .create()
          .show()
      return true
    }
  }
}
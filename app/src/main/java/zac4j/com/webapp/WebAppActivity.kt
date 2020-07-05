package zac4j.com.webapp

import android.R.string
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import zac4j.com.webapp.R.id
import zac4j.com.webapp.R.layout

/**
 * Activity for web app.
 * Created by Zaccc on 2017/7/20.
 */
class WebAppActivity : AppCompatActivity() {
  private lateinit var mBrowser: WebView

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_js_alert)
    mBrowser = findViewById<View>(id.main_webview) as WebView
    mBrowser.settings.javaScriptEnabled = true
    mBrowser.webChromeClient = MyWebChromeClient()
    mBrowser.loadUrl("file:///android_asset/prompt.html")
  }

  private inner class MyWebChromeClient : WebChromeClient() {
    override fun onJsAlert(
      view: WebView,
      url: String,
      message: String,
      result: JsResult
    ): Boolean {
      Builder(this@WebAppActivity)
          .setTitle("WebApp Alert")
          .setMessage(message)
          .setPositiveButton(string.ok) { dialog, which -> result.confirm() }
          .setCancelable(false)
          .create()
          .show()
      return true
    }

    override fun onJsConfirm(
      view: WebView,
      url: String,
      message: String,
      result: JsResult
    ): Boolean {
      Builder(this@WebAppActivity)
          .setTitle("WebApp Confirm")
          .setMessage(message)
          .setPositiveButton(string.ok) { dialog, which -> result.confirm() }
          .setCancelable(false)
          .create()
          .show()
      return true
    }

    override fun onJsPrompt(
      view: WebView,
      url: String,
      message: String,
      defaultValue: String,
      result: JsPromptResult
    ): Boolean {
      Builder(this@WebAppActivity)
          .setTitle("WebApp Prompt")
          .setMessage(message)
          .setPositiveButton(string.ok) { dialog, which -> result.confirm() }
          .setCancelable(false)
          .create()
          .show()
      return true
    }
  }
}
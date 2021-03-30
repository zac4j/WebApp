package zac4j.com.webapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import zac4j.com.webapp.R.id
import zac4j.com.webapp.R.layout

/**
 * Activity for JavaScript Binding
 * Created by Administrator on 2017/7/21.
 */
class WebPdfViewerActivity : AppCompatActivity() {

  /**
   * Web Container
   */
  private lateinit var mBrowser: WebView

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_pdf_viewer)

    mBrowser = findViewById(id.webview)
    mBrowser.webViewClient = MyWebViewClient()
    mBrowser.webChromeClient = WebChromeClient()

    mBrowser.settings.apply {
      javaScriptEnabled = true
      allowUniversalAccessFromFileURLs = true
      setAppCacheEnabled(true)
    }

    WebView.setWebContentsDebuggingEnabled(true)
    findViewById<View>(id.previewPdf).setOnClickListener {
      mBrowser.loadUrl("file:///android_asset/pdf/viewer.html")
    }
  }

  private inner class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(
      view: WebView?,
      request: WebResourceRequest?
    ): Boolean {
      Log.d("PdfViewer", "load url: ${request?.url}")
      return super.shouldOverrideUrlLoading(view, request)
    }
  }
}
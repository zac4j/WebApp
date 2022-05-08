package zac4j.com.webapp

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewAssetLoader.AssetsPathHandler
import zac4j.com.webapp.databinding.FragmentPdfViewerBinding

/**
 * Pdf Viewer
 * Created by Zac on 2017/7/21.
 */
class PdfViewerFragment : Fragment() {

  /**
   * Web Container
   */
  private lateinit var mBrowser: WebView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return FragmentPdfViewerBinding.inflate(inflater, container, false).apply {
      mBrowser = webview
      previewPdf.setOnClickListener {
        mBrowser.loadUrl("file:///android_asset/pdf/viewer.html")
      }
    }.root
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    mBrowser.webViewClient = MyWebViewClient()
    mBrowser.webChromeClient = WebChromeClient()

    mBrowser.settings.apply {
      javaScriptEnabled = true
      // Setting this off for security. Off by default for SDK versions >= 16.
      allowFileAccessFromFileURLs = false
      // Off by default, deprecated for SDK versions >= 30.
      allowUniversalAccessFromFileURLs = true
      // Keeping these off is less critical but still a good idea, especially if your app is not
      // using file:// or content:// URLs.
      allowFileAccess = false
      allowContentAccess = false
    }

    if (requireActivity().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
      WebView.setWebContentsDebuggingEnabled(true)
    }
  }

  /**
   * todo Replace allowUniversalAccessFromFileURLs with [WebViewAssetLoader].
   * Ref: https://github.com/android/views-widgets-samples/blob/main/WebView
   * https://developer.android.com/guide/webapps/load-local-content
   */
  private inner class MyWebViewClient : WebViewClient() {

    val assetLoader = WebViewAssetLoader.Builder()
      .addPathHandler("/assets/", AssetsPathHandler(requireContext()))
      .build()

    override fun shouldInterceptRequest(
      view: WebView?,
      request: WebResourceRequest?
    ): WebResourceResponse? {
      return request?.url?.let { assetLoader.shouldInterceptRequest(it) }
    }

    override fun shouldOverrideUrlLoading(
      view: WebView?,
      request: WebResourceRequest?
    ): Boolean {
      Log.d("PdfViewer", "load url: ${request?.url}")
      return super.shouldOverrideUrlLoading(view, request)
    }
  }
}
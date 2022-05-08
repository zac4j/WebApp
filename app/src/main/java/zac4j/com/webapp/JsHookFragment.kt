package zac4j.com.webapp

import android.R.string
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog.Builder
import androidx.fragment.app.Fragment
import zac4j.com.webapp.databinding.FragmentWebContainerBinding

/**
 * Js hook screen.
 * Created by Zaccc on 2017/7/20.
 */
class JsHookFragment : Fragment() {
  private lateinit var mBrowser: WebView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return FragmentWebContainerBinding.inflate(inflater, container, false).also {
      mBrowser = it.mainWebview
    }.root
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

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
      Builder(requireContext())
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
      Builder(requireContext())
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
      Builder(requireContext())
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
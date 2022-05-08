package zac4j.com.webapp

import android.R.string
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog.Builder
import androidx.fragment.app.Fragment
import zac4j.com.webapp.databinding.FragmentJsBindingBinding

/**
 * JavaScript Binding Screen
 * Created by Zac on 2017/7/21.
 */
class JsBindingFragment : Fragment() {
  private val mCaller = JavascriptCaller()

  private lateinit var mNameInput: EditText
  private lateinit var mBrowser: WebView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return FragmentJsBindingBinding.inflate(inflater, container, false).apply {
      mBrowser = webview
      mNameInput = jsbindEtNameField
      jsbindBtnJscaller.setOnClickListener {
        callJsHelloMethod(getUsername(mNameInput))
      }
    }.root
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    mBrowser.apply {
      // enable javascript
      settings.apply {
        javaScriptEnabled = true
        // enable zoom control
        builtInZoomControls = true
        allowFileAccess = true
      }

      webChromeClient = MyWebChromeClient()
      // add custom js interface
      addJavascriptInterface(mCaller, "jsCaller")
      loadUrl("file:///android_asset/jsbind.html")
    }
  }

  private fun callJsHelloMethod(username: String) {
    // For lower Android API level => mBrowser.loadUrl("javascript:sayHelloFromJS('$username')")
    mBrowser.evaluateJavascript("sayHelloFromJS('$username')") {
      Log.i("ZacLog", "callJsHelloMethod: ")
    }
  }

  private fun getUsername(editText: EditText?): String {
    return if (editText == null) "" else mNameInput.text.toString()
  }

  private inner class JavascriptCaller {
    @get:JavascriptInterface val nameFromEditText: String
      get() = mNameInput.text
        .toString()

    @JavascriptInterface fun sayHelloFromAndroid(actualData: String) {
      Builder(requireContext())
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
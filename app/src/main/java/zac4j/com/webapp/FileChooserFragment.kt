package zac4j.com.webapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import zac4j.com.webapp.databinding.FragmentWebContainerBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * WebView demo for file chooser.
 * Created by Zaccc on 2017/12/5.
 */
class FileChooserFragment : Fragment() {

  private var mWebView: WebView? = null
  private var mFilePathCallback: ValueCallback<Array<Uri>>? = null

  companion object {
    private const val GET_PHOTO_KEY = "get_photo"
    private const val TAKE_PICTURE_KEY = "take_picture"
    private const val PHOTO_PICK_URL = "file:///android_asset/photo_picker/index.html"
  }

  private var getPhoto: ActivityResultLauncher<String>? = null
  private var takePicture: ActivityResultLauncher<Uri>? = null
  private lateinit var pictureUri: Uri

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    getPhoto = activity?.activityResultRegistry?.register(
      GET_PHOTO_KEY,
      this,
      ActivityResultContracts.GetContent()
    ) {
      it?.let { uri ->
        mFilePathCallback?.onReceiveValue(arrayOf(uri))
        mFilePathCallback = null
      }
    }

    takePicture = activity?.activityResultRegistry?.register(
      TAKE_PICTURE_KEY,
      this,
      ActivityResultContracts.TakePicture()
    ) {
      if (it) {
        mFilePathCallback?.onReceiveValue(arrayOf(pictureUri))
        mFilePathCallback = null
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return FragmentWebContainerBinding.inflate(inflater, container, false).apply {
      mWebView = mainWebview
    }.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setUpWebViewDefaults()

    // Check whether we're recreating a previously destroyed instance
    if (savedInstanceState != null) {
      // Restore the previous URL and history stack
      mWebView?.restoreState(savedInstanceState)
    }

    // Load the local index.html file
    if (mWebView?.url == null) {
      mWebView?.loadUrl(PHOTO_PICK_URL)
    }
  }

  fun selectFile() {
    val items = arrayOf("pick photo", "take picture")

    fun takePicture() {
      createImageFile()?.let { file ->
        pictureUri = FileProvider.getUriForFile(
          requireContext(),
          BuildConfig.APPLICATION_ID + ".provider",
          file
        )
        takePicture?.launch(pictureUri)
      }
    }

    AlertDialog.Builder(requireContext())
      .setItems(items) { dialog, which ->
        Log.d("dialog", dialog.toString())
        Log.d("which", which.toString())
        when (which) {
          0 -> getPhoto?.launch("image/*")
          1 -> takePicture()
        }
      }.show()
  }

  /**
   * More info this method can be found at
   * http://developer.android.com/training/camera/photobasics.html
   *
   * @throws IOException
   */
  @Throws(IOException::class) private fun createImageFile(): File? {
    // Create an image file name
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
      .format(Date())
    val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
      "JPEG_${timestamp}", /* prefix */
      ".jpg", /* suffix */
      storageDir /* directory */
    )
  }

  /**
   * Convenience method to set some generic defaults for a
   * given WebView
   */
  @SuppressLint("SetJavaScriptEnabled")
  private fun setUpWebViewDefaults() {
    mWebView?.settings?.apply {
      // Enable Javascript
      javaScriptEnabled = true

      // Use WideViewport and Zoom out if there is no viewport defined
      useWideViewPort = true
      loadWithOverviewMode = true

      // Enable pinch to zoom without the zoom buttons
      builtInZoomControls = true

      // Hide the zoom controls
      displayZoomControls = false
    }

    // Enable remote debugging via chrome://inspect
    WebView.setWebContentsDebuggingEnabled(true)

    // We set the WebViewClient to ensure links are consumed by the WebView rather
    // than passed to a browser if it can
    mWebView?.webViewClient = WebViewClient()

    mWebView?.webChromeClient = object : WebChromeClient() {
      override fun onShowFileChooser(
        webView: WebView,
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams
      ): Boolean {
        if (mFilePathCallback != null) {
          mFilePathCallback?.onReceiveValue(null)
        }
        mFilePathCallback = filePathCallback
        selectFile()
        return true
      }
    }
  }

}
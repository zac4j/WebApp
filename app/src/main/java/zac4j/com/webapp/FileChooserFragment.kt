package zac4j.com.webapp

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import zac4j.com.webapp.R.layout
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
  private var mPermissionsDelegate: PermissionsDelegate? = null
  private var mHasStoragePermission = false
  private var mWebView: WebView? = null
  private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
  private var mCameraPhotoPath: String? = null
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val rootView = inflater.inflate(layout.activity_js_alert, container, false)

    // Get reference of WebView from layout/activity_main.xml
    mWebView = rootView.findViewById<View>(R.id.main_webview) as WebView
    mPermissionsDelegate = PermissionsDelegate(activity)
    mHasStoragePermission = mPermissionsDelegate!!.hasStoragePermission()
    if (!mHasStoragePermission) {
      mPermissionsDelegate!!.requestStoragePermission()
    }
    setUpWebViewDefaults(mWebView)

    // Check whether we're recreating a previously destroyed instance
    if (savedInstanceState != null) {
      // Restore the previous URL and history stack
      mWebView!!.restoreState(savedInstanceState)
    }
    mWebView!!.webChromeClient = object : WebChromeClient() {
      override fun onShowFileChooser(
        webView: WebView,
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams
      ): Boolean {
        if (mFilePathCallback != null) {
          mFilePathCallback!!.onReceiveValue(null)
        }
        mFilePathCallback = filePathCallback
        var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent!!.resolveActivity(activity!!.packageManager) != null) {
          // Create the File where the photo should go
          var photoFile: File? = null
          try {
            photoFile = createImageFile()
            takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
          } catch (ex: IOException) {
            // Error occurred while creating the File
            Log.e(TAG, "Unable to create Image File", ex)
          }

          // Continue only if the File was successfully created
          if (photoFile != null) {
            mCameraPhotoPath = "file:" + photoFile.absolutePath
            takePictureIntent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photoFile)
            )
          } else {
            takePictureIntent = null
          }
        }
        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
        contentSelectionIntent.type = "image/*"

        takePictureIntent?.let {
          val intents = arrayOf(it)
          val chooserIntent = Intent(Intent.ACTION_CHOOSER)
          chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
          chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
          chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents)
          startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
        }
        return true
      }
    }

    // Load the local index.html file
    if (mWebView!!.url == null) {
      mWebView!!.loadUrl("file:///android_asset/www/index.html")
    }
    return rootView
  }

  /**
   * More info this method can be found at
   * http://developer.android.com/training/camera/photobasics.html
   *
   * @throws IOException
   */
  @Throws(IOException::class) private fun createImageFile(): File {
    // Create an image file name
    val timeStamp =
      SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
          .format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    )
    return File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",  /* suffix */
        storageDir /* directory */
    )
  }

  /**
   * Convenience method to set some generic defaults for a
   * given WebView
   */
  @SuppressLint("SetJavaScriptEnabled") @TargetApi(VERSION_CODES.HONEYCOMB)
  private fun setUpWebViewDefaults(webView: WebView?) {
    val settings = webView!!.settings

    // Enable Javascript
    settings.javaScriptEnabled = true

    // Use WideViewport and Zoom out if there is no viewport defined
    settings.useWideViewPort = true
    settings.loadWithOverviewMode = true

    // Enable pinch to zoom without the zoom buttons
    settings.builtInZoomControls = true

    // Hide the zoom controls for HONEYCOMB+
    settings.displayZoomControls = false

    // Enable remote debugging via chrome://inspect
    WebView.setWebContentsDebuggingEnabled(true)

    // We set the WebViewClient to ensure links are consumed by the WebView rather
    // than passed to a browser if it can
    mWebView!!.webViewClient = WebViewClient()
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
      super.onActivityResult(requestCode, resultCode, data)
      return
    }
    var results: Array<Uri>? = null

    // Check that the response is a good one
    if (resultCode == Activity.RESULT_OK) {
      if (data == null) {
        // If there is not data, then we may have taken a photo
        if (mCameraPhotoPath != null) {
          results = arrayOf(Uri.parse(mCameraPhotoPath))
        }
      } else {
        val dataString = data.dataString
        if (dataString != null) {
          results = arrayOf(Uri.parse(dataString))
        }
      }
    }
    mFilePathCallback!!.onReceiveValue(results)
    mFilePathCallback = null
    return
  }

  companion object {
    private val TAG = FileChooserFragment::class.java.simpleName
    const val INPUT_FILE_REQUEST_CODE = 1
    const val EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION"
  }
}
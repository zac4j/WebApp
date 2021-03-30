package zac4j.com.webapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import zac4j.com.webapp.R.layout

class MainActivity : AppCompatActivity(), OnClickListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
    if (savedInstanceState == null) {
      Log.v(TAG, "MainFragment Creation")


      findViewById<View>(R.id.test_file_chooser).setOnClickListener(this)
      findViewById<View>(R.id.test_js_alert).setOnClickListener(this)
      findViewById<View>(R.id.test_jsi).setOnClickListener(this)
      findViewById<View>(R.id.test_pdf).setOnClickListener(this)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId
    return if (id == R.id.action_settings) {
      true
    } else super.onOptionsItemSelected(item)
  }

  companion object {
    private val TAG = MainActivity::class.java.simpleName
  }

  override fun onClick(v: View) {
    val klass = when (v.id) {
      R.id.test_file_chooser -> SecondaryActivity::class.java
      R.id.test_js_alert -> WebAppActivity::class.java
      R.id.test_jsi -> WebJsBindActivity::class.java
      R.id.test_pdf -> WebPdfViewerActivity::class.java
      else -> null
    }

    klass?.let {
      startActivity(Intent(this, klass))
    }
  }
}
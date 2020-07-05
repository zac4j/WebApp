package zac4j.com.webapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import zac4j.com.webapp.R.layout

/**
 * FileChooser Activity
 * Created by Zaccc on 2017/8/7.
 */
class SecondaryActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_secondary)

    supportFragmentManager.beginTransaction()
        .add(R.id.container, FileChooserFragment())
        .commit()
  }
}
package zac4j.com.webapp

import android.Manifest.permission
import android.app.Activity
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

internal class PermissionsDelegate(private val activity: Activity) {
  fun hasCameraPermission(): Boolean {
    val permissionCheckResult = ContextCompat.checkSelfPermission(activity, permission.CAMERA)
    return permissionCheckResult == PackageManager.PERMISSION_GRANTED
  }

  fun hasStoragePermission(): Boolean {
    val permissionCheckResult =
      ContextCompat.checkSelfPermission(activity, permission.WRITE_EXTERNAL_STORAGE)
    return permissionCheckResult == PackageManager.PERMISSION_GRANTED
  }

  fun requestCameraPermission() {
    ActivityCompat.requestPermissions(
      activity, arrayOf(permission.CAMERA),
      REQUEST_CODE
    )
  }

  fun requestStoragePermission() {
    ActivityCompat.requestPermissions(
      activity,
      arrayOf(permission.WRITE_EXTERNAL_STORAGE),
      REQUEST_CODE
    )
  }

  fun storagePermissionGranted(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ): Boolean {
    return requestCode == REQUEST_CODE && grantResults.isNotEmpty() && permissions[0] == permission.WRITE_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED
  }

  companion object {
    private const val REQUEST_CODE = 10
  }
}
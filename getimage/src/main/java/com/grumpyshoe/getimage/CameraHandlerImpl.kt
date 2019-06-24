package com.grumpyshoe.getimage

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.grumpyshoe.module.permissionmanager.PermissionManager
import com.grumpyshoe.module.permissionmanager.model.PermissionRequestExplanation
import java.io.File
import java.io.IOException

class CameraHandlerImpl(val permissionManager: PermissionManager) :
    ImageHandler.CameraHandler {

    override val PERMISSION_REQUEST_CAMERA_USAGE: Int = 3155

    private var cameraImageUri: Uri? = null
    private val LOG_TAG = "CameraHandlerImpl"


    /**
     * trigger new image with camera
     *
     * @param activity
     * @param callback
     * @throws IOException
     */
    override fun selectImageFromCamera(activity: Activity): Int {

        permissionManager.checkPermissions(
            activity = activity,
            permissions = arrayOf(Manifest.permission.CAMERA),
            onPermissionResult = { permissionResult ->
                triggerCamera(activity)
            },
            permissionRequestPreExecuteExplanation = PermissionRequestExplanation(
                title = "Camera Permission",
                message = "The App needs the Camera Permission to be able to create new images."
            ),
            permissionRequestRetryExplanation = PermissionRequestExplanation(
                title = "Retry Custom Permission Hint",
                message = "You denied the permissions previously but this permissions are needed because ..."
            ),
            requestCode = PERMISSION_REQUEST_CAMERA_USAGE
        )

        return PERMISSION_REQUEST_CAMERA_USAGE

    }


    /**
     * trigger camera to create image
     *
     * @param activity
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun triggerCamera(activity: Activity) {

        val context = activity.applicationContext
        val filename = "photo"

        val previousFile = File(activity.externalCacheDir.toString() + filename + ".jpg")
        if (previousFile.exists()) {
            previousFile.delete()
        }

        val photoFile = File.createTempFile(filename, ".jpg", activity.externalCacheDir)
        cameraImageUri = FileProvider.getUriForFile(activity, activity.packageName + ".fileprovider", photoFile)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val resolvedIntentActivities =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolvedIntentInfo in resolvedIntentActivities) {
            val packageName = resolvedIntentInfo.activityInfo.packageName

            context.grantUriPermission(
                packageName,
                cameraImageUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
        activity.startActivityForResult(intent, ImageHandler.ImageSources.CAMERA.dataRequestCode)
    }


    override fun onIntentResult(context: Context, onResult: (Bitmap) -> Unit): Boolean {

        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, cameraImageUri)

            onResult(bitmap)

            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }
}
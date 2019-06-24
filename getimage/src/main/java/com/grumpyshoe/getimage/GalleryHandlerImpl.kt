package com.grumpyshoe.getimage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.grumpyshoe.module.permissionmanager.PermissionManager
import com.grumpyshoe.module.permissionmanager.model.PermissionRequestExplanation
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class GalleryHandlerImpl(val permissionManager: PermissionManager) :
    ImageHandler.GalleryHandler {

    override var PERMISSION_REQUEST_GALLERY_USAGE: Int = 5513
    private val LOG_TAG = "ImageHandlerImpl"
    private lateinit var mFilePath: String


    /**
     * trigger select image from gallery
     * @param activity
     * @param callback
     */
    override fun selectImageFromGallery(activity: Activity):Int {

        permissionManager.checkPermissions(
            activity = activity,
            permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            onPermissionResult = { permissionResult ->
                triggerGallery(activity)
            },
            permissionRequestPreExecuteExplanation = PermissionRequestExplanation(
                title = "Camera Permission",
                message = "The App needs the Camera Permission to be able to create new images."
            ),
            permissionRequestRetryExplanation = PermissionRequestExplanation(
                title = "Retry Custom Permission Hint",
                message = "You denied the permissions previously but this permissions are needed because ..."
            ),
            requestCode = PERMISSION_REQUEST_GALLERY_USAGE
        )

        return PERMISSION_REQUEST_GALLERY_USAGE
    }


    /**
     * trigger gallery to choose an image
     *
     */
    override fun triggerGallery(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"), ImageHandler.ImageSources.GALLERY.dataRequestCode)
    }


    override fun onIntentResult(uri: Uri, activity: Activity, onResult: (Bitmap) -> Unit): Boolean {
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor =
                activity.applicationContext.contentResolver.query(
                    uri,
                    proj,
                    null,
                    null,
                    null
                )
            if (cursor != null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                val s = cursor.getString(columnIndex)
                cursor.close()

                val bmOptions = BitmapFactory.Options()
                val bitmap = BitmapFactory.decodeFile(s, bmOptions)
                onResult(bitmap)

                return true
            }
        } catch (ex: IllegalArgumentException) {
            Log.e(LOG_TAG, ex.message, ex)
        }
        return false
    }


}
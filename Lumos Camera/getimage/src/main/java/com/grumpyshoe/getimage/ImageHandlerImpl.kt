package com.grumpyshoe.getimage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import com.grumpyshoe.module.permissionmanager.PermissionManager
import com.grumpyshoe.module.permissionmanager.impl.PermissionManagerImpl


class ImageHandlerImpl : ImageHandler {

    val permissionManager: PermissionManager = PermissionManagerImpl
    override var cameraHandler: ImageHandler.CameraHandler = CameraHandlerImpl(permissionManager)
    override var galleryHandler: ImageHandler.GalleryHandler = GalleryHandlerImpl(permissionManager)


    private lateinit var mCurrectActivity: Activity
    private lateinit var onImageReceived: (Bitmap) -> Unit
    private var requestCodeTrigger: Int = 0

    override fun getImage(
        activity: Activity,
        sources: List<ImageHandler.ImageSources>,
        onImageReceived: (Bitmap) -> Unit
    ) {

        if (sources.isEmpty()) {
            throw IllegalArgumentException("At least one source needs to be defined")
        }

        this.onImageReceived = onImageReceived
        mCurrectActivity = activity

        if (sources.size == 1) {

            // choose object from source directly
            if (sources[0].equals(ImageHandler.ImageSources.CAMERA)) {
                requestCodeTrigger = cameraHandler.selectImageFromCamera(activity)
            } else {
                requestCodeTrigger = galleryHandler.selectImageFromGallery(activity)
            }
        } else {

            // start dialog to select source
            showSourceChooserDialog(
                dialogTitle = "DialogTite",
                takePhotoTitle = "Photo",
                getImageFromGallerytitle = "galler"
            )
        }

    }

    /**
     * Builds Dialog for user to choose where to load image from.
     * Possibilities are gallery or photo.
     *
     * Triggers imagesource according to users choice
     *
     * @param activity
     * @param dialogTitle
     * @param takePhotoTitle
     * @param getImageFromGallerytitle
     * @param callback
     */
    fun showSourceChooserDialog(
        dialogTitle: String,
        takePhotoTitle: String,
        getImageFromGallerytitle: String
    ) {

        val items = arrayOf<CharSequence>(takePhotoTitle, getImageFromGallerytitle)

        val builder = AlertDialog.Builder(mCurrectActivity)
        builder.setTitle(dialogTitle)
        builder.setItems(items) { dialog, index ->
            if (items[index] == takePhotoTitle) {
                requestCodeTrigger = cameraHandler.selectImageFromCamera(mCurrectActivity)
            } else {
                requestCodeTrigger = galleryHandler.selectImageFromGallery(mCurrectActivity)
            }
        }
        builder.setNegativeButton("Abbrechen", null)
        Handler(Looper.getMainLooper()).post { builder.create().show() }
    }


    /**
     * handle request permission result for creating/getting a picture
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        if (requestCode == cameraHandler.PERMISSION_REQUEST_CAMERA_USAGE && requestCodeTrigger == cameraHandler.PERMISSION_REQUEST_CAMERA_USAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraHandler.triggerCamera(mCurrectActivity)
            }
            return true

        } else if (requestCode == galleryHandler.PERMISSION_REQUEST_GALLERY_USAGE && requestCodeTrigger == galleryHandler.PERMISSION_REQUEST_GALLERY_USAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleryHandler.triggerGallery(mCurrectActivity)
            }
            return true
        } else {
            return false
        }
    }


    /**
     * Gets image mimeType on given path
     *
     * @param imagePath
     */
    override fun getMimeType(imagePath: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(imagePath)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }


    /**
     * handle onActivityResult
     *
     * @param requestCode
     */

    override fun onActivityResult(context: Context, requestCode: Int, resultCode: Int, intent: Intent?): Boolean {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == ImageHandler.ImageSources.CAMERA.dataRequestCode && intent != null) {

                return cameraHandler.onIntentResult(context) {
                    onImageReceived(it)
                }

            } else if (requestCode == ImageHandler.ImageSources.GALLERY.dataRequestCode && intent != null && intent.data != null) {
                return galleryHandler.onIntentResult(intent.data, mCurrectActivity) {
                    onImageReceived(it)
                }
            }
        }

        return false
    }
}
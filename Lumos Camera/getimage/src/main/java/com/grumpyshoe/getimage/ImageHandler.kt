package com.grumpyshoe.getimage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri

interface ImageHandler {

    var cameraHandler: CameraHandler
    var galleryHandler: GalleryHandler

    enum class ImageSources(val dataRequestCode: Int) {
        CAMERA(1000),
        GALLERY(2000);
    }

    fun getImage(
        activity: Activity,
        sources: List<ImageSources>,
        onImageReceived: (Bitmap) -> Unit
    )
    fun getMimeType(imagePath: String): String?
    fun onActivityResult(context: Context, requestCode: Int, resultCode: Int, intent: Intent?): Boolean
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean

    interface CameraHandler {
        val PERMISSION_REQUEST_CAMERA_USAGE: Int
        fun selectImageFromCamera(activity: Activity): Int
        fun triggerCamera(activity: Activity)
        fun onIntentResult(context: Context, onResult: (Bitmap) -> Unit): Boolean
    }

    interface GalleryHandler {
        var PERMISSION_REQUEST_GALLERY_USAGE: Int
        fun selectImageFromGallery(activity: Activity): Int
        fun triggerGallery(activity: Activity)
        fun onIntentResult(uri: Uri, activity: Activity, onResult: (Bitmap) -> Unit): Boolean
    }
    interface ImageConverter {
        fun toBase64(filePath: String): String
    }

}

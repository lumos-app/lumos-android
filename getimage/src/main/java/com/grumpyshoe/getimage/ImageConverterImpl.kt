package com.grumpyshoe.getimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream


class ImageConverterImpl : ImageHandler.ImageConverter {

    /**
     * return value without any size checks
     *
     * @param filePath
     * @return
     */
    override fun toBase64(filePath: String): String {

        // get image as byte array
        val b =
            fileToByteArray(filePath) ?: throw java.lang.IllegalArgumentException("File $filePath could not vbe found")

        // convert byte array to base64 encoded string
        return Base64.encodeToString(b, Base64.NO_WRAP)

    }


    /**
     * convert image to byte array
     *
     * @param filePath
     * @return
     */
    private fun fileToByteArray(filePath: String): ByteArray? {
        try {
            val bm = BitmapFactory.decodeFile(filePath)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            bm.recycle()

            val b = baos.toByteArray()
            baos.close()
            return b
        } catch (e: Exception) {
            Log.e("ImageHandler.Converter", e.message, e)
        }

        return null
    }
}
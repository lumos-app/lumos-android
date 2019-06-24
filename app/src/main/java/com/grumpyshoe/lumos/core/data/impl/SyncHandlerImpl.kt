package com.grumpyshoe.lumos.core.data.impl

import android.content.Context
import android.graphics.Bitmap
import com.grumpyshoe.lumos.core.dagger.Injector
import com.grumpyshoe.lumos.core.data.DataManager
import com.grumpyshoe.lumos.core.data.model.Image
import com.grumpyshoe.lumos.core.data.model.LocalImage
import com.grumpyshoe.lumos.core.data.src.network.NetworkManager
import com.grumpyshoe.module.imagemanager.ImageManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class SyncHandlerImpl : DataManager.SyncHandler {

    @Inject
    protected lateinit var networkManager: NetworkManager
    @Inject
    protected lateinit var context: Context
    @Inject
    protected lateinit var imageManager: ImageManager

    init {
        Injector.INSTANCE.get().inject(this)
    }

    /**
     * request all images from server
     *
     */
    override fun requestAllImages(onImagesLoaded: (List<Image>) -> Unit, onError: () -> Unit) {

        doAsync {

            networkManager.getImages(
                onSuccess = { imagelist ->
                    val images = imagelist.filter { it.isValid() }
                        .map {
                            Image(
                                localId = UUID.randomUUID().toString(),
                                serverId = it.uuid!!,
                                filename = it.filename!!,
                                uploadedFrom = it.uploadedFrom!!,
                                totalViewCount = it.totalViewCount!!,
                                createdDate = it.createdDate!!,
                                show = it.show!!,
                                data = it.data
                            )
                        }

//                    images.forEach {
//                        save(it.serverId, it.data!!)
//                    }

                    uiThread {
                        onImagesLoaded(images)
                    }
                },
                onError = {
                    uiThread { onError() }
                })
        }
    }

//    /**
//     * request all images from server
//     *
//     */
//    private fun save(id: String, base64ImageData: String) {
//        var fos: FileOutputStream? = null
//        try {
//            fos = context.openFileOutput("image_$id.jpg", Context.MODE_PRIVATE)
//            val decodedString = android.util.Base64.decode(base64ImageData, android.util.Base64.DEFAULT)
//            fos.write(decodedString)
//            fos.flush()
//            fos.close()
//
//
//        } catch (e: Exception) {
//
//        } finally {
//            if (fos != null) {
//                fos = null
//            }
//        }
//    }

    /**
     * upload image to server
     *
     */
    override fun uploadImage(localImage: LocalImage, onUploadFinished: () -> Unit) {

        doAsync {

            val base64Image = imageManager.imageConverter.toBase64(localImage.bitmap, Bitmap.CompressFormat.JPEG, 100)

            networkManager.uploadImage(
                id = localImage.imageId,
                author = localImage.author,
                base64Image = base64Image,
                onUploadFinished = {
                    uiThread {
                        onUploadFinished()
                    }
                })
        }
    }
}
package com.grumpyshoe.lumos.core.data

import com.grumpyshoe.lumos.core.data.model.Image
import com.grumpyshoe.lumos.core.data.model.LocalImage
import com.grumpyshoe.module.bonjourconnect.BonjourConnect
import com.grumpyshoe.module.bonjourconnect.models.NetworkService

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
interface DataManager {

    val syncHandler: SyncHandler

    /**
     * search for lumos server
     *
     */
    fun searchForLumosServer(onLumosServerFound: (NetworkService) -> Unit, onError: (BonjourConnect.ErrorType) -> Unit)

    /**
     * SyncHandler contains all sync operations
     *
     */
    interface SyncHandler {

        /**
         * request all images from server
         *
         */
        fun requestAllImages(onImagesLoaded: (List<Image>) -> Unit, onError: () -> Unit)

        /**
         * upload image to server
         *
         */
        fun uploadImage(localImage: LocalImage, onUploadFinished: () -> Unit)
    }
}
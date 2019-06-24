package com.grumpyshoe.lumos.core.data.src.network

import com.grumpyshoe.lumos.core.data.src.network.dto.ImageDto

/**
 * Interface for NetworkManager
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
interface NetworkManager {

    /**
     * get Images
     *
     */
    fun getImages(onSuccess: (List<ImageDto>) -> Unit, onError: () -> Unit)

    /**
     * upload new image
     *
     */
    fun uploadImage(id: String, author: String, base64Image: String, onUploadFinished: () -> Unit)
}
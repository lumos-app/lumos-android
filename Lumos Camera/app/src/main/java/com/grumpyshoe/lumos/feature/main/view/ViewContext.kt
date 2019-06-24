package com.grumpyshoe.lumos.feature.main.view

import android.graphics.Bitmap
import com.grumpyshoe.module.bonjourconnect.BonjourConnect
import com.grumpyshoe.module.bonjourconnect.models.NetworkService
import com.grumpyshoe.module.imagemanager.ImageManager

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
interface ViewContext {

    fun searchForLumos(type: String, onLumosServerFound: (NetworkService) -> Unit, onError: (BonjourConnect.ErrorType) -> Unit)
    fun stopRefreshSpinner()
    fun getImage(sources: List<ImageManager.ImageSources>, onImageReceived: (Bitmap) -> Unit)
    fun closeApp()
}
package com.grumpyshoe.lumos.feature.main

import androidx.lifecycle.MutableLiveData
import com.grumpyshoe.lumos.core.data.model.Image

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
object MainData {

    val images = MutableLiveData<List<Image>>()
}
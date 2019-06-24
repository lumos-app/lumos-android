package com.grumpyshoe.lumos.core.data.src.network.impl.retrofit.dto

import com.grumpyshoe.lumos.core.data.src.network.dto.ImageDto

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
data class ImageListDto(
    val success: Boolean? = null,
    val images: List<ImageDto>? = null
)
package com.grumpyshoe.lumos.core.data.model

import android.graphics.Bitmap

/**
 * Model for representing 'LocalImage'
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
data class LocalImage(val imageId: String, val author: String, val bitmap: Bitmap)
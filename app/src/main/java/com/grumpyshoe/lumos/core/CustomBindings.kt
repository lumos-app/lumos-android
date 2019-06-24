package com.grumpyshoe.lumos.core

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * CustomBindings.kt
 * Lumos
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */

/**
 * handle visibility according to boolean
 *
 */
@BindingAdapter("app:visibility")
fun setVisibility(view: View, visibility: Boolean) {
    if (visibility) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}
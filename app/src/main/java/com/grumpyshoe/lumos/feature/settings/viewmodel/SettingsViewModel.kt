package com.grumpyshoe.lumos.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grumpyshoe.lumos.feature.settings.view.ViewContext

/**
 * Factory class for creating viewModel
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(private val sourceActivityFinished: Boolean, private val viewContext: ViewContext) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(sourceActivityFinished, viewContext) as T
    }
}

/**
 * Get ViewModel
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class SettingsViewModel(sourceActivityFinished: Boolean, viewContext: ViewContext) : ViewModel() {

    val observable = SettingsObservable(sourceActivityFinished, viewContext)
}
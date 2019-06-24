package com.grumpyshoe.lumos.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grumpyshoe.lumos.feature.main.view.GridAdapter
import com.grumpyshoe.lumos.feature.main.view.ViewContext

/**
 * Factory class for creating viewModel
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val viewContext: ViewContext, private val adapter: GridAdapter) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(viewContext, adapter) as T
    }
}

/**
 *
 */

/**
 * Get ViewModel
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class MainViewModel(viewContext: ViewContext, adapter: GridAdapter) : ViewModel() {

    val observable = MainObservable(viewContext, adapter)
}
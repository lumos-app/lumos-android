package com.grumpyshoe.lumos.core.navigation

import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
interface NavigationHandler {

    // core

    fun setNavigationObjects(activity: AppCompatActivity)

    // activity navigation

    fun openSettings(finishPreviousActivity: Boolean = false)
    fun openMain()
}
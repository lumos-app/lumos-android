package com.grumpyshoe.lumos.core

import android.app.Application
import com.grumpyshoe.lumos.core.dagger.Injector
import io.realm.Realm

/**
 * AppApplication.kt
 * Lumos
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class AppApplication : Application() {

    /**
     * onCreate
     *
     */
    override fun onCreate() {
        super.onCreate()

        // init dependency injection
        Injector.INSTANCE.init(this)

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this)
    }
}
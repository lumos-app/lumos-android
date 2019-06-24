package com.grumpyshoe.lumos.core.dagger

import android.app.Application

/**
* Injector for injecting dependencies via Dagger
*
* Created by Thomas Cirksena on 10.06.19.
* Copyright © 2019 Thomas Cirksena. All rights reserved.
*/
enum class Injector {

    INSTANCE;

    /**
     * get generated app component
     *
     * @return
     */
    internal var appComponent: AppComponent? = null

    /**
     * init method
     *
     */
    fun init(application: Application) {
        appComponent = DaggerAppComponent.builder()
                .androidModule(AndroidModule(application))
                .dataModule(DataModule(application))
                .othersModule(OthersModule(application))
                .build()
    }

    /**
     * get app component
     *
     */
    fun get(): AppComponent {
        return appComponent!!
    }

    /**
     * set app component
     *
     * @param appComponent
     */
    fun set(appComponent: AppComponent) {
        this.appComponent = appComponent
    }
}
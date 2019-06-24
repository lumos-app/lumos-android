package com.grumpyshoe.lumos.core.dagger

import com.grumpyshoe.lumos.core.data.impl.DataManagerImpl
import com.grumpyshoe.lumos.core.data.impl.SyncHandlerImpl
import com.grumpyshoe.lumos.core.data.src.network.impl.NetworkManagerImpl
import com.grumpyshoe.lumos.core.data.src.preferences.impl.PreferenceManagerImpl
import com.grumpyshoe.lumos.feature.main.view.MainActivity
import com.grumpyshoe.lumos.feature.main.viewmodel.MainObservable
import com.grumpyshoe.lumos.feature.settings.view.SettingsActivity
import com.grumpyshoe.lumos.feature.settings.viewmodel.SettingsObservable
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
@Singleton
@Component(modules = arrayOf(AndroidModule::class, DataModule::class, OthersModule::class))
interface AppComponent {

    // core

    fun inject(core: SyncHandlerImpl)
    fun inject(core: NetworkManagerImpl)
    fun inject(core: PreferenceManagerImpl)
    fun inject(core: DataManagerImpl)

    // activities

    fun inject(activity: MainActivity)
    fun inject(activity: SettingsActivity)

    // observable

    fun inject(observable: MainObservable)
    fun inject(observable: SettingsObservable)
}
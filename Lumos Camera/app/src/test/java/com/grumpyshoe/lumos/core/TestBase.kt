package com.grumpyshoe.lumos.core

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.grumpyshoe.lumos.core.dagger.AndroidModule
import com.grumpyshoe.lumos.core.dagger.DaggerAppComponent
import com.grumpyshoe.lumos.core.dagger.DataModule
import com.grumpyshoe.lumos.core.dagger.Injector
import com.grumpyshoe.lumos.core.dagger.OthersModule
import com.grumpyshoe.lumos.core.data.DataManager
import com.grumpyshoe.lumos.core.data.src.network.NetworkManager
import com.grumpyshoe.lumos.core.data.src.preferences.PreferenceManager
import com.grumpyshoe.lumos.core.dialog.DialogHandler
import com.grumpyshoe.lumos.core.navigation.NavigationHandler
import com.grumpyshoe.module.imagemanager.ImageManager
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
open class TestBase {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var context: Context
    lateinit var resources: Resources
    lateinit var packageManager: PackageManager
    lateinit var networkManager: NetworkManager
    lateinit var dataManager: DataManager
    lateinit var syncHandler: DataManager.SyncHandler
    lateinit var preferenceManager: PreferenceManager
    lateinit var navigationHandler: NavigationHandler
    lateinit var dialogHandler: DialogHandler
    lateinit var imageManager: ImageManager

    @Before
    open fun setUp() {
        context = mock()
        resources = mock()
        packageManager = mock()
        networkManager = mock()
        navigationHandler = mock()
        dialogHandler = mock()
        preferenceManager = mock()
        dataManager = mock()
        syncHandler = mock()
        imageManager = mock()

        val component = DaggerAppComponent.builder()
            .androidModule(object : AndroidModule(context) {
                override fun provideContext(): Context {
                    return context
                }

                override fun providePackageManager(): PackageManager {
                    return packageManager
                }

                override fun provideResources(): Resources {
                    return resources
                }
            })
            .dataModule(object : DataModule(context) {
                override fun provideDataManager(): DataManager {
                    return dataManager
                }

                override fun provideNetworkManager(): NetworkManager {
                    return networkManager
                }

                override fun providePreferenceManager(): PreferenceManager {
                    return preferenceManager
                }

                override fun provideSyncHandler(): DataManager.SyncHandler {
                    return syncHandler
                }
            })
            .othersModule(object : OthersModule(context) {
                override fun provideNavigationHandler(): NavigationHandler {
                    return navigationHandler
                }

                override fun provideDialogHandler(): DialogHandler {
                    return dialogHandler
                }

                override fun provideImageManager(): ImageManager {
                    return imageManager
                }
            }).build()
        Injector.INSTANCE.set(component)

        whenever(dataManager.syncHandler).thenReturn(syncHandler)
    }
}

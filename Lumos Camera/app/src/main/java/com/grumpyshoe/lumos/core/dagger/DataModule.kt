package com.grumpyshoe.lumos.core.dagger

import android.content.Context
import com.grumpyshoe.lumos.core.data.DataManager
import com.grumpyshoe.lumos.core.data.impl.DataManagerImpl
import com.grumpyshoe.lumos.core.data.impl.SyncHandlerImpl
import com.grumpyshoe.lumos.core.data.src.network.NetworkManager
import com.grumpyshoe.lumos.core.data.src.network.impl.NetworkManagerImpl
import com.grumpyshoe.lumos.core.data.src.preferences.PreferenceManager
import com.grumpyshoe.lumos.core.data.src.preferences.impl.PreferenceManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module containing all parts of data handling
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
@Module
open class DataModule(val context: Context) {

    /**
     * provide DataManager instance
     *
     */
    @Provides
    @Singleton
    open fun provideDataManager(): DataManager {
        return DataManagerImpl()
    }

    /**
     * provide NetworkManager instance
     *
     */
    @Provides
    @Singleton
    open fun provideNetworkManager(): NetworkManager {
        return NetworkManagerImpl()
    }

    /**
     * provide PreferenceManager instance
     *
     */
    @Provides
    @Singleton
    open fun providePreferenceManager(): PreferenceManager {
        return PreferenceManagerImpl(context)
    }

    /**
     * provide SyncHandler instance
     *
     */
    @Provides
    @Singleton
    open fun provideSyncHandler(): DataManager.SyncHandler {
        return SyncHandlerImpl()
    }
}
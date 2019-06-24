package com.grumpyshoe.lumos.core.dagger

import android.content.Context
import com.grumpyshoe.lumos.core.dialog.DialogHandler
import com.grumpyshoe.lumos.core.dialog.impl.DialogHandlerImpl
import com.grumpyshoe.lumos.core.navigation.NavigationHandler
import com.grumpyshoe.lumos.core.navigation.impl.NavigationHandlerImpl
import com.grumpyshoe.module.imagemanager.ImageManager
import com.grumpyshoe.module.imagemanager.impl.ImageManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module containing all parts of other features
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
@Module
open class OthersModule(val context: Context) {

    /**
     * provide ImageUtils instance
     *
     */
    @Provides
    @Singleton
    open fun provideImageManager(): ImageManager {
        return ImageManagerImpl()
    }

    /**
     * provide DialogHandler instance
     *
     */
    @Provides
    @Singleton
    open fun provideDialogHandler(): DialogHandler {
        return DialogHandlerImpl
    }

    /**
     * provide NavigationHandler instance
     *
     */
    @Provides
    @Singleton
    open fun provideNavigationHandler(): NavigationHandler {
        return NavigationHandlerImpl()
    }
}
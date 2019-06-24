package com.grumpyshoe.lumos.core.dagger

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**d
 * Module containing all parts of the Android framework
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
@Module
open class AndroidModule(internal val context: Context) {

    /**
     * method providing context
     *
     */
    @Provides
    @Singleton
    open fun provideContext(): Context {
        return context
    }

    /**
     * method providing PackageManager
     *
     */
    @Provides
    @Singleton
    open fun providePackageManager(): PackageManager {
        return context.packageManager
    }

    /**
     * method providing Resources
     *
     */
    @Provides
    @Singleton
    open fun provideResources(): Resources {
        return context.resources
    }
}
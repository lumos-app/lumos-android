package com.grumpyshoe.lumos.core.jobs

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.grumpyshoe.lumos.core.data.impl.DataManagerImpl
import com.grumpyshoe.lumos.feature.main.MainData

/**
 * DownloadManager for handling asynchronously image sync with server
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class DownloadManager(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        DataManagerImpl().syncHandler.requestAllImages(
            onImagesLoaded = {
                MainData.images.postValue(it)
            },
            onError = {
                // ignore it
            })

        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }
}
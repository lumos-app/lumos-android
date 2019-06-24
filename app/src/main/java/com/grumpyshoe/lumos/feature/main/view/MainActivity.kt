package com.grumpyshoe.lumos.feature.main.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.grumpyshoe.lumos.R
import com.grumpyshoe.lumos.core.dagger.Injector
import com.grumpyshoe.lumos.core.data.model.Image
import com.grumpyshoe.lumos.core.dialog.DialogHandler
import com.grumpyshoe.lumos.core.jobs.DownloadManager
import com.grumpyshoe.lumos.core.navigation.NavigationHandler
import com.grumpyshoe.lumos.databinding.ActivityMainBinding
import com.grumpyshoe.lumos.feature.main.MainData
import com.grumpyshoe.lumos.feature.main.viewmodel.MainViewModel
import com.grumpyshoe.lumos.feature.main.viewmodel.MainViewModelFactory
import com.grumpyshoe.module.bonjourconnect.BonjourConnect
import com.grumpyshoe.module.bonjourconnect.impl.BonjourConnectImpl
import com.grumpyshoe.module.bonjourconnect.models.NetworkService
import com.grumpyshoe.module.imagemanager.ImageManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class MainActivity : AppCompatActivity(), ViewContext {

    @Inject
    lateinit var imageManager: ImageManager
    @Inject
    lateinit var dialogHandler: DialogHandler
    @Inject
    lateinit var navigationHandler: NavigationHandler

    lateinit var gridAdapter: GridAdapter

    /**
     * handle onCreate
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.INSTANCE.get().inject(this)

        // init DialogHandler
        dialogHandler.setActivity(this)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // init recyclerView & adapter
        image_grid.layoutManager = GridLayoutManager(applicationContext, 3, RecyclerView.VERTICAL, false)
        gridAdapter = GridAdapter()
        image_grid.adapter = gridAdapter

        // init viewModel
        val viewModel = ViewModelProviders.of(this, MainViewModelFactory(this, gridAdapter)).get(MainViewModel::class.java)

        // bind observable
        binding.observable = viewModel.observable

        // observe images
        MainData.images.observe(this,
            Observer<List<Image>> { model -> viewModel.observable.onImageListChanged(model) })

        // start async handler for image server sync
        val uploadWorkRequest = PeriodicWorkRequestBuilder<DownloadManager>(3, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork("do it", ExistingPeriodicWorkPolicy.KEEP, uploadWorkRequest)

        // bind observable to OnRefreshListeners onRefresh
        refresh_layout.setOnRefreshListener {
            viewModel.observable.refresh()
        }
    }

    /**
     * handle onResume
     *
     */
    override fun onResume() {
        super.onResume()

        // init DialogHandler
        dialogHandler.setActivity(this)

        // init NavigationHandler
        navigationHandler.setNavigationObjects(this)
    }

    /**
     * handle permisson result
     *
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (!imageManager.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * handle activity result
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!imageManager.onActivityResult(this, requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * start sesrch for lumos server
     *
     */
    override fun searchForLumos(
        type: String,
        onLumosServerFound: (NetworkService) -> Unit,
        onError: (BonjourConnect.ErrorType) -> Unit
    ) {
        val bonjourConnect: BonjourConnect = BonjourConnectImpl(applicationContext)
        bonjourConnect.getServiceInfo(
            type = type,
            onServiceInfoReceived = {
                onLumosServerFound(it)
            },
            onError = {
                onError(it)
            },
            searchTimeout = 10000
        )
    }

    /**
     * stop refresh spinner
     *
     */
    override fun stopRefreshSpinner() {
        refresh_layout.isRefreshing = false
    }

    /**
     * get image
     *
     */
    override fun getImage(sources: List<ImageManager.ImageSources>, onImageReceived: (Bitmap) -> Unit) {
        imageManager.getImage(
            activity = this,
            sources = sources,
            onImageReceived = onImageReceived
        )
    }

    /**
     * close app
     *
     */
    override fun closeApp() {
        finish()
    }
}

package com.grumpyshoe.lumos.feature.main.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import com.grumpyshoe.lumos.R
import com.grumpyshoe.lumos.core.dagger.Injector
import com.grumpyshoe.lumos.core.data.DataManager
import com.grumpyshoe.lumos.core.data.impl.DataManagerImpl
import com.grumpyshoe.lumos.core.data.model.Image
import com.grumpyshoe.lumos.core.data.model.LocalImage
import com.grumpyshoe.lumos.core.data.src.preferences.PreferenceManager
import com.grumpyshoe.lumos.core.dialog.DialogHandler
import com.grumpyshoe.lumos.core.navigation.NavigationHandler
import com.grumpyshoe.lumos.feature.main.MainData
import com.grumpyshoe.lumos.feature.main.view.GridAdapter
import com.grumpyshoe.lumos.feature.main.view.ViewContext
import com.grumpyshoe.module.bonjourconnect.models.NetworkService
import com.grumpyshoe.module.imagemanager.ImageManager
import java.util.Date
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class MainObservable(val viewContext: ViewContext, val adapter: GridAdapter) : BaseObservable() {

    @Inject
    lateinit var preferenceManager: PreferenceManager
    @Inject
    lateinit var dataManager: DataManager
    @Inject
    lateinit var imageManager: ImageManager
    @Inject
    lateinit var dialogHandler: DialogHandler
    @Inject
    lateinit var context: Context
    @Inject
    lateinit var navigationHandler: NavigationHandler

    val capturingEnabled = ObservableBoolean(false)

    init {

        Injector.INSTANCE.get().inject(this)

        checkRequirements()
    }

    /**
     * check if server is registered
     *
     */
    private fun checkRequirements() {

        if (preferenceManager.getServerAddress() == null) {
            searchForServer()
        } else {
            requestImagesFromServer()
        }
    }

    /**
     * search for service '_lumos._tcp' at the network
     *
     */
    private fun searchForServer() {

        dialogHandler.showLoadingDialog(
            title = R.string.search_start_title,
            message = R.string.search_start_text,
            cancelable = false
        )

        viewContext.stopRefreshSpinner()

        dataManager.searchForLumosServer(
            onLumosServerFound = {
                showServerFoundDialog(it)
                dialogHandler.dismiss()
                viewContext.stopRefreshSpinner()
            },
            onError = {
                showServerNotFoundDuringSetupDialog()
                viewContext.stopRefreshSpinner()
            }
        )
    }

    /**
     * show server not found
     *
     */
    private fun showServerNotFoundDuringSetupDialog() {

        dialogHandler.showConfirmDialog(
            title = R.string.search_start_title,
            message = R.string.search_error_no_server_found_text,
            confirmBtnText = R.string.btn_server_manual,
            denyBtnText = R.string.btn_retry,
            onConfirm = {
                showManualInputDialog()
            },
            onDeny = {
                searchForServer()
            }
        )
    }

    /**
     * show input dialog for manual server input
     *
     */
    private fun showManualInputDialog() {

        dialogHandler.showInputDialog(
            title = R.string.common_dialog_lumos_server_title,
            message = R.string.dialog_manual_server_settings_text,
            inputLayout = R.layout.dialog_server_input,
            textviewId = R.id.server_input_text,
            confirmBtnText = R.string.common_btn_save_title,
            denyBtnText = R.string.btn_retry,
            onInput = {
                storeServerData(it)
                showClientNameDialog()
            },
            onCancel = {
                searchForServer()
            })
    }

    /**
     * confirm the found server
     *
     */
    private fun showServerFoundDialog(service: NetworkService) {

        dialogHandler.showInfoDialog(
            title = R.string.server_found_title,
            message = "Server: ${service.name}\nIP: ${service.host}\nPort: ${service.port}",
            onConfirm = {
                storeServerData("http://${service.host}:${service.port}")
                showClientNameDialog()
                viewContext.stopRefreshSpinner()
            }
        )
    }

    /**
     * load thumbnails from server
     *
     */
    private fun requestImagesFromServer() {

        dialogHandler.showLoadingDialog(
            title = R.string.server_request_images_title,
            message = R.string.server_request_images_text,
            cancelable = false
        )

        dataManager.syncHandler.requestAllImages(
            onImagesLoaded = {
                dialogHandler.dismiss()
                viewContext.stopRefreshSpinner()
                capturingEnabled.set(true)
                MainData.images.postValue(it)

                if (preferenceManager.getClientName() == null) {
                    showClientNameDialog()
                }
            },
            onError = {
                showServerNotFoundDialog()
            })
    }

    /**
     * show input dialog for manual server input
     *
     */
    private fun showClientNameDialog() {

        dialogHandler.showInputDialog(
            title = R.string.common_dialog_lumos_server_title,
            message = R.string.dialog_client_name_text,
            inputLayout = R.layout.dialog_client_name_input,
            textviewId = R.id.client_name_input_text,
            confirmBtnText = R.string.common_btn_save_title,
            denyBtnText = R.string.btn_random_name_title,
            onInput = {
                preferenceManager.setClientName(it)
                requestImagesFromServer()
            },
            onCancel = {
                preferenceManager.setClientName(UUID.randomUUID().toString())
                requestImagesFromServer()
            })
    }

    /**
     * show server not found
     *
     */
    private fun showServerNotFoundDialog() {

        dialogHandler.showConfirmDialog(
            title = R.string.search_start_title,
            message = R.string.search_error_server_not_found_anymore_text,
            confirmBtnText = R.string.alert_action_open_settings,
            onConfirm = {
                navigationHandler.openSettings(true)
            },
            denyBtnText = R.string.btn_retry,
            onDeny = {
                searchForServer()
            }
        )
    }

    /**
     * take immge from camera
     *
     */
    fun takePictureFromCamera() {

        getImage(
            sources = listOf(ImageManager.ImageSources.CAMERA),
            onImageReceived = { bitmap ->
                Log.d("Main", "Camera Image loaded")
                adapter.addItem(
                    Image(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        "tc",
                        0L,
                        Date(),
                        true,
                        imageManager.imageConverter.toBase64(bitmap, Bitmap.CompressFormat.JPEG, 100)
                    )
                )
                DataManagerImpl().syncHandler.uploadImage(
                    localImage = LocalImage(UUID.randomUUID().toString(), preferenceManager.getClientName()!!, bitmap),
                    onUploadFinished = {
                    })
            })
    }

    /**
     * take image from gallery
     *
     */
    fun takePictureFromGallery() {

        getImage(
            sources = listOf(ImageManager.ImageSources.GALLERY),
            onImageReceived = { bitmap ->
                Log.d("Main", "Camera Image loaded")
                adapter.addItem(
                    Image(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        "tc",
                        0L,
                        Date(),
                        true,
                        imageManager.imageConverter.toBase64(bitmap, Bitmap.CompressFormat.JPEG, 100)
                    )
                )
                DataManagerImpl().syncHandler.uploadImage(
                    localImage = LocalImage(UUID.randomUUID().toString(), preferenceManager.getClientName()!!, bitmap),
                    onUploadFinished = {})
            })
    }

    /**
     * delegate image request to ImageManager
     *
     */
    private fun getImage(sources: List<ImageManager.ImageSources>, onImageReceived: (Bitmap) -> Unit) {
        viewContext.getImage(sources, onImageReceived)
    }

    /**
     * save data in preferences
     *
     */
    private fun storeServerData(baseUrl: String) {
        preferenceManager.setServerAddress(baseUrl)
    }

    /**
     * refreh images and sync with server
     *
     */
    fun refresh() {
        checkRequirements()
    }

    /**
     * open settings
     *
     */
    fun openSettings() {
        navigationHandler.openSettings()
    }

    /**
     * handle image list change
     *
     */
    fun onImageListChanged(model: List<Image>?) {
        model?.let {
            adapter.items = it.toMutableList()
        }
    }
}
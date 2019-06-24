package com.grumpyshoe.lumos.feature.settings.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.grumpyshoe.lumos.R
import com.grumpyshoe.lumos.core.dagger.Injector
import com.grumpyshoe.lumos.core.data.DataManager
import com.grumpyshoe.lumos.core.data.src.preferences.PreferenceManager
import com.grumpyshoe.lumos.core.dialog.DialogHandler
import com.grumpyshoe.lumos.core.navigation.NavigationHandler
import com.grumpyshoe.lumos.feature.settings.view.ViewContext
import com.grumpyshoe.module.bonjourconnect.models.NetworkService
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class SettingsObservable(val sourceActivityFinished: Boolean, val viewContext: ViewContext) : BaseObservable() {

    @Inject
    lateinit var preferenceManager: PreferenceManager
    @Inject
    lateinit var context: Context
    @Inject
    lateinit var navigationHandler: NavigationHandler
    @Inject
    lateinit var dialogHandler: DialogHandler
    @Inject
    lateinit var dataManager: DataManager

    val clientName = ObservableField<String>()
    val lumosUrl = ObservableField<String>()

    init {

        Injector.INSTANCE.get().inject(this)

        clientName.set(preferenceManager.getClientName())
        lumosUrl.set(preferenceManager.getServerAddress())
    }

    /**
     * save settings
     *
     */
    fun save() {

        viewContext.hideKeyboard()

        if (lumosUrl.get().isNullOrEmpty() || clientName.get().isNullOrEmpty()) {
            Toast.makeText(context, R.string.settings_toast_both_required, Toast.LENGTH_SHORT).show()
        } else {
            preferenceManager.setServerAddress(lumosUrl.get()!!)
            preferenceManager.setClientName(clientName.get()!!)

            navigationHandler.openMain()
        }
    }

    /**
     * start search again
     *
     */
    fun startSearchAgain() {

        viewContext.hideKeyboard()

        dialogHandler.showLoadingDialog(
            title = R.string.search_start_title,
            message = R.string.search_start_text,
            cancelable = false
        )

        dataManager.searchForLumosServer(
            onLumosServerFound = {
                confirmServer(it)
                dialogHandler.dismiss()
            },
            onError = {
                showServerNotFoundDialog()
            }
        )
    }

    /**
     * confirm the found server
     *
     */
    private fun confirmServer(service: NetworkService) {

        dialogHandler.showInfoDialog(
            title = context.getString(R.string.search_start_title),
            message = "Server: ${service.name}\nIP: ${service.host}\nPort: :${service.port}",
            onConfirm = {

                // store server url
                storeServerData("http://${service.host}:${service.port}")

                // check if client name exists
                if (preferenceManager.getClientName() == null) {
                    showClientNameDialog()
                }
            }
        )
    }

    /**
     * show server not found
     *
     */
    private fun showServerNotFoundDialog() {

        dialogHandler.showInfoDialog(
            title = R.string.search_start_title,
            message = R.string.search_error_server_not_found_after_retry_text
        )
    }

    /**
     * store server data
     *
     */
    private fun storeServerData(baseUrl: String) {
        preferenceManager.setServerAddress(baseUrl)
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

                viewContext.hideKeyboard()
            },
            onCancel = {
                preferenceManager.setClientName(UUID.randomUUID().toString())

                viewContext.hideKeyboard()
            })
    }
}
package com.grumpyshoe.lumos.features.main

import com.grumpyshoe.lumos.R
import com.grumpyshoe.lumos.core.TestBase
import com.grumpyshoe.lumos.feature.main.view.GridAdapter
import com.grumpyshoe.lumos.feature.main.view.ViewContext
import com.grumpyshoe.lumos.feature.main.viewmodel.MainObservable
import com.grumpyshoe.module.bonjourconnect.BonjourConnect
import com.grumpyshoe.module.bonjourconnect.models.NetworkService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class MainObservableTest : TestBase() {

    lateinit var viewContext: ViewContext
    lateinit var adapter: GridAdapter

    @Before
    override fun setUp() {
        super.setUp()

        viewContext = mock()
        adapter = mock()
    }

    @Test
    fun `lumos search dialog | is shown | on first open`() {

        // define mock behavior
        whenever(preferenceManager.getServerAddress()).thenReturn(null)

        // init observable
        MainObservable(viewContext, adapter)

        // check assertions
        verify(dialogHandler, times(1)).showLoadingDialog(
            title = eq(R.string.search_start_title),
            message = eq(R.string.search_start_text),
            cancelable = eq(false),
            closePreviousExistingDialog = eq(true)
        )
    }

    @Test
    fun `lumos search | is running | on first open`() {

        // define mock behavior
        whenever(preferenceManager.getServerAddress()).thenReturn(null)

        // init observable
        MainObservable(viewContext, adapter)

        // check assertions
        verify(dataManager, times(1)).searchForLumosServer(
            onLumosServerFound = any(),
            onError = any()
        )
    }

    @Test
    fun `server confirm dialog | is shown | if server could be found`() {

        // test data
        val title = "dummyTitle"
        val service = NetworkService("testName", "testType", "testHost", 315)

        // define mock behavior
        whenever(context.getString(eq(R.string.server_found_title))).thenReturn(title)
        doAnswer {
            val onLumosServerFound = it.arguments[0] as (NetworkService) -> Unit
            onLumosServerFound(service)
        }.whenever(dataManager).searchForLumosServer(
            onLumosServerFound = any(),
            onError = any()
        )

        // init observable
        MainObservable(viewContext, adapter)

        // check assertions
        verify(dialogHandler, times(1)).showInfoDialog(
            title = eq(R.string.server_found_title),
            message = eq("Server: testName\nIP: testHost\nPort: 315"),
            cancelable = eq(false),
            confirmBtnTextResId = eq(R.string.alert_action_ok_title),
            onConfirm = any()
        )
    }

    @Test
    fun `manual server config dialog | is shown | if server could not be found`() {

        // define mock behavior
        doAnswer {
            val onError = it.arguments[1] as (BonjourConnect.ErrorType) -> Unit
            onError(BonjourConnect.ErrorType.UNKNOWN)
        }.whenever(dataManager).searchForLumosServer(
            onLumosServerFound = any(),
            onError = any()
        )

        // init observable
        MainObservable(viewContext, adapter)

        // check assertions
        verify(dialogHandler, times(1)).showConfirmDialog(
            title = eq(R.string.search_start_title),
            message = eq(R.string.search_error_no_server_found_text),
            confirmBtnText = eq(R.string.btn_server_manual),
            denyBtnText = eq(R.string.btn_retry),
            cancelable = eq(false),
            onConfirm = any(),
            onDeny = any()
        )
    }

    @Test
    fun `settings btn | at manual server config dialog | on click | navigate to settings view`() {

        // init observable
        val observable = MainObservable(viewContext, adapter)

        // trigger action
        observable.openSettings()

        // check assertions
        verify(navigationHandler, times(1)).openSettings()
    }

    @Test
    fun `username dialog | is shown | if server is confirmed`() {

        // test data
        val title = "dummyTitle"
        val service = NetworkService("testName", "testType", "testHost", 315)

        // define mock behavior
        whenever(context.getString(eq(R.string.server_found_title))).thenReturn(title)
        doAnswer {
            val onLumosServerFound = it.arguments[0] as (NetworkService) -> Unit
            onLumosServerFound(service)
        }.whenever(dataManager).searchForLumosServer(
            onLumosServerFound = any(),
            onError = any()
        )
        doAnswer { invocation ->
            val onConfirm = invocation.arguments[4] as () -> Unit
            onConfirm()
        }.whenever(dialogHandler).showInfoDialog(
            title = eq(R.string.server_found_title),
            message = eq("Server: testName\nIP: testHost\nPort: 315"),
            cancelable = eq(false),
            confirmBtnTextResId = eq(R.string.alert_action_ok_title),
            onConfirm = any()
        )

        // init observable
        MainObservable(viewContext, adapter)

        // check assertions
        verify(dialogHandler, times(1)).showInputDialog(
            title = eq(R.string.common_dialog_lumos_server_title),
            message = eq(R.string.dialog_client_name_text),
            inputLayout = eq(R.layout.dialog_client_name_input),
            textviewId = eq(R.id.client_name_input_text),
            onInput = any(),
            onCancel = any(),
            cancelable = eq(false),
            confirmBtnText = eq(R.string.common_btn_save_title),
            denyBtnText = eq(R.string.btn_random_name_title),
            closePreviousExistingDialog = eq(true)

        )
    }

    @Test
    fun `image sync dialog | is shown | after username is set`() {

        // test data
        val title = "dummyTitle"
        val service = NetworkService("testName", "testType", "testHost", 315)

        // define mock behavior
        whenever(context.getString(eq(R.string.server_found_title))).thenReturn(title)
        doAnswer {
            val onLumosServerFound = it.arguments[0] as (NetworkService) -> Unit
            onLumosServerFound(service)
        }.whenever(dataManager).searchForLumosServer(
            onLumosServerFound = any(),
            onError = any()
        )
        doAnswer { invocation ->
            val onConfirm = invocation.arguments[4] as () -> Unit
            onConfirm()
        }.whenever(dialogHandler).showInfoDialog(
            title = eq(R.string.server_found_title),
            message = eq("Server: testName\nIP: testHost\nPort: 315"),
            cancelable = eq(false),
            confirmBtnTextResId = eq(R.string.alert_action_ok_title),
            onConfirm = any()
        )
        doAnswer { invocation ->
            reset(dialogHandler)
            val onInput = invocation.arguments[4] as (String) -> Unit
            onInput("test")
        }.whenever(dialogHandler).showInputDialog(
            title = eq(R.string.common_dialog_lumos_server_title),
            message = eq(R.string.dialog_client_name_text),
            inputLayout = eq(R.layout.dialog_client_name_input),
            textviewId = eq(R.id.client_name_input_text),
            onInput = any(),
            onCancel = any(),
            cancelable = eq(false),
            confirmBtnText = eq(R.string.common_btn_save_title),
            denyBtnText = eq(R.string.btn_random_name_title),
            closePreviousExistingDialog = eq(true)

        )

        // init observable
        MainObservable(viewContext, adapter)

        // check assertions
        verify(dialogHandler, times(1)).showLoadingDialog(
            title = eq(R.string.server_request_images_title),
            message = eq(R.string.server_request_images_text),
            cancelable = eq(false),
            closePreviousExistingDialog = eq(true)
        )
        verify(dataManager.syncHandler, times(1)).requestAllImages(
            onImagesLoaded = any(),
            onError = any()
        )
    }
}
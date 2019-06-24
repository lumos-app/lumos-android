package com.grumpyshoe.lumos.core.data.impl

import android.content.Context
import com.grumpyshoe.lumos.core.dagger.Injector
import com.grumpyshoe.lumos.core.data.DataManager
import com.grumpyshoe.module.bonjourconnect.BonjourConnect
import com.grumpyshoe.module.bonjourconnect.impl.BonjourConnectImpl
import com.grumpyshoe.module.bonjourconnect.models.NetworkService
import javax.inject.Inject

/**
 * DataManagerImpl contains all logic for getting data from a webservice
 * and storing it to a local database.
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class DataManagerImpl : DataManager {

    @Inject
    override lateinit var syncHandler: DataManager.SyncHandler

    @Inject
    lateinit var context: Context

    /**
     * inject dependencies
     *
     */
    init {

        Injector.INSTANCE.get().inject(this)

        syncHandler = SyncHandlerImpl()
    }

    /**
     * search for lumos server
     *
     */
    override fun searchForLumosServer(onLumosServerFound: (NetworkService) -> Unit, onError: (BonjourConnect.ErrorType) -> Unit) {

        // use the library 'bonjourConnect' to find host
        val bonjourConnect: BonjourConnect = BonjourConnectImpl(context)
        bonjourConnect.getServiceInfo(
            type = "_lumos._tcp",
            onServiceInfoReceived = onLumosServerFound,
            onError = onError,
            searchTimeout = 5000
        )
    }
}

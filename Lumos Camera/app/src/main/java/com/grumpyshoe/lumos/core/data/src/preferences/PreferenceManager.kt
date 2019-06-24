package com.grumpyshoe.lumos.core.data.src.preferences

/**
 * interface for PreferenceManager
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
interface PreferenceManager {
    fun setServerAddress(baseUrl: String)
    fun getServerAddress(): String?
    fun getClientName(): String?
    fun setClientName(clientName: String)
}
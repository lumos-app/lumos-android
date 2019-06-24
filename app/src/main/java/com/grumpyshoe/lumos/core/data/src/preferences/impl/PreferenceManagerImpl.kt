package com.grumpyshoe.lumos.core.data.src.preferences.impl

import android.annotation.SuppressLint
import android.content.Context
import com.grumpyshoe.lumos.core.data.src.preferences.PreferenceManager

/**
 * Implementation of PreferenceManager
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class PreferenceManagerImpl(val context: Context) : PreferenceManager {

    private val preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context)

    @SuppressLint("ApplySharedPref")
    override fun setServerAddress(baseUrl: String) {
        preferences.edit().putString(SERVER_BASEURL, baseUrl).commit()
    }

    override fun getServerAddress(): String? {
        return preferences.getString(SERVER_BASEURL, null)
    }

    override fun getClientName(): String? {
        return preferences.getString(CLIENT_NAME, null)
    }

    override fun setClientName(clientName: String) {
        preferences.edit().putString(CLIENT_NAME, clientName).apply()
    }

    companion object {
        private const val SERVER_BASEURL = "SERVER_BASEURL"
        private const val CLIENT_NAME = "CLIENT_NAME"
    }
}
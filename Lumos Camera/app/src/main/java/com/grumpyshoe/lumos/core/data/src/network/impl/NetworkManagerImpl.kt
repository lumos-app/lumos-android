package com.grumpyshoe.lumos.core.data.src.network.impl

import android.content.Context
import android.util.Log
import com.grumpyshoe.lumos.core.dagger.Injector
import com.grumpyshoe.lumos.core.data.src.network.NetworkManager
import com.grumpyshoe.lumos.core.data.src.network.dto.ImageDto
import com.grumpyshoe.lumos.core.data.src.network.impl.retrofit.api.LumosBackendApi
import com.grumpyshoe.lumos.core.data.src.network.impl.retrofit.converter.nullOnEmptyConverterFactory
import com.grumpyshoe.lumos.core.data.src.network.impl.retrofit.dto.ImageUploadBody
import com.grumpyshoe.lumos.core.data.src.preferences.PreferenceManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Interface for NetworkManager
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class NetworkManagerImpl : NetworkManager {

    @Inject
    protected lateinit var preferences: PreferenceManager
    @Inject
    protected lateinit var context: Context

    private val timeout = 20L
    private var okHttpClient: OkHttpClient
    private lateinit var apiLumos: LumosBackendApi
    private lateinit var BASE_URL: String

    /**
     * init
     */
    init {

        Injector.INSTANCE.get().inject(this)

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .build()
    }

    /**
     * update API
     *
     */
    private fun updateApi() {
        BASE_URL = preferences.getServerAddress() + "/api/v1/"

        apiLumos = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LumosBackendApi::class.java)
    }

    /**
     * get Images
     *
     */
    override fun getImages(onSuccess: (List<ImageDto>) -> Unit, onError: () -> Unit) {

        try {

            updateApi()

            val response = apiLumos.getImages().execute()

            return when {
                response.errorBody() != null -> {
                    val e = UnknownError("ERROR: (getImages) " + response.errorBody().toString())
                    Log.e(javaClass.simpleName, e.message, e)
                    onError()
                }
                response.body() != null -> {
                    response.body().images?.let(onSuccess) ?: onError()
                }
                else -> onError()
            }
        } catch (e: Exception) {
            Log.e("NetworkManagerImpl", e.message, e)
            onError()
        }
    }

    /**
     * upload images
     *
     */
    override fun uploadImage(id: String, author: String, base64Image: String, onUploadFinished: () -> Unit) {

        updateApi()

        val imageUploadBody = ImageUploadBody(id, author, base64Image)
        val response = apiLumos.uploadImage(imageUploadBody).execute()

        return when {
            response.errorBody() != null -> {
                throw UnknownError("ERROR: (getImages) " + response.errorBody().toString())
            }
            response.body() != null -> {
                onUploadFinished()
            }
            else -> onUploadFinished()
        }
    }
}
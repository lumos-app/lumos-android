package com.grumpyshoe.lumos.core.data.src.network.impl.retrofit.api

import com.grumpyshoe.lumos.core.data.src.network.impl.retrofit.dto.ImageListDto
import com.grumpyshoe.lumos.core.data.src.network.impl.retrofit.dto.ImageUploadBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Retrofit Backend API
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
interface LumosBackendApi {

    /**
     * get all images
     *
     */
    @GET("images")
    fun getImages(): Call<ImageListDto>

    /**
     * upload a new image
     *
     */
    @POST("images/upload")
    fun uploadImage(@Body body: ImageUploadBody): Call<ImageListDto>
}
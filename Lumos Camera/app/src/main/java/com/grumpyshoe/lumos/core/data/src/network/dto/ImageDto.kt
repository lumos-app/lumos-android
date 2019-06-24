package com.grumpyshoe.lumos.core.data.src.network.dto

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

/**
 * Network object
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
data class ImageDto(
    val uuid: String? = null,
    val filename: String? = null,
    val uploadedFrom: String? = null,
    val totalViewCount: Long? = null,
    val createdDate: Date? = null,
    val show: Boolean? = null,
    val data: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        Date(parcel.readLong()),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString()
    )

    /**
     * check if entry is valid
     *
     */
    fun isValid(): Boolean {
        return (!uuid.isNullOrEmpty() &&
                !filename.isNullOrEmpty() &&
//                !uploadedFrom.isNullOrEmpty() &&
                totalViewCount != null &&
                createdDate != null &&
                show != null &&
                !data.isNullOrEmpty()) // TODO : add flag to see if thumbnail has been created
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uuid)
        parcel.writeString(filename)
        parcel.writeString(uploadedFrom)
        parcel.writeValue(totalViewCount)
        parcel.writeLong(createdDate?.time ?: 0L)
        parcel.writeValue(show)
        parcel.writeString(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageDto> {
        override fun createFromParcel(parcel: Parcel): ImageDto {
            return ImageDto(parcel)
        }

        override fun newArray(size: Int): Array<ImageDto?> {
            return arrayOfNulls(size)
        }
    }
}
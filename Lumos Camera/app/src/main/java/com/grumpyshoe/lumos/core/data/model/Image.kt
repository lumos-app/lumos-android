package com.grumpyshoe.lumos.core.data.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

/**
 * Model for representing 'Image'
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class Image(
    val localId: String,
    val serverId: String,
    val filename: String,
    val uploadedFrom: String,
    val totalViewCount: Long,
    val createdDate: Date,
    val show: Boolean,
    val data: String? = null
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readLong(),
        source.readSerializable() as Date,
        1 == source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(localId)
        writeString(serverId)
        writeString(filename)
        writeString(uploadedFrom)
        writeLong(totalViewCount)
        writeSerializable(createdDate)
        writeInt((if (show) 1 else 0))
        writeString(data)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Image> = object : Parcelable.Creator<Image> {
            override fun createFromParcel(source: Parcel): Image = Image(source)
            override fun newArray(size: Int): Array<Image?> = arrayOfNulls(size)
        }
    }
}
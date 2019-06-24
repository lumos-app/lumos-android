package com.grumpyshoe.lumos.feature.main.view

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.grumpyshoe.lumos.R
import com.grumpyshoe.lumos.core.data.model.Image
import org.jetbrains.anko.find

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class GridAdapter : RecyclerView.Adapter<VH>() {

    var items: MutableList<Image> = mutableListOf()
        set(value) {
            field = value
            Handler(Looper.getMainLooper()).post { notifyDataSetChanged() }
        }

    /**
     * add item to start of list
     *
     */
    fun addItem(image: Image) {
        val pos = items.size
        items.add(0, image)
        Handler(Looper.getMainLooper()).post { notifyDataSetChanged() }
    }

    /**
     * create viewholder
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_view, parent, false)
        return VH(view)
    }

    /**
     * bind data to viewholder
     *
     */
    override fun onBindViewHolder(holder: VH, position: Int) {
        if (position < items.size) {
            val v = holder.itemView.find<ImageView>(R.id.image_item)
            val decodedString = Base64.decode(items[position].data, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            holder.image = items[position]
            v.setImageBitmap(decodedByte)
            ViewCompat.setTransitionName(v, items[position].localId)
        }
    }

    /**
     * get item count
     *
     */
    override fun getItemCount(): Int {
        return items.size
    }
}

class VH(val view: View) : RecyclerView.ViewHolder(view) {
    var image: Image? = null
}
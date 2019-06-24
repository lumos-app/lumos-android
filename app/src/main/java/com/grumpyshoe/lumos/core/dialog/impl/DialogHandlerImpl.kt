package com.grumpyshoe.lumos.core.dialog.impl

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.grumpyshoe.lumos.R
import com.grumpyshoe.lumos.core.dialog.DialogHandler
import org.jetbrains.anko.find

/**
 * interface for PreferenceManager
 *
 * The DialogHandlerImpl is the centralized point for showing dialogs.
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
object DialogHandlerImpl : DialogHandler {

    private var activity: AppCompatActivity? = null
    private var dialog: AlertDialog? = null

    override fun setActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

    override fun showInfoDialog(
        title: Any,
        message: Any,
        cancelable: Boolean,
        confirmBtnTextResId: Int,
        onConfirm: (() -> Unit)?
    ) {

        if (activity == null) {
            throw NullPointerException("Dialog could not be shown because 'activity', 'title' or 'message' is null!")
        }

        dialog?.dismiss()

        activity?.let {

            val titleString = when (title) {
                is String -> title
                is Int -> activity?.getString(title)
                else -> throw IllegalArgumentException("Unsupported type")
            }

            val messageString = when (message) {
                is String -> message
                is Int -> activity?.getString(message)
                else -> throw IllegalArgumentException("Unsupported type")
            }

            val d = AlertDialog.Builder(it, R.style.AlertDialogTheme)
                .setTitle(titleString)
                .setCancelable(cancelable)
                .setMessage(messageString)
                .setPositiveButton(confirmBtnTextResId) { dialog, _ ->
                    dialog.dismiss()
                    onConfirm?.invoke()
                }

            Handler(Looper.getMainLooper()).post {
                dialog = d.create()
                dialog!!.show()
            }
        }
    }

    override fun showConfirmDialog(
        title: Any,
        message: Any,
        confirmBtnText: Any,
        denyBtnText: Any,
        cancelable: Boolean,
        onConfirm: () -> Unit,
        onDeny: (() -> Unit)?
    ) {

        dialog?.dismiss()

        if (activity == null) {
            throw NullPointerException("Dialog could not be shown because 'activity', 'title' or 'message' is null!")
        }

        val titleString = when (title) {
            is String -> title
            is Int -> activity?.getString(title)
            else -> throw IllegalArgumentException("Unsupported type")
        }

        val messageString = when (message) {
            is String -> message
            is Int -> activity?.getString(message)
            else -> throw IllegalArgumentException("Unsupported type")
        }

        val confirmString = when (confirmBtnText) {
            is String -> confirmBtnText
            is Int -> activity?.getString(confirmBtnText)
            else -> throw IllegalArgumentException("Unsupported type")
        }

        val denyString = when (denyBtnText) {
            is String -> denyBtnText
            is Int -> activity?.getString(denyBtnText)
            else -> throw IllegalArgumentException("Unsupported type")
        }

        activity?.let {

            val d = AlertDialog.Builder(it, R.style.AlertDialogTheme)
                .setTitle(titleString)
                .setMessage(messageString)
                .setCancelable(cancelable)
                .setPositiveButton(confirmString) { dialog, _ ->
                    dialog.dismiss()
                    onConfirm()
                }
                .setNegativeButton(denyString) { dialog, _ ->
                    dialog.dismiss()
                    onDeny?.invoke()
                }

            Handler(Looper.getMainLooper()).post {

                dialog = d.create()
                dialog!!.show()
            }
        }
    }

    override fun showLoadingDialog(
        title: Any,
        message: Any,
        cancelable: Boolean,
        closePreviousExistingDialog: Boolean
    ) {

        dialog?.dismiss()

        if (activity == null) {
            throw NullPointerException("Dialog could not be shown because 'activity', 'title' or 'message' is null!")
        }

        val titleString = when (title) {
            is String -> title
            is Int -> activity?.getString(title)
            else -> throw IllegalArgumentException("Unsupported type")
        }

        val messageString = when (message) {
            is String -> message
            is Int -> activity?.getString(message)
            else -> throw IllegalArgumentException("Unsupported type")
        }

        activity?.let {

            val d = AlertDialog.Builder(it, R.style.AlertDialogTheme)
                .setTitle(titleString)
                .setCancelable(cancelable)
                .setView(R.layout.dialog_loading)
                .setMessage(messageString)

            Handler(Looper.getMainLooper()).post {

                dialog = d.create()
                dialog!!.show()
            }
        }
    }

    override fun showInputDialog(
        title: Any,
        message: Any?,
        @LayoutRes inputLayout: Int,
        textviewId: Int,
        onInput: (String) -> Unit,
        onCancel: () -> Unit,
        cancelable: Boolean,
        confirmBtnText: Any,
        denyBtnText: Any,
        closePreviousExistingDialog: Boolean
    ) {

        dialog?.dismiss()

        if (activity == null) {
            throw NullPointerException("Dialog could not be shown because 'activity', 'title' or 'message' is null!")
        }

        val titleString = when (title) {
            is String -> title
            is Int -> activity?.getString(title)
            else -> throw IllegalArgumentException("Unsupported type")
        }

        val messageString = when (message) {
            is String -> message
            is Int -> activity?.getString(message)
            else -> null
        }

        val confirmString = when (confirmBtnText) {
            is String -> confirmBtnText
            is Int -> activity?.getString(confirmBtnText)
            else -> throw IllegalArgumentException("Unsupported type")
        }

        val denyString = when (denyBtnText) {
            is String -> denyBtnText
            is Int -> activity?.getString(denyBtnText)
            else -> throw IllegalArgumentException("Unsupported type")
        }

        activity?.let {

            val builder = AlertDialog.Builder(it, R.style.AlertDialogTheme)
            builder.setTitle(titleString)

            messageString?.let {
                builder.setMessage(messageString)
            }
            builder.setCancelable(cancelable)

            val view = LayoutInflater.from(activity).inflate(inputLayout, null, false)

            builder.setView(view)

            builder.setPositiveButton(confirmString) { dialog, _ ->
                dialog.dismiss()
                onInput(view.find<TextInputEditText>(textviewId).text.toString())
            }
            builder.setNegativeButton(denyString) { dialog, _ ->
                dialog.dismiss()
                onCancel()
            }

            Handler(Looper.getMainLooper()).post {

                dialog = builder.create()
                dialog!!.show()
            }
        }
    }

    override fun dismiss() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }
}
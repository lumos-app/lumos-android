package com.grumpyshoe.lumos.core.dialog

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.grumpyshoe.lumos.R

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
interface DialogHandler {

    fun setActivity(activity: AppCompatActivity)

    fun showInfoDialog(
        title: Any,
        message: Any,
        cancelable: Boolean = false,
        confirmBtnTextResId: Int = R.string.alert_action_ok_title,
        onConfirm: (() -> Unit)? = null
    )

    fun showConfirmDialog(
        title: Any,
        message: Any,
        confirmBtnText: Any = R.string.alert_action_ok_title,
        denyBtnText: Any = R.string.alert_action_ok_title,
        cancelable: Boolean = false,
        onConfirm: () -> Unit,
        onDeny: (() -> Unit)? = null
    )

    fun showLoadingDialog(
        title: Any,
        message: Any,
        cancelable: Boolean = false,
        closePreviousExistingDialog: Boolean = true
    )

    fun showInputDialog(
        title: Any,
        message: Any? = null,
        @LayoutRes inputLayout: Int,
        textviewId: Int,
        onInput: (String) -> Unit,
        onCancel: () -> Unit,
        cancelable: Boolean = false,
        confirmBtnText: Any = R.string.alert_action_ok_title,
        denyBtnText: Any = R.string.alert_action_cancel_title,
        closePreviousExistingDialog: Boolean = true
    )

    fun dismiss()
}
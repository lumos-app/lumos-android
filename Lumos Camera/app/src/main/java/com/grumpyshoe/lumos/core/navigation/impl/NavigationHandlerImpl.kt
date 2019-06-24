package com.grumpyshoe.lumos.core.navigation.impl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.grumpyshoe.lumos.R
import com.grumpyshoe.lumos.core.navigation.NavigationHandler
import com.grumpyshoe.lumos.feature.main.view.MainActivity
import com.grumpyshoe.lumos.feature.settings.view.SettingsActivity

/**
 * The NavigationHandlerImpl is the centralized point of contact for navigation within the app.
 *
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class NavigationHandlerImpl : NavigationHandler {

    var activity: AppCompatActivity? = null

    /**
     * set objects for navigation handling.
     * This method needs at least to be called on every activities 'onResume' to be sure the correct
     * context is taking care of navigation.
     * If there shoudl be a fragment navigation on you init then this method needs to be called before.
     *
     * @param activity - the activity that should handle the current navigation
     *
     */
    override fun setNavigationObjects(activity: AppCompatActivity) {

        this.activity = activity
    }

    /**
     * open settings
     *
     */
    override fun openSettings(finishPreviousActivity: Boolean) {
        val intent = Intent(activity, SettingsActivity::class.java)
        SettingsActivity.addExtra(intent, finishPreviousActivity)
        navigateToActivity(intent, finishPreviousActivity)
    }

    /**
     * open main
     *
     */
    override fun openMain() {
        val intent = Intent(activity, MainActivity::class.java)
        navigateToActivity(intent)
    }

    /**
     * central method for activity navigation
     *
     * @param targetIntent - the target intent
     * @param finishSource - flag that indicates if the source activity should be finished or not (default: false)
     */
    private fun navigateToActivity(targetIntent: Intent, finishSource: Boolean = false) {

        activity?.let {
            it.startActivity(targetIntent)
            if (finishSource) {
                it.parent?.finish()
                it.finish()
            }
            it.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        } ?: throw NullPointerException("No Navigation possible because 'activity' is null!")
    }
}
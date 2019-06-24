package com.grumpyshoe.lumos.feature.settings.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.grumpyshoe.lumos.R
import com.grumpyshoe.lumos.core.dagger.Injector
import com.grumpyshoe.lumos.core.dialog.DialogHandler
import com.grumpyshoe.lumos.core.navigation.NavigationHandler
import com.grumpyshoe.lumos.databinding.ActivitySettingsBinding
import com.grumpyshoe.lumos.feature.settings.viewmodel.SettingsViewModel
import com.grumpyshoe.lumos.feature.settings.viewmodel.SettingsViewModelFactory
import javax.inject.Inject

/**
 * Created by Thomas Cirksena on 10.06.19.
 * Copyright © 2019 Thomas Cirksena. All rights reserved.
 */
class SettingsActivity : AppCompatActivity(), ViewContext {

    @Inject
    lateinit var navigationHandler: NavigationHandler
    @Inject
    lateinit var dialogHandler: DialogHandler

    /**
     * handle onCreate
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.INSTANCE.get().inject(this)

        // init DialogHandler
        dialogHandler.setActivity(this)

        // init NavigationHandler
        navigationHandler.setNavigationObjects(this)

        // get id of vehicle
        val bundle = intent?.extras
        if (bundle?.getBoolean(ARG_SOURCE_ACTIVITY_FINISHED) == null) {
            throw IllegalArgumentException("Couldn't find vehicle in bundle")
        }
        val sourceActivityFinished = bundle.getBoolean(ARG_SOURCE_ACTIVITY_FINISHED)

        val binding = DataBindingUtil.setContentView<ActivitySettingsBinding>(this, R.layout.activity_settings)
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        if (!sourceActivityFinished) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        // init viewModel
        val viewModel = ViewModelProviders.of(this, SettingsViewModelFactory(sourceActivityFinished, this)).get(SettingsViewModel::class.java)

        // bind observable
        binding.observable = viewModel.observable
    }

    /**
     * handle onResume
     *
     */
    override fun onResume() {
        super.onResume()
        navigationHandler.setNavigationObjects(this)
        dialogHandler.setActivity(this)
    }

    /**
     * hide softkeyboard
     *
     */
    override fun hideKeyboard() {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        // Find the currently focused view, so we can grab the correct window token from it.
        var view = this.currentFocus
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    companion object {

        private const val ARG_SOURCE_ACTIVITY_FINISHED = "ARG_SOURCE_ACTIVITY_FINISHED"

        /**
         * add extras for activity
         *
         */
        fun addExtra(
            intent: Intent,
            sourceActivityFinished: Boolean
        ) {
            intent.putExtra(ARG_SOURCE_ACTIVITY_FINISHED, sourceActivityFinished)
        }
    }
}

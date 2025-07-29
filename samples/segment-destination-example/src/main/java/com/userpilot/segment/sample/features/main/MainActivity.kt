package com.userpilot.segment.sample.features.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.userpilot.segment.sample.base.BaseActivity
import com.userpilot.segment.sample.databinding.ActivityMainBinding
import com.userpilot.segment.sample.features.events.CustomEventActivity
import com.userpilot.segment.sample.features.identify.IdentifyActivity
import com.userpilot.segment.sample.features.main.adapter.OptionsAdapter
import com.userpilot.segment.sample.features.main.models.Content
import com.userpilot.segment.sample.features.screens.ScreenOneActivity
import com.userpilot.segment.sample.managers.SegmentManager

/**
 * Created by Motasem Hamed
 * on 28 Aug, 2024
 */
class MainActivity : BaseActivity() {

    //region Properties
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val optionsAdapter: OptionsAdapter by lazy { OptionsAdapter(contents, ::onOptionItemClickListener) }
    private val contents: List<Content> by lazy {
        mutableListOf(
            Content.IDENTIFY,
            Content.SCREENS,
            Content.EVENTS
        )
    }
    //endregion

    //region override
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            rvOptions.adapter = optionsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        SegmentManager.screen("main")
    }
    //endregion

    //region Click Recycle view
    private fun onOptionItemClickListener(position: Int) {
        when (optionsAdapter.getItems()[position]) {
            Content.IDENTIFY -> {
                IdentifyActivity.start(this)
            }
            Content.SCREENS -> {
                ScreenOneActivity.start(this)
            }
            Content.EVENTS -> {
                CustomEventActivity.start(this)
            }
        }
    }
    //endregion
}

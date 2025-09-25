package com.userpilot.segment.sample

import android.app.Application
import com.userpilot.segment.sample.managers.SegmentManager

/**
 * Created by Motasem Hamed
 * on 20 Aug, 2024
 */

class UserpilotSample : Application() {

    override fun onCreate() {
        super.onCreate()

        setupUserpilotSDK()
    }

    private fun setupUserpilotSDK() {
        SegmentManager.initialize(this)
    }
}

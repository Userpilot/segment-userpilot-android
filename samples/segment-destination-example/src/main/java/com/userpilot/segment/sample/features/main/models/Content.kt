package com.userpilot.segment.sample.features.main.models

import android.content.Context
import com.userpilot.segment.sample.R

/**
 * Created by Motasem Hamed
 * on 01 Sep, 2024
 */

enum class Content(private val titleResId: Int) {
    IDENTIFY(R.string.identify_title),
    SCREENS(R.string.screens_title),
    EVENTS(R.string.events_title);

    fun getTitle(context: Context): String {
        return context.getString(titleResId)
    }
}

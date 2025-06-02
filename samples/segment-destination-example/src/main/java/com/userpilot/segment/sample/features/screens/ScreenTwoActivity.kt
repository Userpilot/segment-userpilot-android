package com.userpilot.segment.sample.features.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.userpilot.segment.sample.base.BaseActivity
import com.userpilot.segment.sample.databinding.ActivityScreenTwoBinding
import com.userpilot.segment.sample.managers.SegmentManager

/**
 * Created by Motasem Hamed
 * on 03 Sep, 2024
 */

class ScreenTwoActivity : BaseActivity() {

    //region Properties
    private val binding by lazy { ActivityScreenTwoBinding.inflate(layoutInflater) }
    //endregion

    //region override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        SegmentManager.screen("screen two")
    }
    //endregion

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ScreenTwoActivity::class.java)
            context.startActivity(starter)
        }
    }
}

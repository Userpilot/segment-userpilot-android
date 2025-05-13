package com.userpilot.segment.sample.features.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.userpilot.segment.sample.base.BaseActivity
import com.userpilot.segment.sample.databinding.ActivityScreenOneBinding
import com.userpilot.segment.sample.managers.UserpilotManager

/**
 * Created by Motasem Hamed
 * on 03 Sep, 2024
 */

class ScreenOneActivity : BaseActivity() {

    //region Properties
    private val binding by lazy { ActivityScreenOneBinding.inflate(layoutInflater) }
    //endregion

    //region override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }
        binding.btnNext.setOnClickListener { ScreenTwoActivity.start(this) }
    }

    override fun onResume() {
        super.onResume()
        UserpilotManager.screen("screen one")
    }
    //endregion

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ScreenOneActivity::class.java)
            context.startActivity(starter)
        }
    }
}

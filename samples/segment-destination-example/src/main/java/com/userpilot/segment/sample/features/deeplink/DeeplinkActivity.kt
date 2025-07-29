package com.userpilot.segment.sample.features.deeplink

import android.os.Bundle
import com.userpilot.segment.sample.base.BaseActivity
import com.userpilot.segment.sample.databinding.ActivityDeeplinkBinding

/**
 * Created by Motasem Hamed
 * on 24 Sep, 2024
 */
class DeeplinkActivity : BaseActivity() {

    //region Properties
    private val binding by lazy { ActivityDeeplinkBinding.inflate(layoutInflater) }

    //region override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }
    }
}

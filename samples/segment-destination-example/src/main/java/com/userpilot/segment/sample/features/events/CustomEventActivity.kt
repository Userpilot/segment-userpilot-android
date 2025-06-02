package com.userpilot.segment.sample.features.events

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.userpilot.segment.sample.base.BaseActivity
import com.userpilot.segment.sample.base.adatper.PropertiesAdapter
import com.userpilot.segment.sample.base.adatper.PropertiesAdapterInterface
import com.userpilot.segment.sample.databinding.ActivityCustomEventBinding
import com.userpilot.segment.sample.dialogs.AddPropertyDialog
import com.userpilot.segment.sample.extensions.getKeyValueByIndex
import com.userpilot.segment.sample.managers.SegmentManager

/**
 * Created by Motasem Hamed
 * on 16 09, 2024
 */
class CustomEventActivity : BaseActivity() {

    //region Properties
    private val binding by lazy { ActivityCustomEventBinding.inflate(layoutInflater) }

    private val eventPropertiesAdapter: PropertiesAdapter by lazy {
        PropertiesAdapter(
            mutableMapOf(),
            eventPropertiesAdapterInterface
        )
    }
    //endregion

    //region override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
    }

    override fun onResume() {
        super.onResume()
        SegmentManager.screen("custom events")
    }
    //endregion

    //region listener
    private val eventPropertiesAdapterInterface = object : PropertiesAdapterInterface {
        override fun onEditProperty(position: Int) {
            eventPropertiesAdapter.getProperties().getKeyValueByIndex(position)?.let {
                addUserProperty(it.first, it.second.toString())
            }
        }

        override fun onDeleteProperty(position: Int) {
            eventPropertiesAdapter.deleteProperty(position)
        }
    }
    //endregion

    //region add property
    private fun addUserProperty(cachedTitle: String = "", cachedValue: String = "") {
        AddPropertyDialog.newInstance(TAG, "AddPropertyDialog", cachedTitle, cachedValue)
            .show(supportFragmentManager) { title, value ->
                eventPropertiesAdapter.onNewProperty(title, value)
            }
    }
    //endregion

    //region setup views
    private fun setupViews() {
        with(binding) {
            btnTrackEvent.setOnClickListener {
                if (etEventName.text.toString().isNotEmpty()) {
                    SegmentManager.track(
                        etEventName.text.toString(),
                        eventPropertiesAdapter.getProperties()
                    )
                } else {
                    Toast.makeText(this@CustomEventActivity, "Event name is required", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            rvEventProperties.adapter = eventPropertiesAdapter

            ivAddEventProperty.setOnClickListener { addUserProperty() }
            ivBack.setOnClickListener { finish() }
        }
    }
    //endregion

    companion object {
        val TAG: String = CustomEventActivity::class.java.simpleName

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, CustomEventActivity::class.java)
            context.startActivity(starter)
        }
    }
}

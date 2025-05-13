package com.userpilot.segment.sample.features.identify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.userpilot.segment.sample.base.BaseActivity
import com.userpilot.segment.sample.base.adatper.PropertiesAdapter
import com.userpilot.segment.sample.base.adatper.PropertiesAdapterInterface
import com.userpilot.segment.sample.databinding.ActivityIdentifyBinding
import com.userpilot.segment.sample.dialogs.AddPropertyDialog
import com.userpilot.segment.sample.extensions.getKeyValueByIndex
import com.userpilot.segment.sample.managers.UserpilotManager
import com.userpilot.segment.sample.utils.showAlertDialog

/**
 * Created by Motasem Hamed
 * on 12 Sep, 2024
 */

typealias Properties = MutableMap<String, Any>

class IdentifyActivity : BaseActivity() {

    //region Properties
    private val binding by lazy { ActivityIdentifyBinding.inflate(layoutInflater) }

    private val userPropertiesAdapter: PropertiesAdapter by lazy {
        PropertiesAdapter(
            mutableMapOf(),
            userPropertiesAdapterInterface
        )
    }
    private val userCompanyPropertiesAdapter: PropertiesAdapter by lazy {
        PropertiesAdapter(
            mutableMapOf(),
            userCompanyPropertiesAdapterInterface
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
        UserpilotManager.screen("identify")
    }
    //endregion

    //region listener
    private val userPropertiesAdapterInterface = object : PropertiesAdapterInterface {
        override fun onEditProperty(position: Int) {
            userPropertiesAdapter.getProperties().getKeyValueByIndex(position)?.let {
                addUserProperty(it.first, it.second.toString())
            }
        }

        override fun onDeleteProperty(position: Int) {
            userPropertiesAdapter.deleteProperty(position)
        }
    }

    private val userCompanyPropertiesAdapterInterface = object : PropertiesAdapterInterface {
        override fun onEditProperty(position: Int) {
            userCompanyPropertiesAdapter.getProperties().getKeyValueByIndex(position)?.let {
                addUserCompanyProperty(it.first, it.second.toString())
            }
        }

        override fun onDeleteProperty(position: Int) {
            userCompanyPropertiesAdapter.deleteProperty(position)
        }
    }
    //endregion

    //region add property
    private fun addUserProperty(cachedTitle: String = "", cachedValue: String = "") {
        AddPropertyDialog.newInstance(TAG, "AddPropertyDialog", cachedTitle, cachedValue)
            .show(supportFragmentManager) { title, value ->
                userPropertiesAdapter.onNewProperty(title, value)
            }
    }

    private fun addUserCompanyProperty(cachedTitle: String = "", cachedValue: String = "") {
        AddPropertyDialog.newInstance(TAG, "AddPropertyDialog", cachedTitle, cachedValue)
            .show(supportFragmentManager) { title, value ->
                userCompanyPropertiesAdapter.onNewProperty(title, value)
            }
    }
    //endregion

    //endregion

    //region setup views
    private fun setupViews() {
        with(binding) {
            btnIdentify.setOnClickListener {
                if (etUserId.text.toString().isNotEmpty()) {
                    val userProperties = userPropertiesAdapter.getProperties()
                    val companyProperties = userCompanyPropertiesAdapter.getProperties()

                    val properties: HashMap<String, Any> = hashMapOf()
                    properties.putAll(userProperties)

                    // Only add "company" key if it's not empty
                    if (companyProperties.isNotEmpty()) {
                        properties["company"] = companyProperties
                    }

                    UserpilotManager.identify(
                        etUserId.text.toString(),
                        properties
                    )
                } else {
                    showAlertDialog(this@IdentifyActivity, "Please insert User ID!")
                }
            }
            btnAnonymous.setOnClickListener {
                UserpilotManager.anonymous()
            }
            btnLogout.setOnClickListener {
                showAlertDialog(this@IdentifyActivity, "User logged out successfully!")
                UserpilotManager.logout()
            }

            rvUserProperties.adapter = userPropertiesAdapter
            rvUserCompanyProperties.adapter = userCompanyPropertiesAdapter

            ivAddUserProperty.setOnClickListener { addUserProperty() }
            ivAddUserCompanyProperty.setOnClickListener { addUserCompanyProperty() }
            ivBack.setOnClickListener { finish() }
        }
    }
    //endregion

    companion object {
        val TAG: String = IdentifyActivity::class.java.simpleName

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, IdentifyActivity::class.java)
            context.startActivity(starter)
        }
    }
}

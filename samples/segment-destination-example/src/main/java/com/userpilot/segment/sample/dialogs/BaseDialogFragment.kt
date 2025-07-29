package com.userpilot.segment.sample.dialogs

import android.content.res.Resources
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment

/**
 * Created by Motasem Hamed
 * on 12 Sep, 2024
 */
open class BaseDialogFragment : DialogFragment() {

    @Suppress("MagicNumber")
    override fun onResume() {
        if (dialog != null && dialog!!.window != null) {
            val params = dialog!!.window!!.attributes
            params.width = Resources.getSystem().displayMetrics.widthPixels - 100
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        }
        super.onResume()
    }
}

package com.userpilot.segment.sample.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.FragmentManager
import com.userpilot.segment.sample.R
import com.userpilot.segment.sample.databinding.DialogAddPropertyBinding

/**
 * Created by Motasem Hamed
 * on 12 Sep, 2024
 */
class AddPropertyDialog : BaseDialogFragment() {

    private var _binding: DialogAddPropertyBinding? = null
    private val binding get() = _binding!!

    private lateinit var callback: (String, String) -> Unit

    companion object {
        var didShowAlertDialog = false
        private const val EXTRA_DIALOG_TAG = "EXTRA_DIALOG_TAG"
        private const val EXTRA_DIALOG_CALLER_TAG = "EXTRA_DIALOG_CALLER_TAG"
        private const val EXTRA_DIALOG_PROPERTY_TITLE = "EXTRA_DIALOG_PROPERTY_TITLE"
        private const val EXTRA_DIALOG_PROPERTY_VALUE = "EXTRA_DIALOG_PROPERTY_VALUE"

        @JvmStatic
        fun newInstance(
            callerTag: String,
            dialogTag: String,
            propertyTitle: String,
            propertyValue: String,
        ) =
            AddPropertyDialog().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_DIALOG_CALLER_TAG, callerTag)
                    putString(EXTRA_DIALOG_TAG, dialogTag)
                    putString(EXTRA_DIALOG_PROPERTY_TITLE, propertyTitle)
                    putString(EXTRA_DIALOG_PROPERTY_VALUE, propertyValue)
                }
                didShowAlertDialog = true
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddPropertyBinding.inflate(LayoutInflater.from(context))
        return Dialog(requireContext(), R.style.SampleDialogStyle).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getString(com.userpilot.segment.sample.dialogs.AddPropertyDialog.Companion.EXTRA_DIALOG_PROPERTY_TITLE)
                ?.let { title -> binding.etTitle.setText(title) }
            it.getString(com.userpilot.segment.sample.dialogs.AddPropertyDialog.Companion.EXTRA_DIALOG_PROPERTY_VALUE)
                ?.let { value -> binding.etValue.setText(value) }

            binding.btnAdd.setOnClickListener { onConfirmationButtonClicked() }
            binding.btnCancel.setOnClickListener { dismissDialog() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        com.userpilot.segment.sample.dialogs.AddPropertyDialog.Companion.didShowAlertDialog = false
        _binding = null
    }

    private fun onConfirmationButtonClicked() {
        if (::callback.isInitialized && binding.etTitle.text.toString()
                .isNotEmpty() && binding.etValue.text.toString().isNotEmpty()
        ) {
            callback(binding.etTitle.text.toString(), binding.etValue.text.toString())
        }
        dismiss()
    }

    private fun dismissDialog() {
        dismiss()
    }

    fun show(manager: FragmentManager, callback: (String, String) -> Unit) {
        this.callback = callback
        super.show(manager, tag)
    }
}

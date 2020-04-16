package com.gateway.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.gateway.android.R
import com.gateway.android.utils.SharedPreferencesWrapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_edit_text_bottom_sheet.*

class EditTextBottomSheetFragment : BottomSheetDialogFragment() {

    override fun getTheme() = R.style.AppTheme_BottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        layoutInflater.inflate(R.layout.fragment_edit_text_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textInputView.hint = getString(R.string.hint_server_url)
        textInputView.text = SharedPreferencesWrapper.getInstance().baseUrl

        cancelButton.setOnClickListener {
            dismiss()
        }

        saveButton.setOnClickListener {
            SharedPreferencesWrapper.getInstance().baseUrl = textInputView.text
            Toast.makeText(context, getString(R.string.restart_app), Toast.LENGTH_LONG).show()
            dismiss()
        }

        textInputView.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveButton.performClick()
                true
            } else {
                false
            }
        })

        textInputView.requestFocus()
    }

    companion object {
        fun newInstance() = EditTextBottomSheetFragment()
    }
}

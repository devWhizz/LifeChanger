package com.example.lifechanger.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lifechanger.R
import com.example.lifechanger.databinding.FragmentAboutBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AboutBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAboutBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeButton = binding.closeBTN
        closeButton.setOnClickListener {
            dismiss()
        }

        val feedbackButton = binding.feedbackBTN
        feedbackButton.setOnClickListener {
            val emailAddress = "example@example.com"
            val subject = "LifeChanger App | Feedback"

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "message/rfc822" // MIME-Typ for Emails
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

            // check if email application is installed on device
            if (emailIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(emailIntent)
            } else {
                showErrorToast()
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(
            requireContext(),
            (getString(R.string.errorMessageEmail)),
            Toast.LENGTH_SHORT
        ).show()
    }
}
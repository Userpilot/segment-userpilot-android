package com.userpilot.segment.sample.utils

import android.app.AlertDialog
import android.content.Context

/**
 * Created by Motasem Hamed
 * on 17 Nov, 2024
 */

fun showAlertDialog(context: Context, message: String) {
    // Create the AlertDialog Builder
    val builder = AlertDialog.Builder(context)

    // Set the title and message
    builder.setTitle("Userpilot")
    builder.setMessage(message)

    // Set the "OK" button action
    builder.setPositiveButton("OK") { dialog, _ ->
        // Action for "OK" button tap
        dialog.dismiss() // Close the dialog
    }

    // Create and show the alert dialog
    val alertDialog = builder.create()
    alertDialog.show()
}

package vn.tutorial.simplealarmandroid.components

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import vn.tutorial.simplealarmandroid.R

object CommonComponents {
    fun confirmDialog(context: Context, title: String, message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(R.string.confirm) { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    fun toastText(context: Context, message: String) {
        Toast.makeText(
            context, message, Toast.LENGTH_SHORT
        ).show()
    }
}
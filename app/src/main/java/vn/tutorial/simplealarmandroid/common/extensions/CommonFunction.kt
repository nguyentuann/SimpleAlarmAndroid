package vn.tutorial.simplealarmandroid.common.extensions

import android.app.AlertDialog
import android.content.Context

object CommonFunction {
    fun showAlertDialog(context: Context, title: String, message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Confirm") { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }
}
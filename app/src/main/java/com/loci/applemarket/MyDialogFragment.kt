package com.loci.applemarket

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment


class MyDialogFragment(
    private val title: String,
    private val message: String,
    private val icon: Int,
    private val type: String,
    private val position: Int
) : DialogFragment() {

    interface DialogListener{
        fun positiveClick(position: Int)
    }
    private var dialogListener: DialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mainActivity = activity as MainActivity
        return mainActivity.let {
            val builder = AlertDialog.Builder(it)

            when (type) {
                "EXIT" -> {
                    builder
                        .setTitle(title)
                        .setMessage(message)
                        .setIcon(icon)
                        .setPositiveButton("확인") { dialog, id ->
                            mainActivity.finish()
                        }
                        .setNegativeButton("취소") { dialog, id ->
                            dismiss()
                        }
                }

                "REMOVE" -> {
                    builder
                        .setTitle(title)
                        .setMessage(message)
                        .setIcon(icon)
                        .setPositiveButton("확인") { dialog, id ->
                            dialogListener?.positiveClick(position)
                        }
                        .setNegativeButton("취소") { dialog, id ->
                            dismiss()
                        }

                }
            }

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
package com.loci.applemarket

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment


class MyDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mainActivity = activity as MainActivity
        return mainActivity.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage("정말 종료하시겠습니까?")
                .setTitle("종료")
                .setIcon(R.drawable.chat)
                .setPositiveButton("확인") { dialog, id ->
                    mainActivity.finish()
                }
                .setNegativeButton("취소") { dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
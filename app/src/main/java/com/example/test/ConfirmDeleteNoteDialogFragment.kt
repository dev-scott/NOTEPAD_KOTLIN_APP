package com.example.test

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmDeleteNoteDialogFragment(val noteTitle : String?="") : DialogFragment() {

    interface ConfirmDeleteDialogListener{
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    var listener:ConfirmDeleteDialogListener?= null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    var builder = AlertDialog.Builder(activity)

        builder.setMessage("Etes vous sur de supprimer la note \"$noteTitle\"?")
            .setPositiveButton("Supprimer",
            DialogInterface.OnClickListener{dialog, id -> listener?.onDialogPositiveClick()  })
            .setNegativeButton("Annuler", DialogInterface.OnClickListener{dialog,id->listener?.onDialogNegativeClick()})

        return builder.create()
    }


}

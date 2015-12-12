package dfgden.pxart.com.pxart.dialogs;


import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import dfgden.pxart.com.pxart.R;

public class ClearDialog extends DialogFragment implements DialogInterface.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.cleardialog_title))
                .setPositiveButton("OK", this)
                        .setNegativeButton("NO", this)
                                .create();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}

package com.example.planes.Interface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import com.example.planes.R;

/**
 * Created by egor on 23.08.15.
 */
public class DialogHelper {
    public static void showOKDialog(final Activity ctx, String title, String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(msg).setTitle(title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        ctx.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!ctx.isFinishing()) {
                    builder.create().show();
                }
            }
        });
    }

    public static ProgressDialog showProgressDialog(final Activity act, String title, String msg, DialogInterface.OnCancelListener listener){
        final ProgressDialog dlg = ProgressDialog.show(act, title, msg, true);
        if(listener != null) {
            dlg.setCancelable(false);

            dlg.setOnCancelListener(listener);
        }
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!act.isFinishing()) {
                    dlg.show();
                }
            }
        });

        return dlg;
    }


}

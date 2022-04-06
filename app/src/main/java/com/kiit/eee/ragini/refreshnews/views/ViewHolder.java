package com.kiit.eee.ragini.refreshnews.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.kiit.eee.ragini.refreshnews.R;

/**
 * Created by 1807340_RAGINI on 04,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class ViewHolder {
    private static String TAG = "ViewHolder";
    public static AlertDialog alertDialogRef;
    private static AlertDialog.Builder alertDialog;

    public static void showDialog(Activity act, String msg, DialogInterface.OnClickListener listener, int operation) {
        //  AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);
        if(alertDialog  == null) {
            alertDialog = new AlertDialog.Builder(act);
        }
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(act.getString(R.string.retry), listener);
        if(alertDialogRef == null) {
            alertDialogRef = alertDialog.create();
        }
        alertDialogRef.setCancelable(true);
        alertDialogRef.setCanceledOnTouchOutside(false);
        alertDialogRef.show();
    }

    public static void dismissShowDialog(){
        if(alertDialogRef != null && alertDialogRef.isShowing()){
            alertDialogRef.dismiss();
        }
    }

    public static void showAlertDialog(Activity act, String title, String msg, String btnName, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(act);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton(btnName, listener);
        alert.create();
        alert.show();
    }

    public static void showDialogWithCancelBtn(Activity act, String msg, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onCancelListener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(act);
        alert.setMessage(msg);
        alert.setPositiveButton(act.getString(R.string.ok), onClickListener);
        alert.setNegativeButton(act.getString(R.string.cancel), onCancelListener);
        alert.create();
        alert.show();
    }

    public static void setProgressBarVisibility(ProgressBar progressBar, int visibility) {
        if (progressBar != null) {
            progressBar.setVisibility(visibility);
        }
    }

}

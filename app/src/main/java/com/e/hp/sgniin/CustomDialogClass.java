package com.e.hp.sgniin;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class CustomDialogClass {
    Dialog myDialog;
    private Context context;

    public void showdialog()
    {

        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.custom_alert);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }




}



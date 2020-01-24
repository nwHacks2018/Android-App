package com.nwhacks.luminescence.wifind.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.nwhacks.luminescence.wifind.R;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Dialogs {

    public static void requireLocation(final Activity thisActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);

        builder.setTitle(R.string.dialog_location_request_title)
                .setMessage(R.string.dialog_location_request_message)

                .setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Navigation.goToLocationSettings(thisActivity);
                            }
                        }
                )

                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Navigation.exitApplication(thisActivity);
                            }
                        }
                );

        builder.create().show();
    }

}


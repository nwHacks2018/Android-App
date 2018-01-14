package com.nwhacks.connieho.sampleapplication.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.nwhacks.connieho.sampleapplication.R;

import java.util.zip.Inflater;

/**
 * Created by Owner on 1/13/2018.
 */

public class AuthenticationFragment extends DialogFragment {

    private String password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.authentication_layout, null);
        Button connectButton = (Button) view.findViewById(R.id.connect_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.show_password_check_box);
        setCancelable(true);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect((EditText) view.findViewById(R.id.password_edit_text));
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText passwordEditText = ((EditText)view.findViewById(R.id.password_edit_text));
                if (checkBox.isChecked()){
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else{
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD  | InputType.TYPE_CLASS_TEXT );
                }
            }
        });
        return view;
    }

    public String getPassword(){
        return password;
    }

    private void connect(EditText passwordEditText){
        String passwordText = passwordEditText.getText().toString();
        if (passwordText.equals("")){
            return;
        }
        password = passwordText;
        dismiss();
    }

    private void cancel(){
        dismiss();
    }
}

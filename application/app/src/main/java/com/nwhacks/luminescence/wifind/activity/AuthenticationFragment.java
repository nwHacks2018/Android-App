package com.nwhacks.luminescence.wifind.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.nwhacks.luminescence.wifind.R;

/**
 * Created by Owner on 1/13/2018.
 */

public class AuthenticationFragment extends DialogFragment {

    private String password;
    private String networkName;

    public AuthenticationFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.authentication_layout, null);
        Button connectButton = (Button) view.findViewById(R.id.connect_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.show_password_check_box);
        TextView networkNameTextView = (TextView) view.findViewById(R.id.network_name_text_view);
        networkName = getArguments().getString("networkName");
        networkNameTextView.setText(networkName);
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

    /* code to call to open dialog

    public void showAuthenticationDialog(View view){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        Bundle args = new Bundle();
        AuthenticationFragment dialog = new AuthenticationFragment();
        args.putString("networkName", "enterNetworkNameHere");
        dialog.setArguments(args);
        dialog.show(fragmentManager, "Authentication dialog");
    }

     */
}

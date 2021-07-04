package com.example.fit5046_assignment3.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.fit5046_assignment3.R;
import com.google.android.material.navigation.NavigationView;

public class DataEntryOneTimeWarning extends DialogFragment {

    private View v;

    public DataEntryOneTimeWarning(View v) {
        this.v = v;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new AlertDialog.Builder(getContext()).setTitle("Warning").setMessage("Only One Record Allowed Daily")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Navigation.findNavController(v).navigate(R.id.nav_record_fragment);

                    }
                }).create();


    }
}

package com.example.andriy.openeyes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class FilturePlace extends DialogFragment {
    CheckBox filtureAll, filtureGasStation, filtureFarmace, filtureHospital, filtureCafe, filtureShop, filtureCulturePlace, filtureOther;
    public FilturePlace() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_filture_place, null);
        filtureAll=(CheckBox) view.findViewById(R.id.filtureAll);
        filtureCafe=(CheckBox) view.findViewById(R.id.filtureCafe);
        filtureCulturePlace=(CheckBox) view.findViewById(R.id.filtureCulturePlace);
        filtureFarmace=(CheckBox) view.findViewById(R.id.filtureFarmace);
        filtureGasStation=(CheckBox) view.findViewById(R.id.filtureGasStation);
        filtureHospital=(CheckBox) view.findViewById(R.id.filtureHospital);
        filtureOther=(CheckBox) view.findViewById(R.id.filtureOther);
        filtureShop=(CheckBox) view.findViewById(R.id.filtureShop);
        filtureAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (filtureAll.isChecked()){
                    filtureCafe.setChecked(true);
                    filtureCulturePlace.setChecked(true);
                    filtureFarmace.setChecked(true);
                    filtureGasStation.setChecked(true);
                    filtureHospital.setChecked(true);
                    filtureOther.setChecked(true);
                    filtureShop.setChecked(true);

                }else {
                        filtureCafe.setChecked(false);
                        filtureCulturePlace.setChecked(false);
                        filtureFarmace.setChecked(false);
                        filtureGasStation.setChecked(false);
                        filtureHospital.setChecked(false);
                        filtureOther.setChecked(false);
                        filtureShop.setChecked(false);
                }
            }
        });

                builder.setView(view)
                .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FilturePlace.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
package com.example.andriy.openeyes;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


@SuppressLint("ValidFragment")
public class TabInfrormationComfortablePlace extends Fragment {
    TextView describeText, adressText, categoryText;
    String adress, describe, categoty;
    boolean isHaveToilet, isHaveElevator, isHaveRamp, isHaveButtonHelp, isHaveSwaddingTable;
    CheckBox haveToilet, haveElevator, haveButtonHelp, haveRamp, haveSwaddingTable;

    public TabInfrormationComfortablePlace(String adr, String desc, String setCategory, boolean setIsHaveToilet,
                                            boolean setIsHaveElevator, boolean setIsHaveRamp,
                                           boolean setIsHaveButtonHelp, boolean setIsHaveSwaddingTable ) {
        adress = adr;
        describe = desc;
        categoty=setCategory;
        isHaveButtonHelp=setIsHaveButtonHelp;
        isHaveElevator=setIsHaveElevator;
        isHaveRamp=setIsHaveRamp;
        isHaveSwaddingTable=setIsHaveSwaddingTable;
        isHaveToilet=setIsHaveToilet;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_infrormation_comfortable_place, container, false);
        describeText = (TextView) view.findViewById(R.id.describeTextComfortablePlace);
        describeText.setText(describe);
        adressText = (TextView) view.findViewById(R.id.adressTextComfortablePlace);
        adressText.setText(adress);
        categoryText=(TextView) view.findViewById(R.id.categoryText);
        categoryText.setText(categoty);
        haveButtonHelp=(CheckBox) view.findViewById(R.id.haveButtonHelp);
        haveButtonHelp.setChecked(isHaveButtonHelp);
        haveButtonHelp.setEnabled(false);
        haveElevator=(CheckBox) view.findViewById(R.id.haveElevator);
        haveElevator.setChecked(isHaveElevator);
        haveElevator.setEnabled(false);
        haveRamp=(CheckBox) view.findViewById(R.id.haveRamp);
        haveRamp.setChecked(isHaveRamp);
        haveRamp.setEnabled(false);
        haveSwaddingTable=(CheckBox) view.findViewById(R.id.haveSwaddingTable);
        haveSwaddingTable.setChecked(isHaveSwaddingTable);
        haveSwaddingTable.setEnabled(false);
        haveToilet=(CheckBox) view.findViewById(R.id.haveToilet);
        haveToilet.setChecked(isHaveToilet);
        haveToilet.setEnabled(false);
        return view;
    }


}
